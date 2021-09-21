package com.example.covid_19tracker.enums;

public enum TimePeriod {
    WEEK(7), MONTH(30), ALLTIME(-1);
    private int numberDays;

    TimePeriod(int numberDays) {
        this.numberDays = numberDays;
    }

    public int getNumberDays() {
        return numberDays;
    }
}
