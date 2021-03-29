package fsjMessaging;

import java.time.LocalDateTime;

public class ChatMessage extends Message{
    private long roomID;
    private int messageNum;

    public ChatMessage() {
    }

    public ChatMessage(String msgText, LocalDateTime msgDateTime, long userID, long roomID, int messageNum) {
        super(msgText, msgDateTime, userID);
        this.roomID = roomID;
        this.messageNum = messageNum;
    }
}
