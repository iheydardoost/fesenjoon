package fsjMessaging;

import java.time.LocalDateTime;

public class ChatMessage extends Message{
    private long roomID;
    private long messageID;
    private long lastMessageID = Long.MIN_VALUE;

    public ChatMessage() {
    }

    public ChatMessage(String msgText, LocalDateTime msgDateTime, long userID, long roomID, long messageID) {
        super(msgText, msgDateTime, userID);
        this.roomID = roomID;
        this.messageID = messageID;
    }

    public long generateID(){
        return (++lastMessageID);
    }
}
