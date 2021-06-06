import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

//TODO:增加ABR帧解析
//TODO:重构构造函数
//TODO:类文件拆分
public class MP3Split {

    private static final String[]ABR_TEST=new String[]{"abr032.mp3","abr040.mp3","abr192.mp3"};
    private static final String[]CBR_TEST=new String[]{"mp3-032.mp3","mp3-112.mp3","mp3-320.mp3"};

    private static final String DIR_TEST_MP3_DIR="testMp3/";
    private static final String DIR_MP3_OUTPUT="output/";

    private static long outputCount=0;

    public static void main(String[] args) throws IOException {
        long timer=System.currentTimeMillis();
        File file=new File(DIR_TEST_MP3_DIR+CBR_TEST[0]);
//        File file=new File(DIR_TEST_MP3_DIR+"ring.mp3");
        MP3Split hs=new MP3Split(file);
        hs.subsequence(0,18000);
        hs.subsequence(18000,25000);
        hs.subsequence(25000,370000);
        System.out.println("time spend: "+(System.currentTimeMillis()-timer)+" ms");

    }

    private static class ID3V2Header{
        public byte[] version=new byte[2];
        public byte flags=0;
        public long tagSize=0;
    }

    private static class ID3V1Header{
    }

    private static class FrameHead{
        public long pos=0;
        public long bitrate=0;
        public int sampleRate=0;
        public int frameLen=0;//first frame length
        public int padding=0;
        public String toString(){
            return "pos: "+pos+" ,bitrate: "+bitrate+" ,sampleRate: "
                    +sampleRate+" ,frameLen: "+frameLen+" ,padding: "+padding;
        }
    }

    private static final int []BITRATE_IDX=new int[]{0,32,40,48,56,64,80,96,112,128,160,192,224,256,320,0};
    private static final int []SAMPLERATE_IDX=new int[]{44100,48000,32000,0};

    private long headSize=0;
    private long numOfFrame=0;
    private long duration=0;//Music duration of the mp3
    private double mspf=0;//ms per frame
    private FrameHead head=null;
    private ArrayList<FrameHead> sequence=null;
    private ID3V2Header v2Header=null;
    private ID3V1Header v1Header=null;

    private File inputFile=null;
    private File outputDir=null;

    private ID3V2Header readID3V2Header() throws IOException {
        FileInputStream fileInputStream=new FileInputStream(inputFile);
        if(fileInputStream.available()<10)return null;
        byte[]bytes=new byte[10];
        if(fileInputStream.available()<10)throw new IOException("invalid mp3 file - size < 10 bytes");
        int readSize=10;
        while(readSize!=0){
            readSize-=fileInputStream.read(bytes,10-readSize,readSize);
        }

        if(bytes[0]=='I'&&bytes[1]=='D'&&bytes[2]=='3'){
            ID3V2Header header=new ID3V2Header();
            header.version=Arrays.copyOfRange(bytes,3,4);
            header.flags= bytes[5];
            header.tagSize=bytes[6]*0x200000+bytes[7]*0x4000+bytes[8]*0x80+bytes[9];
            return header;
        }else{
            return null;
        }
    }

