package Client;


public class Terminal {

    private String user;
    private InitState initState;
    private LogInState logInState;
    private LoggedInState loggedInState;
    private SignUpState signUpstate;
    private State currentState;

    public Terminal(){
        this.user = null;
        this.initState = new InitState(this);
        this.loggedInState = new LoggedInState(this);
        this.logInState = new LogInState(this);
        this.signUpstate = new SignUpState(this);
        this.currentState = this.initState;

    }

    public void setState(State st){
        this.currentState = st;
    }

    public State getState(){
        return this.currentState;
    }

    public InitState getInitState(){
        return this.initState;
    }
    public LoggedInState getLoggedInState(){
        return  this.loggedInState;
    }
    public LogInState getLogInState(){
        return this.logInState;
    }
    public SignUpState getSignUpstate(){
        return this.signUpstate;
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }
}
