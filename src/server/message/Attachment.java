package server.message;

import server.enums.FileType;

import java.util.ArrayList;

public class Attachment {
    private String attachmentId;
    private String filePath;
    private FileType fileType;

    public Attachment(String filePath, FileType fileType) {
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
