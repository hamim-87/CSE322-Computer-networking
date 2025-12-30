package Server;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HandleData implements Runnable{
    private NetworkUtils dataNetworkUtils;
    private Server server;
    private long totalChunk;
    private String user;
    private long fileId;

    public HandleData(Socket socket,Server server) throws IOException {
        this.dataNetworkUtils = new NetworkUtils(socket);
        this.server = server;
        try {
            Request request =(Request) dataNetworkUtils.read();

            String user = (String) request.getPayload().get("body");
            this.user = user;

            server.dataConnections.put(user,dataNetworkUtils);


        }catch (Exception e){
            System.out.println(e);
        }

        Thread thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {

        while(true){
            try {
                Request request = (Request) dataNetworkUtils.read();

                if(request.getHeader().equalsIgnoreCase("start")){

                    System.out.println("receiving ....");

                    String total_s = (String) request.getPayload().get("body");
                    String filid = (String) request.getPayload().get("username");
                    this.fileId = Integer.parseInt(filid);

                    System.out.println("File id is : "+ filid);

                    this.totalChunk = Integer.parseInt(total_s);
                    try{
                        Response res = new Response("uploadShit", "start the upolad");
                        this.dataNetworkUtils.write(res);


                        ArrayList<byte[]> chunks = receive_chunks();
                        Response resp = new Response("done","File uploaded..");
                        this.dataNetworkUtils.write(resp);

                        byte[] file = merge_chunks(chunks);

                        saveFile(file);
                        System.out.println("file save");




                    }catch (Exception e){
                        System.out.println(e);
                    }
                }else if(request.getHeader().equalsIgnoreCase("download")){

                    String pat =(String) request.getPayload().get("body");
                    File file = new File(pat);

                    int rem = (int) file.length();
                    FileInputStream fs = new FileInputStream(file);

                    while(rem>0){
                        byte[] chunk = new byte[Math.min((int)this.server.MAX_CHUNK_SIZE,rem)];
                        try {
                            fs.read(chunk);
                            rem -= chunk.length;

                            Response res = new Response("continue", chunk);
                            try{
                                this.dataNetworkUtils.write(res);



                            }catch (Exception e){
                                System.out.println(e);
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    String fileName = Paths.get(pat).getFileName().toString();
                    System.out.println(fileName);
                    Response rq = new Response("finished",fileName);

                    this.dataNetworkUtils.write(rq);
                }


            }catch (Exception e){
                System.out.println(e);
            }

        }


    }

    ArrayList<byte[]> receive_chunks() throws Exception {
        ArrayList<byte[]> chunks = new ArrayList<byte[]>();
        Request request = (Request) dataNetworkUtils.read();
        while (request.getHeader().equalsIgnoreCase("upload")) {
            byte[] chunk = (byte[]) request.getPayload().get("chunk");
            chunks.add(chunk);
            Response response = new Response("SUCCESS","Chunk receive");
            this.dataNetworkUtils.write(response);
            request = (Request) dataNetworkUtils.read();
        }
        return chunks;
    }

    byte[] merge_chunks(ArrayList<byte[]> _chunks) {
        int total_chunksize = (int) this.totalChunk;
        byte[] merged_chunk = new byte[total_chunksize];
        int current_idx = 0;
        for (byte[] chunk : _chunks) {
            for (int i = 0; i < chunk.length; i++) {
                merged_chunk[current_idx++] = chunk[i];
            }
        }
        return merged_chunk;
    }

    public void saveFile(byte[] chunk) throws IOException {

        Path basePath = Paths.get("src/Server/Storage", this.user);

        String privacy = server.fileMeta.get(fileId).get(1);
        String filename = server.fileMeta.get(fileId).get(0);

        Path dirPath = basePath.resolve(privacy);

        File dir = dirPath.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, filename);

        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(chunk);
        }
    }
}
