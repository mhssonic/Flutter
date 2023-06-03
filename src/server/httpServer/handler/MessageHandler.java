package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import server.database.ChoiceDB;
import server.database.UserDB;
import server.enums.error.ErrorType;
import server.httpServer.FlutterHttpServer;
import server.message.Direct.DirectMessage;
import server.message.tweet.Comment;
import server.message.tweet.Quote;
import server.message.tweet.Retweet;
import server.message.tweet.Tweet;
import server.message.tweet.poll.Poll;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MessageHandler {
    public static void tweetHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Tweet tweet = objectMapper.treeToValue(jsonNode, Tweet.class);
            ErrorType errorType = Tweet.tweet(id, tweet.getText(), tweet.getAttachmentId(), tweet.getHashtag()); //TODO HASHTAG?

            String response = errorType.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    public static void retweetHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Retweet retweet = objectMapper.treeToValue(jsonNode, Retweet.class);
            ErrorType errorType = Retweet.retweet(retweet.getRetweetedMessageId(), id); //TODO HASHTAG?
            if (errorType != ErrorType.SUCCESS) {
                String response = errorType.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    public static void quoteHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Quote quote = objectMapper.treeToValue(jsonNode, Quote.class);
            ErrorType errorType = Quote.quote(id, quote.getText(), quote.getAttachmentId(), quote.getHashtag(), Integer.parseInt(quote.getMessageId().toString())); //TODO HASHTAG?
            if (errorType != ErrorType.SUCCESS) {
                String response = errorType.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    public static void commentHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Comment comment = objectMapper.treeToValue(jsonNode, Comment.class);
            ErrorType errorType = Comment.comment(id, comment.getText(), comment.getAttachmentId(), comment.getHashtag(), comment.getReplyFrom()); //TODO HASHTAG?
            if (errorType != ErrorType.SUCCESS) {
                String response = errorType.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }

    }

    public static void directMessageHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            DirectMessage directMessage = objectMapper.treeToValue(jsonNode, DirectMessage.class);
            ErrorType error = DirectMessage.sendDirectMessage(id, directMessage.getTargetUser(), directMessage.getText(),directMessage.getReply() , directMessage.getAttachmentId());

            String response = error.toString();

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
        FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
        System.out.println(e.getMessage());
        }
    }

    public static void pollHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            Poll poll = objectMapper.treeToValue(jsonNode, Poll.class);

            ArrayList<String> choices = new ArrayList<>();
            for (Object choice : poll.getChoices()) {
                choices.add((String) choice);
            }

            Integer[] choiceId = ChoiceDB.creatChoices(choices);

            ErrorType errorType = Poll.poll(id, poll.getText(), poll.getAttachmentId(), poll.getHashtag(), choiceId); //TODO HASHTAG?
            if (errorType != ErrorType.SUCCESS) {
                String response = errorType.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    public static void showTweetHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {

    }

    public static void voteHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        int choiceId = jsonNode.get("choice-id").asInt();

        ErrorType errorType = ChoiceDB.addVoters(id, choiceId);
        if (errorType != ErrorType.SUCCESS) {
            String response = errorType.toString();
            try {
                exchange.sendResponseHeaders(200, response.getBytes().length);

                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);

    }

    public static void likeHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int messageId = jsonNode.get("message-id").asInt();

            ErrorType errorType = Tweet.like(id, messageId);
            if (errorType != ErrorType.SUCCESS) {
                String response = errorType.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    public static void unlikeHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int messageId = jsonNode.get("message-id").asInt();

            ErrorType errorType = Tweet.removeLike(id, messageId);
            if (errorType != ErrorType.SUCCESS) {
                String response = errorType.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}