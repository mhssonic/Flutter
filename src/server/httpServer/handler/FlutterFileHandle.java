package server.httpServer.handler;

import com.sun.net.httpserver.HttpExchange;

public interface FlutterFileHandle {
    public void handle(HttpExchange exchange, int id);
}
