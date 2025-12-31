package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;
import Utils.Log;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DownloadTask implements Task{

    private NetworkUtils networkUtils;
    private Server server;

    public DownloadTask(NetworkUtils networkUtils, Server server){
        this.networkUtils = networkUtils;
        this.server = server;
    }

    @Override
    public void execute(Request req) {

        long fileId = Long.parseLong((String) req.getPayload().get("body")) ;

        System.out.println("file id "+ fileId);

        List<String> metadata = server.fileMeta.get(fileId);


        Response response;
        if (metadata == null || metadata.size() < 3) {
             response= new Response("FAIL","Invalid fileID.");

        }else{
            String fileName = metadata.get(0);
            String privacy = metadata.get(1);
            String user = metadata.get(2);

            Log.log(user,"download",fileName);

            String filePath = Paths.get("src/Server/Storage", user, privacy, fileName).toString();
            response = new Response("SUCCESS",filePath);

        }
        try {
            networkUtils.write(response);

        }catch (Exception e){
            System.out.println(e);
        }


    }
}
