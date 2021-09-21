package com.example.covid_19tracker.ChatSupportActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    DatabaseReference reference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    private static final int REQUEST_CODE = 103;
    FirebaseAuth auth;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Profile");
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImage");
        showProfileData();
        binding.profileImageView.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE);
                }
        );
        binding.saveProfileDetails.setOnClickListener(v -> updateProfileDetails());
    }

    private void updateProfileDetails() {
        String userNameProfile = Objects.requireNonNull(binding.textInputProfileName.getEditText()).getText().toString();
        String userEmailProfile = Objects.requireNonNull(binding.textInputProfileEmail.getEditText()).getText().toString();
        String userLocationProfile = Objects.requireNonNull(binding.textInputProfileLocation.getEditText()).getText().toString();
        String userAboutProfile = Objects.requireNonNull(binding.textInputProfileAbout.getEditText()).getText().toString();
        progressDialog.setTitle("Updating Details");
        progressDialog.setMessage("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        storageReference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storageReference.child(auth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                            HashMap<String, Object> userDataMap = new HashMap<>();
                            userDataMap.put("username", userNameProfile);
                            userDataMap.put("location", userLocationProfile);
                            userDataMap.put("email", userEmailProfile);
                            userDataMap.put("about", userAboutProfile);
                            userDataMap.put("profilepic", uri.toString());
                            reference.child(auth.getCurrentUser().getUid()).updateChildren(userDataMap).addOnSuccessListener(unused -> {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    });
                        });
                    }
                });
//        HashMap<String ,Object> userDataMap=new HashMap<>();
//        userDataMap.put("username",userNameProfile);
//        userDataMap.put("location",userLocationProfile);
//        userDataMap.put("email",userEmailProfile);
//        userDataMap.put("about",userAboutProfile);
//        userDataMap.put("profilepic",imageUri.toString());
//        reference.child(auth.getCurrentUser().getUid()).updateChildren(userDataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(ProfileActivity.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    private void showProfileData() {
        reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = snapshot.child("profilepic").getValue(String.class);
                    String userNAme = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String about = snapshot.child("about").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    Picasso.get().load(profileImageUrl).placeholder(R.drawable.blank).into(binding.profileImageView);
                    Objects.requireNonNull(binding.textInputProfileName.getEditText()).setText(userNAme);
                    Objects.requireNonNull(binding.textInputProfileEmail.getEditText()).setText(email);
                    Objects.requireNonNull(binding.textInputProfileLocation.getEditText()).setText(location);
                    Objects.requireNonNull(binding.textInputProfileAbout.getEditText()).setText(about);
                } else {
                    Toast.makeText(ProfileActivity.this, "Data not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.profileImageView.setImageURI(imageUri);
        }
    }
}