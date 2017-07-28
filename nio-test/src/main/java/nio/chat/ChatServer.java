package nio.chat;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by zhangshan193 on 16/11/3.
 */
public class ChatServer {

    private Selector selector;

    @Before
    public void init()throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(ChatConstants.SERVER_PORT));

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Test
    public void start()throws Exception{
        while(true){
            if(selector.select() > 0){
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                System.out.println("--3333");
                while(it.hasNext()){
                    SelectionKey selectionKey = it.next();
                    if(selectionKey.isAcceptable()){
                        it.remove();
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

                        SocketChannel socketChannel = serverSocketChannel.accept();
                        if(socketChannel == null){
                            continue;
                        }

                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);

                    }else if(selectionKey.isReadable()){
                        SocketChannelUtils.recMsg(selectionKey,selector);
                        //readFileMsg2(selectionKey);
                       // readFile2(selectionKey);
                    }else if(selectionKey.isWritable()){
                        writeMsg(selectionKey);
                    }else{
                        System.out.println(" error :");
                    }

                    //Thread.sleep(1000);
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

    private void readFileMsg2(SelectionKey selectionKey)throws Exception {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

        Message msg = FileUtils.readMsgFromSocketChannel(socketChannel);

        System.out.println("server msg:"+msg);
        String fileName = "maven_" + System.currentTimeMillis() + ".zip";

        FileUtils.writeMsg2File(msg,ChatConstants.WRITE_FILE_DIR+fileName);

        System.out.println("----server file read over ---");
        System.out.println("server file length:"+new File(ChatConstants.WRITE_FILE_DIR + fileName).length());
        Thread.sleep(60*60*1000);

        socketChannel.register(selector,SelectionKey.OP_WRITE);

    }

    private void readFileMsg(SelectionKey selectionKey)throws Exception {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocate(ChatConstants.BUF_SIZE);
        buf.clear();
        System.out.println("---- enter  readFileMsg ------");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int i = socketChannel.read(buf);
        boolean firstFlag = true;
        String fileName = "maven_" + System.currentTimeMillis() + ".zip";
        File file = new File("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileChannel fosChannel = new FileOutputStream(file).getChannel();
        ByteBuffer fileBuf = ByteBuffer.allocate(ChatConstants.BUF_FILE_SIZE);
        fileBuf.clear();
        while(i > 0){
            System.out.println("i="+i);
            buf.flip();
            baos.write(buf.array(),0,i);
            baos.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);

            Object obj = ois.readObject();
            Message msg = (Message) obj;
            if(firstFlag == true){

                firstFlag = false;
            }
            // 写入文件中
            System.out.println("msg.getReadByteCount():"+msg.getReadByteCount());
            fileBuf.put(msg.getFileData(),0,msg.getReadByteCount());
            fileBuf.flip();
            while(fileBuf.hasRemaining()) {
                fosChannel.write(fileBuf);
            }
            fileBuf.clear();

            System.out.println(msg);
            buf.clear();
            i = socketChannel.read(buf);
        }

        System.out.println("---- file read over ---");
        System.out.println("file length:"+new File("/Users/zhangshan193/Documents/projects/idea_projects/study_project/nio_test/files/" + fileName).length());
        Thread.sleep(60*60*1000);

        socketChannel.register(selector,SelectionKey.OP_WRITE);

    }

    private void readMsg(SelectionKey selectionKey)throws Exception {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocate(ChatConstants.BUF_SIZE);
        buf.clear();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int i = socketChannel.read(buf);
        while(i > 0){
            buf.flip();
            baos.write(buf.array(),0,i);
            buf.clear();
            i = socketChannel.read(buf);
        }
        baos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        Message msg = (Message) obj;
        System.out.println(msg);
        socketChannel.register(selector,SelectionKey.OP_WRITE);

    }

    private void writeMsg(SelectionKey selectionKey)throws Exception {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocate(ChatConstants.BUF_SIZE);
        buf.clear();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        Message msg = new Message();
        msg.setMsg("一切都是短暂的,只有猪肉卷是永恒的 by:加菲老大");
        msg.setType(ChatEnum.MSG_STRING.CODE);
        msg.setSendTimestamp(System.currentTimeMillis());
        String[] ipAddrs = socketChannel.getRemoteAddress().toString().replace("/","").split(":");
        msg.setIp(ipAddrs[0]);
        msg.setPort(Integer.parseInt(ipAddrs[1]));
        msg.setUserName("[系统发送]");
        oos.writeObject(msg);
        oos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        byte[] byteBuf = new byte[ChatConstants.BUF_SIZE];
        int j = bais.read(byteBuf);

        // 将对象数组循环输入
        while(j != -1){
            buf.put(byteBuf,0,j);
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
            j = bais.read(byteBuf);
        }
        SafeClose.close(oos,baos,bais);
        socketChannel.register(selector,SelectionKey.OP_READ);

    //    socketChannel.finishConnect();

    }

}
