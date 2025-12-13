package Client;

import Utils.FileInfo;
import Utils.NetworkUtils;
import Utils.Request;
import Utils.Response;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ControllerCLI implements Runnable{

    private NetworkUtils networkUtils;
    private Terminal terminal;
    private Thread thread ;
    private NetworkUtils dataNet;

    public ControllerCLI(NetworkUtils _networkUils){
        this.networkUtils = _networkUils;
        this.terminal = new Terminal();
        this.thread = new Thread(this);
        this.thread.start();
    }


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);


        while(true){
            State state = terminal.getState();
            state.showPrompt();

            String input = scanner.nextLine();
            if(terminal.getState() instanceof InitState){
                handleInitState(input);
            }else if(terminal.getState() instanceof  LogInState){
                handleLogInState(input);
            }else if (terminal.getState() instanceof LoggedInState){
                handleLoggedInState(input);
            }else if(terminal.getState() instanceof  SignUpState){
                handleSignUpState(input);
            }
        }

    }

    private void handleSignUpState(String input){

        Request req = new Request("signup",input);
        try {
            networkUtils.write(req);
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            Response res =(Response)networkUtils.read();
            if(res.getCode().equals("FAIL")){
                System.out.println(res.getPayload());
            }else{
                System.out.println(res.getPayload());
                this.terminal.setState(this.terminal.getLogInState());
            }
        }catch (Exception e){
            System.out.println(e);
        }



    }

    private void handleLoggedInState(String input){
            if(input.equals("logout")){
                Request request = new Request("logout", terminal.getUser());
                try{
                    networkUtils.write(request);
                }catch (Exception e){
                    System.out.println(e);
                }
                try{
                    Response res = (Response) networkUtils.read();

                    if(res.getCode().equals("SUCCESS")){
                        this.terminal.setState(terminal.getInitState());
                    }else{
                        System.out.println(res.getPayload());
                    }
                }catch (Exception e){
                    System.out.println(e);
                }
            }else if(input.equals("who")){
                Request request = new Request("who","7");
                try{
                    networkUtils.write(request);
                }catch (Exception e){
                    System.out.println(e);
                }
                try{
                    Response res = (Response) networkUtils.read();

                    if(res.getCode().equals("SUCCESS")){
                        String payload = res.getPayload().toString();
                        payload = payload.substring(1, payload.length() - 1);

                        String[] users = payload.split(",");

                        System.out.println("Users:");
                        for (String user : users) {
                            System.out.println(user.trim());
                        }


                    }else{
                        System.out.println(res.getPayload());
                    }
                }catch (Exception e){
                    System.out.println(e);
                }

            }else if(input.equalsIgnoreCase("find")){
                Request request = new Request("find","find the files");
                try{
                    this.networkUtils.write(request);
                    Response res = (Response) this.networkUtils.read();

                    Map<Long, List<String>> files = res.files;

                    System.out.println("FileID | FileName | privacy | owner");

                    for (Map.Entry<Long, List<String>> entry : files.entrySet()) {
                        Long fileId = entry.getKey();
                        List<String> info = entry.getValue();

                        System.out.print(fileId+" ");
                        for (int i = 0; i < info.size(); i++) {
                            System.out.print(info.get(i)+ " ");
                        }
                        System.out.println(" ");
                    }


                }catch (Exception e){
                    System.out.println(e);
                }

            }else if(input.equalsIgnoreCase("ls")){
                Request request = new Request("ls", terminal.getUser());
                try{
                    this.networkUtils.write(request);
                    Response res = (Response) this.networkUtils.read();

                    Map<Long, List<String>> files = res.files;

                    System.out.println("FileID | FileName | privacy ");

                    for (Map.Entry<Long, List<String>> entry : files.entrySet()) {
                        Long fileId = entry.getKey();
                        List<String> info = entry.getValue();

                        System.out.print(fileId+" ");
                        for (int i = 0; i < info.size()-1; i++) {
                            System.out.print(info.get(i)+ " ");
                        }
                        System.out.println(" ");
                    }


                }catch (Exception e){
                    System.out.println(e);
                }
            }

            else{
                String[] parts = input.split(" ");
                if(parts[0].equals("scp")){
                    File file = new File(parts[1]);
                    if(!file.exists()){
                        System.out.println("Invalid File Path");
                    }else{
                        Request req = new Request("scp",file.getName(),file.length(),parts[2],terminal.getUser());
                        try{
                            this.networkUtils.write(req);

                            Response res =(Response) this.networkUtils.read();

                            if(res.getCode().equals("SUCCESS")){
                                System.out.println(res.getPayload());

                                FileInfo info = extractInfo(res.getPayload());
                                info.totalChunkSize = file.length();

                                new SendFile(dataNet,info,file);



                            }else{
                                System.out.println(res.getPayload());
                            }


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    }

    public void handleInitState(String input){
        if(input.equals("login")){
            terminal.setState(terminal.getLogInState());
        }else if(input.equals("signup")){
            terminal.setState(terminal.getSignUpstate());
        }
    }

    public void handleLogInState(String input){
        Request req = new Request("login",input);
        try {
            networkUtils.write(req);
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            Response res =(Response)networkUtils.read();
            if(res.getCode().equals("FAIL")){
                System.out.println(res.getPayload());
            }else{
                System.out.println(res.getPayload());
                this.terminal.setUser(input.split(" ")[0]);
                this.terminal.setState(this.terminal.getLoggedInState());
                this.dataNet = new NetworkUtils("localhost",33333);
                Request request = new Request("username", terminal.getUser());
                this.dataNet.write(request);


            }
        }catch (Exception e){
            System.out.println(e);
        }


    }





    public static FileInfo extractInfo(String log) {
        try {
            // Extract fileID
            String fileIdStr = log.split("fileID=")[1].split("\\)")[0];
            int fileId = Integer.parseInt(fileIdStr.trim());

            // Extract chunk size
            String chunkStr = log.split("chunk size=")[1];
            int chunkSize = Integer.parseInt(chunkStr.trim());

            return new FileInfo(fileId, chunkSize);
        } catch (Exception e) {
            throw new IllegalArgumentException("Log string format is invalid: " + log);
        }
    }





}
