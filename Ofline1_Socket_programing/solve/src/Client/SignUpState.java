package Client;

public class SignUpState implements State{
    private String green = "\u001B[32m";
    private String reset = "\u001B[0m";

    private Terminal terminal;
    public SignUpState(Terminal terminal){
        this.terminal = terminal;
    }

    @Override
    public void showPrompt() {
        String prompt =green+ "cmd~ "+reset+"SIGNUP\n"+
                "Enter: <username> <password>\n";

        System.out.println(prompt);

    }
}
