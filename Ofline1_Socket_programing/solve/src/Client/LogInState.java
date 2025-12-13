package Client;

public class LogInState implements State{
    private String green = "\u001B[32m";
    private String reset = "\u001B[0m";

    private Terminal terminal;
    public LogInState(Terminal _terminal){
        this.terminal = _terminal;
    }

    public void showPrompt(){
        String prompt =green+ "cmd~ "+reset+"LOGIN\n"+
                "Enter: <username> <password>\n";


        System.out.println(prompt);
    }
}
