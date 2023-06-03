package server.httpServer.handler;

import com.sun.net.httpserver.HttpExchange;
import server.database.AttachmentDB;
import server.httpServer.FlutterHttpServer;
import server.message.Attachment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileHttpHandler {
    private static final String FILE_PATH = "files/";
    public static void uploadFile(HttpExchange exchange, int id){
        try {
            InputStream inputStream = exchange.getRequestBody();
            String format = exchange.getRequestHeaders().get("Content-Type").get(0).split("file/")[1];

            String response = Attachment.saveFile(inputStream, format, FILE_PATH);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);

        }catch (NullPointerException e){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }
}
