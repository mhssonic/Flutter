package server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import server.message.Direct.DirectMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DirectMessageDB {
    static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    static MongoDatabase database = mongoClient.getDatabase("flutter");

    public static void main(String[] args) {
        run();
        SQLDB.run();
        ArrayList<Integer> attachment = new ArrayList<>();
        attachment.add(123);
        createDirectMessage(-20001, -111, "hi",  0, attachment);
        DirectMessage directMessage = getMessage(-20001);
    }

    public static void run(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("flutter");
        try{
            database.createCollection("direct_message");
        }catch (Exception e){
        }
    }

    public static void createDirectMessage(int id, int user, String context, int reply, ArrayList<Integer> attachments){
        MongoCollection messageCollection = database.getCollection("direct_message");

        Document direct = DirectMessage.messageToDoc(id, user, context, reply, LocalDateTime.now(), attachments);
        messageCollection.insertOne(direct);
    }

    public static DirectMessage getMessage(int messageId){
        MongoCollection messageCollection = database.getCollection("direct_message");
        Document doc = (Document) messageCollection.find(Filters.eq("_id", messageId)).first();

        DirectMessage directMessage = new DirectMessage(messageId, (int) doc.get("author"), (String) doc.get("context"), (LocalDateTime) doc.get("time"), (ArrayList<String>) doc.get("attachment"), (int) doc.get("reply"));
        return directMessage;
    }
}
