package server.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import server.httpServer.handler.FlutterHttpHandler;
import server.httpServer.handler.MessageHandler;
import server.httpServer.handler.UserAuthHandler;
import server.httpServer.handler.UserHandler;

import java.net.InetSocketAddress;

public class FlutterHttpServer {
    public static void run(){
        try {

            InetSocketAddress socket = new InetSocketAddress(5050);
            HttpServer httpServer = HttpServer.create(socket,50);

            httpServer.createContext("/sign-up", new FlutterHttpHandler(UserAuthHandler::signUpHandler));
            httpServer.createContext("/sign-in", new FlutterHttpHandler(UserAuthHandler::signInHandler));
            httpServer.createContext("/block", new FlutterHttpHandler(UserHandler::blockHandler));
            httpServer.createContext("/unblock", new FlutterHttpHandler(UserHandler::unBlockHandler));
            httpServer.createContext("/follow", new FlutterHttpHandler(UserHandler::followHandler));
            httpServer.createContext("/unfollow", new FlutterHttpHandler(UserHandler::unFollowHandler));
            httpServer.createContext("/show-direct", new FlutterHttpHandler(UserHandler::showDirectHandler));
            httpServer.createContext("/show-profile", new FlutterHttpHandler(UserHandler::showProfileHandler));
            httpServer.createContext("/show-timeline", new FlutterHttpHandler(UserHandler::showTimelineHandler));
            httpServer.createContext("/update-profile", new FlutterHttpHandler(UserHandler::updateProfileHandler));

            httpServer.createContext("/tweet", new FlutterHttpHandler(MessageHandler::tweetHandler));
            httpServer.createContext("/retweet", new FlutterHttpHandler(MessageHandler::retweetHandler));
            httpServer.createContext("/quote", new FlutterHttpHandler(MessageHandler::quoteHandler));
            httpServer.createContext("/poll", new FlutterHttpHandler(MessageHandler::pollHandler));//TODO we haven't done it
            httpServer.createContext("/directMessageHandler", new FlutterHttpHandler(MessageHandler::directMessageHandler));
            httpServer.createContext("/commentHandler", new FlutterHttpHandler(MessageHandler::commentHandler));
            httpServer.createContext("/show-tweet", new FlutterHttpHandler(MessageHandler::showTweetHandler));//TODO we haven't dont it
            httpServer.createContext("/like", new FlutterHttpHandler(MessageHandler::likeHandler));
            httpServer.createContext("/unlike", new FlutterHttpHandler(MessageHandler::unlikeHandler));
            httpServer.createContext("/vote", new FlutterHttpHandler(MessageHandler::voteHandler));

            httpServer.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void sendNotOkResponse(HttpExchange exchange, int responseCode){
        try {
            exchange.sendResponseHeaders(responseCode, -1);
        }
        catch (Exception e){}
    }
}
