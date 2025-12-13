package Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {

    private String header;
    private Map<String, Object> payload;

    public Request(String header, String body){
        this.header = header;
        this.payload = new HashMap<>();
        this.payload.put("body", body);
    }
    public Request(String header, String username,String body){
        this.header = header;
        this.payload = new HashMap<>();
        this.payload.put("username",username);
        this.payload.put("body",body);
    }
    public Request(String header, String fileName,long fileSize,String privacy,String username){
        this.header = header;
        this.payload = new HashMap<>();
        this.payload.put("filename",fileName);
        this.payload.put("filesize",fileSize);
        this.payload.put("privacy",privacy);
        this.payload.put("username",username);
    }

    public Request(String header, byte[] fileChunk, long fileId){
        this.header = header;
        this.payload = new HashMap<>();
        this.payload.put("chunk", fileChunk);
        this.payload.put("fileid", fileId);
    }

    public String getHeader() {
        return header;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}
