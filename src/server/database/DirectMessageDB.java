package server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.internal.connection.Time;
import org.bson.Document;
import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.ObjectId;
import server.message.Direct.DirectMessage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class DirectMessageDB {
    static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    static MongoDatabase database = mongoClient.getDatabase("flutter");

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
        Date date = (Date) doc.get("time");
        DirectMessage directMessage = new DirectMessage(messageId, (int) doc.get("author"), (String) doc.get("context"), date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), (ArrayList<Integer>) doc.get("attachment"), (int) doc.get("reply"));
        return directMessage;
    }
}
