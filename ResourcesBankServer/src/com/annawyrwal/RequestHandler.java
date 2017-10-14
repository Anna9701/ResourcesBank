package com.annawyrwal;

import java.time.LocalDate;
import java.util.LinkedList;

public class RequestHandler extends Thread {
    private Listener.Client client;
    private int option;
    private LinkedList<String> messages;

    RequestHandler(Listener.Client client, LinkedList messages) {
        this.messages = messages;
        this.client = client;
    }

    public void run () {
        while (true) {
            if (messages.size() == 0)
                try {
                    Thread.sleep(0);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            try {
                option = Integer.parseInt(messages.remove().toString());
                menuOptionHandler(option);
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
                client.sendMessage("Wrong input, please try again.");
            }

        }

    }

    private void menuOptionHandler (int option) {
        switch (option) {
            case 1:
                addService();
                break;
            case 2:
                reserveService();
                break;
            case 3:
                printAllClientServices();
                break;
            case 4:
                printAllServices();
                break;
            case 5:
                cancelReservation();
                break;
            case 6:
                removeService();
                break;
            case 0:
                disconnectClient();
                break;
        }

        client.sendMessage('\n' + Listener.getStartMenuMessage());
    }

    private void reserveService() {
        if (!client.reserveService(getServiceFromClient()))
            client.sendMessage("Reservation failed, because service is unavailable or is yours\n");
    }

    private void addService() {
        client.addServiceToResources(getServiceFromClient());
    }

    private Service getServiceFromClient() {
        String titleRequest = "Please, enter the following: \n" + "Title: ";
        String titleResponse = askForResponse(titleRequest);
        String dateRequest = "Date: ";
        String dateResponse = askForResponse(dateRequest);
        LocalDate date = DateUtil.parse(dateResponse);
        Service service = new Service(titleResponse, date);

        return service;
    }

    private String askForResponse (String question) {
        client.sendMessage(question);
        while (messages.size() == 0) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return messages.remove();
    }

    private void printAllClientServices() {
        String services = client.printClientsServices();
        client.sendMessage(services);
    }

    private void printAllServices() {
        String services = client.printAllServices();
        client.sendMessage(services);
    }

    private void cancelReservation() {
        if (!client.cancelReservation(getServiceFromClient())) {
            // cancellation failed
            client.sendMessage("Cancellation failed!");
        }
    }

    private void removeService() {
        Service service = getServiceFromClient();
        if (!client.deleteServiceFromResources(service))
            client.sendMessage("You have no permission to remove this service!");
    }

    private void disconnectClient() {
        client.disconnect();
    }
}
