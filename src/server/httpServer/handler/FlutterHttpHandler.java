package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Tools;
import server.httpServer.FlutterHttpServer;

import java.io.IOException;
import java.net.HttpURLConnection;

public class FlutterHttpHandler implements HttpHandler {
    FlutterHandle handler;

    public FlutterHttpHandler(FlutterHandle handler) {
        this.handler = handler;
    }

    public void handle(HttpExchange exchange) throws IOException {
        if(! exchange.getRequestMethod().equals("POST")){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD);
            return;
        }

        if(! exchange.getRequestHeaders().get("Content-Type").contains("application/json")){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(exchange.getRequestBody());

        String jwt = exchange.getRequestHeaders().get("token").get(0);
        String strId = Tools.decodeJWT(jwt).getId();
        if(strId != null) {
            int id = Integer.parseInt(strId);
            handler.handle(exchange, objectMapper,  jsonNode, id);
        }
        else {
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }
}
