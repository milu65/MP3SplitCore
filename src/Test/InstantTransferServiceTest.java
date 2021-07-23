import com.AudioSplitter.Service.InstantTransferService;

import java.io.File;
import java.io.IOException;

public class InstantTransferServiceTest {
    public static void main(String[] args) throws IOException {
        InstantTransferService its=new InstantTransferService();
        System.out.println(its.generateFileHash(new File("F:\\git\\GitHub\\MP3SplitCore\\testMp3/days.mp3")));
    }
}
