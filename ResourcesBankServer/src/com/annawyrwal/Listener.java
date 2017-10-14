package com.annawyrwal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class Listener extends Thread {
    private int portNumber;
    private ServerSocket serverSocket;
    private ArrayList<Client> clients;
    private Resources resources;

    public Listener(int number) {
        clients = new ArrayList<>();
        resources = new Resources();
        portNumber = number;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStartMenuMessage() {
        String text = "Hello on our resources bank!\n" +
                "1. Add resource \n" +
                "2. Reserve resource \n" +
                "3. Print my resources \n" +
                "4. Print all available resources \n" +
                "5. Cancel reservation \n" +
                "6. Remove resource \n" +
                "0. Exit";
        return text;
    }

    public void run() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostName());
                Client client = new Client(socket);
                client.sendMessage(getStartMenuMessage());
                clients.add(client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void sendToAllClients (String msg) {
        for (Client client : clients) {
            client.sendMessage(msg);
        }
    }

     class Client extends  Thread {
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private ArrayList<Service> reservedServices; // reserved by client
        private ArrayList<Service> addedByClientServices;
        private LinkedList<String> messages;

        public Client(Socket socket) throws IOException {
            client = socket;
            messages = new LinkedList<>();
            reservedServices = new ArrayList<>();
            addedByClientServices = new ArrayList<>();
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }

        public void addServiceToResources(Service service) {
            resources.addService(service);
            addedByClientServices.add(service);
            sendToAllClients("Added service: \n" + service.toString());
        }

        public boolean deleteServiceFromResources(Service service) {
            if (Resources.checkIfInServices(service, addedByClientServices)) {
                if (!resources.deleteService(service))
                    return false;

                addedByClientServices.remove(Resources.findService(service, addedByClientServices));
                sendToAllClients("Removed service: \n" + service.toString());
                return true;
            }

            return false;
        }

        public boolean cancelReservation (Service service) {
            if (!Resources.checkIfInServices(service, reservedServices)) {
                // no such service reserved by this client
                return false;
            }

            resources.addService(service);
            reservedServices.remove(Resources.findService(service, reservedServices));
            sendToAllClients("Reservation cancelled: " + service.toString());
            return true;
        }

        public boolean reserveService(Service service) {
            if (Resources.checkIfInServices(service, addedByClientServices)) {
                // can't reserve own service
                return false;
            }

            if (!resources.reserveService(service)) {
                //no such service in resources
                return false;
            }

            reservedServices.add(service);
            sendToAllClients("Reserved service: \n" + service);
            return true;
        }

        public String printAllServices() {
            String allServices = resources.getAllServices();
            return allServices;
        }

        public String printClientsServices() {
            String allServices = "Added services: \n";
            for (Service s : addedByClientServices)
                allServices += s.toString() + '\n';

            allServices += "\nReserved services:\n";
            for (Service s : reservedServices)
                allServices += s.toString() + '\n';

            return allServices;
        }

        public void disconnect () {
            try {
                client.close();
                clients.remove(client);
            } catch (IOException ex) {
                System.err.println("Exception during disconnecting : " + ex.getMessage());
                return;
            }
        }

        public void run() {
            try {
                Client currentClient = this;
                Thread handle = new Thread () {
                    public void run() {
                        new RequestHandler(currentClient, messages).start();
                    }
                };
                handle.start();
                while (true) {
                    String msg = in.readLine();
                    messages.add(msg);
                }
            } catch (IOException e) {
                System.err.println("Client " + client.getInetAddress().getHostName() + " disconnected");
                try {
                    client.close();
                    clients.remove(client);
                } catch (IOException e1) {
                    System.err.println("Closing connection error " + e1.getMessage());
                    return;
                }
                return;
            }
        }

        public void sendMessage(String msg) {
            out.println(msg);
        }
    }
}

