package nio.chat;

/**
 * Created by zhangshan193 on 16/11/11.
 */
public enum TransEnum {

    TRANS_START(0,"传输开始"),
    TRANS_SENDING(1,"传输中"),
    TRANS_OVER(2,"传输完成")

    ;
    int CODE;
    String MSG;

    private TransEnum(int CODE,String MSG){
        this.CODE = CODE;
        this.MSG = MSG;
    }

}
