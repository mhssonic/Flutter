package server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DirectMessageDB {
    static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    static MongoDatabase database = mongoClient.getDatabase("flutter");

    public void run(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("flutter");
        try{
            database.createCollection("direct-message");
        }catch (Exception e){
        }
    }
}
