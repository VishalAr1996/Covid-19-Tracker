package com.example.covid_19tracker.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.covid_19tracker.MainActivity;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(this);
        binding.loginBtn.setOnClickListener(v -> {
            authenticateUser();
        });
        binding.gotoSignup.setOnClickListener(v -> startActivity(new Intent(this,SignupActivity.class)));
        //binding.textInputLoginPassword.setEndIconMode(TextInputLayout.END_ICON_NONE);
    }

    private void authenticateUser() {
        if(binding.loginEmail.getText().toString().equals("")){
            showError(binding.textInputLoginEmail,"Not a valid email");
        }else if(binding.loginPassword.getText().toString().equals("")){
            showError(binding.textInputLoginPassword," Password can't be empty");
        }else{
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Checking Details...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(binding.loginEmail.getText().toString()
                    ,binding.loginPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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