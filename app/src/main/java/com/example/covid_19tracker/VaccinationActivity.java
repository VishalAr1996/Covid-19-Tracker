package com.example.covid_19tracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19tracker.ModelClass.TestedSamples;
import com.example.covid_19tracker.apiHandler.DataArrayResponse;
import com.example.covid_19tracker.apiHandler.DataFetcher;
import com.example.covid_19tracker.databinding.ActivityVaccinationBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VaccinationActivity extends AppCompatActivity {
    ActivityVaccinationBinding binding;
    String registrationUrl= "https://selfregistration.cowin.gov.in/";
    String nearbyLocationUrl="https://www.cowin.gov.in/home";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVaccinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Vaccination Details");
        binding.nearest.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        binding.nearest.setSelected(true);
        progressDialog = new ProgressDialog(VaccinationActivity.this);
        progressDialog.setMessage("Loading Data..");
        progressDialog.show();
        binding.registration.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(registrationUrl));
            startActivity(intent);
        });
        binding.nearbyVaccinationCentre.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(nearbyLocationUrl));
            startActivity(intent);
        });
        getVaccinationData();
    }

    private void getVaccinationData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19india.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataFetcher dataFetcher = retrofit.create(DataFetcher.class);
        Call<DataArrayResponse> call = dataFetcher.getTotalData();
        call.enqueue(new Callback<DataArrayResponse>() {
            @SuppressLint({"SetTextI18n", "NewApi"})
            @Override
            public void onResponse(@NotNull Call<DataArrayResponse> call, @NotNull Response<DataArrayResponse> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(VaccinationActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                DataArrayResponse dataArrayResponse = response.body();
                assert dataArrayResponse != null;
                List<TestedSamples> sampleTested = new ArrayList<>(Arrays.asList(dataArrayResponse.getTestedSamplesArray()));
                TestedSamples currentItem=sampleTested.get(sampleTested.size()-1);
                String totalRegistered=currentItem.getTotalindividualsregistered();
                String totalVaccinated=currentItem.getTotalindividualsvaccinated();
                String firstDose=currentItem.getFirstdoseadministered();
                String secondDose=currentItem.getSeconddoseadministered();
                String registration18plus=currentItem.getRegistrationEighteenPlus();
                String registrationFourtyPlus=currentItem.getRegistrationabove45years();
                if(!totalRegistered.equals("")) {
                    binding.totalRegistered.setText(NumberFormat.getInstance().format(Integer.parseInt(totalRegistered)));
                }else {
                    binding.totalRegistered.setText("yet to update");
                }

                if(!totalVaccinated.equals("")){
                    binding.totalVaccinated.setText(NumberFormat.getInstance().format(Integer.parseInt(currentItem.getTotalindividualsvaccinated())));
                }else{
                    binding.totalVaccinated.setText("yet to update");
                }


                if(!firstDose.equals("")){
                    binding.firstDose.setText(NumberFormat.getInstance().format(Integer.parseInt(currentItem.getFirstdoseadministered())));
                }else{
                    binding.firstDose.setText("yet to update");
                }
                if(!secondDose.equals("")){
                    binding.secondDose.setText(NumberFormat.getInstance().format(Integer.parseInt(currentItem.getSeconddoseadministered())));
                }else{
                    binding.secondDose.setText("yet to update");
                }


                if(!registration18plus.equals("")){
                    binding.registrationEighteenPlus.setText(NumberFormat.getInstance().format(Integer.parseInt(currentItem.getRegistrationEighteenPlus())));
                }else{
                    binding.registrationEighteenPlus.setText("yet to update");
                }

                if(!registrationFourtyPlus.equals("")){
                    binding.registrationFourtyPlus.setText(NumberFormat.getInstance().format(Integer.parseInt(currentItem.getRegistrationabove45years())));
                }else {
                    binding.registrationFourtyPlus.setText("yet to update");
                }
            }

            @Override
            public void onFailure(@NotNull Call<DataArrayResponse> call, @NotNull Throwable t) {
                Toast.makeText(VaccinationActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}