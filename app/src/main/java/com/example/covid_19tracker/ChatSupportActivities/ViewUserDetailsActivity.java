package com.example.covid_19tracker.ChatSupportActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.databinding.ActivityViewUserDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import java.util.Map;
import java.util.Objects;

public class ViewUserDetailsActivity extends AppCompatActivity {
    ActivityViewUserDetailsBinding binding;
    DatabaseReference reference, requestReference, friendRef;
    FirebaseAuth mAuth;
    String userName, profileImageUrl, about, email, location;
    String myUserName,myProfileUrl,myAbout,myEmail,myLocation;
    String currentState = "nothing_happened";
    String userID;
    String baseUrl="https://fcm.googleapis.com/fcm/send";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userID = getIntent().getStringExtra("userKey");
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        requestReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        requestQueue= Volley.newRequestQueue(this);
        setTitle("Profile");

        //this method will load other users details in my app
        LoadUserData();

        //this method will load my data into other users app
        loadMyData();

        binding.sendHelpRequest.setOnClickListener(v -> sendHelpRequest(userID));
        binding.cancelHelpRequest.setOnClickListener(v -> declineRequest(userID));

        checkUserExistance(userID);
    }

    @SuppressLint("SetTextI18n")
    private void declineRequest(String userId) {
        if (currentState.equals("request_received_but_pending")) {
            HashMap<String, Object> requestHashMap = new HashMap<>();
            requestHashMap.put("requestStatus", "declined");
            requestReference.child(userId).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                    .updateChildren(requestHashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Request Declined", Toast.LENGTH_SHORT).show();
                    currentState = "request_sent_but_declined";
                    binding.sendHelpRequest.setText("Send Help Request");
                    binding.cancelHelpRequest.setVisibility(View.GONE);
                }
            });
        }
        if (currentState.equals("accepted")) {
            friendRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(userId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    friendRef.child(userId).child(mAuth.getCurrentUser().getUid()).removeValue()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(ViewUserDetailsActivity.this, "User Removed", Toast.LENGTH_SHORT).show();
                                    currentState = "nothing_happened";
                                    binding.sendHelpRequest.setText("Send Help Request");
                                    binding.cancelHelpRequest.setVisibility(View.GONE);
                                }
                            });
                }
            });
        }
    }



    // here we will check if friend request sent to a user or already friend
    @SuppressLint("SetTextI18n")
    private void checkUserExistance(String userId) {
        friendRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentState = "accepted";
                    binding.sendHelpRequest.setText("Send Message");
                    binding.cancelHelpRequest.setText("Remove User");
                    binding.cancelHelpRequest.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        friendRef.child(userId).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentState = "accepted";
                    binding.sendHelpRequest.setText("Send Message");
                    binding.cancelHelpRequest.setText("Remove User");
                    binding.cancelHelpRequest.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        requestReference.child(mAuth.getCurrentUser().getUid()).child(userId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (Objects.requireNonNull(snapshot.child("requestStatus").getValue()).toString().equals("pending")) {
                        currentState = "request_sent_but_pending";
                        binding.sendHelpRequest.setText("cancel Help Request");
                        binding.cancelHelpRequest.setVisibility(View.GONE);
                    }
                    if (Objects.requireNonNull(snapshot.child("requestStatus").getValue()).toString().equals("declined")) {
                        currentState = "nothing_happened";
                        binding.sendHelpRequest.setText("send Help Request");
                        binding.cancelHelpRequest.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        requestReference.child(userId).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (Objects.requireNonNull(snapshot.child("requestStatus").getValue()).toString().equals("pending")) {
                        currentState = "request_received_but_pending";
                        binding.sendHelpRequest.setText("Accept Help Request");
                        binding.cancelHelpRequest.setText("Decline Friend Request");
                        binding.cancelHelpRequest.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        if (currentState.equals("nothing_happened")) {
            currentState = "nothing_happened";
            binding.sendHelpRequest.setText("Send help request");
            binding.cancelHelpRequest.setVisibility(View.GONE);
        }
    }


    //this message for handling send request btn
    @SuppressLint("SetTextI18n")
    private void sendHelpRequest(String userId) {
        //for sending requests
        if (currentState.equals("nothing_happened")) {
            sendNotification();
            HashMap<String, Object> requestHashMap = new HashMap<>();
            requestHashMap.put("requestStatus", "pending");
            requestReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(userId)
                    .updateChildren(requestHashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ViewUserDetailsActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                    binding.cancelHelpRequest.setVisibility(View.GONE);
                    currentState = "request_sent_but_pending";
                    binding.sendHelpRequest.setText("cancel Help Request");
                } else {
                    Toast.makeText(ViewUserDetailsActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // for cancelling and declining the request
        if (currentState.equals("request_sent_but_pending") || currentState.equals("request_sent_but_declined")) {
            requestReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(userId)
                    .removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ViewUserDetailsActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                    currentState = "nothing_happened";
                    binding.sendHelpRequest.setText("Send Help Request");
                    binding.cancelHelpRequest.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ViewUserDetailsActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }
        //for receiving requests and accepting request
        if (currentState.equals("request_received_but_pending")) {
            requestReference.child(userId).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                    .removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //otherUsersDetails Updating on firebase
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("requestStatus", "accepted");
                    hashMap.put("username", userName);
                    hashMap.put("profilepic", profileImageUrl);
                    hashMap.put("location",location);

                    //my details updating on firebase
                    HashMap<String, Object> myHashMap = new HashMap<>();
                    myHashMap.put("requestStatus", "accepted");
                    myHashMap.put("username", myUserName);
                    myHashMap.put("profilepic", myProfileUrl);
                    myHashMap.put("location",myLocation);

                    friendRef.child(mAuth.getCurrentUser().getUid()).child(userId).updateChildren(hashMap)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    friendRef.child(userId).child(mAuth.getCurrentUser().getUid()).updateChildren(myHashMap)
                                            .addOnCompleteListener(task11 -> {
                                                currentState = "accepted";
                                                binding.sendHelpRequest.setText("Send Message");
                                                binding.cancelHelpRequest.setText("Remove User");
                                                binding.cancelHelpRequest.setVisibility(View.VISIBLE);
                                            });
                                }
                            });
                }
            });
        }


        //do work after accepting the request
        if (currentState.equals("accepted")) {
            Intent intent=new Intent(ViewUserDetailsActivity.this,ChattingActivity.class);
            intent.putExtra("otherUserKey",userID);
            startActivity(intent);
        }
    }

    private void sendNotification() {
        JSONObject data=new JSONObject();
        try {
            data.put("to","/topics/"+userID);

            JSONObject messageObjet=new JSONObject();
            messageObjet.put("title","You have a help request");
            messageObjet.put("body","from "+myUserName);
            //messageObjet.put("click_action","CHAT_CLICK_ACTION");

            JSONObject userJson=new JSONObject();
            userJson.put("requestUserId",mAuth.getCurrentUser().getUid());
            userJson.put("type","request");

            data.put("notification",messageObjet);
            data.put("data",userJson);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,baseUrl, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.i("myTag",msg);
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


    //this method will load the data of the user we will select
    private void LoadUserData() {
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    profileImageUrl = snapshot.child("profilepic").getValue(String.class);
                    userName = snapshot.child("username").getValue(String.class);
                    location = snapshot.child("location").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    about = snapshot.child("about").getValue(String.class);
                    Picasso.get().load(profileImageUrl).placeholder(R.drawable.blank).into(binding.userProfilePic);
                    binding.helperName.setText(userName);
                    binding.helperEmail.setText(email);
                    binding.helperLocation.setText(location);
                    binding.helperAbout.setText(about);
                } else {
                    Toast.makeText(ViewUserDetailsActivity.this, "Data not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ViewUserDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadMyData() {
        reference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myProfileUrl = snapshot.child("profilepic").getValue(String.class);
                    myUserName = snapshot.child("username").getValue(String.class);
                    myLocation = snapshot.child("location").getValue(String.class);
                    myEmail = snapshot.child("email").getValue(String.class);
                    myAbout = snapshot.child("about").getValue(String.class);
                } else {
                    Toast.makeText(ViewUserDetailsActivity.this, "Data not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ViewUserDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}