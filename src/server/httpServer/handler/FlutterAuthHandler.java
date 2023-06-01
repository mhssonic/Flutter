package server.httpServer.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class FlutterAuthHandler implements HttpHandler {
    HttpHandler handler;

    public FlutterAuthHandler(HttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        if(! exchange.getRequestMethod().equals("POST")){
//            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD);
//            return;
//        }

//        if(! exchange.getRequestHeaders().get("Content-Type").contains("application/json")){
//            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
//            return;
//        }

        handler.handle(exchange);
    }
}
