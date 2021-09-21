package com.example.covid_19tracker.ModelClass;

import com.google.gson.annotations.SerializedName;

public class WorldData {
   @SerializedName("active")
    private  String active;
   @SerializedName("cases")
    private  String confirmed;
   @SerializedName("todayCases")
    private  String confirmedNew;
    @SerializedName("recovered")
    private  String recovered;
    @SerializedName("todayRecovered")
    private  String recoveredNew;
    @SerializedName("deaths")
    private  String deceased;
    @SerializedName("todayDeaths")
    private  String deceasedNew;
    @SerializedName("tests")
    private  String totalTests;

    public WorldData(String active, String confirmed, String confirmedNew, String recovered, String recoveredNew, String deceased, String deceasedNew, String totalTests) {
        this.active = active;
        this.confirmed = confirmed;
        this.confirmedNew = confirmedNew;
        this.recovered = recovered;
        this.recoveredNew = recoveredNew;
        this.deceased = deceased;
        this.deceasedNew = deceasedNew;
        this.totalTests = totalTests;
    }

    public String getActive() {
        return active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getConfirmedNew() {
        return confirmedNew;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getRecoveredNew() {
        return recoveredNew;
    }

    public String getDeceased() {
        return deceased;
    }

    public String getDeceasedNew() {
        return deceasedNew;
    }

    public String getTotalTests() {
        return totalTests;
    }
}
