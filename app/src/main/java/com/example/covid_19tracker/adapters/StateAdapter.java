package com.example.covid_19tracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.EachStateDataActivity;
import com.example.covid_19tracker.ModelClass.TotalData;
import com.example.covid_19tracker.databinding.StateRowBinding;

import java.text.NumberFormat;
import java.util.ArrayList;

import static com.example.covid_19tracker.utilities.keyConstants.STATE_ACTIVE;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_CONFIRMED;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_CONFIRMED_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_DEATH;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_DEATH_NEW;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_LAST_UPDATE;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_NAME;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_RECOVERED;
import static com.example.covid_19tracker.utilities.keyConstants.STATE_RECOVERED_NEW;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private Context context;
    private ArrayList<TotalData> totalData;

    public StateAdapter(Context context, ArrayList<TotalData> totalData) {
        this.context = context;
        this.totalData = totalData;
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StateViewHolder(StateRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {
                 holder.binding.StateTextView.setText(totalData.get(position).getStateName());
                 holder.binding.StateTotalCaseTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(totalData.get(position).getConfirmed())));
                 holder.binding.parentLayout.setOnClickListener(v -> {
                     TotalData current=totalData.get(position);
                     Intent intent=new Intent(context, EachStateDataActivity.class);
                     intent.putExtra(STATE_NAME,current.getStateName());
                     intent.putExtra(STATE_CONFIRMED, current.getConfirmed());
                     intent.putExtra(STATE_CONFIRMED_NEW, current.getDeltaconfirmed());
                     intent.putExtra(STATE_ACTIVE, current.getActive());
                     intent.putExtra(STATE_DEATH, current.getDeaths());
                     intent.putExtra(STATE_DEATH_NEW, current.getDeltadeaths());
                     intent.putExtra(STATE_RECOVERED, current.getRecovered());
                     intent.putExtra(STATE_RECOVERED_NEW, current.getDeltarecovered());
                     intent.putExtra(STATE_LAST_UPDATE, current.getLastUpdated());
                     context.startActivity(intent);
                 });
    }

    @Override
    public int getItemCount() {
        return totalData.size();
    }

    public void stateSearchedList(ArrayList<TotalData> stateList) {
        totalData=stateList;
        notifyDataSetChanged();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder {
       private StateRowBinding binding;
        public StateViewHolder(StateRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

    }
}
