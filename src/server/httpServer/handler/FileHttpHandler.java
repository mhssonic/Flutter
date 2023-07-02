package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import server.database.AttachmentDB;
import server.enums.error.ErrorType;
import server.httpServer.FlutterHttpServer;
import server.message.Attachment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileHttpHandler {
    private static final String FILE_PATH = "files/";
    public static void uploadFile(HttpExchange exchange, int id){
        try {
            System.out.println("im here");
            InputStream inputStream = exchange.getRequestBody();
            String format = "jpg";

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

    public static void downloadFile(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){
        try {
            int attachmentId = jsonNode.get("attachment-id").asInt();
            File file = Attachment.getFile(attachmentId);
            String name = file.getName();
            String format = name.split("\\.")[1];
            exchange.getResponseHeaders().set("Content-Type", "file/" + format);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, file.length());

            OutputStream outputStream = exchange.getResponseBody();
            Files.copy(file.toPath(), outputStream);

            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND);
        }catch (NullPointerException e){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }
}
