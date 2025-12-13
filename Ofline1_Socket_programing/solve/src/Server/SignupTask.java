package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.IOException;

public class SignupTask implements Task{
    private  UserDB userDB;
    private Response res;
    private NetworkUtils networkUtils;


    public SignupTask(NetworkUtils networkUtils){
        this.userDB = new UserDB();
        this.networkUtils = networkUtils;
    }

    public void execute(Request req) {

        String input = (String) req.getPayload().get("body");
        String[] parts = input.split(" ");



        System.out.println(parts[0]);
        System.out.println(parts[1]);
        System.out.println(parts.length);

        if(parts.length>2){
            this.res = new Response("FAIL", "Invalid Format...");
        }else{
            String username = parts[0];
            String password = parts[1];
            try{
                if(userDB.getUser(username) == null){
                    userDB.setUser(username,password);
                    this.res = new Response("SUCCESS",username+" is created.");
                }else{
                    this.res = new Response("FAIL","User already exist");
                }



            }catch (Exception e){
                System.out.println(e);
            }


        }

        try{
            networkUtils.write(this.res);

        }catch (Exception e){
            System.out.println(e);
        }



    }
}
