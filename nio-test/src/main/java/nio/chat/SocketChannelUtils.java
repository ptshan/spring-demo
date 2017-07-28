package nio.chat;

import java.io.File;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by zhangshan193 on 16/12/1.
 */
public class SocketChannelUtils {

    public static void recMsg(SelectionKey selectionKey,Selector selector)throws Exception{
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

    public static void sendMsg(SelectionKey selectionKey,Selector selector)throws Exception{
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

        System.out.println("----- writeFile2 over ");
        // Thread.sleep(60*60*1000);
        //     SafeClose.close(oos,ois);
        socketChannel.register(selector,SelectionKey.OP_READ);
        // 只有当socketChannel.close()的时候,服务端才会返回-1,否则服务端返回0;
        socketChannel.close();
    }



}
