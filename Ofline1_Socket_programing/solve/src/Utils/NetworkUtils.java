package Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkUtils {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public NetworkUtils(String _host, int _port) throws IOException{

        this.socket = new Socket(_host,_port);
        oos =new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        ois =new ObjectInputStream(socket.getInputStream());


    }

    public NetworkUtils(Socket socket) throws IOException{
        this.socket = socket;
        oos =new ObjectOutputStream(this.socket.getOutputStream());
        oos.flush();
        ois =new ObjectInputStream(this.socket.getInputStream());
    }

    public void write(Object o) throws IOException{
        oos.reset();
        oos.writeUnshared(o);
    }

    public Object read() throws IOException, ClassNotFoundException {
        return  ois.readUnshared();
    }

    public void closeConnection() throws IOException{
        ois.close();
        oos.close();
    }
}
