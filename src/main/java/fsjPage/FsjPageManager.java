package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjDataManager.JsonHandler;
import fsjLogger.LogHandler;
import fsjMain.Main;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FsjPageManager {
    private static final Page loginPage = new Page("Login Page", new CommandParser(), new ArrayList<>());
    public enum CompleteState{SIGN_UP_COMPLETE,LOG_IN_COMPLETE,NONE,EXIT}

    public FsjPageManager() {
    }

    public static void initLoginPage(){
        loginPage.addChoice("sign-up");
        loginPage.addChoice("log-in");
        loginPage.getCommandParser().addValidCommand("exit").addValidCommand("select").addValidCommand("list");
        loginPage.getCommandParser().addValidTag("--choice");
    }

    public static CompleteState loginPageManager(){
        loginPage.printChoiceList();
        CompleteState result;

        while(true){
            if(loginPage.getCommandParser().listenToUser()){
                switch (loginPage.getCommandParser().getCommand()){
                    case "exit":
                        return CompleteState.EXIT;
                    case "select":
                        result = selectCommand();
                        if(result!=CompleteState.NONE) return result;
                        break;
                    case "list":
                        loginPage.printChoiceList();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static CompleteState selectCommand(){
        if(loginPage.getCommandParser().getArgs().isEmpty()) {
            loginPage.getCommandParser().improperInput(false, "use : select --choice <XXX>");
            return CompleteState.NONE;
        }
        switch (loginPage.getCommandParser().getArgs().get(0)){
            case "sign-up":
                return signupFunction();
            case "log-in":
                return loginFunction();
            default:
                loginPage.getCommandParser().improperInput(false, "choose a valid choice.");
                return CompleteState.NONE;
        }
    }

    private static CompleteState signupFunction(){
        int level=0;
        String inStr;
        User.UserBuilder userBuilder = new User.UserBuilder();

        System.out.println("=== Sign Up ===");
        String starMessage = "fields with \"*\" must be filled";
        System.out.println(starMessage);
        while(level<7) {
            switch (level) {
                case 0:
                    System.out.print("* enter user name :");
                    loginPage.getCommandParser().listen();
                    inStr = loginPage.getCommandParser().inStr;
                    if(inStr.length()==0) {
                        loginPage.getCommandParser().improperInput(false, starMessage);
                        break;
                    }
                    if(inStr.equals("--quit")) return CompleteState.NONE;
                    if(User.userNameList.containsKey(inStr)){
                        loginPage.getCommandParser().improperInput(false, "user name already exist.");
                        break;
                    }
                    userBuilder.setUserName(inStr);
                    level++;
                    break;
                case 1:
                    System.out.print("* enter first name :");
                    loginPage.getCommandParser().listen();
                    inStr = loginPage.getCommandParser().inStr;
                    if(inStr.length()==0) {
                        loginPage.getCommandParser().improperInput(false, starMessage);
                        break;
                    }
                    if(inStr.equals("--quit")) return CompleteState.NONE;
                    userBuilder.setFirstName(inStr);
                    level++;
                    break;
                case 2:
                    System.out.print("* enter last name :");
                    loginPage.getCommandParser().listen();
                    inStr = loginPage.getCommandParser().inStr;
                    if(inStr.length()==0) {
                        loginPage.getCommandParser().improperInput(false, starMessage);
                        break;
                    }
                    if(inStr.equals("--quit")) return CompleteState.NONE;
                    userBuilder.setLastName(inStr);
                    level++;
                    break;
                case 3:
                    System.out.print("* enter password :");
                    loginPage.getCommandParser().listen();
                    inStr = loginPage.getCommandParser().inStr;
                    if(inStr.length()==0) {
                        loginPage.getCommandParser().improperInput(false, starMessage);
                        break;
                    }
                    if(inStr.equals("--quit")) return CompleteState.NONE;
                    userBuilder.setPassword(inStr);
                    level++;
                    break;
                case 4:
                    System.out.print("enter date of birth (yyyy-mm-dd) :");
                    loginPage.getCommandParser().listen();
                    inStr = loginPage.getCommandParser().inStr;
                    if(inStr.equals("--quit")) return CompleteState.NONE;
                    try {
                        userBuilder.setDateOfBirth(LocalDate.parse(inStr, DateTimeFormatter.ISO_DATE));
                    } catch (Exception e) {
                        //e.printStackTrace();
                        loginPage.getCommandParser().improperInput(false,"date format must be (yyyy-mm-dd).");
                        break;
                    }
                    level++;
                    break;
                case 5:
                    System.out.print("* enter email :");
                    loginPage.getCommandParser().listen();
                    inStr = loginPage.getCommandParser().inStr;
                    if(inStr.length()==0) {
                        loginPage.getCommandParser().improperInput(false, starMessage);
                        break;
                    }
                    if(inStr.equals("--quit")) return CompleteState.NONE;
                    if(User.emailList.containsKey(inStr)){
                        loginPage.getCommandParser().improperInput(false, "there is an account signed up with this email.");
                        break;
                    }
                    userBuilder.setEmail(inStr);
                    level++;
                    break;
                case 6:
                    System.out.print("enter biography :");
                    loginPage.getCommandParser().listen();
                    inStr = loginPage.getCommandParser().inStr;
                    if(inStr.equals("--quit")) return CompleteState.NONE;
                    userBuilder.setBio(inStr);
                    level++;
                    break;
                default:
                    break;
            }
        }

        Main.mainUser = userBuilder.build();
        LogHandler.logger.info("user "+ Long.toHexString(Main.mainUser.userID) + " signed up successfully.");
        return CompleteState.SIGN_UP_COMPLETE;
    }

    private static CompleteState loginFunction(){
        String inStr;
        long userID;
        User user;
        System.out.println("=== Log In ===");

        while(true) {
            /******************--User Name--*********************/
            System.out.println("enter user name :");
            loginPage.getCommandParser().listen();
            inStr = loginPage.getCommandParser().inStr;
            if (inStr.length() == 0) continue;
            if (inStr.equals("--quit")) return CompleteState.NONE;
            if (!User.userNameList.containsKey(inStr)) {
                loginPage.getCommandParser().improperInput(false, "user name does not exist.");
                continue;
            }
            userID = User.userNameList.get(inStr);
            break;
        }

        String path = ".\\src\\main\\ServerData\\UserList\\"+Long.toHexString(userID)+".json";
        File file = new File(path);
        if(!file.exists())
            LogHandler.logger.error("file " + path + " does not exist.");
        try {
            user = JsonHandler.mapper.readValue(file,User.class);
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("can not read " + path);
            return CompleteState.NONE;
        }
        LogHandler.logger.info("user " + userID + " loaded.");

        while(true){
            /******************--User Password--*********************/
            System.out.println("enter password :");
            loginPage.getCommandParser().listen();
            inStr = loginPage.getCommandParser().inStr;
            if (inStr.equals("--quit")) return CompleteState.NONE;
            if(!user.isPassCorrect(inStr)){
                loginPage.getCommandParser().improperInput(false, "password is not correct.");
                continue;
            }else {
                Main.mainUser = user;
                break;
            }
        }

        LogHandler.logger.info("user " + userID + " logged in successfully.");
        return CompleteState.LOG_IN_COMPLETE;
    }

}
