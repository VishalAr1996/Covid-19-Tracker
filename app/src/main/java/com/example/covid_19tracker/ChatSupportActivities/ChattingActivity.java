package com.example.covid_19tracker.ChatSupportActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19tracker.MessageNotification.NotificationApi;
import com.example.covid_19tracker.ModelClass.Chats;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.adapters.ChatAdapter;
import com.example.covid_19tracker.databinding.ActivityChattingBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChattingActivity extends AppCompatActivity {
  ActivityChattingBinding binding;
  String receiverId;
  ArrayList<Chats> myChats;
  DatabaseReference userReference,messageReference;
//  FirebaseRecyclerOptions<Chats> chatOptions;
//  FirebaseRecyclerAdapter<Chats,ChatViewHolder> chatAdapter;
    ChatAdapter chatAdapter;
  String receiverProfileUrl,myProfileUrl,myUserName;
  String baseUrl="https://fcm.googleapis.com/fcm/send";
  RequestQueue requestQueue;
  FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding=ActivityChattingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        receiverId=getIntent().getStringExtra("otherUserKey");
        mAuth=FirebaseAuth.getInstance();
        binding.chatRecyclerView.setHasFixedSize(true);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestQueue=Volley.newRequestQueue(this);
        userReference= FirebaseDatabase.getInstance().getReference().child("Users");
        messageReference=FirebaseDatabase.getInstance().getReference().child("Messages");
        loadMyProfilePic();
       // binding.sendMessage.setOnClickListener(v -> sendMessage());
        binding.sendMessage.setOnClickListener(v -> {
            String msg= binding.typeMessage.getText().toString();
            if(!msg.equals("")){
                sendMessage(mAuth.getCurrentUser().getUid(),receiverId,msg);
            }else{
                Toast.makeText(this, "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
            binding.typeMessage.setText("");
        });
        loadOtherParticipantData();
        binding.back.setOnClickListener(v -> onBackPressed());
        //loadAllChats();
        readAllChats(mAuth.getCurrentUser().getUid(),receiverId);
    }

    private void readAllChats(String senderId, String receiverId) {
        myChats=new ArrayList<>();
       messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myChats.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chats chat=dataSnapshot.getValue(Chats.class);
                    if(chat.getReceiver().equals(senderId) && chat.getSender().equals(receiverId)
                            ||chat.getReceiver().equals(receiverId) && chat.getSender().equals(senderId)){
                        myChats.add(chat);
                    }

                    if(!myChats.isEmpty()){
                        chatAdapter=new ChatAdapter(myChats,ChattingActivity.this);
                        binding.chatRecyclerView.smoothScrollToPosition(myChats.size()-1);
                        binding.chatRecyclerView.setAdapter(chatAdapter);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private  void sendMessage(String sender,String receiver , String message){
        sendNotification(message);
        String timestamp=String.valueOf(System.currentTimeMillis());
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        messageReference.push().setValue(hashMap);

        final DatabaseReference chatReference=FirebaseDatabase.getInstance().getReference("ChatList")
                .child(mAuth.getUid())
                .child(receiverId);
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatReference.child("id").setValue(receiverId);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private void loadMyProfilePic() {
        userReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    myProfileUrl=snapshot.child("profilepic").getValue().toString();
                    myUserName=snapshot.child("username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ChattingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadAllChats() {
//        chatOptions=new FirebaseRecyclerOptions.Builder<Chats>()
//                .setQuery(messageReference.child(mAuth.getCurrentUser().getUid()),Chats.class)
//                .build();
//        chatAdapter=new FirebaseRecyclerAdapter<Chats, ChatViewHolder>(chatOptions) {
//            @Override
//            protected void onBindViewHolder(@NonNull @NotNull ChatViewHolder holder, int position, @NonNull @NotNull Chats model) {
//                if(model.getSenderId().equals(mAuth.getCurrentUser().getUid())){
//                    holder.receiverText.setVisibility(View.GONE);
//                    holder.receiverProfile.setVisibility(View.GONE);
//                    holder.senderText.setVisibility(View.VISIBLE);
//                    holder.senderProfile.setVisibility(View.VISIBLE);
//                    holder.senderText.setText(model.getMsg());
//                    Picasso.get().load(myProfileUrl).placeholder(R.drawable.blank)
//                            .into(holder.senderProfile);
//                }else{
//                    holder.receiverText.setVisibility(View.VISIBLE);
//                    holder.receiverProfile.setVisibility(View.VISIBLE);
//                    holder.senderText.setVisibility(View.GONE);
//                    holder.senderProfile.setVisibility(View.GONE);
//                    holder.receiverText.setText(model.getMsg());
//                    Picasso.get().load(receiverProfileUrl).placeholder(R.drawable.blank)
//                            .into(holder.receiverProfile);
//                }
//            }
//
//            @NonNull
//            @NotNull
//            @Override
//            public ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_chat_item,parent,false);
//                return new ChatViewHolder(view);
//            }
//        };
//        chatAdapter.startListening();
//        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
//            }
//        });
//        binding.chatRecyclerView.setAdapter(chatAdapter);
    }

    private void sendMessages() {
        String msg=binding.typeMessage.getText().toString();
        if(msg.isEmpty()){
            Toast.makeText(this, "Can't send empty message", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,Object> msgHashmap=new HashMap<>();
            msgHashmap.put("msg",msg);
            msgHashmap.put("msgStatus","unseen");
            msgHashmap.put("senderId",mAuth.getCurrentUser().getUid());
            messageReference.child(receiverId).child(mAuth.getCurrentUser().getUid()).push().updateChildren(msgHashmap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                messageReference.child(mAuth.getCurrentUser().getUid()).child(receiverId).push()
                                        .updateChildren(msgHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            sendNotification(msg);
                                            binding.typeMessage.setText("");

                                        }
                                    }
                                });
                            }
                        }
                    });
        }
    }

    private void sendNotification(String msg) {
        JSONObject data=new JSONObject();
        try {
            data.put("to","/topics/"+receiverId);

            JSONObject messageObjet=new JSONObject();
            messageObjet.put("title","Message from "+myUserName);
            messageObjet.put("body",msg);
            //messageObjet.put("click_action","CHAT_CLICK_ACTION");

            JSONObject userJson=new JSONObject();
            userJson.put("userID",mAuth.getCurrentUser().getUid());
            userJson.put("type","msg");

            data.put("notification",messageObjet);
            data.put("data",userJson);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,baseUrl, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("myTag",msg);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("myTag","failed");
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> map=new HashMap<>();
                    map.put("content-type","application/json");
                    map.put("authorization","key=AAAAiuHyqLs:APA91bFx24JmAnA08SX3Fgii7N88hMvduTh_LJaDVYewwZoDxxQhtCdmFVgWTlxC4xzB7MIeJqPoWO5c1s_co6mzzhvJ9QwBpj2mM1WnHKYGF1eIa3xVc4WDDNKourzTDryogWUb_hnI");
                    return map;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadOtherParticipantData() {
        userReference.child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    receiverProfileUrl=snapshot.child("profilepic").getValue().toString();
                    binding.chatUserName.setText(snapshot.child("username").getValue().toString());
                    Picasso.get().load(receiverProfileUrl).placeholder(R.drawable.blank)
                            .into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ChattingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

