package com.example.covid_19tracker.ModelClass;

import com.google.gson.annotations.SerializedName;

public class TestedSamples {


    @SerializedName("totalsamplestested")
    private String totalSampleTested;
    @SerializedName("samplereportedtoday")
    private String newlyTested;

    @SerializedName("totalindividualsvaccinated")
    private String totalindividualsvaccinated;

    @SerializedName("totalindividualsregistered")
    private String totalindividualsregistered;

    @SerializedName("firstdoseadministered")
    private String firstdoseadministered;

    @SerializedName("seconddoseadministered")
    private String seconddoseadministered;

    @SerializedName("registration18-45years")
    private String registrationEighteenPlus;

    @SerializedName("registrationabove45years")
    private String registrationabove45years;

    public TestedSamples(String totalSampleTested, String newlyTested) {
        this.totalSampleTested = totalSampleTested;
        this.newlyTested = newlyTested;
    }

    public TestedSamples(String totalSampleTested, String newlyTested, String totalindividualsvaccinated, String totalindividualsregistered, String firstdoseadministered, String seconddoseadministered, String registrationEighteenPlus, String registrationabove45years) {
        this.totalSampleTested = totalSampleTested;
        this.newlyTested = newlyTested;
        this.totalindividualsvaccinated = totalindividualsvaccinated;
        this.totalindividualsregistered = totalindividualsregistered;
        this.firstdoseadministered = firstdoseadministered;
        this.seconddoseadministered = seconddoseadministered;
        this.registrationEighteenPlus = registrationEighteenPlus;
        this.registrationabove45years = registrationabove45years;
    }

    public String getTotalindividualsvaccinated() {
        return totalindividualsvaccinated;
    }

    public String getTotalindividualsregistered() {
        return totalindividualsregistered;
    }

    public String getFirstdoseadministered() {
        return firstdoseadministered;
    }

    public String getSeconddoseadministered() {
        return seconddoseadministered;
    }

    public String getRegistrationEighteenPlus() {
        return registrationEighteenPlus;
    }

    public String getRegistrationabove45years() {
        return registrationabove45years;
    }

    public String getTotalSampleTested() {
        return totalSampleTested;
    }

    public String getNewlyTested() {
        return newlyTested;
    }
}
