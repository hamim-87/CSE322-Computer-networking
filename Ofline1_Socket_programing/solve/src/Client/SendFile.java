package Client;

import Utils.FileInfo;
import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SendFile implements Runnable{
    private NetworkUtils dataNet;
    private FileInfo fileInfo;
    private Thread thread;
    private File file;

    public SendFile( NetworkUtils dataNet,FileInfo fileInfo, File file){
        this.dataNet = dataNet;
        this.fileInfo = fileInfo;
        this.file = file;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        int rem = (int) this.file.length();
        Request reque = new Request("start",String.valueOf(fileInfo.fileId),String.valueOf(fileInfo.totalChunkSize));

        try{
            this.dataNet.write(reque);


            Response response = (Response) dataNet.read();


        }catch (Exception e){
            System.out.println(e);
        }

        try{
            FileInputStream fs = new FileInputStream(this.file);
            while(rem>0){
                byte[] chunk = new byte[Math.min(this.fileInfo.chunkSize,rem)];
                try {
                    fs.read(chunk);
                    rem -= chunk.length;

                    Request request = new Request("upload",chunk, fileInfo.fileId);
                    try{
                        dataNet.write(request);
                        Response response = (Response) dataNet.read();

                        if(response.getCode().equals("FAIL")){
                            fs.close();
                            System.out.println("File id = "+this.fileInfo.fileId + " is failed to be upload");
                        }

                    }catch (Exception e){
                        System.out.println(e);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            Request request = new Request("complete",String.valueOf(fileInfo.fileId));
            try {
                this.dataNet.write(request);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println("File uploaded.");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
