package nio.chat;

import java.io.Serializable;

/**
 * Created by zhangshan193 on 16/11/3.
 */
public class Message implements Serializable{

    private static final long serialVersionUID = 3268410715976645487L;

    // 0 退出 1字符串消息 2 文件消息
    private int type;

    private String msg;

    private String fileName;

    private String suffixName;

    private long fileLength;

    private byte[] fileData;

    private long sendTimestamp;

    private String ip;

    private String userName;

    private int port;

    private int transType;

    private int readByteCount;

    private String charset = "UTF-8";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public String getSuffixName() {
        return suffixName;
    }

    public void setSuffixName(String suffixName) {
        this.suffixName = suffixName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", msg='" + msg + '\'' +
                ", fileName='" + fileName + '\'' +
                ", suffixName='" + suffixName + '\'' +
                ", fileLength=" + fileLength +
                ", transType=" + transType +
                ", sendTimestamp=" + sendTimestamp +
                ", ip='" + ip + '\'' +
                ", userName='" + userName + '\'' +
                ", port=" + port +
                ", readByteCount=" + readByteCount +
                ", charset='" + charset + '\'' +
                '}';
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public int getReadByteCount() {
        return readByteCount;
    }

    public void setReadByteCount(int readByteCount) {
        this.readByteCount = readByteCount;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void clear(){

    }
}
