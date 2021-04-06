package fsjMain;

import com.fasterxml.jackson.core.type.TypeReference;
import fsjAccount.User;
import fsjDataManager.JsonHandler;
import fsjLogger.LogHandler;
import fsjPage.FsjPageManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Main {
    public static User mainUser;
    private static ArrayList<String> reportedMessages;
    private static Hashtable<String, Long> deletedUsers;

    public static void main(String[] args) {
        Initialization.initClasses();
        FsjPageManager.CompleteState state = FsjPageManager.CompleteState.LOG_OUT;

        while (true) {
            switch (state) {
                case LOG_OUT:
                    state = FsjPageManager.loginPageManager();
                    break;
                case LOG_IN_COMPLETE:
                    state = FsjPageManager.mainPageManager();
                    break;
                case SIGN_UP_COMPLETE:
                    state = FsjPageManager.mainPageManager();
                    break;
                case EXIT:
                    exitCommand();
                    break;
            }

            System.out.println(state);
        }
    }

    public static void exitCommand() {
        try {
            User.saveUserClass();
            mainUser.lastSeen= LocalDateTime.now();
            mainUser.saveToUserList();
            Main.saveServerData();
        } catch (Exception e) {
            //e.printStackTrace();
            LogHandler.logger.error("data could not be saved on exit.");
        }
        System.exit(0);
    }

    public static void initServer() {
        String path1 = ".\\src\\main\\ServerData\\Report.json";
        File file1 = new File(path1);
        String path2 = ".\\src\\main\\ServerData\\deletedUsers.json";
        File file2 = new File(path2);
        try {
            if (file1.isFile())
                reportedMessages = (ArrayList<String>) JsonHandler.mapper.readValue(file1, new TypeReference<List<String>>() {
                });
            if (file2.isFile())
                deletedUsers = JsonHandler.mapper.readValue(file1, new TypeReference<Hashtable<String, Long>>() {
                });
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("Server data could not be loaded.");
        }
    }

    private static void saveServerData() throws IOException {
        String path1 = ".\\src\\main\\ServerData\\Report.json";
        File file1 = new File(path1);
        JsonHandler.mapper.writeValue(file1, reportedMessages);
        String path2 = ".\\src\\main\\ServerData\\deletedUsers.json";
        File file2 = new File(path2);
        JsonHandler.mapper.writeValue(file2, deletedUsers);
    }

    public static void deleteUser(){
        deletedUsers.put(mainUser.userName,mainUser.getUserID());
    }
    public static void report(String str) {
        str += " ,reporterUserID " + mainUser.getUserID();
        reportedMessages.add(str);
    }
}
