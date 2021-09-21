package com.example.covid_19tracker.apiHandler;

import com.example.covid_19tracker.ModelClass.CountryData;
import com.example.covid_19tracker.ModelClass.WorldData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataFetcher {

    @GET("data.json")
    Call<DataArrayResponse> getTotalData();

    @GET("v2/all")
    Call<WorldData> getWorldData();

    @GET("v2/countries")
    Call<List<CountryData>> getAllCountryList();
}
