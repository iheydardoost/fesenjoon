package fsjMessaging;

import java.time.LocalDateTime;

public class Message {
    private String msgText;
    private LocalDateTime msgDateTime;
    private long userID;

    public Message() {
    }

    public Message(String msgText, LocalDateTime msgDateTime, long userID) {
        this.msgText = msgText;
        this.msgDateTime = msgDateTime;
        this.userID = userID;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public LocalDateTime getMsgDateTime() {
        return msgDateTime;
    }

    public void setMsgDateTime(LocalDateTime sendDateTime) {
        this.msgDateTime = sendDateTime;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
