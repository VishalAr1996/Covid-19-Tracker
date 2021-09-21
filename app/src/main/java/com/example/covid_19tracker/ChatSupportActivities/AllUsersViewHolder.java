package com.example.covid_19tracker.ChatSupportActivities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.R;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profilePhoto;
    TextView user_name,location;

    public AllUsersViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        profilePhoto=itemView.findViewById(R.id.profilePhoto);
        user_name=itemView.findViewById(R.id.singleUserName);
        location=itemView.findViewById(R.id.location);
    }
}
