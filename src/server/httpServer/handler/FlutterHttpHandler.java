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

        if(! exchange.getRequestHeaders().get("Content-Type").get(0).contains("application/json")){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(exchange.getRequestBody());

        try {
            String strCookies = exchange.getRequestHeaders().get("Cookie").get(0);
            String[] cookies = strCookies.split("token=", 2);
            String[] token = cookies[1].split(";", 2);

            String strId = Tools.decodeJWT(token[0]).getId();
            if(strId != null) {
                int id = Integer.parseInt(strId);
                handler.handle(exchange, objectMapper,  jsonNode, id);
            }
            else {
                FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
            }
        }catch (Exception e){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }
}
