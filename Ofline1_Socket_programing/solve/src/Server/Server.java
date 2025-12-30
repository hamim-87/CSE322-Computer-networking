package Server;

import Utils.NetworkUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.*;


public class Server {
    public HashMap<String,NetworkUtils> userConnections = new HashMap<>();
    public HashMap<String,NetworkUtils> dataConnections = new HashMap<>();
    // id-> filename,privacy,username
    public Map<Long, List<String>> fileMeta = new HashMap<>();


    public long MAX_BUFFER_SIZE = 11111;
    public long MIN_CHUNK_SIZE = 100;
    public long MAX_CHUNK_SIZE = 150;
    public long current_buffer_size = 0;
    public long fileIDCount = 0;

    public static void main(String[] args) throws IOException{
        int PORT = 6666;
        int DATA_PORT=33333;

        ServerSocket severSocket = new ServerSocket(PORT);
        Server server = new Server();

        ServerSocket dataSocket = new ServerSocket(DATA_PORT);



        while(true){
            System.out.println("Waiting for client...");
            Socket socket = severSocket.accept();

            System.out.println("Client trying to established connection.");

            new HandleConnection(socket,server);
            new HandleDataListener(server,dataSocket);


        }

    }

    public void addUserConnection(String user, NetworkUtils networkUtils){
        userConnections.put(user,networkUtils);
    }

}
