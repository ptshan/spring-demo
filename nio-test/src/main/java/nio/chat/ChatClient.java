package nio.chat;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by zhangshan193 on 16/11/7.
 */
public class ChatClient {

    private Selector selector;

    @Before
    public void init()throws Exception{

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(ChatConstants.SERVER_ADDRESS,ChatConstants.SERVER_PORT));

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

    }

    @Test
    public void start()throws Exception{
        while(true){
            int n = selector.select();

            if(n > 0){
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                System.out.println("-------111");
                while(it.hasNext()){
                    SelectionKey selectionKey = it.next();
                    if(selectionKey.isConnectable()){
                        it.remove();
                        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                        if(socketChannel == null ){
                            System.out.println("客户端SocketChannel为空");
                            continue;
                        }
                        // 如果正在连接，则完成连接
                        if(socketChannel.isConnectionPending()){
                            socketChannel.finishConnect();

                        }
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_WRITE);

                    }else if(selectionKey.isReadable()){
                        readMsg(selectionKey);
                    }else if(selectionKey.isWritable()){
                        //writeMsg(selectionKey);
                        //writeFileMsg2(selectionKey);
                      //  writeFile2(selectionKey);
                        SocketChannelUtils.sendMsg(selectionKey,selector);
                    }else{
                        System.out.println("客户端感兴趣事件异常");
                    }
                }
            }
        }
    }

    public void writeFile2(SelectionKey selectionKey)throws Exception{
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        String fname = ChatConstants.WRITE_FILE_DIR+"message.txt" ;
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

    }

    /**
     * 将文件所有数据放入字节数组中
     * @param selectionKey
     * @throws Exception
     */
    private void writeFileMsg2(SelectionKey selectionKey)throws Exception {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

        String fname = "/Users/zhangshan193/Downloads/apache-maven-3.3.9-bin.zip" ;
        File file = new File(fname);

        // 组装msg
        Message msg = new Message();
        String[] ipAddrs = socketChannel.getRemoteAddress().toString().replace("/","").split(":");
        //msg.setMsg("山不在高,有仙则名,水不在深,有龙则灵!");
        msg.setType(ChatEnum.MSG_FILE.CODE);
        msg.setSendTimestamp(System.currentTimeMillis());
        msg.setTransType(TransEnum.TRANS_OVER.CODE);
        msg.setIp(ipAddrs[0]);
        msg.setPort(Integer.parseInt(ipAddrs[1]));
        msg.setUserName("[客户端发送]");
        msg.setFileName(file.getName());
        msg.setFileLength(file.length());
        msg.setFileData(FileUtils.getFileBytes(fname));

        System.out.println("client length:"+file.length());
        System.out.println("msg:"+msg);

        FileUtils.writeMsg2SocketChannel(socketChannel,msg);
        // 只有当socketChannel.close()的时候,服务端才会返回-1,否则服务端返回0;
        socketChannel.close();
        System.out.println("----- writeFile2 over ");
        // Thread.sleep(60*60*1000);
        //     SafeClose.close(oos,ois);
        socketChannel.register(selector,SelectionKey.OP_READ);

    }


    /**
     * 每次读取一个缓冲的文件数据,然后发送
     * @param selectionKey
     * @throws Exception
     */
    private void writeFileMsg(SelectionKey selectionKey)throws Exception {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocate(ChatConstants.BUF_SIZE);
        buf.clear();
        System.out.println("---- enter client writeFileMsg ----");
        String fname = "/Users/zhangshan193/Downloads/apache-maven-3.3.9-bin.zip" ;
        File file = new File(fname);
        FileChannel fileChannel = new FileInputStream(file).getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        Message msg = null;
        String[] ipAddrs = socketChannel.getRemoteAddress().toString().replace("/","").split(":");
        ByteBuffer fileBuf = ByteBuffer.allocate(ChatConstants.BUF_FILE_SIZE);
        fileBuf.clear();
        int i = fileChannel.read(fileBuf);
        long fileLength = file.length();
        long yushu = fileLength % ChatConstants.BUF_FILE_SIZE;
        boolean lastDataFlag = false;
        long readTimes = 0;
        if(yushu  == 0) { // 说明文件被除后,返回
            readTimes =  fileLength / ChatConstants.BUF_FILE_SIZE;

        }else {
            readTimes =  fileLength / ChatConstants.BUF_FILE_SIZE + 1;
        }
        int trans = 0;
        while(i > -1){
            trans++;
            buf.flip();
           // while(buf.hasRemaining()) {
                //socketChannel.write(buf);
            msg = new Message();
                //msg.setMsg("山不在高,有仙则名,水不在深,有龙则灵!");
                msg.setType(ChatEnum.MSG_FILE.CODE);

                msg.setSendTimestamp(System.currentTimeMillis());
                System.out.println("trans:"+trans+",readTimes:"+readTimes);
                if(trans == readTimes) {
                    msg.setTransType(TransEnum.TRANS_OVER.CODE);
                }else if(trans == 1 && readTimes != 1) {
                    msg.setTransType(TransEnum.TRANS_START.CODE);
                }else {
                    msg.setTransType(TransEnum.TRANS_SENDING.CODE);
                }
                msg.setReadByteCount(i);
                fileBuf.flip();
                msg.setFileData(fileBuf.array());
                fileBuf.clear();
                msg.setIp(ipAddrs[0]);
                msg.setPort(Integer.parseInt(ipAddrs[1]));
                msg.setUserName("[客户端发送]");
                msg.setFileName(file.getName());
                msg.setFileLength(file.length());
            System.out.println("msg:"+msg);
                // 将对象写到内存
                oos.writeObject(msg);
                buf.clear();
         //   }

            // 将对象字节数组,写入socketChannel
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



            buf.clear();
            i = fileChannel.read(fileBuf);
            System.out.println("--- client i="+i);
        }
        fileChannel.close();
        System.out.println("----- writeFile over ");
        Thread.sleep(60*60*1000);
        //     SafeClose.close(oos,ois);
        socketChannel.register(selector,SelectionKey.OP_READ);

        //socketChannel.finishConnect();

    }


    private void readFileMsg(SelectionKey selectionKey)throws Exception {
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
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        Message msg = (Message) obj;
        System.out.println(msg);
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
        System.out.println("---- enter client writeMsg ----");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(baos);

        Message msg = new Message();
        msg.setMsg("山不在高,有仙则名,水不在深,有龙则灵!");
        msg.setType(ChatEnum.MSG_STRING.CODE);
        msg.setSendTimestamp(System.currentTimeMillis());

        String[] ipAddrs = socketChannel.getRemoteAddress().toString().replace("/","").split(":");
        msg.setIp(ipAddrs[0]);
        msg.setPort(Integer.parseInt(ipAddrs[1]));
        msg.setUserName("[客户端发送]");
        oos.writeObject(msg);
        oos.flush();
        byte[] temp = baos.toByteArray();
        System.out.println("-- client temp.length:"+temp.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(temp);
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
   //     SafeClose.close(oos,ois);
        socketChannel.register(selector,SelectionKey.OP_READ);

        //socketChannel.finishConnect();

    }



}
