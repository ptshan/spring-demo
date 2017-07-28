package nio.file;

import com.pingan.study.test.nio.chat.Message;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by zhangshan193 on 16/11/2.
 */
public class FileNioServer {

    private Selector selector;
    private long count=0;

    @Before
    public void init()throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9094));
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

    }

    @Test
    public void start()throws Exception{
        while(true){
            System.out.println("wait n");
            int n = selector.select();
            System.out.println("n="+n);
            if(n > 0){
               Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey selectionKey = it.next();
                    if(selectionKey.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                        if(serverSocketChannel == null){
                            continue;
                        }
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }
                    if(selectionKey.isReadable()){
                        readFile(selectionKey);
                        //return;
                    }

                    it.remove();
                }
            }
        }
    }

    public void readFile2(SelectionKey selectionKey)throws Exception{
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        int i = socketChannel.read(buf);
        if(i > 0) {
            count++;
            System.out.println("------>>>>> count:"+count);
            String fileName = "maven_" + System.currentTimeMillis() + ".zip";
            File file = new File("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileChannel fosChannel = new FileOutputStream(file).getChannel();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            long j = 0;
            while (i > -1) {

                j++;
                //buf.flip();
                //while(buf.hasRemaining()) {
                baos.write(buf.array(),0,i);

                baos.flush();
                //}
                buf.clear();
                i = socketChannel.read(buf);
                System.out.println("i=" + i);
            }
            byte[] baosByte = baos.toByteArray();
            System.out.println("baosByte.length:"+baosByte.length);
            ByteArrayInputStream bais = new ByteArrayInputStream(baosByte);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Message msg = (Message)ois.readObject();
            System.out.println("-->msg:"+msg);
            System.out.println("-->msg fileByteSize:"+msg.getFileData().length);
            fosChannel.close();
            socketChannel.close();
            file = new File("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/" + fileName);
            long length = file.length();
            System.out.println("j=" + j);
            System.out.println("length:" + file.length());
            Thread.sleep(10000);
        }
    }

    public void readFile(SelectionKey selectionKey)throws Exception{
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        int i = socketChannel.read(buf);
        if(i > 0) {
            count++;
            System.out.println("------>>>>> count:"+count);
            String fileName = "maven_" + System.currentTimeMillis() + ".zip";
            File file = new File("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileChannel fosChannel = new FileOutputStream(file).getChannel();
            long j = 0;
            while (i > -1) {

                j++;
                buf.flip();
                while(buf.hasRemaining()) {
                    fosChannel.write(buf);
                }
                buf.clear();
                i = socketChannel.read(buf);
                System.out.println("i=" + i);
            }
            fosChannel.close();
            socketChannel.close();
            file = new File("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/" + fileName);
            long length = file.length();
            System.out.println("j=" + j);
            System.out.println("length:" + file.length());
            System.out.println("length:" + file.length());
            Thread.sleep(10000);
        }
    }

    public void writeFile()throws Exception{

    }

}