    public MP3Split(File file) throws IOException {
        inputFile=file;
        outputDir=new File(file.getAbsolutePath().replace(file.getName(),DIR_MP3_OUTPUT));
        sequence=new ArrayList<>();

        BufferedInputStream fileInputStream=new BufferedInputStream(new FileInputStream(file));
        v2Header=readID3V2Header();
        if(v2Header!=null){
            headSize=v2Header.tagSize+10;
            System.out.println("v2Header found size: "+headSize);
            long skipSize=headSize;
            while(skipSize!=0){
                long skipped=fileInputStream.skip(skipSize);
                skipSize-=skipped;
                if(skipped==0){
                    break;
                }
            }
        }

        fileInputStream.mark(0);//TODO:重新理顺mark的相关使用
        long lastIndex=0;
        long index=headSize;
        int undefinedBytesCount=0;
        while(fileInputStream.available()!=0){
            int sign=fileInputStream.read();
            index++;
            FrameHead headL=null;
            if(sign==0xFF){//TODO: else判断内容，对于废帧的处理
                fileInputStream.mark(0);
                lastIndex=index;
                int left=fileInputStream.read();
                int mid=fileInputStream.read();
                int right=fileInputStream.read();
                headL= readCBRFrameHead(sign,left,mid,right);
                if(headL!=null){
                    headL.pos=index-1;
                    sequence.add(headL);
                    System.out.println(String.format("%5d ",sequence.size())+headL+" range: "+(index-1)+"->"+(index+headL.frameLen-1));
                    if(sequence.size()==2&&v2Header!=null){
                        headSize=headL.pos-sequence.get(0).frameLen;
                    }
                    long skipSize=headL.frameLen-4;
                    while(skipSize!=0){
                        long skipped=fileInputStream.skip(skipSize);
                        skipSize-=skipped;
                        if(skipped==0){
                            System.out.println("buffer error");
                            break;
                        }
                    }
                    index+=headL.frameLen-1-skipSize;
                }else{
                    if(sequence.size()!=0){
                        sequence.remove(sequence.size()-1);
                    }
                    fileInputStream.reset();
                    System.out.println("reset from " + index+" to "+lastIndex);
                    index=lastIndex;
                }
            }else{
                undefinedBytesCount++;
                System.out.print(" skip "+Integer.toHexString(sign)+" "+(index-1));
            }
        }
        head=sequence.get(0);
        numOfFrame=sequence.size();
        duration=(long)((1152*1000.0/head.sampleRate)*numOfFrame);
        mspf=(1152*1000.0/head.sampleRate);

        System.out.println("num of frame: "+numOfFrame);
        System.out.println("duration: "+duration+" ms");
        System.out.println("undefined bytes count: "+(undefinedBytesCount-(v2Header!=null?0:headSize)));
        System.out.println("headSize: "+headSize);
        fileInputStream.close();
    }

    private FrameHead readCBRFrameHead(int sign, int left, int mid, int right) {
        if(sign!=0xFF||left<0||right<0||mid<0)return null;
        int bitrate=BITRATE_IDX[mid>>4];
        if(bitrate==0)return null;
        bitrate*=1000;
        int sampleRate=SAMPLERATE_IDX[(0xC&mid)>>2];
        int padding=(0x2&mid)>>1;
        int frameLen=(int)(144.0*bitrate/sampleRate+padding);
        FrameHead headL= new FrameHead();
        headL.bitrate=bitrate;
        headL.sampleRate=sampleRate;
        headL.padding=padding;
        headL.frameLen=frameLen;
        return headL;
    }

    /**
     * subsequence
     *
     * subsequence of mp3 file
     *
     * @param start start position (unit ms
     * @param end end position (unit ms
     * @throws IOException
     */
    public void subsequence(long start,long end) throws IOException {
        if(end>this.duration){
            end=this.duration;
        }
        if(start<0||start>=end){
            System.out.println("subsequence(): input(end-start) out of range");
            return;
        }

        FileInputStream fileInputStream=new FileInputStream(inputFile);

        int numFrameSkip=(int)(start/mspf);
        int numFrame=(int)((end-start)/mspf);
        numFrame+=1;//fill up

        long endByte=sequence.get(numFrameSkip+numFrame-1).pos;
        long skipBytes=0;

        if(start!=0){
            skipBytes=sequence.get(numFrameSkip).pos;
        }else{
            skipBytes=headSize;
        }

        long skipSize=skipBytes;
        while(skipSize!=0){//skip useless bytes
            long gone=fileInputStream.skip(skipSize);
            skipSize-=gone;
            if(gone==0){
                break;
            }
        }

        byte[]frameBuffer =new byte[head.frameLen];
        FileOutputStream fileOutputStream=new FileOutputStream(
                new File(outputDir,inputFile.getName().replace(".mp3","")
                        +"-subsequence"+(outputCount++)+".mp3"));
//                        +System.currentTimeMillis()+".mp3"));

        for(long i=skipBytes;i<endByte;){//take bytes from input file
            int readLen=fileInputStream.read(frameBuffer);
            if(endByte-i<readLen)readLen=(int)(endByte-i);
            fileOutputStream.write(frameBuffer, 0, readLen);
            i+=readLen;
        }
        fileOutputStream.close();
    }
}
