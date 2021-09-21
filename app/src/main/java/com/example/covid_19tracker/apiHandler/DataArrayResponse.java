package com.example.covid_19tracker.apiHandler;

import com.example.covid_19tracker.ModelClass.DailyData;
import com.example.covid_19tracker.ModelClass.TestedSamples;
import com.example.covid_19tracker.ModelClass.TotalData;
import com.google.gson.annotations.SerializedName;

public class DataArrayResponse {

    @SerializedName("cases_time_series")
    private DailyData[] dailyDataArray;

   @SerializedName("statewise")
    private TotalData[] totalDataArray;

   @SerializedName("tested")
    private TestedSamples[] testedSamplesArray;




    public TotalData[] getTotalDataArray() {
        return totalDataArray;
    }

    public TestedSamples[] getTestedSamplesArray() {
        return testedSamplesArray;
    }

    public DailyData[] getDailyDataArray() {
        return dailyDataArray;
    }
}
