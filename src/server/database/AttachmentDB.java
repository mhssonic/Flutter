package server.database;

import server.enums.FileType;
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

    public static boolean checkAttachments(ArrayList<Integer> attachments){
        return containIdsInTable("attachment", attachments);
    }

    public static ArrayList<Attachment> getAttachment(int[]attachmentIds){
        ResultSet resultSet;
        ArrayList<Attachment> attachments = new ArrayList<>();
        for (int attachmentId : attachmentIds) {
            resultSet = getResultSet("attachment" , attachmentId);
            try {
                if (resultSet.next()){
                    FileType type = (FileType) resultSet.getObject("type");
                    String path = resultSet.getString("path");
                    attachments.add(new Attachment(path , type));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return attachments;
    }

    public static Object getAttachmentPath(int id){
        return getFieldObject("attachment", id, "path");
    }

    public static String getRandomPath(){
        try {
            preparedStatement = connection.prepareStatement("select NEXTVAL('seq_file_path')");

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Integer.toString(resultSet.getInt("nextval"));
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }
}
