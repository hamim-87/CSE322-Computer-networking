package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.IOException;
import java.util.ArrayList;

public class UploadTask implements Task{
    private NetworkUtils networkUtils;
    private Server server;

    public UploadTask(NetworkUtils networkUtils, Server server){
        this.networkUtils = networkUtils;
        this.server = server;

    }

    @Override
    public void execute(Request req) {
        try {
            long fileSize =(long) req.getPayload().get("filesize");
            Response response;
            if(server.current_buffer_size+ fileSize > server.MAX_BUFFER_SIZE ){
                response = new Response("FAIL","Buffer overflow.Can't upload this file");
            }else{
                long random_chunk = (long)(Math.random() * (server.MAX_CHUNK_SIZE - server.MIN_CHUNK_SIZE + 1)) + server.MIN_CHUNK_SIZE;
                long fileId = server.fileIDCount++;
                String fileName = (String) req.getPayload().get("filename");
                String privacy = (String)  req.getPayload().get("privacy");
                String username = (String) req.getPayload().get("username");

                server.fileMeta.put(fileId,new ArrayList<>());
                server.fileMeta.get(fileId).add(fileName);
                server.fileMeta.get(fileId).add(privacy);
                server.fileMeta.get(fileId).add(username);


                String priv = server.fileMeta.get(fileId).get(1);
                String filename = server.fileMeta.get(fileId).get(0);
                System.out.println(priv);
                System.out.println(filename);



                response = new Response("SUCCESS", "File "+fileName+" (fileID="+fileId+") is uploading in background with chunk size="+random_chunk);

            }

            try {
                networkUtils.write(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
