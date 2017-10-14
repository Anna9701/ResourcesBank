package com.annawyrwal;


public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("You should pass <port number>");
            System.exit(-1);
        }

        int portNumber = Integer.parseInt(args[0]);
        Listener listener = new Listener(portNumber);
        listener.start();
    }
}




