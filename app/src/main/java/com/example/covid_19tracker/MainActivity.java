package com.example.covid_19tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.covid_19tracker.Authentication.LoginActivity;
import com.example.covid_19tracker.ChatSupportActivities.MyChatsActivity;
import com.example.covid_19tracker.ChatSupportActivities.ProfileActivity;
import com.example.covid_19tracker.ChatSupportActivities.AllUsersActivity;
import com.example.covid_19tracker.ChatSupportActivities.ExistingFriendsActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView stateWise;
    ImageButton stats;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    CircleImageView profileImageHeader;
    TextView usernameHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        stateWise = findViewById(R.id.statewise);
        stats=findViewById(R.id.stats);
        mAuth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        stats.setOnClickListener(v -> {
            startActivity(new Intent(this,StatisticsChartActivity.class));

        });
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        welcomeText.setSelected(true);

        TextView chartText = findViewById(R.id.chartText);
        chartText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        chartText.setSelected(true);


        TextView helpline = findViewById(R.id.helpline);
        helpline.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        helpline.setSelected(true);
        helpline.setOnClickListener(v -> {
            startActivity(new Intent(this, HelpLineActivity.class));
        });

        FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid());
        LinearLayout nationWide = findViewById(R.id.nationWide);
        nationWide.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LiveStatsIndiaActivity.class)));

        LinearLayout worldWide = findViewById(R.id.worldWide);
        worldWide.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LiveStatsWorldActivity.class)));

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        View view=navigationView.inflateHeaderView(R.layout.header);
        profileImageHeader=view.findViewById(R.id.profileImage);
        usernameHeader=view.findViewById(R.id.userName);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String profileImageUrl=snapshot.child("profilepic").getValue().toString();
                        String userNAme=snapshot.child("username").getValue().toString();
                        Picasso.get().load(profileImageUrl).placeholder(R.drawable.blank).into(profileImageHeader);
                        usernameHeader.setText(userNAme);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getDetails(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home: {
                break;
            }
            case R.id.nav_help: {
                startActivity(new Intent(this, HelpLineActivity.class));
                break;
            }
            case R.id.nav_sym: {
                startActivity(new Intent(this, SymptomsActivity.class));
                break;
            }
            case R.id.nav_vac: {
                startActivity(new Intent(this, VaccinationActivity.class));
                break;
            }
            case R.id.nav_profile: {
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            }
            case R.id.nav_find_help: {
                startActivity(new Intent(this, AllUsersActivity.class));
                break;
            }
            case R.id.nav_friends: {
                startActivity(new Intent(this, ExistingFriendsActivity.class));
                break;
            }
            case R.id.nav_chats: {
                startActivity(new Intent(this, MyChatsActivity.class));
                break;
            }
            case R.id.nav_logout: {
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}