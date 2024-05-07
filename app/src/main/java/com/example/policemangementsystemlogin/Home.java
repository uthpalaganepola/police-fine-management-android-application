package com.example.policemangementsystemlogin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView; // Import TextView class

public class Home extends AppCompatActivity {

    private Button userpro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userpro = findViewById(R.id.userpro);
        TextView nicTextView = findViewById(R.id.view);

        // Retrieve NIC number from Intent
        final String nic = getIntent().getStringExtra("NIC");

        // Set NIC number to TextView
        nicTextView.setText(nic);

        userpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity_user_profile(nic);
            }
        });
    }

    public void openactivity_user_profile(String nic) {
        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("NIC", nic);
        startActivity(intent);
    }
}

