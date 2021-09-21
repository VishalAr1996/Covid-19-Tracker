package com.example.covid_19tracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19tracker.ModelClass.TestedSamples;
import com.example.covid_19tracker.ModelClass.TotalData;
import com.example.covid_19tracker.apiHandler.DataArrayResponse;
import com.example.covid_19tracker.apiHandler.DataFetcher;
import com.example.covid_19tracker.databinding.ActivityLiveStatsIndiaBinding;
import com.example.covid_19tracker.utilities.DateFormatUtil;
import com.example.covid_19tracker.utilities.PieChartUtil;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStatsIndiaActivity extends AppCompatActivity {
    static int activePie, recoveredPie, deceasedPie, confirmedPie;
     ProgressDialog progressDialog;
    ActivityLiveStatsIndiaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_live_stats);
        binding = ActivityLiveStatsIndiaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("India Data");
        progressDialog = new ProgressDialog(LiveStatsIndiaActivity.this);
        progressDialog.setMessage("Loading Data..");
        progressDialog.show();
        fetchDataFromServer();
        binding.stateWiseData.setOnClickListener(v -> startActivity(new Intent(LiveStatsIndiaActivity.this, StateWiseData.class)));


    }

    private void fetchDataFromServer() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19india.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataFetcher dataFetcher = retrofit.create(DataFetcher.class);
        Call<DataArrayResponse> call = dataFetcher.getTotalData();
        call.enqueue(new Callback<DataArrayResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DataArrayResponse> call, @NotNull Response<DataArrayResponse> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(LiveStatsIndiaActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                DataArrayResponse dataArrayResponse = response.body();
                assert dataArrayResponse != null;
                List<TotalData> data = new ArrayList<>(Arrays.asList(dataArrayResponse.getTotalDataArray()));
                List<TestedSamples> sampleTested = new ArrayList<>(Arrays.asList(dataArrayResponse.getTestedSamplesArray()));

                binding.activityMainActiveTextview.setText(NumberFormat.getInstance().format(Integer.parseInt(data.get(0).getActive())));
                int activeNew1 = Integer.parseInt(data.get(0).getDeltaconfirmed()) - (Integer.parseInt(data.get(0).getDeltarecovered()) + Integer.parseInt(data.get(0).getDeltadeaths()));
               if(activeNew1<0){
                   binding.activityMainActiveNewTextview.setText(NumberFormat.getInstance().format(activeNew1));
               }else{
                   binding.activityMainActiveNewTextview.setText("+" + NumberFormat.getInstance().format(activeNew1));
               }

                activePie = Integer.parseInt(data.get(0).getActive());

                binding.activityMainConfirmedTextview.setText(NumberFormat.getInstance().format(Integer.parseInt(data.get(0).getConfirmed())));
                binding.activityMainConfirmedNewTextview.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(data.get(0).getDeltaconfirmed())));
                confirmedPie = Integer.parseInt(data.get(0).getConfirmed());

                binding.activityMainRecoveredTextview.setText(NumberFormat.getInstance().format(Integer.parseInt(data.get(0).getRecovered())));
                binding.activityMainRecoveredNewTextview.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(data.get(0).getDeltarecovered())));
                recoveredPie = Integer.parseInt(data.get(0).getRecovered());

                binding.activityMainDeathTextview.setText(NumberFormat.getInstance().format(Integer.parseInt(data.get(0).getDeaths())));
                binding.activityMainDeathNewTextview.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(data.get(0).getDeltadeaths())));
                deceasedPie = Integer.parseInt(data.get(0).getDeaths());
                String test = sampleTested.get(sampleTested.size() - 1).getTotalSampleTested();
                String newTest = sampleTested.get(sampleTested.size() - 1).getNewlyTested();
                if (!test.equals("") && !newTest.equals("")) {
                    binding.activityMainSamplesTextview.setText(NumberFormat.getInstance().format(Integer.parseInt(test)));
                    binding.activityMainSamplesNewTextview.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(newTest)));
                } else {
                    binding.activityMainSamplesTextview.setText("0");
                    binding.activityMainSamplesNewTextview.setText("+" + 0);
                }
                binding.activityMainDateTextview.setText(DateFormatUtil.FormatDate(data.get(0).getLastUpdated(), 1));
                binding.activityMainTimeTextview.setText(DateFormatUtil.FormatDate(data.get(0).getLastUpdated(), 2));
                PieChartUtil.getPieChart(binding.dataChart, activePie, confirmedPie, deceasedPie, recoveredPie,"India");
            }

            @Override
            public void onFailure(@NotNull Call<DataArrayResponse> call, @NotNull Throwable t) {
                Toast.makeText(LiveStatsIndiaActivity.this,t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            Toast.makeText(this, "Refresh Completed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}