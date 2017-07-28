package nio;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by zhangshan193 on 17/2/2.
 */
public class SocketClient {


    public static void main(String[] args) throws IOException {
        try {
                         // 1、创建客户端Socket，指定服务器地址和端口
                         // Socket socket=new Socket("127.0.0.1",5200);
             Socket socket;
            socket = new Socket("192.168.0.20", 8888);
            System.out.println("客户端启动成功");
                         // 2、获取输出流，向服务器端发送信息
                         // 向本机的52000端口发出客户请求
                         // 由系统标准输入设备构造BufferedReader对象
             PrintWriter pw = new PrintWriter(socket.getOutputStream());
                         // 由Socket对象得到输出流，并构造PrintWriter对象
                         //3、获取输入流，并读取服务器端的响应信息



             pw.write("hello");
            pw.flush();
            pw.close();
             socket.close(); // 关闭Socket

        } catch (Exception e) {
             System.out.println("can not listen to:" + e);// 出错，打印出错信息

        }

    }
}