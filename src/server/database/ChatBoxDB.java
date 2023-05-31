package server.database;

import server.enums.TweetType;
import server.message.Message;

import java.sql.*;
import java.util.ArrayList;

public class ChatBoxDB extends SQLDB {
    public static void main(String[] args) {
        run();
//        System.out.println(returnMessage(1713722456));
    }

    public static void creatChatBox(int id) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO chat_box (id) VALUES (?)");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void appendMessage(int chatBox, int messageId) {
        appendToArrayField("chat_box", chatBox, "message_id", messageId);
    }

    public static boolean containChatBox(int id) {
        return containFieldKey("chat_box", "id", id);
    }

    public static Array getMessageId(int chatBoxId) {
        return (Array) SQLDB.getFieldObject("chat_box", chatBoxId, "message_id");
    }

    public static void getMessage(int start, int finish, Object[] messageIds) {
        try {
            Message message = null;
            ArrayList<Message> messages = new ArrayList<>();
            for (Object messageId : messageIds) {
                int type = (int) messageId % (TweetType.count);
                switch (type) {

                    case 0:
                        message = TweetDB.getTweet((int) messageId);
                        break;
                    case 1:
                        message = CommentDB.getTweet((int) messageId);
                        break;
                    case 2:
                        message = RetweetDB.getTweet((int) messageId);
                        break;
                    case 3:
                        message = PollDB.getTweet((int) messageId);
                        break;
                    case 4:
                        message = QuoteDB.getTweet((int) messageId);
                        break;
                    case 5:
//                        message = DirectMessageDB.getDirect((int) messageId);
                        break;
                }
                if (!(message == null)) {
                    messages.add(message);
                }

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

