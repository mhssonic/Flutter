package client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class client {
    public static void main(String[] args) {
        try {
            // Create URL object
            URL url = new URL("http://localhost:5050/upload-file");

            // Open connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set request method
            con.setRequestMethod("POST");

            // Set request headers (optional)
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "file/png");
            con.setRequestProperty("Cookie", "token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiItMTk5OTk5OTk5MCIsImlhdCI6MTY4NTczNzgwMCwiZXhwIjoxNjg4MzI5ODAwfQ.nZUwjuPMImomet1PaePqHny7uEYNvqJyLuCcNWIUgEA");
            con.setDoOutput(true);

            File file = new File("test/test.png");

            DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();


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
