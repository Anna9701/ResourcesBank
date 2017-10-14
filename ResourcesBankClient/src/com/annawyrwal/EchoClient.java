package com.annawyrwal;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {

    public EchoClient(String hostName, int portNumber) {
        try {
            Socket serverSocket = new Socket(hostName, portNumber);
            ClientServerOutputReader csor = new ClientServerOutputReader(serverSocket);
            csor.start();
            ClientUserInputReader cuir = new ClientUserInputReader(serverSocket);
            cuir.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }




}
