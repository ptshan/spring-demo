package nio.file;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by zhangshan193 on 16/11/2.
 */
public class FileNioClient {

    private Selector selector;

    @Before
    public void init()throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        //socketChannel.connect(new InetSocketAddress("127.0.0.1",12345));
        socketChannel.connect(new InetSocketAddress("192.168.1.106",9094));

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

    }

    @Test
    public void start() throws Exception{
        while(true){
            int n = selector.select();
            System.out.println("n="+n);
            if(n > 0){
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey selectionKey = it.next();
                    if(selectionKey.isConnectable()){

                        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                        // 如果正在连接，则完成连接
                        if(socketChannel.isConnectionPending()){
                            socketChannel.finishConnect();

                        }
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_WRITE);
                    }
                    if(selectionKey.isWritable()){
                        writeFile2(selectionKey);
                        return ;
                    }
                    it.remove();
                }
            }
        }

    }

    public void writeFile(SelectionKey selectionKey)throws Exception{
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        String fname = "/Users/zhangshan193/Downloads/apache-maven-3.3.9-bin.zip" ;
        FileChannel fileChannel = new FileInputStream(fname).getChannel();
        int i = fileChannel.read(buf);
        long j=0;
        while(i > -1){

            j++;
            buf.flip();
            while(buf.hasRemaining()) {
                socketChannel.write(buf);
            }

            buf.clear();
            i = fileChannel.read(buf);
            if(i == 0 || i != 48) {
                System.out.println("--- client i="+i);
            }
        }
        fileChannel.close();
        socketChannel.close();
        File file = new File(fname);
        System.out.println("j="+j);
        System.out.println("client length:"+file.length());
        System.out.println("client length:"+file.length());

    }

    public void writeFile2(SelectionKey selectionKey)throws Exception{
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

        ByteBuffer buf = ByteBuffer.allocate(1024*1024*1000);
        buf.clear();
        String fname = "/Users/zhangshan193/Downloads/sempre.zip" ;
        FileChannel fileChannel = new FileInputStream(fname).getChannel();
        int i = fileChannel.read(buf);
        long j=0;
        while(i > -1){

            j++;
            buf.flip();
            while(buf.hasRemaining()) {
                socketChannel.write(buf);
            }

            buf.clear();
            i = fileChannel.read(buf);

        }
        fileChannel.close();
        socketChannel.close();
        File file = new File(fname);
        System.out.println("j="+j);
        System.out.println("client length:"+file.length());

    }

}
