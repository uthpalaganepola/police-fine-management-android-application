package com.example.policemangementsystemlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Login extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://police-fine-management-default-rtdb.asia-southeast1.firebasedatabase.app/users");
    private Button signin;
    private Button fogotpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        final EditText editTextNumberDecimal = findViewById(R.id.editTextNumberDecimal);
        final EditText editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);
        final Button log = findViewById(R.id.log);
        signin = findViewById(R.id.signin);
        fogotpw=findViewById(R.id.fogotpw);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nicTxt = editTextNumberDecimal.getText().toString();
                final String passwordtxt = editTextTextPassword2.getText().toString();

                if (nicTxt.isEmpty() || passwordtxt.isEmpty()) {
                    Toast.makeText(User_Login.this, "Please enter your NIC and password correctly", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child(nicTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String actualPassword = dataSnapshot.child("password").getValue(String.class);
                                if (actualPassword.equals(passwordtxt)) {
                                    // Password is correct, proceed with your logic here
                                    Toast.makeText(User_Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    // Inside onDataChange method
                                    openactivity_home(nicTxt);

                                } else {
                                    // Password is incorrect
                                    Toast.makeText(User_Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // NIC not found in database
                                Toast.makeText(User_Login.this, "NIC not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(User_Login.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity_user_register();
            }
        });

        fogotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity_forgetpw();
            }
        });
    }

    public void openactivity_user_register() {
        Intent intent = new Intent(this, UserRegister.class);
        startActivity(intent);
    }
    public void openactivity_forgetpw() {
        Intent intent = new Intent(this, Forgetpw.class);
        startActivity(intent);
    }

    public void openactivity_home(String nic) {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("NIC", nic);
        startActivity(intent);
    }
}
