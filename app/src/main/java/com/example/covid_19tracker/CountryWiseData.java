package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covid_19tracker.ModelClass.CountryData;
import com.example.covid_19tracker.adapters.CountryAdapter;
import com.example.covid_19tracker.apiHandler.DataFetcher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryWiseData extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView countryRecyclerView;
    ArrayList<CountryData> countryData=new ArrayList<>();
    CountryAdapter countryAdapter;
    EditText etSearchCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_wise);
        etSearchCountry=findViewById(R.id.etSearchCountry);
        countryRecyclerView=findViewById(R.id.countryRecyclerView);
        setTitle("Affected Countries");
        progressDialog = new ProgressDialog(CountryWiseData.this);
        progressDialog.setMessage("Loading Data..");
        progressDialog.show();
        fetchWorldData();
        etSearchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchCountry(s.toString());
            }
        });
    }

    private void fetchWorldData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataFetcher dataFetcher = retrofit.create(DataFetcher.class);
        Call<List<CountryData>> call = dataFetcher.getAllCountryList();
        call.enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(CountryWiseData.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<CountryData> data=response.body();
                for(int pos = 0; pos<data.size()-1; pos++){
                    String countryName=data.get(pos).getCountry();
                    String active=data.get(pos).getActive();
                    String flag=data.get(pos).getCountryInfo().getFlag();
                    CountryData.CountryInfo countryInfo=new CountryData.CountryInfo(flag);
                   // Log.i("myTag",countryName+flag);
                    CountryData dataOfCountry=new CountryData(countryName,active,data.get(pos).getConfirmed(),data.get(pos).getConfirmedNew(),
                            data.get(pos).getRecovered(),data.get(pos).getRecoveredNew(),data.get(pos).getDeceased(),data.get(pos).getDeceasedNew()
                            ,data.get(pos).getTotalTests(),countryInfo);
                    countryData.add(dataOfCountry);
                   // Log.i("myTag",countryData.get(pos).getCountry()+countryData.get(pos).getConfirmed()+countryData.get(pos).getCountryInfo().getFlag());
                }
                initializeRecyclerView();

            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(CountryWiseData.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private  void searchCountry(String text){
        ArrayList<CountryData> allCountryData=new ArrayList<>();
        for(CountryData eachCountryData:countryData){
            if(eachCountryData.getCountry().toLowerCase().contains(text.toLowerCase())){
                allCountryData.add(eachCountryData);
            }

        }
        countryAdapter.searchCountry(allCountryData);


    }
    private void initializeRecyclerView(){
        countryRecyclerView.setHasFixedSize(true);
        countryAdapter = new CountryAdapter(CountryWiseData.this, countryData);
        countryRecyclerView.setAdapter(countryAdapter);
        countryRecyclerView.setLayoutManager(new LinearLayoutManager(CountryWiseData.this));
    }
}