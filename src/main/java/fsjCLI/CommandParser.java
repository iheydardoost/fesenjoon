package fsjCLI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

@JsonIgnoreProperties({"inStr","scanner","commandsMap","tagsMap","validCommands","validTags"})

public class CommandParser implements TextInputInterface, ConsoleColors{
    public String inStr;
    static private Scanner scanner = new Scanner(System.in);
    private String command;
    private LinkedList<String> args;
    private LinkedList<Integer> argTags;
    static public Hashtable<String, Integer> commandsMap = new Hashtable<>();
    static public Hashtable<String, Integer> tagsMap = new Hashtable<>();
    private ArrayList<String> validCommands;
    private ArrayList<String> validTags;

    public CommandParser() {
        this.command = null;
        this.args = new LinkedList<String>();
        this.argTags = new LinkedList<Integer>();
        this.validCommands = new ArrayList<>();
        this.validTags = new ArrayList<>();
    }

    public LinkedList<String> getArgs() { return this.args; }

    public LinkedList<Integer> getArgTags() { return this.argTags; }

    public String getCommand() {
        return this.command;
    }

    public CommandParser addValidCommand(String commandStr){
        if(validCommands.contains(commandStr)) return this;
        validCommands.add(commandStr);
        return this;
    }

    public CommandParser addValidTag(String tagStr){
        if(validTags.contains(tagStr)) return this;
        validTags.add(tagStr);
        return this;
    }

    public static void addCommand(Integer commandID, String commandWord){
        if(commandsMap.containsValue(commandWord)) return;
        commandsMap.put(commandWord, commandID);
    }

    public static void addCommandTag(Integer tagID, String tagWord){
        if(tagsMap.containsKey(tagWord)) return;
        tagsMap.put(tagWord, tagID);
    }

    public boolean listenToUser(){
        listen();
        parseCommand();
        if(this.command==null) return false;
        if(this.command.equals("help")){
            printHelp();
            return false;
        }
        return true;
    }

    private void parseCommand(){
        String[] words = this.inStr.split("\\s+");
        if(!commandsMap.containsKey(words[0])) {
            improperInput(false, words[0] + " is not a command.");
            return;
        }
        if(!validCommands.contains(words[0]) && !words[0].equals("help")) {
            improperInput(false, words[0] + " is not a valid command in this page.");
            return;
        }
        this.command = words[0];

        for(int i=1;i<words.length;i++){
            if(i%2==1){
                if(!tagsMap.containsKey(words[i])) {
                    improperInput(false, words[i] + " is not a tag.");
                    return;
                }
                if(!validTags.contains(words[i])){
                    improperInput(false, words[i] + " is not a valid tag in this page.");
                    return;
                }
                this.argTags.add(tagsMap.get(words[i]));
            }else{
                this.args.add(words[i]);
            }
        }
    }

    private void printValidCommands(){
        System.out.println(WHITE_BACKGROUND + "Valid Commands List :" + COLOR_RESET);
        for(int i=0;i<this.validCommands.size();i++){
            System.out.println(BLUE_BRIGHT + this.validCommands.get(i) + COLOR_RESET);
        }
    }

    private void printValidTags(){
        System.out.println(WHITE_BACKGROUND + "Valid Tags List :" + COLOR_RESET);
        for(int i=0;i<this.validTags.size();i++){
            System.out.println(BLUE_BRIGHT + this.validTags.get(i) + COLOR_RESET);
        }
    }

    private void printHelp(){
        if(this.validCommands.size()!=0) printValidCommands();
        if(this.validTags.size()!=0) printValidTags();
    }

    @Override
    public boolean askYesNoQ(String question) {
        while(true) {
            System.out.println(question);
            System.out.println("answer (Y/N) >>");
            this.inStr = scanner.nextLine();
            if (this.inStr.length() != 1) {
                improperInput(false, "Please write Y(yes) or N(no)");
                continue;
            } else {
                if (this.inStr.charAt(0) == 'y' || this.inStr.charAt(0) == 'Y') return true;
                else if (this.inStr.charAt(0) == 'n' || this.inStr.charAt(0) == 'N') return false;
                else continue;
            }
        }
    }

    @Override
    public void listen() {
        this.command = null;
        this.args.clear();
        this.argTags.clear();
        this.inStr = scanner.nextLine();
    }

    @Override
    public void improperInput(boolean defaultMsg,String msg) {
        this.command = null;
        this.args.clear();
        this.argTags.clear();
        if(defaultMsg) System.out.println( RED_UNDERLINED + improperMsg + COLOR_RESET);
        else System.out.println(RED_UNDERLINED + msg + COLOR_RESET);
    }

}
