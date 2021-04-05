package fsjPage;

import fsjCLI.CommandParser;

import java.util.ArrayList;

public class MessageRoom extends Page{
    private long roomID;
    private ArrayList<Long> userIdList;
    private RoomType roomType;
    private int lastMessageID;
    private static long lastMessageRoomID = Long.MIN_VALUE;

    public MessageRoom() {
    }

    private MessageRoom(String pageName, CommandParser commandParser, ArrayList<String> choiceList, long roomID, RoomType roomType) {
        super(pageName, commandParser, choiceList);
        this.roomID = roomID;
        this.userIdList = new ArrayList<>();
        this.roomType = roomType;
        this.lastMessageID = 0;
    }

    public class MessageRoomBuilder{
        public String pageName;
        public CommandParser commandParser;
        public RoomType roomType;
        public ArrayList<String> choiceList;

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

        public MessageRoomBuilder setChoiceList(ArrayList<String> choiceList){
            this.choiceList = choiceList;
            return this;
        }

        public MessageRoom build(){
            return new MessageRoom(this.pageName,this.commandParser,this.choiceList,generateID(),this.roomType);
        }
    }

    private long generateID(){
        return (++lastMessageRoomID);
    }

    enum RoomType {PRIVATE, GROUP}
}
