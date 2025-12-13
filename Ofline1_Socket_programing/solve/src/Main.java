import org.json.JSONObject;

import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String path = "/home/hamim/Downloads/a.py";
        File file = new File(path);
        System.out.println(file.length());



    }
}