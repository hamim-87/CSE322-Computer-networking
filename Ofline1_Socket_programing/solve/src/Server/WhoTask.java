package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.util.List;

public class WhoTask implements Task{
    private NetworkUtils networkUtils;
    private UserDB userDB;
    public WhoTask(NetworkUtils networkUtils){
        this.networkUtils = networkUtils;
        userDB = new UserDB();
    }

    @Override
    public void execute(Request req) {
        try {
            List<String> usr = userDB.showAllUser();
            Response response = new Response("SUCCESS",usr.toString());
            try{
                networkUtils.write(response);
            }catch (Exception e){
                System.out.println(e);
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }
}
