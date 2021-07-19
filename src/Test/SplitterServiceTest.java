import com.AudioSplitter.Service.SplitterService;
import com.AudioSplitter.Task.SplitTaskObject;

import javax.jms.JMSException;
import java.io.IOException;

public class SplitterServiceTest {

    public static void main(String[] args) throws IOException, JMSException {
        SplitterService ss=new SplitterService(null);
        SplitTaskObject localFileTask=new SplitTaskObject(0,"oirejfwpe"
                ,0,6000
                ,"F:\\git\\GitHub\\MP3SplitCore\\testMp3\\mp3-320.mp3");
        ss.split(localFileTask);
    }
}
