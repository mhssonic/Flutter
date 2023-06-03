package server.message;

import server.database.AttachmentDB;
import server.enums.FileType;
import server.httpServer.handler.FileHttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public static String saveFile(InputStream inputStream, String format, String filePath) throws IOException {
        String fileName = AttachmentDB.getRandomPath() + "." + format;
        Path path = Paths.get(filePath + fileName);
        FileType fileType = formatToFileType(format);
        if(fileType != null){
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            Attachment attachment = new Attachment(path.toString(), fileType);
            return Integer.toString(AttachmentDB.createAttachment(attachment));
        }
        throw new IOException();
    }

    private static FileType formatToFileType(String format){
        if(format.equals("png") || format.equals("jpg"))
            return FileType.IMAGE;
        if(format.equals("mkv") || format.equals("mp4"))
            return FileType.VIDEO;
        return null;
    }
}
