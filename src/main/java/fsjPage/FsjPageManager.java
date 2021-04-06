package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjLogger.LogHandler;
import fsjMain.Main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FsjPageManager {
    private static final Page loginPage = new Page("Login Page", new CommandParser(), new ArrayList<>());
    private static final Page mainPage = new Page("Main Page",new CommandParser(),new ArrayList<>());
    public enum CompleteState{SIGN_UP_COMPLETE,LOG_IN_COMPLETE,NONE,EXIT,LOG_OUT,USER_INACTIVE}

    public FsjPageManager() {
    }

    public static void initLoginPage(){
        loginPage.addChoice("sign-up").addChoice("log-in");
        loginPage.getCommandParser().addValidCommand("exit").addValidCommand("select").addValidCommand("list");
        loginPage.getCommandParser().addValidTag("--choice");
    }

    public static void initMainPage(){
        mainPage.addChoice("personal-page").addChoice("timeline").addChoice("explorer").addChoice("messaging").addChoice("setting");
        mainPage.getCommandParser().addValidCommand("exit").addValidCommand("select").addValidCommand("list");
        mainPage.getCommandParser().addValidTag("--choice");
    }

    public static CompleteState mainPageManager(){
        CompleteState result;

        while(true){
            mainPage.printChoiceList();
            if(mainPage.getCommandParser().listenToUser()){
                switch (mainPage.getCommandParser().getCommand()){
                    case "exit":
                        return CompleteState.EXIT;
                    case "select":
                        result = selectCommand(mainPage);
                        if(result==CompleteState.EXIT || result==CompleteState.LOG_OUT)
                            return result;
                        else
                            break;
                    case "list":
                        mainPage.printChoiceList();
                        break;
                    default:
                        break;
                }
            }
        }
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
                        result = selectCommand(loginPage);
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

    private static CompleteState selectCommand(Page whatPage){
        if(whatPage.getCommandParser().getArgs().isEmpty()) {
            whatPage.getCommandParser().improperInput(false, "use : select --choice <XXX>");
            return CompleteState.NONE;
        }
        if(whatPage.getPageName().equals(loginPage.getPageName())) {
            switch (loginPage.getCommandParser().getArgs().get(0)) {
                case "sign-up":
                    return signupFunction();
                case "log-in":
                    return loginFunction();
                default:
                    loginPage.getCommandParser().improperInput(false, "choose a valid choice.");
                    return CompleteState.NONE;
            }
        }
        if(whatPage.getPageName().equals(mainPage.getPageName())){
            switch (mainPage.getCommandParser().getArgs().get(0)){
                case "personal-page":
                    return PersonalPage.personalPageManager();
                case "timeline":
                    return TimelinePage.timelinePageManager();
                case "explorer":
                    return ExplorerPage.explorerPageManager();
                case "messaging":
                    return MessagingPage.messagingPageManager();
                case "setting":
                    return SettingPage.settingPageManager();
                default:
                    mainPage.getCommandParser().improperInput(false,"choose a valid choice.");
                    return CompleteState.NONE;
            }
        }

        return CompleteState.NONE;
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
                    if(inStr.length()>0) {
                        try {
                            userBuilder.setDateOfBirth(LocalDate.parse(inStr, DateTimeFormatter.ISO_DATE));
                        } catch (Exception e) {
                            //e.printStackTrace();
                            loginPage.getCommandParser().improperInput(false, "date format must be (yyyy-mm-dd).");
                            break;
                        }
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
        LogHandler.logger.info("user "+ Long.toHexString(Main.mainUser.getUserID()) + " signed up successfully.");
        try {
            User.saveUserClass();
        } catch (Exception e) {
            //e.printStackTrace();
            LogHandler.logger.error("UserClass data could not be saved.");
        }
        return CompleteState.SIGN_UP_COMPLETE;
    }

    private static CompleteState loginFunction(){
        String inStr;
        long userID;
        User user;
        System.out.println("=== Log In ===");

        while(true) {
            /******************--User Name--*********************/
            System.out.print("enter user name :");
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

        user = User.loadUser(userID);
        if(user == null)
            return CompleteState.NONE;

        while(true){
            /******************--User Password--*********************/
            System.out.print("enter password :");
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
