package nio;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by zhangshan193 on 16/11/1.
 */
public class NioServer {
    static{
        PropertyConfigurator.configure("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/src/main/resources/log4j.properties");
    }

    private static Logger logger = Logger.getLogger(NioServer.class);

    private Selector selector;
    private int num;

    //@Before
    public void init(){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(9911));
            serverSocketChannel.configureBlocking(false);
             selector = Selector.open();
            SelectionKey selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)throws Exception {
        new NioServer().start();
    }

    @Test
    public void start()throws Exception{
        init();
        logger.info("---- server start");
        while(true){
            System.out.println("wait n");
            int n = selector.select();
            System.out.println("n="+n);
            if(n > 0){
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey selectionKey = it.next();
                    if(selectionKey.isAcceptable()){
                        it.remove();
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        if(socketChannel == null) continue;
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }

                    if(selectionKey.isReadable()){
                        readFromSK(selectionKey);
                    }

                    if(selectionKey.isWritable()){
                        write2SK(selectionKey);
                    }
                }
            }
        }
    }

    public void readFromSK(SelectionKey sk)throws Exception{
        SocketChannel socketChannel = (SocketChannel) sk.channel();
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
        logger.info("服务端-客户端发送了:"+sb.toString());
        socketChannel.register(selector,SelectionKey.OP_WRITE);
    }

    public void write2SK(SelectionKey sk)throws Exception{
        SocketChannel socketChannel = (SocketChannel) sk.channel();
        String str = "i am server num = "+num++;
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byteBuffer.clear();
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        socketChannel.register(selector,SelectionKey.OP_READ);
    }

}
