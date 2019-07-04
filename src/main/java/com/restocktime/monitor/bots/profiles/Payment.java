package com.restocktime.monitor.bots.profiles;

public class Payment {
    private String number;
    private String cvv;
    private String month;
    private String year;

    public Payment(String number, String cvv, String month, String year) {
        this.number = number;
        this.cvv = cvv;
        this.month = month;
        this.year = year;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
