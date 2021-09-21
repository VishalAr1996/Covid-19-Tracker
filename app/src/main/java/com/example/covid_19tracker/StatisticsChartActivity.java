package com.example.covid_19tracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.covid_19tracker.ModelClass.CountryData;
import com.example.covid_19tracker.ModelClass.DailyData;
import com.example.covid_19tracker.ModelClass.TotalData;
import com.example.covid_19tracker.adapters.ChartAdapter;
import com.example.covid_19tracker.apiHandler.DataArrayResponse;
import com.example.covid_19tracker.apiHandler.DataFetcher;
import com.example.covid_19tracker.databinding.ActivityStatisticsChartBinding;
import com.example.covid_19tracker.enums.CaseTypes;
import com.example.covid_19tracker.enums.TimePeriod;
import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatisticsChartActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ActivityStatisticsChartBinding binding;
    ArrayList<CountryData> countryDataItem;
    ArrayList<TotalData> stateDataItem;
    private List<DailyData> nationalDailyData;
    ChartAdapter chartAdapter;
    String cases;
    private List<DailyData> currentlyShowingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("India Graphical Statistics");
        countryDataItem = new ArrayList<>();
        stateDataItem = new ArrayList<>();
        progressDialog = new ProgressDialog(StatisticsChartActivity.this);
        progressDialog.setMessage("Loading Data..");
        progressDialog.show();
        fetchNationWideData();
    }

    private void fetchNationWideData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19india.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataFetcher dataFetcher = retrofit.create(DataFetcher.class);
        Call<DataArrayResponse> call = dataFetcher.getTotalData();
        call.enqueue(new Callback<DataArrayResponse>() {
            @Override
            public void onResponse(Call<DataArrayResponse> call, Response<DataArrayResponse> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(StatisticsChartActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                DataArrayResponse dataArrayResponse = response.body();
                List<DailyData> data = new ArrayList<>(Arrays.asList(dataArrayResponse.getDailyDataArray()));
                if (data != null) {
                    setChartDataEvent();
                    nationalDailyData = data;
//                  float ratio=(Integer.parseInt(nationalDailyData.get(nationalDailyData.size()-1).getDailyRecovered()))/(Integer.parseInt(nationalDailyData.get(nationalDailyData.size()-1).getDailyConfirmed()));
//                    Log.d("myTag", );
                    updateChartWithData(nationalDailyData);

                }
            }

            @Override
            public void onFailure(Call<DataArrayResponse> call, Throwable t) {
                Toast.makeText(StatisticsChartActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void setChartDataEvent() {
        //  binding.tvRatio.setCharacterLists(TickerUtils.provideNumberList());
        binding.tvCaseCount.setCharacterLists(TickerUtils.provideNumberList());
        binding.sparkChart.setScrubEnabled(true);
        binding.sparkChart.setFillType(SparkView.FillType.DOWN);//iska build bhejna and baokhi dekhlo last wale ka just reverse show ke eha graph but working me hai
        binding.sparkChart.setScrubListener(value -> {
            if (value instanceof DailyData) {
                updateInfoForDate((DailyData) value);
            }
        });
        binding.radioGroupTimePeriod.setOnCheckedChangeListener((n, checkedId) -> {
            switch (checkedId) {
                case R.id.lastWeek: {
                    updateTimePeriodData(TimePeriod.WEEK);
                    break;
                }
                case R.id.lastMonth: {
                    updateTimePeriodData(TimePeriod.MONTH);
                    break;
                }
                case R.id.allTime: {
                    updateTimePeriodData(TimePeriod.ALLTIME);
                    break;
                }
            }
        });
        binding.radioGroupCases.setOnCheckedChangeListener((n, checkedId) -> {
            switch (checkedId) {
                case R.id.confirmed: {
                    binding.sparkChart.setLineColor(ContextCompat.getColor(this, R.color.confirmed));
                    binding.tvCaseCount.setTextColor(ContextCompat.getColor(this, R.color.confirmed));
                    updateCaseTypeData(CaseTypes.CONFIRMED);
                    break;
                }
                case R.id.recovered: {
                    binding.sparkChart.setLineColor(ContextCompat.getColor(this, R.color.recovered));
                    binding.tvCaseCount.setTextColor(ContextCompat.getColor(this, R.color.recovered));
                    updateCaseTypeData(CaseTypes.RECOVERED);
                    break;
                }
            }
            ;
        });
    }

    private void updateTimePeriodData(TimePeriod timePeriod) {
        chartAdapter.timePeriod = timePeriod;
        chartAdapter.notifyDataSetChanged();
    }

    private void updateCaseTypeData(CaseTypes caseTypes) {
        chartAdapter.caseTypes = caseTypes;
        chartAdapter.notifyDataSetChanged();
        updateInfoForDate(currentlyShowingData.get(currentlyShowingData.size() - 1));
    }

    private void updateChartWithData(List<DailyData> dailyData) {
        currentlyShowingData = dailyData;
        chartAdapter = new ChartAdapter(dailyData);
        binding.sparkChart.setAdapter(chartAdapter);
        binding.recovered.setChecked(true);
        binding.allTime.setChecked(true);
        updateInfoForDate(dailyData.get(dailyData.size() - 1));

    }

    private void updateInfoForDate(DailyData dailyData) {
        if (chartAdapter.caseTypes == CaseTypes.CONFIRMED) {
            cases = dailyData.getDailyConfirmed();
        } else {
            cases = dailyData.getDailyRecovered();
        }
        binding.tvCaseCount.setText(NumberFormat.getInstance().format(Integer.parseInt(cases)));
        binding.tvDate.setText(dailyData.getDate());


    }

}