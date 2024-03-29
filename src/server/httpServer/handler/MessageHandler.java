package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;
import server.database.ChoiceDB;
import server.database.TweetDB;
import server.enums.error.ErrorType;
import server.httpServer.FlutterHttpServer;
import server.message.Direct.DirectMessage;
import server.message.Message;
import server.message.tweet.Comment;
import server.message.tweet.Quote;
import server.message.tweet.Retweet;
import server.message.tweet.Tweet;
import server.message.tweet.poll.Poll;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MessageHandler {
    /**
     *<h2>Json input</h2>
     * {<br>
     *     "text":"kar ma shayad in hast, ke miyan ghole nilofar va gharn, pey avaz haghighat bedavim",<br>
     *     "attachment-id":[-1999999970],<br>
     *     "hashtag":["hi"]<br>
     * }
     *
     *  <h2>output</h2>
     *      SUCCESS,<br>
     *     DOESNT_EXIST,<br>
     *     TOO_LONG,<br>
     * response: bad request
     */
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

    /**
     *<h2>Json input</h2>
     * {<br>
     *     "message-id":-1999999970,<br>
     * }
     *
     *  <h2>output</h2>
     *      SUCCESS,<br>
     * response: bad request
     */
    public static void retweetHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int messageId = jsonNode.get("message-id").asInt();
            ErrorType errorType = Retweet.retweet(messageId, id); //TODO HASHTAG?

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
            ErrorType error = DirectMessage.sendDirectMessage(id, directMessage.getTargetUser(), directMessage.getText(), directMessage.getReply(), directMessage.getAttachmentId());

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
        } catch (Exception e) {
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

        try {
            int messageId = jsonNode.get("message-id").asInt();
            Message message = Message.getMessage(messageId);
            Tweet tweet = (Tweet)message;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            if(tweet.getComment() == null){
                ArrayList<Message> messages = new ArrayList<>();
                String jsonResponse = ow.writeValueAsString(messages);
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                exchange.getResponseBody().write(jsonResponse.getBytes());
                exchange.getResponseBody().close();
                return;
            }
            ArrayList<Message> messages = Message.getMessages(tweet.getComment());

            String jsonResponse = ow.writeValueAsString(messages);
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            exchange.getResponseBody().write(jsonResponse.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            System.out.println(e.getMessage());
        }

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

    public static void unlikeHandler(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int messageId = jsonNode.get("message-id").asInt();

            ErrorType errorType = Tweet.removeLike(id, messageId);
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

    public static void alreadyLiked(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id) {
        try {
            int messageId = jsonNode.get("message-id").asInt();

            boolean bool = TweetDB.likedBefore(messageId, id);
            String response = Boolean.toString(bool);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}