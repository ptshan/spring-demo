package nio.chat;

/**
 * Created by zhangshan193 on 16/11/3.
 */
public enum ChatEnum {
    // 0 退出 1字符串消息 2 文件消息
    QUIT(0,"退出"),
    MSG_STRING(1,"退出"),
    MSG_FILE(2,"文件消息 ")

    ;
    int CODE;
    String MSG;

    private ChatEnum(int CODE,String MSG){
        this.CODE = CODE;
        this.MSG = MSG;
    }

}
