package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.covid_19tracker.Authentication.LoginActivity;
import com.example.covid_19tracker.Authentication.UserSetUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SplashScreen extends AppCompatActivity {
   FirebaseAuth mAuth;
   DatabaseReference reference;
   FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();
        fUser=mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        if(fUser!=null){
            reference.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    }else{
                        startActivity(new Intent(SplashScreen.this, UserSetUpActivity.class));
                    }
                    finish();

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }else{
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        }


    }
}