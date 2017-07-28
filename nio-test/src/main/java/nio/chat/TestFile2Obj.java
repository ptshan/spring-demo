package nio.chat;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhangshan193 on 16/12/1.
 */
public class TestFile2Obj {

    @Test
    public void fileChange()throws Exception{

        //msg2File();
//        文件大小:8617253
//        文件字节数组大小:8617253
//        message文件大小:8617627
        Message msg = file2Msg();
        System.out.println("msg:"+msg);
        System.out.println("msg fileByteSize:"+msg.getFileData().length);

    }

    private Message file2Msg()throws Exception{
        InputStream is = new FileInputStream(ChatConstants.WRITE_FILE_DIR+"message.txt");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buf = new byte[ChatConstants.BUF_SIZE];
//        int i = is.read(buf);
//        while(i != -1){
//            baos.write(buf,0,i);
//            i = is.read(buf);
//        }
        //message文件大小:8617627
        ObjectInputStream ois = new ObjectInputStream(is);
        Message msg = (Message)ois.readObject();
        return msg;

    }

    private void msg2File()throws Exception{
        Message msg = wrapMsg();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        // 将对象字节数组,写入socketChannel
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        FileOutputStream fos = new FileOutputStream(ChatConstants.WRITE_FILE_DIR+"message.txt");
        byte[] buf = new byte[ChatConstants.BUF_SIZE];
        int i = bais.read(buf);
        while(i != -1){
            fos.write(buf,0,i);
            i = bais.read(buf);
        }
        // 8617627
        System.out.println("message文件大小:"+new File(ChatConstants.WRITE_FILE_DIR+"message.txt").length());

    }

    private Message wrapMsg()throws Exception{
        Message msg = new Message();
        //msg.setMsg("山不在高,有仙则名,水不在深,有龙则灵!");
        msg.setType(ChatEnum.MSG_FILE.CODE);

        msg.setSendTimestamp(System.currentTimeMillis());
        msg.setTransType(TransEnum.TRANS_OVER.CODE);

        msg.setIp("127.0.0.1");
        msg.setPort(Integer.parseInt("6688"));
        msg.setUserName("[客户端发送]");
        msg.setFileName("apache-maven-3.3.9-bin.zip");
        msg.setFileLength(8617253);
        byte[] fileBytes = getFile2Bytes();
        System.out.println("文件字节数组大小:"+fileBytes.length);
        msg.setFileData(fileBytes);

        return msg;
    }

    private byte[] getFile2Bytes()throws Exception{
        String fname = "/Users/zhangshan193/Downloads/apache-maven-3.3.9-bin.zip" ;
        File file = new File(fname);
        System.out.println("文件大小:"+file.length());
        FileChannel fileChannel = new FileInputStream(file).getChannel();

        // 将对象写到内存
        ByteArrayOutputStream fileBaos = new ByteArrayOutputStream();
        ByteBuffer fileBuf = ByteBuffer.allocate(ChatConstants.BUF_FILE_SIZE);
        fileBuf.clear();
        int i = fileChannel.read(fileBuf);
        long fileLength = file.length();

        while(i > -1){
            fileBuf.flip();
            fileBaos.write(fileBuf.array(),0,i);
            fileBaos.flush();


            fileBuf.clear();
            i = fileChannel.read(fileBuf);
        }
        return fileBaos.toByteArray();
    }






}
