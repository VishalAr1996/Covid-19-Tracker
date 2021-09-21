package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import com.example.covid_19tracker.databinding.ActivityEachCountrydataBinding;

import com.example.covid_19tracker.utilities.PieChartUtil;

import java.text.NumberFormat;

import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_ACTIVE;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_CONFIRMED;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_CONFIRMED_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_DEATH;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_DEATH_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_NAME;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_RECOVERED;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_RECOVERED_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_TESTS;

public class EachCountryDataActivity extends AppCompatActivity {

    private String countryName, confirmed, confirmedNew,active, death, deathNew,
            recovered, recoveredNew, totalTests;
    ActivityEachCountrydataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEachCountrydataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtrasFromAdapter();
        setTitle(countryName);
        fetchDataForEachCountry();

    }

    @SuppressLint("SetTextI18n")
    private void fetchDataForEachCountry() {
        binding.singleCountryActive.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));
        int activeNew=Integer.parseInt(confirmedNew)-(Integer.parseInt(recoveredNew)+Integer.parseInt(deathNew));
        if(activeNew>=0){
            binding.singleCountryActiveNew.setText("+"+NumberFormat.getInstance().format(activeNew));
        }else{
            binding.singleCountryActiveNew.setText(NumberFormat.getInstance().format(activeNew));
        }

        binding.singleCountryConfirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(confirmed)));
        binding.singleCountryConfirmedNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(confirmedNew)));
        binding.singleCountryRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(recovered)));
        binding.singleCountryRecoveredNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(recoveredNew)));
        binding.singleCountryDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(death)));
        binding.singleCountryDeathNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(deathNew)));
        binding.singleCountryTotalTests.setText(totalTests);


        PieChartUtil.getPieChart(binding.countryPieChart,Integer.parseInt(active),Integer.parseInt(confirmed),Integer.parseInt(death),Integer.parseInt(recovered),countryName);
    }

    private void getIntentExtrasFromAdapter() {
        Intent intent = getIntent();
        countryName = intent.getStringExtra(COUNTRY_NAME);
        active = intent.getStringExtra(COUNTRY_ACTIVE);
        confirmed = intent.getStringExtra(COUNTRY_CONFIRMED);
        confirmedNew = intent.getStringExtra(COUNTRY_CONFIRMED_NEW);
        recovered = intent.getStringExtra(COUNTRY_RECOVERED);
        recoveredNew = intent.getStringExtra(COUNTRY_RECOVERED_NEW);
        death = intent.getStringExtra(COUNTRY_DEATH);
        deathNew = intent.getStringExtra(COUNTRY_DEATH_NEW);
        totalTests = intent.getStringExtra(COUNTRY_TESTS);
    }
}