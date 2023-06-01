package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import server.enums.error.ErrorType;
import server.httpServer.FlutterHttpServer;
import server.message.tweet.Tweet;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MessageHandler {
    public static void tweetHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){
        try {
            Tweet tweet = objectMapper.treeToValue(jsonNode, Tweet.class);
            ErrorType errorType = Tweet.tweet(id , tweet.getText() , tweet.getAttachments() , new ArrayList[]{tweet.getHashtag()}); //TODO HASHTAG?
            if ( errorType != ErrorType.SUCCESS){
                String response = errorType.toString();
                exchange.sendResponseHeaders(200 , response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    public static void retweetHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void quoteHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void commentHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void directMessageHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void pollHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void showTweetHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void voteHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void likeHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }

    public static void unlikeHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id){

    }
}