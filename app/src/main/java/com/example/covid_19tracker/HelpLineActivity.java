package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.covid_19tracker.databinding.ActivityHelpLineBinding;

public class HelpLineActivity extends AppCompatActivity {
    ActivityHelpLineBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line);
        binding=ActivityHelpLineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Helpline");
        binding.contact.setOnClickListener(v -> {
            Uri u = Uri.parse("tel:" +binding.contact.getText());
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            try
            {
                startActivity(i);
            }
            catch (SecurityException s)
            {
                Toast.makeText(HelpLineActivity.this, "An error occurred", Toast.LENGTH_LONG).show();
            }
        });


        binding.tollFree.setOnClickListener(v -> {
            Uri u = Uri.parse("tel:" +binding.tollFree.getText());
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            try
            {
                startActivity(i);
            }
            catch (SecurityException s)
            {
                Toast.makeText(HelpLineActivity.this, "An error occurred", Toast.LENGTH_LONG).show();
            }
        });

        binding.email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);

            // this can be use if you want to open only email app
           // Intent intent = new Intent(Intent.ACTION_SENDTO);
            //intent.setData(Uri.parse("mailto:"));
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { binding.email.getText().toString() });
            startActivity(Intent.createChooser(intent, "Choose Email app"));
        });
    }
}