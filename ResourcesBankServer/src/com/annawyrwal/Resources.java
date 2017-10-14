package com.annawyrwal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Resources {
    private ArrayList<Service> services;

    public Resources() {
        services = new ArrayList<>();
    }

    public String getService(Service service) {
        if (!services.contains(service))
            return null;

        return service.toString();
    }

    public String getAllServices() {
        String allServices = "";
        for (Service service : services) {
                allServices += getService(service) + '\n';
        }

        return allServices;
    }

    public void addService(Service service) {
        services.add(service);
    }

    public boolean deleteService(Service service) {
        if (!checkIfInServices(service, services))
            return false; // operation failed

        services.remove(findService(service, services));
        return true;
    }

    public static Service findService (Service service, ArrayList<Service> services) {
        for (Service s : services) {
            if (s.getTitle().equals(service.getTitle()))
                if (s.getDate().equals(service.getDate()))
                    return s;
        }
        return null;
    }



    public static boolean checkIfInServices (Service service, ArrayList<Service> services) {
        for (Service s : services) {
            if (s.getTitle().equals(service.getTitle()))
                if (s.getDate().equals(service.getDate()))
                    return true;
        }
        return false;
    }

    public boolean reserveService(Service service) {
        if (!checkIfInServices(service, services))
            return false;

        services.remove(findService(service, services));
        return true;
    }

}


