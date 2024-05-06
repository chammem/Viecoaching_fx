package com.asma.asma.entities;


import java.sql.Date;
import java.time.LocalDate;

public class Reservation {

    int id;
    String sujet;

    String description;
    String background_color;
    String border_color;
    String text_color;
    private LocalDate date;
    public Reservation() {
    }

    public Reservation(int id) {
        this.id = id;
    }

    public Reservation(int id, String sujet, Date date, String description, String background_color, String border_color, String text_color) {
        this.id = id;
        this.sujet = sujet;

        this.description = description;
        this.background_color = background_color;
        this.border_color = border_color;
        this.text_color = text_color;
    }

    public Reservation(String sujet, String description, String background_color, String border_color, String text_color) {
        this.sujet = sujet;

        this.description = description;
        this.background_color = background_color;
        this.border_color = border_color;
        this.text_color = text_color;
    }

    public Reservation(String eventName, String description, String toString) {
        this.sujet = sujet;

        this.description = description;
        this.background_color = background_color;
        this.border_color = border_color;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getBorder_color() {
        return border_color;
    }

    public void setBorder_color(String border_color) {
        this.border_color = border_color;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", sujet='" + sujet + '\'' +

                ", description='" + description + '\'' +
                ", background_color='" + background_color + '\'' +
                ", border_color='" + border_color + '\'' +
                ", text_color='" + text_color + '\'' +
                '}';
    }
}
