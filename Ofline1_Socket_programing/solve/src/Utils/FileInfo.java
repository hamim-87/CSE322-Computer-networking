package Utils;


public class FileInfo {
    public int fileId;
    public int chunkSize;
    public long totalChunkSize;


    public FileInfo(int fileId, int chunkSize) {
        this.fileId = fileId;
        this.chunkSize = chunkSize;
    }
}