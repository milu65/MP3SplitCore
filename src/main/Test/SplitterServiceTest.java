import com.AudioSplitter.Service.SplitterService;
import com.Task.SplitTaskObject;

import javax.jms.JMSException;
import java.io.IOException;

public class SplitterServiceTest {

    public static void main(String[] args) throws IOException, JMSException {
        SplitterService ss=new SplitterService();
        SplitTaskObject localFileTask=new SplitTaskObject(0,"oirejfwpe"
                ,0,6000,true
                ,"F:\\git\\GitHub\\MP3SplitCore\\testMp3\\mp3-320.mp3");
        SplitTaskObject remoteFileTask=new SplitTaskObject(0,"oirejfwpe"
                ,0,10000,false
                ,"mp3-112.mp3");
        ss.split(remoteFileTask);
    }
}
