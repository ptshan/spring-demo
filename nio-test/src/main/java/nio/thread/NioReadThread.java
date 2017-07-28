package nio.thread;

import com.pingan.study.test.nio.NioClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANGSHAN193
 * Date: 16-11-1
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public class NioReadThread extends Thread{
    static{
        PropertyConfigurator.configure("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/src/main/resources/log4j.properties");
    }
    private static Logger logger = Logger.getLogger(NioReadThread.class);

    private NioClient nioClient;

    public NioReadThread(NioClient nioClient){
        this.nioClient = nioClient;
    }


    @Override
    public void run() {
        while(true){
            logger.error("----wwww---");
            nioClient.readMsg();
    }
    }
}
