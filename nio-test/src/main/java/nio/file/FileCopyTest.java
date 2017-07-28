package nio.file;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhangshan193 on 16/11/3.
 */
public class FileCopyTest {

    @Test
    public void testFileCopy()throws Exception{
        FileInputStream fis = new FileInputStream("/Users/zhangshan193/Downloads/apache-maven-3.3.9-bin.zip");
        File file = new File("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/aa.zip");
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/aa.zip");

        ByteBuffer buf = ByteBuffer.allocate(48);
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
        int i = fisChannel.read(buf);
        while(i> -1){
            buf.flip();
            fosChannel.write(buf);
            buf.clear();
            i = fisChannel.read(buf);
        }
        fos.close();
        fis.close();
        File srcFile = new File("/Users/zhangshan193/Downloads/apache-maven-3.3.9-bin.zip");
        System.out.println("src length:"+srcFile.length());
        System.out.println("desc length:"+file.length());
    }


}
