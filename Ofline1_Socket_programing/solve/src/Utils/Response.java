package Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {

    private String code;


    private String payload;
    public Response(String code, String payload) {
        this.code = code;
        this.payload = payload;
    }

    public String getCode() {
        return code;
    }

    public String getPayload() {
        return payload;
    }

    public Map<Long, List<String>> files;

    public Response(String code, Map<Long, List<String>> files) {
        this.code = code;
        this.files = new HashMap<>();

        for (Map.Entry<Long, List<String>> entry : files.entrySet()) {
            List<String> info = entry.getValue();

            if (info != null && info.size() > 1) {
                String privacy = info.get(1);

                if (!privacy.equalsIgnoreCase("private")) {
                    this.files.put(entry.getKey(), info);
                }
            }
        }
    }

    public Response(String code, Map<Long, List<String>> files,String name) {
        this.code = code;
        this.files = new HashMap<>();

        for (Map.Entry<Long, List<String>> entry : files.entrySet()) {
            List<String> info = entry.getValue();

            if (info != null && info.size() > 2) {
                String privacy = info.get(2);

                if (privacy.equalsIgnoreCase(name)) {
                    this.files.put(entry.getKey(), info);
                }
            }
        }
    }
}
