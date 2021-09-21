package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.covid_19tracker.ModelClass.WorldData;
import com.example.covid_19tracker.apiHandler.DataFetcher;
import com.example.covid_19tracker.databinding.ActivityLiveStatsWorldBinding;
import com.example.covid_19tracker.utilities.PieChartUtil;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStatsWorldActivity extends AppCompatActivity {
  ActivityLiveStatsWorldBinding binding;
  ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stats_world);
        binding=ActivityLiveStatsWorldBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("WorldWide Data");
        progressDialog = new ProgressDialog(LiveStatsWorldActivity.this);
        progressDialog.setMessage("Loading Data..");
        progressDialog.show();
        fetchWorldData();
        binding.countryWiseData.setOnClickListener(v -> startActivity(new Intent(LiveStatsWorldActivity.this,CountryWiseData.class)));
    }

    private void fetchWorldData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataFetcher dataFetcher = retrofit.create(DataFetcher.class);
        Call<WorldData> call = dataFetcher.getWorldData();
        call.enqueue(new Callback<WorldData>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<WorldData> call, @NotNull Response<WorldData> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(LiveStatsWorldActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                WorldData worldData=response.body();
                binding.worldWideActive.setText(NumberFormat.getInstance().format(Integer.parseInt(worldData.getActive())));
                int activeNew=Integer.parseInt(worldData.getConfirmedNew())-(Integer.parseInt(worldData.getRecoveredNew())+Integer.parseInt(worldData.getDeceasedNew()));
               if(activeNew<0){
                   binding.worldWideActiveNew.setText(NumberFormat.getInstance().format(activeNew));
               }else{
                   binding.worldWideActiveNew.setText("+"+NumberFormat.getInstance().format(activeNew));
               }

                binding.worldWideConfirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(worldData.getConfirmed())));
                binding.worldWideConfirmedNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(worldData.getConfirmedNew())));
                binding.worldWideRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(worldData.getRecovered())));
                binding.worldWideRecoveredNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(worldData.getRecoveredNew())));
                binding.worldWideDeceased.setText(NumberFormat.getInstance().format(Integer.parseInt(worldData.getDeceased())));
                binding.worldWideDeceasedNew.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(worldData.getDeceasedNew())));
                binding.worldWideTotalTested.setText(worldData.getTotalTests());
                PieChartUtil.getPieChart(binding.worldPieChart,Integer.parseInt(worldData.getActive()),Integer.parseInt(worldData.getConfirmed())
                        ,Integer.parseInt(worldData.getDeceased()),Integer.parseInt(worldData.getRecovered()),"the World");
            }

            @Override
            public void onFailure(@NotNull Call<WorldData> call, @NotNull Throwable t) {
                Toast.makeText(LiveStatsWorldActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}