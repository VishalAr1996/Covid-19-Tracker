package com.example.covid_19tracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.ModelClass.TotalData;
import com.example.covid_19tracker.adapters.StateAdapter;
import com.example.covid_19tracker.apiHandler.DataArrayResponse;
import com.example.covid_19tracker.apiHandler.DataFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StateWiseData extends AppCompatActivity {
    EditText etSearch;
    RecyclerView stateRecyclerView;
    ArrayList<TotalData> totalData = new ArrayList<>();
    ProgressDialog progressDialog;
    StateAdapter stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise_data);
        etSearch = findViewById(R.id.etSearch);
        stateRecyclerView = findViewById(R.id.stateRecyclerView);
        setTitle("Affected States");
        progressDialog = new ProgressDialog(StateWiseData.this);
        progressDialog.setMessage("Loading Data..");
        progressDialog.show();

        initializeData();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchState(s.toString());
            }
        });

    }


    private void initializeData() {

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
                    Toast.makeText(StateWiseData.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                DataArrayResponse dataArrayResponse = response.body();
                List<TotalData> data = new ArrayList<>(Arrays.asList(dataArrayResponse.getTotalDataArray()));
                for (int i = 1; i < data.size() - 1; i++) {
                    TotalData allData = new TotalData(data.get(i).getStateName(),data.get(i).getActive(),data.get(i).getConfirmed(),data.get(i).getDeltaconfirmed(),data.get(i).getRecovered(),data.get(i).getDeltarecovered(),data.get(i).getDeaths(),data.get(i).getDeltadeaths(),data.get(i).getLastUpdated());
                    totalData.add(allData);
                }
              initializeRecyclerView();
            }

            @Override
            public void onFailure(Call<DataArrayResponse> call, Throwable t) {
                Toast.makeText(StateWiseData.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void SearchState(String text) {
        ArrayList<TotalData> stateList = new ArrayList<>();
        for (TotalData stateName : totalData) {
            if (stateName.getStateName().toLowerCase().contains(text.toLowerCase())) {
                stateList.add(stateName);
            }
        }
        stateAdapter.stateSearchedList(stateList);
    }

    private void initializeRecyclerView(){
        stateRecyclerView.setHasFixedSize(true);
        stateAdapter = new StateAdapter(StateWiseData.this, totalData);
        stateRecyclerView.setAdapter(stateAdapter);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(StateWiseData.this));
    }

}