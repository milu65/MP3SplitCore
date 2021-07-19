import com.AudioSplitter.Service.AWS.S3Client;

public class S3ClientTest {
    public static void main(String[] args) {
        String downloadAddr="C:\\Users\\millby\\Desktop\\upload";
        String bucketName="testbucket324129384";
        String uploadTarget="F:\\git\\GitHub\\MP3SplitCore\\testMp3\\mp3-112.mp3";
        S3Client s3=new S3Client();
//        System.out.println(s3.uploadFile(bucketName,"mp3-112.mp3",uploadTarget));
//        System.out.println(s3.createPublicDownloadAddressExp24h(bucketName,"testUpload"));
        System.out.println(s3.downloadFile(bucketName,"mp3-112.mp3",downloadAddr));
    }
}
