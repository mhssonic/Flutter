package server.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import server.database.SecretKeyDB;
import server.httpServer.handler.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlutterHttpServer {
    public static void run(){
        try {
//            char[] keystorePassword = SecretKeyDB.getKeyStorePassword().toCharArray();
//            KeyStore keyStore = KeyStore.getInstance("JKS");
//            FileInputStream fis = new FileInputStream("src/server/httpServer/keystore.jks");
//            keyStore.load(fis, keystorePassword);
//
//// Create SSL context
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//            kmf.init(keyStore, keystorePassword);
//            sslContext.init(kmf.getKeyManagers(), null, null);

            InetSocketAddress socket = new InetSocketAddress(5050);

//            HttpsServer httpsServer = HttpsServer.create(socket,10);
//            httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext));
            HttpServer httpsServer = HttpServer.create(socket,10);

            ExecutorService executorService = Executors.newFixedThreadPool(50);

            httpsServer.createContext("/sign-up", new FlutterAuthHandler(UserAuthHandler::signUpHandler));
            httpsServer.createContext("/sign-in", new FlutterAuthHandler(UserAuthHandler::signInHandler));
            httpsServer.createContext("/block", new FlutterHttpHandler(UserHandler::blockHandler));
            httpsServer.createContext("/unblock", new FlutterHttpHandler(UserHandler::unBlockHandler));
            httpsServer.createContext("/follow", new FlutterHttpHandler(UserHandler::followHandler));
            httpsServer.createContext("/already-follow", new FlutterHttpHandler(UserHandler::alreadyFollowHandler));
            httpsServer.createContext("/unfollow", new FlutterHttpHandler(UserHandler::unFollowHandler));
            httpsServer.createContext("/show-direct", new FlutterHttpHandler(UserHandler::showDirectHandler));
            httpsServer.createContext("/show-profile", new FlutterHttpHandler(UserHandler::showProfileHandler));
            httpsServer.createContext("/update-profile", new FlutterHttpHandler(UserHandler::updateProfileHandler));
            httpsServer.createContext("/show-timeline", new FlutterHttpHandler(UserHandler::showTimelineHandler));
            httpsServer.createContext("/get-friends", new FlutterHttpHandler(UserHandler::getFriends));
            httpsServer.createContext("/search-users", new FlutterHttpHandler(UserHandler::searchUsers));

            httpsServer.createContext("/tweet", new FlutterHttpHandler(MessageHandler::tweetHandler));
            httpsServer.createContext("/retweet", new FlutterHttpHandler(MessageHandler::retweetHandler));
            httpsServer.createContext("/quote", new FlutterHttpHandler(MessageHandler::quoteHandler));
            httpsServer.createContext("/poll", new FlutterHttpHandler(MessageHandler::pollHandler));
            httpsServer.createContext("/direct-message", new FlutterHttpHandler(MessageHandler::directMessageHandler));
            httpsServer.createContext("/comment", new FlutterHttpHandler(MessageHandler::commentHandler));
            httpsServer.createContext("/show-tweet", new FlutterHttpHandler(MessageHandler::showTweetHandler));
            httpsServer.createContext("/like", new FlutterHttpHandler(MessageHandler::likeHandler));
            httpsServer.createContext("/unlike", new FlutterHttpHandler(MessageHandler::unlikeHandler));
            httpsServer.createContext("/already-liked", new FlutterHttpHandler(MessageHandler::alreadyLiked));
            httpsServer.createContext("/vote", new FlutterHttpHandler(MessageHandler::voteHandler));//TODO we haven't done it

            httpsServer.createContext("/upload-file", new FileReceiveHttpHandler(FileHttpHandler::uploadFile));
            httpsServer.createContext("/download-file", new FlutterHttpHandler(FileHttpHandler::downloadFile));

//            System.setProperty("javax.net.debug", "ssl");
//            Logger serverLogger = Logger.getLogger("com.sun.net.httpserver");
//            ConsoleHandler consoleHandler = new ConsoleHandler();
//            consoleHandler.setLevel(Level.ALL);
//            serverLogger.addHandler(consoleHandler);
//            serverLogger.setLevel(Level.ALL);

            httpsServer.setExecutor(executorService);
            httpsServer.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void sendWithoutBodyResponse(HttpExchange exchange, int responseCode){
        try {
            exchange.sendResponseHeaders(responseCode, -1);
        }
        catch (Exception e){}
    }
}
