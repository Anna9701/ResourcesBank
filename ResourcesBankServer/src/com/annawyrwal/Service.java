package com.annawyrwal;

import java.time.LocalDate;
import java.util.Date;

public class Service {
    private String title;
    private LocalDate date;

    public Service(String title, LocalDate date) {
        this.date = date;
        this.title = title;
    }


    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Name: " + getTitle() + "\nDate: " + getDate();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public void setTitle(String title) {
        this.title = title;
    }
}
