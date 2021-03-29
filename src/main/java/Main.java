import fsjCLI.CommandParser;

public class Main {
    public static void main(String[] args) {
            Initialization.initClasses();
            CommandParser commandParser = new CommandParser();

            while (true) {
                commandParser.listenToUser();
                System.out.println("command = " + commandParser.getCommand());
                System.out.println("args = " + commandParser.getArgs());
                System.out.println("argTags = " + commandParser.getArgTags());
            }
    }
}
