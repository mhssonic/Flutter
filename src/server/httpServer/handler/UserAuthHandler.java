package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import server.Tools;
import server.enums.error.ErrorType;
import server.user.SignUpForm;
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
                FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
                return;
            }
            exchange.getResponseHeaders().add("Set-Cookie", "token=" + jwt);

            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static void signUpHandler(HttpExchange exchange)  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(exchange.getRequestBody());

            SignUpForm signUpForm = objectMapper.treeToValue(jsonNode, SignUpForm.class);
            ErrorType errorType = userController.signUp(signUpForm.getFirstName(), signUpForm.getLastName(), signUpForm.getUserName(), signUpForm.getPassword(), signUpForm.getConfirmPassword(), signUpForm.getEmail(), signUpForm.getPhoneNumber(), signUpForm.getCountry(), signUpForm.getBirthdate(), signUpForm.getBiography(), signUpForm.getAvatarPath(), signUpForm.getHeaderPath());
            if ( errorType != ErrorType.SUCCESS){
                String response = errorType.toString();
                exchange.sendResponseHeaders(200 , response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                return;
            }
            String jwt = Tools.creatJWT(signUpForm.getUserName(), LocalDate.now(), LocalDate.now().plusDays(VALID_TOKEN));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
