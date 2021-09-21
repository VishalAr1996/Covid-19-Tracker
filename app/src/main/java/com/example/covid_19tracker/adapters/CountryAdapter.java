package com.example.covid_19tracker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.covid_19tracker.EachCountryDataActivity;
import com.example.covid_19tracker.ModelClass.CountryData;
import com.example.covid_19tracker.databinding.CountryRowBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_ACTIVE;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_CONFIRMED;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_CONFIRMED_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_DEATH;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_DEATH_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_NAME;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_RECOVERED;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_RECOVERED_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.COUNTRY_TESTS;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<CountryData> countryData;
    public CountryAdapter(Context mContext, ArrayList<CountryData> countryData) {
        this.context = mContext;
        this.countryData = countryData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CountryRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String countryRank = String.valueOf(position + 1);
        holder.countryRowBinding.countryName.setText(countryData.get(position).getCountry());
        holder.countryRowBinding.countryConfirmedCases.setText(NumberFormat.getInstance().format(Integer.parseInt(countryData.get(position).getConfirmed())));
        holder.countryRowBinding.countryRank.setText(countryRank + ".");
        Glide.with(context).load(countryData.get(position).getCountryInfo().getFlag()).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.countryRowBinding.countryFlag);
        holder.countryRowBinding.countryContainer.setOnClickListener(v -> {
            Intent intent = new Intent(context, EachCountryDataActivity.class);
            CountryData currentItem = countryData.get(position);
            intent.putExtra(COUNTRY_NAME, currentItem.getCountry());
            intent.putExtra(COUNTRY_ACTIVE, currentItem.getActive());
            intent.putExtra(COUNTRY_CONFIRMED, currentItem.getConfirmed());
            intent.putExtra(COUNTRY_CONFIRMED_NEW, currentItem.getConfirmedNew());
            intent.putExtra(COUNTRY_RECOVERED, currentItem.getRecovered());
            intent.putExtra(COUNTRY_RECOVERED_NEW, currentItem.getRecoveredNew());
            intent.putExtra(COUNTRY_DEATH, currentItem.getDeceased());
            intent.putExtra(COUNTRY_DEATH_NEW, currentItem.getDeceasedNew());
            intent.putExtra(COUNTRY_TESTS, currentItem.getTotalTests());
            context.startActivity(intent);
        });
    }

    public void searchCountry(ArrayList<CountryData> allCountryData) {
        countryData = allCountryData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return countryData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CountryRowBinding countryRowBinding;

        public MyViewHolder(CountryRowBinding countryRowBinding) {
            super(countryRowBinding.getRoot());
            this.countryRowBinding = countryRowBinding;
        }
    }
}
