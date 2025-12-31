package Server;

import Utils.FileRequestInfo;
import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.util.List;

public class ShowTask implements Task{
    private NetworkUtils networkUtils;
    private Server server;

    public ShowTask(NetworkUtils networkUtils, Server server){
        this.networkUtils = networkUtils;
        this.server = server;
    }

    @Override
    public void execute(Request req) {
        String user =(String) req.getPayload().get("body");

        List<FileRequestInfo> info = server.requestInfo.get(user);

        Response res = new Response("SUCCESS",info);

        try {
            networkUtils.write(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
