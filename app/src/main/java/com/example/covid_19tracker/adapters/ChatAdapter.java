package com.example.covid_19tracker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.ChatSupportActivities.ChattingActivity;
import com.example.covid_19tracker.ModelClass.Chats;
import com.example.covid_19tracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter {
    private final ArrayList<Chats> chats;
    private final Context context;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    public ChatAdapter(ArrayList<Chats> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.send_chat_item,parent,false);
        return new SenderViewHolder(view);
        }else{
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_chat_item,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
             Chats chatModel=chats.get(position);
             String timestamp=chatModel.getTimestamp();
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime= DateFormat.format("hh:mm a",cal).toString();

             holder.itemView.setOnLongClickListener(v -> {
               new AlertDialog.Builder(context)
                       .setTitle("Delete")
                       .setMessage("Are you sure you want to delete this message")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                                       deleteMessage(position);

                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       }).show();
                 return false;
             });
             if(holder.getClass()==SenderViewHolder.class){
                 ((SenderViewHolder)holder).senderMsg.setText(chatModel.getMessage());
                 ((SenderViewHolder)holder).senderTime.setText(dateTime);
             }else {
                 ((ReceiverViewHolder)holder).receiverMsg.setText(chatModel.getMessage());
                 ((ReceiverViewHolder)holder).receiverTime.setText(dateTime);
             }


    }

    private void deleteMessage(int position) {
        String timestamp=chats.get(position).getTimestamp();
        DatabaseReference msgRef=FirebaseDatabase.getInstance().getReference("Messages");
        Query query=msgRef.orderByChild("timestamp").equalTo(timestamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        dataSnapshot.getRef().removeValue();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getSender().equals(FirebaseAuth.getInstance().getUid())){
           return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg,senderTime;
        public SenderViewHolder(@NonNull  View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg,receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg=itemView.findViewById(R.id.receiverText);
            receiverTime=itemView.findViewById(R.id.receiverTime);
        }
    }
}
