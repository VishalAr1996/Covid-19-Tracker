package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.covid_19tracker.databinding.ActivityEachStatedataBinding;
import com.example.covid_19tracker.utilities.DateFormatUtil;
import com.example.covid_19tracker.utilities.PieChartUtil;

import java.text.NumberFormat;

import static com.example.covid_19tracker.utilities.keyConstants.STATE_ACTIVE;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_CONFIRMED;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_CONFIRMED_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_DEATH;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_DEATH_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_LAST_UPDATE;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_NAME;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_RECOVERED;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_RECOVERED_NEW;

public class EachStateDataActivity extends AppCompatActivity {
    ActivityEachStatedataBinding binding;

    private String stateName, confirmed, confirmedNew,active, death, deathNew,
            recovered, recoveredNew, lastUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEachStatedataBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        getIntentExtrasFromAdapter();
        setTitle(stateName);
        initializeData();
    }

    @SuppressLint("SetTextI18n")
    private void initializeData() {
        binding.singleStateActive.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));
        int activeNew=Integer.parseInt(confirmedNew)-(Integer.parseInt(recoveredNew)+Integer.parseInt(deathNew));
        if(activeNew<0){
            binding.singleStateActiveNew.setText(NumberFormat.getInstance().format(activeNew));
        }else{
            binding.singleStateActiveNew.setText("+"+NumberFormat.getInstance().format(activeNew));
        }
        binding.singleStateConfirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(confirmed)));
        binding.singleStateConfirmedNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(confirmedNew)));
        binding.singleStateRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(recovered)));
        binding.singleStateRecoveredNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(recoveredNew)));
        binding.singleStateDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(death)));
        binding.singleStateDeathNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(deathNew)));
        binding.singleStateLastUpdateDate.setText(DateFormatUtil.FormatDate(lastUpdate,1));
        binding.singleStateLastUpdateTime.setText(DateFormatUtil.FormatDate(lastUpdate,2));

        PieChartUtil.getPieChart(binding.statePieChart,Integer.parseInt(active),Integer.parseInt(confirmed),Integer.parseInt(death),Integer.parseInt(recovered),stateName);
    }

    private void getIntentExtrasFromAdapter() {
        Intent intent = getIntent();
        stateName = intent.getStringExtra(STATE_NAME);
        confirmed = intent.getStringExtra(STATE_CONFIRMED);
        confirmedNew = intent.getStringExtra(STATE_CONFIRMED_NEW);
        active = intent.getStringExtra(STATE_ACTIVE);
        death = intent.getStringExtra(STATE_DEATH);
        deathNew = intent.getStringExtra(STATE_DEATH_NEW);
        recovered = intent.getStringExtra(STATE_RECOVERED);
        recoveredNew = intent.getStringExtra(STATE_RECOVERED_NEW);
        lastUpdate = intent.getStringExtra(STATE_LAST_UPDATE);
    }
}