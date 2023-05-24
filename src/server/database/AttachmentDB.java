package server.database;

import server.enums.FileType;
import server.message.Attachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AttachmentDB extends SQLDB {
    ArrayList<Attachment> attachments = new ArrayList<>();


    public static int createAttachment(Attachment attachment) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO attachment(path , type) VALUES (?) returning id" );
            preparedStatement.setString(2, attachment.getFilePath());
            if (attachment.getFileType() == FileType.IMAGE){

            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
