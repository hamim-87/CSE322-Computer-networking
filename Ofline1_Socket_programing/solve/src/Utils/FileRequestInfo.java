package Utils;

import java.io.Serializable;

public class FileRequestInfo implements Serializable {
    public int requestId;
    public String sender;
    public String description;
    public String recipient; // username or "ALL"

    public FileRequestInfo(int requestId, String sender, String description, String recipient) {
        this.requestId = requestId;
        this.sender = sender;
        this.description = description;
        this.recipient = recipient;
    }
}
