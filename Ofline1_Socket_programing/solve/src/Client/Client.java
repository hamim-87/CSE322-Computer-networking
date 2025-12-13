package Client;

import Utils.NetworkUtils;

import java.io.IOException;


public class Client {

    public static void main(String[] args) throws IOException {

        NetworkUtils networkUtil = new NetworkUtils("localhost",6666);
        new ControllerCLI(networkUtil);

    }
}
