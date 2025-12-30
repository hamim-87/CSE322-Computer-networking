package Client;

import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class RecieveFile implements Runnable{

    NetworkUtils dataNet;
    String path;
    String filename;

    public RecieveFile(String path, NetworkUtils dataNet,String filename){

        this.path = path;
        this.dataNet = dataNet;
        System.out.println(filename);
        this.filename = filename;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        Request req = new Request("download",this.path);

        try {
            dataNet.write(req);

            ArrayList<byte[]> chunks = receive_chunks();

            byte[] file = merge_chunks(chunks);

            saveFile(file,this.filename);

            System.out.println(this.filename+" is donwloaded.");

        }catch (Exception e){
            System.out.println(e);
        }

    }

    ArrayList<byte[]> receive_chunks() throws Exception {
        ArrayList<byte[]> chunks = new ArrayList<>();

        Response response = (Response) dataNet.read();

        while (response.getCode().equalsIgnoreCase("continue")) {
            byte[] chunk = (byte[]) response.getLoads().get("body");
            chunks.add(chunk);
            response = (Response) dataNet.read();
        }


        return chunks;
    }

    byte[] merge_chunks(ArrayList<byte[]> _chunks) {
        int total_chunksize = 0;
        for(byte[] chunk: _chunks){
            total_chunksize+=chunk.length;
        }

        byte[] merged_chunk = new byte[total_chunksize];
        int current_idx = 0;
        for (byte[] chunk : _chunks) {
            for (int i = 0; i < chunk.length; i++) {
                merged_chunk[current_idx++] = chunk[i];
            }
        }
        return merged_chunk;
    }

    public void saveFile(byte[] chunk,String filename) throws IOException {

//        Path basePath = Paths.get("./");
//
//
//
//
//
//        File dir = basePath.toFile();
//
//
//        File file = new File(dir, filename);
//
//        try (FileOutputStream fos = new FileOutputStream(file, true)) {
//            fos.write(chunk);
//        }

        Path filePath = Paths.get(filename);

        Files.write(
                filePath,
                chunk,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
