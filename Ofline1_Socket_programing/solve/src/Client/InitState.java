package Client;

public class InitState implements State{

    private String green = "\u001B[32m";
    private String reset = "\u001B[0m";

    private Terminal terminal;

    public InitState(Terminal _terminal){
        this.terminal = _terminal;
    }

    @Override
    public void showPrompt(){
        String prompt =green+ "cmd~ "+reset+
                "\n"+"Do you want to log in? (type login)\n"+
                "Do you want to sign up? (type signup)\n"+
                "Do you want to quit? (type quit)";

        System.out.println(prompt);
    }

    public void login(){
        this.terminal.setState(this.terminal.getLogInState());
    }

    public void singup(){
        this.terminal.setState(this.terminal.getSignUpstate());
    }
}
