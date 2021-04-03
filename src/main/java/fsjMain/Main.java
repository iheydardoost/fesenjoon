package fsjMain;

import fsjAccount.User;
import fsjDataManager.JsonHandler;
import fsjPage.FsjPageManager;

import java.io.File;
import java.io.IOException;

public class Main {
    public static User mainUser;

    public static void main(String[] args) {
        Initialization.initClasses();
        FsjPageManager.CompleteState state = FsjPageManager.CompleteState.LOG_OUT;

        while (true) {
            switch (state){
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

    public static void exitCommand(){
        try {
            User.saveUserClass();
            mainUser.saveToUserList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
