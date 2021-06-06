import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MP3Split {

    public static void main(String[] args) throws IOException {
        MP3Split ms=new MP3Split("days.mp3");
//        ms.subsequence(0,6000);
//        ms.subsequence(6000,10000);
//        ms.subsequence(10000,11000);
//        ms.subsequence(22500,25000);
    }

    private static final int []BITRATE_IDX=new int[]{0,32,40,48,56,64,80,96,112,128,160,192,224,256,320,0};
    private static final int []SAMPLERATE_IDX=new int[]{44100,48000,32000};

    private long headSize=0;
    private long bitrate=0;
    private int sampleRate=0;
    private long numOfFrame=0;//estimate
    private int frameLen=0;//first frame length
    private long duration=0;//length of the mp3
    private double mspf=0;//ms per frame
    private File inputFile=null;
    private File outputDir=null;

    public MP3Split(String location) throws IOException {
        this(new File(location));
    }

    public MP3Split(File file) throws IOException{
        inputFile=file;
        outputDir=new File(file.getAbsolutePath().replace(file.getName(),""));

        FileInputStream fileInputStream=new FileInputStream(file);
        int sign=0;
        long len=file.length();
        while((sign= fileInputStream.read())!=-1){//find begging sign
            if(sign==0xFF){
                break;
            }else{
                headSize++;
            }
        }

        if(sign==-1){
            System.out.println("no frameHead");
            return;
        }

        if(!readFrameHead(fileInputStream)){
            System.out.println("bad frameHead");
            return;
        }

        if(headSize!=0)headSize--;
        len-=headSize;

        numOfFrame=len/frameLen;
        duration=(long)((1152*1000.0/sampleRate)*numOfFrame);
        mspf=(1152*1000.0/sampleRate);

        fileInputStream.close();
        System.out.println(this);
    }

    private boolean readFrameHead(FileInputStream fis) throws IOException {
        int left=fis.read();//head byte2
        int mid=fis.read();//head byte3
        int right=fis.read();//head byte4

        bitrate=BITRATE_IDX[mid>>4];
        if(bitrate==0)return false;
        bitrate*=1000;
        sampleRate=SAMPLERATE_IDX[(0xC&mid)>>2];
        int padding=(0x2&mid)>>1;
        frameLen=(int)(144.0*bitrate/sampleRate+padding);
        return true;
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
        FileInputStream fileInputStream=new FileInputStream(inputFile);
        fileInputStream.skip(headSize);

        long framesSkip=(long)(start/mspf)*frameLen;
        long numFrame=(long)((end-start)/mspf);

        numFrame+=2;//fill up

        fileInputStream.skip(framesSkip);

        byte[]frameBuffer =new byte[frameLen];
        FileOutputStream fileOutputStream=new FileOutputStream(
                new File(outputDir,"subsequence-"+System.currentTimeMillis()+".mp3"));

        for(int i=0;i<numFrame;i++){//take numFrame frames
            int readLen=fileInputStream.read(frameBuffer);
            fileOutputStream.write(frameBuffer, 0, readLen);
            if(readLen<frameBuffer.length-1)break;
        }
        fileOutputStream.close();
    }

    public String toString(){
        return "fileName: "+inputFile.getAbsolutePath()+
                "\nfileSize: "+inputFile.length()+" bytes"+
                "\nsampleRate: "+sampleRate+
                "\nbitRate: "+ bitrate+
                "\nframeLen: "+ frameLen+
                "\nnumOfFrame: "+numOfFrame+
                "\nduration: "+duration+" ms";
    }
}