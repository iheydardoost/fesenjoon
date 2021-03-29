package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;

import java.util.ArrayList;

public class MessageRoom extends Page{
    private long roomID;
    private ArrayList<User> userList;
    private RoomType roomType;
    private int lastMessageNum;
    private static long lastMessageRoomID = Long.MIN_VALUE;

    public MessageRoom() {
    }

    private MessageRoom(String pageName, CommandParser commandParser, long roomID, RoomType roomType) {
        super(pageName, commandParser);
        this.roomID = roomID;
        this.userList = new ArrayList<>();
        this.roomType = roomType;
        this.lastMessageNum = 0;
    }

    public class MessageRoomBuilder{
        public String pageName;
        public CommandParser commandParser;
        public RoomType roomType;

        public MessageRoomBuilder setPageName(String pageName) {
            this.pageName = pageName;
            return this;
        }

        public MessageRoomBuilder setCommandParser(CommandParser commandParser) {
            this.commandParser = commandParser;
            return this;
        }

        public MessageRoomBuilder setRoomType(RoomType roomType) {
            this.roomType = roomType;
            return this;
        }

        public MessageRoom build(){
            return new MessageRoom(this.pageName,this.commandParser,generateID(),this.roomType);
        }
    }

    private long generateID(){
        return (++lastMessageRoomID);
    }

    enum RoomType {PRIVATE, GROUP}
}
