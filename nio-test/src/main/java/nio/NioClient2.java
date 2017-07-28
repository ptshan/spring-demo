package nio;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by zhangshan193 on 16/11/1.
 */
public class NioClient2 {
    static{
        PropertyConfigurator.configure("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/src/main/resources/log4j.properties");
    }
    private static Logger logger = Logger.getLogger(NioClient2.class);

    private SocketChannel socketChannel;
    private Selector selector;

//    public NioClient2(){
//        try {
//            init();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Before
    public void init()throws Exception{
         socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9911));
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }


    @Test
    public void start()throws Exception{
        while(true){
            int n = selector.select();
            logger.info("n="+n);
            if(n > 0){
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey selectionKey = it.next();
                    if(selectionKey.isConnectable()){
                        it.remove();
                        SocketChannel socketChannelTemp = (SocketChannel)selectionKey.channel();
                        // 如果正在连接，则完成连接
                        if(socketChannelTemp.isConnectionPending()){
                            socketChannelTemp.finishConnect();

                        }
                        socketChannelTemp.configureBlocking(false);
                        socketChannelTemp.register(selector,SelectionKey.OP_WRITE);

                    }
                    if(selectionKey.isReadable()){
                        readMsg();
                    }
                    if(selectionKey.isWritable()){
                        writeMsg(null);
                    }
                }
            }
        }
    }



    public void readMsg()throws Exception{
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        StringBuffer sb = new StringBuffer();
        byteBuffer.clear();
        int i = socketChannel.read(byteBuffer);
        while(i > 0){
            byteBuffer.flip();
            sb.append(new String(byteBuffer.array(),0,i));
            byteBuffer.clear();
            i = socketChannel.read(byteBuffer);
        }

        logger.info("服务端发送的数据：" + sb.toString());
        socketChannel.register(selector,SelectionKey.OP_WRITE);
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
            socketChannel.register(selector,SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
