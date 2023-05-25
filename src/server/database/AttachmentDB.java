package server.database;

import server.message.Attachment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AttachmentDB extends SQLDB {

    public static int createAttachment(Attachment attachment) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO attachment(path , type) VALUES (?,type_file(?)) returning id" );
            preparedStatement.setString(1, attachment.getFilePath());
            preparedStatement.setString(2, attachment.getFileType().getSqlVersion());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer[] creatAttachments(ArrayList<Attachment> attachments){
        Integer[] attachmentId = new Integer[attachments.size()];
        int i = 0;
        for ( Attachment attachment : attachments) {
            attachmentId[i] = AttachmentDB.createAttachment(attachment);
            i++;
        }
        return attachmentId;
    }
}
