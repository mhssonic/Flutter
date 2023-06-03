package server.httpServer.handler;

import com.sun.net.httpserver.HttpExchange;
import server.database.AttachmentDB;
import server.httpServer.FlutterHttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileHttpHandler {
    static final String FILE_PATH = "../Files/";
    public static void uploadFile(HttpExchange exchange, int id){
        try {

            InputStream inputStream = exchange.getRequestBody();
            String format = exchange.getRequestHeaders().get("Content-Type").get(0).split("file/")[1];
            String fileName = AttachmentDB.getRandomPath() + "." + format;
            Path path = Paths.get(FILE_PATH + fileName);
            Files.copy(inputStream, path, StandardCopyOption.ATOMIC_MOVE);//TODO what is atomic move
        }catch (NullPointerException e){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}
