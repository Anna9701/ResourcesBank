package com.annawyrwal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientServerOutputReader extends Thread {
    private Socket serverSocket;

    public ClientServerOutputReader(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            String outputFromServer = "";
            while ((outputFromServer = in.readLine()) != null) {
                //This part is printing the output to console
                //Instead it should be appending the output to some file
                //or some swing element. Because this output may overlap
                //the user input from console
                System.out.println(outputFromServer);
            }
        } catch (IOException e) {
            System.err.println("Server error occurred.");
            System.exit(-1);
        }

    }
}
