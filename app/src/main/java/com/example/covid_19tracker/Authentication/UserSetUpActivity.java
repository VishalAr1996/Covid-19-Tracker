package com.example.covid_19tracker.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.covid_19tracker.MainActivity;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.databinding.ActivityUserSetUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserSetUpActivity extends AppCompatActivity {
    ActivityUserSetUpBinding binding;
    private static  final int REQUEST_CODE=101;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference reference;
    StorageReference storageReference;
    Uri imageUri;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserSetUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference= FirebaseStorage.getInstance().getReference().child("profileImage");
        binding.textInputLayoutCurrentUser.getEditText().setText(getIntent().getStringExtra("signup.Name"));
        binding.textInputLayoutEmail.getEditText().setText(getIntent().getStringExtra("signup.Email"));
        binding.currentUserProfile.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_CODE);

        });
        binding.save.setOnClickListener(v -> {
            saveUserDataToFirebase();
        });

    }

    private void saveUserDataToFirebase() {
        String userName=binding.textInputLayoutCurrentUser.getEditText().getText().toString();
        String location=binding.textInputLayoutLocation.getEditText().getText().toString();
        String about=binding.textInputLayoutAbout.getEditText().getText().toString();
        String email=binding.textInputLayoutEmail.getEditText().getText().toString();

        if(userName.isEmpty() ){
            showError(binding.textInputLayoutCurrentUser,"Username is too short");
        }else if(location.isEmpty()){
            showError(binding.textInputLayoutLocation,"Field can't be empty");
        }else if(about.isEmpty()){
            showError(binding.textInputLayoutAbout,"Field can't be empty");
        }
        else if(email.isEmpty()){
            showError(binding.textInputLayoutEmail,"Field can't be empty");
        }else if(imageUri==null){
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Saving Details");
            progressDialog.setMessage("Please wait..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            storageReference.child(mUser.getUid()).putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                storageReference.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap<String ,Object> userDataMap=new HashMap<>();
                                        userDataMap.put("username",userName);
                                        userDataMap.put("location",location);
                                        userDataMap.put("email",email);
                                        userDataMap.put("about",about);
                                        userDataMap.put("profilepic",uri.toString());
                                        //userDataMap.put("status","offline");
                                        reference.child(mUser.getUid()).updateChildren(userDataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(UserSetUpActivity.this, MainActivity.class));
                                                finish();
                                                Toast.makeText(UserSetUpActivity.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(UserSetUpActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
          imageUri=data.getData();
          binding.currentUserProfile.setImageURI(imageUri);
        }
    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}