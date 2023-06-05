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
import server.message.Message;
import server.user.SignUpForm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserHandler {


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

    public static void showProfileHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            String targetUsername = jsonNode.get("username").asText();

            SignUpForm signUpForm = SQLDB.getUserProfile(targetUsername);
            if (signUpForm == null) {
                String response = ErrorType.DOESNT_EXIST.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonResponse = ow.writeValueAsString(signUpForm);
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            exchange.getResponseBody().write(jsonResponse.getBytes());
            exchange.getResponseBody().close();

            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

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

    public static void showTimelineHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Array messageIds = ChatBoxDB.getMessageIds(Tools.jenkinsHash(id, id, false));
            if (messageIds != null) {
                Object[] tmpMessages = (Object[]) messageIds.getArray();
                ArrayList<Message> messages = Message.getMessages(tmpMessages);
//                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//                StringBuffer jsonResponse = null;
//                for (Message tmp : messages) {
//                    jsonResponse.append(ow.writeValueAsString(tmp));
//
//                }
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String jsonResponse = ow.writeValueAsString(messages);
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                exchange.getResponseBody().write(jsonResponse.getBytes());
                exchange.getResponseBody().close();
                FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void showDirectHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {

    }
}
