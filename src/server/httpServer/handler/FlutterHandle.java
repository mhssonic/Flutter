package server.httpServer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public interface FlutterHandle {
    public void handle(HttpExchange exchange, ObjectMapper objectMapper, JsonNode jsonNode, int id);
}
