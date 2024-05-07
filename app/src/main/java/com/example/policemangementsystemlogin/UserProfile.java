package com.example.policemangementsystemlogin;



import static com.example.policemangementsystemlogin.R.id.Statbtn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private EditText  upname, upemail, upphone;
    private TextView upnic;
    private Button upsearchbtn;
    private Button upupdatebtn;
    private Button Updeletebtn;

    private Button Statbtn;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize EditText fields and buttons
        upnic = findViewById(R.id.upnic);
        upname = findViewById(R.id.upname);
        upemail = findViewById(R.id.upemail);
        upphone = findViewById(R.id.upphone);
        upsearchbtn = findViewById(R.id.upsearchbtn);
        upupdatebtn = findViewById(R.id.upupdatebtn);
        Updeletebtn = findViewById(R.id.Updeletebtn);
        Statbtn = findViewById(R.id.Statbtn);

        // Get reference to your Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        // Get NIC from Intent
        String nic = getIntent().getStringExtra("NIC");

        // Set NIC to EditText field
        upnic.setText(nic);

        // Set OnClickListener for the search button
        upsearchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });

        // Set OnClickListener for the update button
        upupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    updateUser();
                }
            }
        });

        // Set OnClickListener for the delete button
        Updeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
                openMainActivity();
            }
        });

        Statbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity_stats();
            }
        });
    }

    private void openactivity_stats() {
        Intent intent = new Intent(this, Stats .class);

        startActivity(intent);
    }

    private void searchUser() {
        final String nic = upnic.getText().toString().trim();

        // Read data from Firebase Database based on NIC
        databaseReference.child(nic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists, fetch and display it
                    String name = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("Email").getValue(String.class);
                    String phone = dataSnapshot.child("phonenumber").getValue(String.class);

                    // Set fetched data to EditText fields
                    upname.setText(name);
                    upemail.setText(email);
                    upphone.setText(phone);
                } else {
                    // Handle the case when user data doesn't exist
                    upname.setText("");
                    upemail.setText("");
                    upphone.setText("");
                    Toast.makeText(UserProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(UserProfile.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {
        final String nic = upnic.getText().toString().trim();
        final String name = upname.getText().toString().trim();
        final String email = upemail.getText().toString().trim();
        final String phone = upphone.getText().toString().trim();

        // Update data in Firebase Database
        databaseReference.child(nic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Update user details
                    dataSnapshot.getRef().child("username").setValue(name);
                    dataSnapshot.getRef().child("Email").setValue(email);
                    dataSnapshot.getRef().child("phonenumber").setValue(phone);
                    Toast.makeText(UserProfile.this, "User details updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser() {
        final String nic = upnic.getText().toString().trim();

        // Delete user details from Firebase Database
        databaseReference.child(nic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Remove user details
                    dataSnapshot.getRef().removeValue();
                    Toast.makeText(UserProfile.this, "User details deleted successfully", Toast.LENGTH_SHORT).show();

                    // Clear EditText fields
                    upname.setText("");
                    upemail.setText("");
                    upphone.setText("");
                } else {
                    Toast.makeText(UserProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean validateInputs() {
        boolean valid = true;

        String email = upemail.getText().toString().trim();
        String phone = upphone.getText().toString().trim();

        if (!email.contains("@")) {
            upemail.setError("Invalid email");
            valid = false;
        }

        if (phone.length() != 10 || !phone.matches("\\d+")) {
            upphone.setError("Invalid phone number");
            valid = false;
        }

        return valid;
    }
}
