package Server;

import Utils.NetworkUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HandleDataListener implements Runnable{

    private Thread thread;
    private ServerSocket dataSocket;
    private Server server;

    public HandleDataListener(Server server,ServerSocket dataSocket) throws IOException {
        this.dataSocket = dataSocket;
        this.thread = new Thread(this);
        this.server = server;
        this.thread.start();

    }

    @Override
    public void run() {
        while (true){
            try {
                Socket dataSocket = this.dataSocket.accept();
                System.out.println("New data connection...");
                new HandleData(dataSocket,server);


            }catch (Exception e){

            }


        }

    }
}
