package server;

import server.database.DirectMessageDB;
import server.database.SQLDB;
import server.httpServer.FlutterHttpServer;

public class server {
    public static void main(String[] args) {
        SQLDB.run();
        DirectMessageDB.run();
        FlutterHttpServer.run();
    }
}
