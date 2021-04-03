package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjDataManager.JsonHandler;
import fsjLogger.LogHandler;
import fsjMain.Main;
import fsjMessaging.Tweet;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PersonalPage {
    private static final Page personalPage = new Page("Personal Page", new CommandParser(), new ArrayList<>());

    public PersonalPage() {
    }

    public static void initPersonalPage() {
        personalPage.addChoice("new --tweet").addChoice("goto --list myTweets").addChoice("edit --info <XXX>");
        personalPage.addChoice("show --info").addChoice("goto --list followings").addChoice("goto --list followers");
        personalPage.addChoice("goto --list blackList").addChoice("goto --list notifications");
        personalPage.getCommandParser().addValidCommand("new").addValidCommand("show").addValidCommand("goto");
        personalPage.getCommandParser().addValidCommand("exit").addValidCommand("edit").addValidCommand("list").addValidCommand("return");
        personalPage.getCommandParser().addValidTag("--tweet").addValidTag("--list").addValidTag("--info");
    }

    public static FsjPageManager.CompleteState personalPageManager() {
        personalPage.printChoiceList();
        FsjPageManager.CompleteState state = FsjPageManager.CompleteState.NONE;

        while (true) {
            if (personalPage.getCommandParser().listenToUser()) {
                switch (personalPage.getCommandParser().getCommand()) {
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    case "new":
                        state = newCommand();
                        break;
                    case "show":
                        state = showCommand();
                        break;
                    case "goto":
                        state = gotoCommand();
                        break;
                    case "list":
                        personalPage.printChoiceList();
                        break;
                    case "edit":
                        state = editCommand();
                        break;
                    default:
                        personalPage.getCommandParser().improperInput(false, "choose a valid command.");
                        state = FsjPageManager.CompleteState.NONE;
                        break;
                }
            }

            if (state == FsjPageManager.CompleteState.EXIT) return state;
        }
    }

    private static FsjPageManager.CompleteState newCommand() {

        if (personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--tweet")) {
            personalPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }

        String msg = "";
        System.out.print("type your tweet\n(type \"--end\" in one line to end)\n(type \"--quit\" to cancel)\n:");
        while(true) {
            personalPage.getCommandParser().listen();
            if(personalPage.getCommandParser().inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
            if(personalPage.getCommandParser().inStr.equals("--end")) break;
            msg += personalPage.getCommandParser().inStr;
            msg += "\n";
        }

        Tweet tweet = new Tweet(msg,LocalDateTime.now(), Main.mainUser);
        String path = ".\\src\\main\\ServerData\\Tweets\\" + Long.toHexString(tweet.getUserID());
        File file1 = new File(path);
        if(!file1.isDirectory()) file1.mkdir();
        path = path + "\\" + Long.toHexString(tweet.getTweetID());
        File file2 = new File(path);
        try {
            JsonHandler.mapper.writeValue(file2,tweet);
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("tweet " + tweet.getTweetID() + " could not be saved.");
        }

        System.out.println("your tweet is successfully saved.");
        LogHandler.logger.info("user (ID) " + tweet.getUserID() + " tweeted (ID) " + tweet.getTweetID());
        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState showCommand() {
        if (personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--info")) {
            personalPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }
        Main.mainUser.printUserInfo();
        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState editCommand() {
        if (personalPage.getCommandParser().getArgs().size() != 1 &&
                personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--info")) {
            personalPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }

        String inStr;
        switch (personalPage.getCommandParser().getArgs().get(0)){
            case "firstName":
                System.out.print("enter first name :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.length()==0) {
                    personalPage.getCommandParser().improperInput(false, "can not be empty.");
                    break;
                }
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setFirstName(inStr);
                System.out.println("firstName changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.userID + " changed firstName.");
            case "lastName":
                System.out.print("enter last name :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.length()==0) {
                    personalPage.getCommandParser().improperInput(false, "can not be empty.");
                    break;
                }
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setLastName(inStr);
                System.out.println("lastName changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.userID + " changed lastName.");
            case "phoneNumber":
                System.out.print("enter phoneNumber :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setPhoneNumber(inStr);
                System.out.println("phoneNumber changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.userID + " changed phoneNumber.");
            case "bio":
                System.out.print("enter bio :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setBio(inStr);
                System.out.println("bio changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.userID + " changed bio.");
            default:
                personalPage.getCommandParser().improperInput(false, "can not change.");
                break;
        }

        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState gotoCommand() {
        if (personalPage.getCommandParser().getArgs().size() != 1 &&
                personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--list")) {
            personalPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }
        return FsjPageManager.CompleteState.NONE;
    }

}
