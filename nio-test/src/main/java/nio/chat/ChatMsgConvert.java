package nio.chat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by zhangshan193 on 16/11/11.
 */
public class ChatMsgConvert implements Serializable{

    private static final long serialVersionUID = -8872574200492249247L;

    private ByteArrayOutputStream baos = null;
    private ObjectOutputStream oos = null;
   // private ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

    public ChatMsgConvert()
    {

        baos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
