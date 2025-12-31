package Server;

import Utils.NetworkUtils;
import Utils.Request;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class HandleConnection implements Runnable{

    private Socket socket;
    private Thread thread;
    private NetworkUtils networkUtils;
    HashMap<String,Task> commands;
    private Server server;


    public HandleConnection(Socket socket,Server server) throws IOException{
        this.socket = socket;
        this.networkUtils = new NetworkUtils(socket);
        this.server = server;

        this.commands = new HashMap<>();
        this.commands.put("signup",new SignupTask(networkUtils,server));
        this.commands.put("login",new LoginTask(networkUtils,server));
        this.commands.put("who", new WhoTask(networkUtils));
        this.commands.put("logout",new LogoutTask(networkUtils));
        this.commands.put("scp", new UploadTask(networkUtils,server));
        this.commands.put("find", new ShowFilesTask(networkUtils,server));
        this.commands.put("ls", new OwnFileTask(networkUtils,server));
        this.commands.put("download",new DownloadTask(networkUtils,server));
        this.commands.put("req",new RequestTask(networkUtils,server));
        this.commands.put("show",new ShowTask(networkUtils,server));

        this.thread = new Thread(this);




        this.thread.start();
    }

    @Override
    public void run() {

        while(true){
            try{

                Request req = (Request) this.networkUtils.read();
                String header = req.getHeader();
                this.commands.get(header).execute(req);


            }catch (Exception e){
                System.out.println(e);
            }

        }
    }
}
