package fsjCLI;

public interface TextInputInterface {
    public static String improperMsg = "The command line you entered is improper.";

    public void listen();
    public void improperInput(boolean defaultMsg,String msg);
    public boolean askYesNoQ(String question);
}
