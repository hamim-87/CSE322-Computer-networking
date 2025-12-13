package Server;

import java.io.*;
import java.security.MessageDigest;
import java.util.*;

public class UserDB {

    private static String USER_DB_PATH = "src/Server/UserDB/users.properties";
    private static String ONLINE_PATH = "src/Server/UserDB/online.properties";


    private String sha256(String input) throws Exception{
        MessageDigest md = MessageDigest.getInstance("sha-256");
        byte[] hash = md.digest(input.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hash);

    }

    public void setUser(String user, String password) throws FileNotFoundException, IOException,Exception {
        Properties prop = new Properties();

        File oldFile = new File(USER_DB_PATH);
        if(oldFile.exists()){
            prop.load(new FileInputStream(oldFile));
        }else{
            System.out.println("missing user db");
        }

        prop.setProperty(user,sha256(password));

        prop.store(new FileOutputStream(oldFile),user+" is added");

    }
    public void setOnlineUser(String user) throws Exception{
        Properties prop = new Properties();

        File oldFile = new File(ONLINE_PATH);
        if(oldFile.exists()){
            prop.load(new FileInputStream(oldFile));
        }else{
            System.out.println("missing user db");
        }

        prop.setProperty(user,"1");
        prop.store(new FileOutputStream(oldFile),user+" is added");

    }

    public boolean isOnline(String user) throws Exception{
        Properties prop = new Properties();

        File oldFile = new File(ONLINE_PATH);
        if(oldFile.exists()){
            prop.load(new FileInputStream(oldFile));
        }else{
            System.out.println("missing user db");
        }
        return prop.getProperty(user) != null;
    }

    public String getUser(String username) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream(USER_DB_PATH));
        return prop.getProperty(username);
    }

    public boolean isValidPassword(String username,String password) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream(USER_DB_PATH));

        return prop.getProperty(username).equals(sha256(password));
    }

    public List<String> showAllUser() throws Exception{

        Properties propOn = new Properties();
        propOn.load(new FileInputStream(ONLINE_PATH));

        Set<Object> onlineUser = propOn.keySet();
        ArrayList<String> activeUser = new ArrayList<>();
        for(Object usr : onlineUser){
            activeUser.add((String) usr);
        }

        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(USER_DB_PATH);
        props.load(fis);

        Set<Object> keys = props.keySet();

        List<String> ls = new ArrayList<>();

        for (Object key : keys) {
            if(activeUser.contains((String) key)){
                ls.add((String) key + " (Online)");

            }else{
                ls.add((String)key + " (Offline)");
            }
        }


        return ls;
    }
    public boolean removeOnlineUser(String user) {
        try {
            Properties prop = new Properties();
            File file = new File(ONLINE_PATH);
            if (file.exists()) {
                prop.load(new FileInputStream(file));
            } else {
                return false;
            }

            if (prop.containsKey(user)) {
                prop.remove(user);
                prop.store(new FileOutputStream(file),user+" is added");
            }else{
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }






}
