package com.spr;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CheapSpring {

    final String HTML_FILE = "page/example.html";

    private StringBuilder htmlResponse;
    private String response = "{ \"key\": \"value\"}";

    public CheapSpring() {
        try {
            ServerSocket welcomeSocket = new ServerSocket(8080);
            while (true) {
                Socket client = welcomeSocket.accept();
                this.readHtmlPage();
                System.out.println("NEW REQUEST...");
                this.processConnection(client);
                client.close();
                System.out.println("REQUEST END...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHtmlPage() {
        try {
            FileReader file = new FileReader(HTML_FILE);
            BufferedReader in = new BufferedReader(file);
            String line;
            this.htmlResponse = new StringBuilder();
            while ((line = in.readLine()) != null) {
                this.htmlResponse.append(line);
                if (line.isEmpty()) { break; }
            }
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processConnection(Socket client) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            this.readRequest(in);
            this.sendResponse(out, Response.HTML_RESPONSE, htmlResponse.toString());
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readRequest(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            if (line.isEmpty()) { break; }
        }
    }

    private void sendResponse(BufferedWriter out, String contentType, String contentData) throws IOException {
        out.write("HTTP/1.0 200 OK\r\n");
        out.write("Server: " + Response.SERVER_NAME + "\r\n");
        out.write("Content-Type: " + contentType + "\r\n");
        out.write("Content-Length: " + contentData.length() + "\r\n");
        out.write("\r\n");
        out.write(contentData);
    }

}
