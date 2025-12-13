package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class LoginTask implements Task{

    private NetworkUtils networkUtils;
    private UserDB userDB;
    private Response res;
    private Server server;

    public LoginTask(NetworkUtils networkUtils,Server server){
        this.networkUtils = networkUtils;
        this.userDB = new UserDB();
        this.server = server;
    }

    @Override
    public void execute(Request req) {

        String input = (String) req.getPayload().get("body");
        String[] parts = input.split(" ");

        if(parts.length>2){
            this.res = new Response("FAIL", "Invalid Format...");
        }else{
            String username = parts[0];
            String password = parts[1];
            try{
                if(userDB.getUser(username) == null){
                    this.res = new Response("FAIL","Please create an account");

                }else{
                    if(userDB.isValidPassword(username,password)){
                        if(userDB.isOnline(username)){
                            this.res = new Response("FAIL", "User Already logged in!");
                        }else{
                            this.res = new Response("SUCCESS","Log in successful.");
                            userDB.setOnlineUser(username);
                            this.createDir(username);
                            this.server.addUserConnection(username,networkUtils);
                        }

                    }else{
                        this.res = new Response("FAIL","Password is not correct");

                    }
                }




            }catch (Exception e){
                System.out.println(e);
            }


        }

        try {
            networkUtils.write(this.res);

        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void createDir(String username){
        Path userDir = Paths.get("src/Server/Storage/",username);
        if(Files.exists(userDir)){
            return;
        }
        try{
            Files.createDirectories(userDir);
            System.out.println("Directory is created for "+username);
        }catch (Exception e){
            System.out.println(e);
        }
    }


}
