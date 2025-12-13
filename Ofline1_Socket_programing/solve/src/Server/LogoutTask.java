package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

public class LogoutTask implements Task{
    private NetworkUtils networkUtils;
    private UserDB userDB;

    public LogoutTask(NetworkUtils networkUtils){
        this.networkUtils = networkUtils;
        this.userDB = new UserDB();

    }

    @Override
    public void execute(Request req) {
        String user = (String) req.getPayload().get("body");

        boolean succ = this.userDB.removeOnlineUser(user);

        Response res;

        if(succ){
            res = new Response("SUCCESS",user+ "is logged out.");
        }else{
            res = new Response("FAIL","Logout fail.");
        }

        try{
            networkUtils.write(res);

        }catch (Exception e){
            System.out.println(e);
        }

    }
}
