package server.httpServer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;
import server.Tools;
import server.database.ChatBoxDB;
import server.database.SQLDB;
import server.database.UserDB;
import server.enums.error.ErrorType;
import server.httpServer.FlutterHttpServer;
import server.message.Direct.DirectMessage;
import server.message.Message;
import server.user.SignUpForm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class UserHandler {


    /**
     *<h2>Json input</h2>{
     *     <Tr>"first-name":"omid",<br>
     *     "last-name":"varam",<br>
     *     "email": "dorosdstjbashe@asd.com",<br>
     *     "phone-number":"09pahsmak",<br>
     *     "country":"CA",<br>
     *     "username": "hebsdfo",<br>
     *     "password":"pAssword",<br>
     *     "confirm-password":"pAssword",<br>
     *     "birthdate":"1382-12-08",<br>
     *     "avatar":-1999999960, (id of a attachment)<br>
     *     "header":1999999960 (id of a attachment)<br>
     *}
     *      <h2>output</h2>
     *    SUCCESS,<br>
     *     DOESNT_EXIST (avatar or header attachment doesn't exist),<br>
     *     DUPLICATED_USERNAME,<br>
     *     INVALID_PASS,<br>
     *     MISMATCH,<br>
     *     DUPLICATED_EMAIL,<br>
     *     INVALID_EMAIL,<br>
     *     DUPLICATED_PHONE_NUMBER,<br>
     *     INVALID_PHONE_NUMBER,<br>
     *     INVALID_BIRTHDATE,<br>
     *     UNDER_AGE,<br>
     *     NOT_VALID_COUNTRY,<br>
     */
    public static void updateProfileHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Map<String, Object> data = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {
            });
            HashMap<String, Object> updatedData = (HashMap<String, Object>) data;
            SQLDB.updateUserProfile(updatedData, id);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     *<h2>Json input</h2>{
     *     "username": "hebsdfo",<br>
     *     "user-id":555,<br>
     *     one of them should be sent<br>
     *}
     *      <h2>output</h2>
     *      {<br>
     *   "id" : -1999999970,<br>
     *   "country" : "CA",<br>
     *   "birthdate" : "1382-12-08",<br>
     *   "biography" : null,<br>
     *   "avatar" : 800,<br>
     *   "header" : 0,<br>
     *   "username" : "hebo",<br>
     *   "first-name" : "omid",<br>
     *   "last-name" : "varam",<br>
     *   "phone-number" : null<br>
     * }
     * response: bad request, http not found
     */
    public static void showProfileHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            if(jsonNode.has("user-id")){
                int targetId = jsonNode.get("user-id").asInt();
                SignUpForm signUpForm = SQLDB.getUserProfileByUserID(targetId);
                if (signUpForm == null) {
                    FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND);
                    return;
                }
                String response;

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                response = ow.writeValueAsString(signUpForm);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            else if(jsonNode.has("username")) {
                String targetUsername = jsonNode.get("username").asText();
                SignUpForm signUpForm = SQLDB.getUserProfileByUsername(targetUsername);
                if (signUpForm == null) {
                    FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND);
                    return;
                }
                String response;
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                response = ow.writeValueAsString(signUpForm);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            else {
                FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     *      <h2>output</h2>
     *      <br>
     *      [123, ....]
     *      <br>
     *      a set of user ids
     */
    public static void getFriends(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            HashSet<Integer> friends = UserDB.getFriendsInSet(id);
            String response;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            response = ow.writeValueAsString(friends);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     *<h2>Json input</h2>
     * {<br>
     *     "target-id":-1999999990<br>
     * }
     *  <h2>output</h2>
     *      SUCCESS,<br></>
     *     DOESNT_EXIST,<br>
     *     BLOCKED,<br>
     *     ALREADY_EXIST,<br>
     *     SAME_PERSON you can't follow yourself<br>
     * response: bad request
     */
    public static void followHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int targetId = jsonNode.get("target-id").asInt();
            ErrorType error = UserDB.follow(id, targetId);

            String response = error.toString();

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (NullPointerException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            System.out.println(e.getMessage());
        }
    }

    /**
     *<h2>Json input</h2>
     *
     * {<br>
     *     "target-id":-1999999990<br>
     * }
     *
     *
     *  <h2>output</h2>
     *      SUCCESS,<br></>
     *     DOESNT_EXIST,<br>
     *     HAVE_NOT_FOLLOWED,<br>
     *     SAME_PERSON you can't follow yourself<br>
     * response: bad request
     */
    public static void unFollowHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int targetId = jsonNode.get("target-id").asInt();
            ErrorType error = UserDB.unFollow(id, targetId);

            String response = error.toString();

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (NullPointerException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            System.out.println(e.getMessage());
        }
    }

    /**
     *<h2>Json input</h2>
     * {<br>
     *     "target-id":-1999999990<br>
     * }
     *  <h2>output</h2>
     *      SUCCESS,<br></>
     *     DOESNT_EXIST,<br>
     *     ALREADY_EXIST,<br>
     *     SAME_PERSON you can't follow yourself<br>
     * response: bad request
     */
    public static void blockHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int targetId = jsonNode.get("target-id").asInt();
            ErrorType error = UserDB.block(id, targetId);

            String response = error.toString();

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (NullPointerException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            System.out.println(e.getMessage());
        }
    }


    /**
     *<h2>Json input</h2>
     *
     * {<br>
     *     "target-id":-1999999990<br>
     * }
     *
     *
     *  <h2>output</h2>
     *      SUCCESS,<br></>
     *     DOESNT_EXIST,<br>
     *     HAVE_NOT_BLOCKED,<br>
     *     SAME_PERSON you can't unblock yourself<br>
     * response: bad request
     */
    public static void unBlockHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int targetId = jsonNode.get("target-id").asInt();
            ErrorType error = UserDB.unBlock(id, targetId);

            String response = error.toString();

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (NullPointerException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            System.out.println(e.getMessage());
        }
    }

    /**
     *  <h2>output</h2>
     *  [array of messages]<br>
     * response: bad request
     */
    public static void showTimelineHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Array messageIds = ChatBoxDB.getMessageIds(Tools.jenkinsHash(id, id, false));
            if (messageIds != null) {
                Object[] tmpMessages = (Object[]) messageIds.getArray();
                ArrayList<Message> messages = Message.getMessages(tmpMessages);

                String jsonResponse = objectMapper.writeValueAsString(messages);
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                exchange.getResponseBody().write(jsonResponse.getBytes());
                exchange.getResponseBody().close();
                return;
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);


        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *<h2>Json input</h2>
     *
     * {<br>
     *     "target-id":-1999999990<br>
     * }
     *
     *  <h2>output</h2>
     *  [array of messages]<br>
     * response: bad request
     */
    public static void showDirectHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int targetId = jsonNode.get("target-id").asInt();
            ArrayList<Message> messages = DirectMessage.getDirectMessage(id, targetId);
            if (messages != null) {
                String jsonResponse = objectMapper.writeValueAsString(messages);
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                exchange.getResponseBody().write(jsonResponse.getBytes());
                exchange.getResponseBody().close();
                return;
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);


        } catch (JsonProcessingException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
