package nio.thread;

import com.pingan.study.test.nio.NioClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.nio.channels.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANGSHAN193
 * Date: 16-11-1
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class NioWriteThread extends Thread {
    static{
        PropertyConfigurator.configure("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/src/main/resources/log4j.properties");
    }
    private static Logger logger = Logger.getLogger(NioWriteThread.class);

    private SocketChannel socketChannel;
    private int num=0;
    private NioClient nioClient;

    public NioWriteThread(NioClient nioClient){
        this.nioClient = nioClient;
    }

    @Override
    public void run() {
        while(true){
            logger.error("---ccc---");
            String msg = "i am 小小 client  "+num++;
            nioClient.writeMsg(msg);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            logger.error("为毛客户端写线程挂了");
        }

    }
}
