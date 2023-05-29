package server.httpServer.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.httpServer.FlutterHttpServer;

import java.io.IOException;
import java.net.HttpURLConnection;

public class FlutterHttpHandler implements HttpHandler {
    HttpHandler handler;

    public FlutterHttpHandler(HttpHandler handler) {
        this.handler = handler;
    }

    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod() != "POST")
            FlutterHttpServer.sendNotOkResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD);

        handler.handle(exchange);
    }
}
