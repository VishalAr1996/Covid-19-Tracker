package com.example.covid_19tracker.ModelClass;

import com.google.gson.annotations.SerializedName;

public class CountryData {
    @SerializedName("country")
    private String country;
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
    @SerializedName("countryInfo")
    private CountryInfo countryInfo;

    public CountryData(String country, String active, String confirmed, String confirmedNew, String recovered, String recoveredNew, String deceased, String deceasedNew, String totalTests, CountryInfo countryInfo) {
        this.country = country;
        this.active = active;
        this.confirmed = confirmed;
        this.confirmedNew = confirmedNew;
        this.recovered = recovered;
        this.recoveredNew = recoveredNew;
        this.deceased = deceased;
        this.deceasedNew = deceasedNew;
        this.totalTests = totalTests;
        this.countryInfo = countryInfo;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public String getCountry() {
        return country;
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

    public static class CountryInfo {
            @SerializedName("flag")
            private String flag;

            public CountryInfo(String flag) {
                this.flag = flag;
            }

            public String getFlag() {
                return flag;
            }
    }
}






