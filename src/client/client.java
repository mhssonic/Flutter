package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class client {
    public static void main(String[] args) {
        try {
            // Create URL object
            URL url = new URL("http://localhost:5050/sign-in");

            // Open connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set request method
            con.setRequestMethod("POST");

            // Set request headers (optional)
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{\"username\": \"John\", \"password\": 30}";
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(jsonInputString);
            out.flush();
            out.close();


            // Read response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print response
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
