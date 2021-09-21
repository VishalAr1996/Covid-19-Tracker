package com.example.covid_19tracker.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(this);
        binding.signupBtn.setOnClickListener(v -> {
            completeRegistration();
        });
        binding.gotoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this,LoginActivity.class));
        });
        binding.textInputPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
    }

    private void completeRegistration() {
        if(binding.signupEmail.getText().toString().equals("")){
            showError(binding.textInputEmail,"Not a valid email");
        }else if(binding.signupPassword.getText().toString().equals("")){
            showError(binding.textInputPassword," Password can't be empty");
        }else{
              progressDialog.setTitle("Please wait");
              progressDialog.setMessage("Registration is in progress...");
              progressDialog.setCanceledOnTouchOutside(false);
              progressDialog.show();
              mAuth.createUserWithEmailAndPassword(binding.signupEmail.getText().toString()
                      ,binding.signupPassword.getText().toString())
                      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                              if(task.isSuccessful()){
                                  progressDialog.dismiss();
                                  Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                  Intent intent=new Intent(SignupActivity.this,UserSetUpActivity.class);
                                  intent.putExtra("signup.Email",binding.signupEmail.getText().toString());
                                  intent.putExtra("signup.Name",binding.signupName.getText().toString());
                                  startActivity(intent);
                                  finish();
                              }else{
                                  progressDialog.dismiss();
                                  Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              }
                          }
                      });
        }
    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}