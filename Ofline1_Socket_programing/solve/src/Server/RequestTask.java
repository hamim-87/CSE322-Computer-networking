package Server;

import Utils.FileRequestInfo;
import Utils.NetworkUtils;
import Utils.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestTask implements Task{

    private NetworkUtils networkUtils;
    private  Server server;
    public RequestTask(NetworkUtils networkUtils, Server server){
        this.networkUtils = networkUtils;
        this.server = server;
    }
    @Override
    public void execute(Request req) {
        String user = (String) req.getPayload().get("username");
        String des = (String) req.getPayload().get("body");
        String sender = (String) req.getPayload().get("sender");




        if(user.equalsIgnoreCase("all")){
            for(String usr: server.getUsers()){
                FileRequestInfo info = new FileRequestInfo((int)server.reqId++,sender,des,usr);

                server.addRequestInfo(usr,info);
                System.out.println(server.requestInfo.get(usr));

            }
        }else{
                FileRequestInfo in = new FileRequestInfo((int)server.reqId++,sender,des,user);
                server.requestInfo
                        .computeIfAbsent(user, k -> new ArrayList<>())
                        .add(in);

                System.out.println(server.requestInfo.get(user));


        }
    }
}
