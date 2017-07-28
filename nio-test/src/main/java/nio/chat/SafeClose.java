package nio.chat;

import java.io.Closeable;

/**
 * Created by zhangshan193 on 16/11/7.
 */
public class SafeClose {


    public static void close(Closeable ...closeables){
        for(Closeable c:closeables){
            if(c == null) continue;
            try{
                c.close();
            }catch (Exception e){
                System.out.println(e);
            }finally {
                try{
                    c.close();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }
    }

}
