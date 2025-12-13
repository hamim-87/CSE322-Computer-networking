package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.IOException;

public class ShowFilesTask implements Task{
    private  NetworkUtils networkUtils;
    private  Server server;

    public ShowFilesTask(NetworkUtils networkUtils, Server server){
        this.networkUtils = networkUtils;
        this.server = server;
    }

    @Override
    public void execute(Request req) {
        Response response = new Response("find",this.server.fileMeta);
        try {
            this.networkUtils.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
