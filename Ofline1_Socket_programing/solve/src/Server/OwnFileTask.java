package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.IOException;

public class OwnFileTask implements Task{
    private NetworkUtils networkUtils;
    private  Server server;

    public OwnFileTask(NetworkUtils networkUtils, Server server){
        this.networkUtils = networkUtils;
        this.server = server;
    }

    @Override
    public void execute(Request req) {
        String name =(String) req.getPayload().get("body");
        Response response = new Response("ls",this.server.fileMeta,name);
        try {
            this.networkUtils.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
