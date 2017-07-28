package nio;

import com.pingan.study.test.nio.thread.NioReadThread;
import com.pingan.study.test.nio.thread.NioWriteThread;

/**
 * Created by zhangshan193 on 16/11/2.
 */
public class ClientStart {

    public static void main(String[] args) {
        NioClient nioClient = new NioClient();
        NioReadThread nioReadThread = new NioReadThread(nioClient);
        NioWriteThread nioWriteThread = new NioWriteThread(nioClient);
        nioReadThread.start();
        nioWriteThread.start();
    }

}
