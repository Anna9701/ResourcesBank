package com.annawyrwal;


public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("<host name> <port number> required!");
            System.exit(-1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        new EchoClient(hostName, portNumber);
    }
}




