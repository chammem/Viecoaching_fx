package com.asma.asma.entities;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime date;
    private String clientName;
    private Integer serviceNo;
    private int someNumber; // Utilisé pour le coloriage ou autre fonctionnalité

    public CalendarActivity(ZonedDateTime eventTime, String description, Integer serviceNo, int i) {

        this.clientName = description;
        this.serviceNo = serviceNo;
        this.someNumber = someNumber;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(Integer serviceNo) {
        this.serviceNo = serviceNo;
    }

    public int getSomeNumber() {
        return someNumber;
    }

    public void setSomeNumber(int someNumber) {
        this.someNumber = someNumber;
    }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                ", clientName='" + clientName + '\'' +
                ", serviceNo=" + serviceNo +
                ", someNumber=" + someNumber +
                '}';
    }
}
