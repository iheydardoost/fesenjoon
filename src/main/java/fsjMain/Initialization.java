package fsjMain;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjDataManager.JsonHandler;
import fsjLogger.LogHandler;
import fsjPage.FsjPageManager;

public class Initialization {

    public Initialization() {
    }

    public static void initClasses(){
        try {
            initCommands();
            JsonHandler.InitMapper();
            FsjPageManager.initLoginPage();
            LogHandler.initLogger(true);
            User.initUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initCommands(){
        /***----------------------Commands------------------------------***/
        //commandId should be an integer between 100 to 699
        CommandParser.addCommand(100, "exit");
        CommandParser.addCommand(101, "goto");
        CommandParser.addCommand(102, "delete");
        CommandParser.addCommand(103, "reject");
        CommandParser.addCommand(104, "accept");
        CommandParser.addCommand(105, "follow");
        CommandParser.addCommand(106, "select");
        CommandParser.addCommand(107, "search");
        CommandParser.addCommand(108, "return");
        CommandParser.addCommand(109, "comment");
        CommandParser.addCommand(110, "report");
        CommandParser.addCommand(111, "silent");
        CommandParser.addCommand(112, "block");
        CommandParser.addCommand(113, "send");
        CommandParser.addCommand(114, "forward");
        CommandParser.addCommand(115, "add");
        CommandParser.addCommand(116, "like");
        CommandParser.addCommand(117, "move");
        CommandParser.addCommand(118, "answer");
        CommandParser.addCommand(119, "remove");
        CommandParser.addCommand(120, "new");
        CommandParser.addCommand(121, "show");
        CommandParser.addCommand(122, "edit");
        CommandParser.addCommand(123, "list");
        CommandParser.addCommand(124, "help");

        /***----------------------Command_Tags------------------------------***/
        //tagId should be an integer between 700 to 999
        CommandParser.addCommandTag(700, "--choice");
        CommandParser.addCommandTag(701, "--tweet");
    }
}
