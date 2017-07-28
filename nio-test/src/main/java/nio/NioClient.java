package nio;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by zhangshan193 on 16/11/1.
 */
public class NioClient {
    static{
        PropertyConfigurator.configure("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/src/main/resources/log4j.properties");
    }
    private static Logger logger = Logger.getLogger(NioClient.class);

    private SocketChannel socketChannel;

    public NioClient(){
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init()throws Exception{
         socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9911));
     //   selector = Selector.open();
     //   socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
    }


//    public void start()throws Exception{
//        init();
//        new NioReadThread(socketChannel).start();
//        new NioWriteThread(socketChannel).start();
//    }

//    public static void main(String[] args) throws Exception{
//        new NioClient().start();
//    }

    public void readMsg(){
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        try {
            i = socketChannel.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(i > 0){
            buf.flip();
            sb.append(new String(buf.array(), 0, i));
            buf.clear();
            try {
                i = socketChannel.read(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("服务端发送的数据：" + sb.toString());
    }

    public void writeMsg(String msg){
        if(msg == null || msg.length()==0){
            msg = "我是小小客户端";
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byteBuffer.clear();
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
