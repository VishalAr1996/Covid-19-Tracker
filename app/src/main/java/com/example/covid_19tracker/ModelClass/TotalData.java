package com.example.covid_19tracker.ModelClass;

import com.google.gson.annotations.SerializedName;

public class TotalData {
   @SerializedName("active")
    private String active;

   @SerializedName("confirmed")
    private String confirmed;

   @SerializedName("deltaconfirmed")
    private String deltaconfirmed;

  @SerializedName("recovered")
   private String recovered;

  @SerializedName("deltarecovered")
   private String  deltarecovered;

  @SerializedName("deaths")
   private String  deaths;

  @SerializedName("deltadeaths")
   private String  deltadeaths;

  @SerializedName("lastupdatedtime")
    private String lastUpdated;

  @SerializedName("state")
  private String stateName;

    public TotalData(String stateName,String active, String confirmed, String deltaconfirmed, String recovered, String deltarecovered, String deaths, String deltadeaths, String lastUpdated) {
        this.active = active;
        this.confirmed = confirmed;
        this.deltaconfirmed = deltaconfirmed;
        this.recovered = recovered;
        this.deltarecovered = deltarecovered;
        this.deaths = deaths;
        this.deltadeaths = deltadeaths;
        this.lastUpdated = lastUpdated;
        this.stateName = stateName;
    }


    public String getStateName() {
        return stateName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDeltarecovered() {
        return deltarecovered;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getDeltadeaths() {
        return deltadeaths;
    }

    public String getActive() {
        return active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getDeltaconfirmed() {
        return deltaconfirmed;
    }
}
