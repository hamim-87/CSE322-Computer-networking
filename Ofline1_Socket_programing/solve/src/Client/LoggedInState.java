package Client;

public class LoggedInState implements State{
    private Terminal terminal;
    private String red = "\u001B[31m";
    private String reset = "\u001B[0m";

    public LoggedInState(Terminal terminal){
        this.terminal = terminal;
    }

    public void showPrompt(){
        String prompt =red+ terminal.getUser()+"@cmd~"+reset;
        System.out.print(prompt);
    }
}
