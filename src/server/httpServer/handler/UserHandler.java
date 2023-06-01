package server.httpServer.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import server.database.SQLDB;
import server.database.UserDB;
import server.enums.error.ErrorType;
import server.httpServer.FlutterHttpServer;
import server.user.UserController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


public class UserHandler {

    static int userId;

    public static void updateProfileHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){
        try {
            Map<String , Object> data = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>(){});
            HashMap<String, Object> updatedData = (HashMap<String, Object>) data;
            SQLDB.updateUserProfile(updatedData , userId);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void showProfileHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){
    }

    public static void followHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){
        try {
            int targetId = jsonNode.get("target-id").asInt();
            ErrorType error = UserDB.follow(id, targetId);
            if (error == ErrorType.SUCCESS){
                FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
                return;
            }
            String response = objectMapper.writeValueAsString(error);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK , response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
        catch (NullPointerException e){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }catch (IOException e) {
        System.out.println(e.getMessage());
        }
    }

    public static void unFollowHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void blockHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void unBlockHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void showTimelineHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void showDirectHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }
}
