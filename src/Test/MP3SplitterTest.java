import com.AudioSplitter.Service.MP3Splitter;

import java.io.File;
import java.io.IOException;

public class MP3SplitterTest {

    private static final String[]ABR_TEST=new String[]{"abr032.mp3","abr040.mp3","abr192.mp3"};
    private static final String[]CBR_TEST=new String[]{"mp3-032.mp3","mp3-112.mp3","mp3-320.mp3"};

    public static final String DIR_TEST_MP3_DIR="testMp3/";
    private static final String DIR_MP3_OUTPUT="output/";
    public static void main(String[] args) throws IOException {
        long timer=System.currentTimeMillis();
        File file=new File(DIR_TEST_MP3_DIR+CBR_TEST[0]);
//        File file=new File(DIR_TEST_MP3_DIR+"ring.mp3");
        MP3Splitter hs=new MP3Splitter(file);
        File dir=new File(file.getAbsolutePath().replace(file.getName(),DIR_MP3_OUTPUT));
        hs.subsequence(0,18000,dir);
        hs.subsequence(18000,25000,dir);
        hs.subsequence(25000,370000,dir);
        System.out.println("time spend: "+(System.currentTimeMillis()-timer)+" ms");

    }
}
