package com.annawyrwal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientUserInputReader extends Thread {
    private Socket serverSocket;

    public ClientUserInputReader(Socket serverSocket) {
        this.serverSocket = serverSocket;

    }

    public void run() {
        try {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out;
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            String userInput;

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
        } catch (IOException e) {
            System.err.println("Server or IO error occurred.");
            System.exit(-1);
        }
    }
}