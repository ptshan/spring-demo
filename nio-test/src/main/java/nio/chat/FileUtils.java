package nio.chat;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by zhangshan193 on 16/12/1.
 */
public class FileUtils {

    /**
     * 获取文件的二进制字节数组
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] getFileBytes(String path)throws Exception{
        File file = new File(path);
        System.out.println("文件大小:"+file.length());
        FileChannel fileChannel = new FileInputStream(file).getChannel();

        // 将对象写到内存
        ByteArrayOutputStream fileBaos = new ByteArrayOutputStream();
        ByteBuffer fileBuf = ByteBuffer.allocate(ChatConstants.BUF_FILE_SIZE);
        fileBuf.clear();
        int i = fileChannel.read(fileBuf);

        while(i > -1){
            fileBuf.flip();
            if(fileBuf.hasArray()) {
                fileBaos.write(fileBuf.array(), 0, i);
                fileBaos.flush();
            }else{
                System.out.println("getFile2Bytes fileBuf 不存在字节数组");
            }
            fileBuf.clear();
            i = fileChannel.read(fileBuf);
        }
        return fileBaos.toByteArray();
    }

    /**
     * 将msg对象写到socketChannel中
     * @param socketChannel
     * @param msg
     * @throws Exception
     */
    public static void writeMsg2SocketChannel(SocketChannel socketChannel,Message msg)throws Exception{
        ByteBuffer buf = ByteBuffer.allocate(ChatConstants.BUF_SIZE);
        buf.clear();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        // 将对象写到内存
        oos.writeObject(msg);
        // 将对象字节数组,写入socketChannel
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        byte[] byteBuf = new byte[ChatConstants.BUF_SIZE];
        int i = bais.read(byteBuf);
        buf.clear();
        // 将对象字节数组循环写到服务器
        while(i > -1){
            buf.put(byteBuf,0,i);
            buf.flip();
            while(buf.hasRemaining()) {
                socketChannel.write(buf);
            }
            buf.clear();
            i = bais.read(byteBuf);
        }
    }

    /**
     * 从SocketChannel中获取Msg对象
     * @param socketChannel
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Message readMsgFromSocketChannel(SocketChannel socketChannel)throws IOException,ClassNotFoundException{
        ByteBuffer buf = ByteBuffer.allocate(ChatConstants.BUF_SIZE);
        buf.clear();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int i = socketChannel.read(buf);

        while(i > -1){
            if(buf.hasArray()) {
                baos.write(buf.array(), 0, i);
                baos.flush();
            }else{
                System.out.println("getMsgFromSocketChannel buf 不存在字节数组");
            }

            buf.clear();
            i = socketChannel.read(buf);
        }
        System.out.println("server length:"+baos.toByteArray().length);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        Object obj = ois.readObject();
        Message msg = null;
        if(obj instanceof Message){
            msg = (Message) obj;
        }
        return msg;
    }

    /**
     *
     * @param msg
     * @param path 文件绝对路径和文件名
     * @throws Exception
     */
    public static void writeMsg2File(Message msg,String path)throws Exception{



        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(msg.getFileData());
        fos.flush();
        fos.close();

    }


}
