package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import server.enums.error.ErrorType;
import server.user.SignUpForm;
import server.httpServer.FlutterHttpServer;
import server.user.UserController;

import java.io.IOException;
import java.net.HttpURLConnection;

public class UserAuthHandler {
    public final static int VALID_TOKEN = 30;
    public static void signInHandler(HttpExchange exchange){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(exchange.getRequestBody());
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            String jwt = UserController.signIn(username, password);
            if(jwt == null){
                FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
                return;
            }

            exchange.getResponseHeaders().add("Set-Cookie", "token=" + jwt);
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
    public static void signUpHandler(HttpExchange exchange)  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(exchange.getRequestBody());

            SignUpForm signUpForm = objectMapper.treeToValue(jsonNode, SignUpForm.class);
            ErrorType errorType = UserController.signUp(signUpForm.getFirstName(), signUpForm.getLastName(), signUpForm.getUsername(), signUpForm.getPassword(), signUpForm.getConfirmPassword(), signUpForm.getEmail(), signUpForm.getPhoneNumber(), signUpForm.getCountry(), signUpForm.getBirthdate(), signUpForm.getBiography(), signUpForm.getAvatar(), signUpForm.getHeader());

            String response = errorType.toString();
            exchange.sendResponseHeaders(200 , response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
