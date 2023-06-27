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

    /**
     * "username": userId
     * "password": "paassword"
     */
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
    //TODO add too long
    /**
     *<h2>Json input</h2>{
     *     <Tr>"first-name":"omid",<br>
     *     "last-name":"varam",<br>
     *     "email": "dorosdstjbashe@asd.com",<br>
     *     "phone-number":"09pahsmak" (phone number or email you can send both but it need at least one),<br>
     *     "bio":"hi" ,<br></>
     *     "country":"CA",<br>
     *     "username": "hebsdfo",<br>
     *     "password":"pAssword",<br>
     *     "confirm-password":"pAssword",<br>
     *     "birthdate":"1382-12-08" -> yyyy-MM-d,<br>
     *     "avatar":-1999999960, (id of a attachment)<br>
     *     "header":1999999960 (id of a attachment)<br>
     *}
     *      <h2>output</h2>
     *    SUCCESS,<br>
     *     REQUIRED_FIELD_EMPTY,(email or phone number)<br>
     *     DOESNT_EXIST (avatar or header attachment doesn't exist),<br>
     *     DUPLICATED_USERNAME,<br>
     *     INVALID_PASS,<br>
     *     TOO_LONG,<br>
     *     MISMATCH,<br>
     *     DUPLICATED_EMAIL,<br>
     *     INVALID_EMAIL,<br>
     *     DUPLICATED_PHONE_NUMBER,<br>
     *     INVALID_PHONE_NUMBER,<br>
     *     INVALID_BIRTHDATE,<br>
     *     UNDER_AGE,<br>
     *     NOT_VALID_COUNTRY,<br>
     */
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
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
    }
}
