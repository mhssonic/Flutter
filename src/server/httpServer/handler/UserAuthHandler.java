package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import server.Tools;
import server.database.UserDB;
import server.httpServer.FlutterHttpServer;
import server.user.userController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;

public class UserAuthHandler {
    public final static int VALID_TOKEN = 30;
    public static void signInHandler(HttpExchange exchange){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(exchange.getRequestBody());
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();

            String jwt = userController.signIn(username, password);

            if(jwt == null){
                FlutterHttpServer.sendNotOkResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
                return;
            }
            exchange.getResponseHeaders().add("Set-Cookie", "token=" + jwt);

            FlutterHttpServer.sendNotOkResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
    }

    public static void signUpHandler(HttpExchange exchange){
    }
}
