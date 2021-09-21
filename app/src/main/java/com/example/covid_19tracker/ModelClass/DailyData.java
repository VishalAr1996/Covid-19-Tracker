package com.example.covid_19tracker.ModelClass;

import com.google.gson.annotations.SerializedName;

public class DailyData {
    @SerializedName("dailyconfirmed")
     private String dailyConfirmed;

    @SerializedName("dailyrecovered")
    private String dailyRecovered;

    @SerializedName("date")
    private String date;

    public DailyData(String dailyConfirmed, String dailyRecovered, String  date) {
        this.dailyConfirmed = dailyConfirmed;
        this.dailyRecovered = dailyRecovered;
        this.date = date;
    }

    public DailyData() {

    }

    public String getDailyConfirmed() {
        return dailyConfirmed;
    }

    public String getDailyRecovered() {
        return dailyRecovered;
    }

    public String  getDate() {
        return date;
    }
}
