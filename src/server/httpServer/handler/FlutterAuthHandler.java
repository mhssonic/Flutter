package server.httpServer.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.httpServer.FlutterHttpServer;

import java.io.*;
import java.net.HttpURLConnection;

public class FlutterAuthHandler implements HttpHandler {
    HttpHandler handler;

    public FlutterAuthHandler(HttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(! exchange.getRequestMethod().equals("POST")){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD);
            return;
        }
        System.out.println("incoming request");
        exchange.getRequestHeaders().keySet().forEach(System.out::println);
        try {
            System.out.println(exchange.getRequestHeaders().get("Cookie"));
        }catch (Exception e){}

//        System.out.println(exchange.getRequestHeaders().get("Cookie").get(0));
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = null;
//        try {
//            InputStream inputStream = exchange.getRequestBody();
//            if (inputStream != null) {
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead = -1;
//                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//            } else {
//                stringBuilder.append("");
//            }
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ex) {
//                    throw ex;
//                }
//            }
//        }
//        System.out.println(stringBuilder);

        if(! exchange.getRequestHeaders().get("Content-Type").get(0).contains("application/json")){
            FlutterHttpServer.sendWithoutBodyResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        handler.handle(exchange);
    }
}
