package com.example.policemangementsystemlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.policemangementsystemlogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserRegister extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://police-fine-management-default-rtdb.asia-southeast1.firebasedatabase.app/users");
    private Button registerf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        final EditText rusername = findViewById(R.id.rusername);
        final EditText remail = findViewById(R.id.Remail);
        final EditText rnic = findViewById(R.id.rnic);
        final EditText rphone = findViewById(R.id.rTextPhone);
        final EditText password = findViewById(R.id.rTextPassword);
        final EditText cpassword = findViewById(R.id.rcTextPassword);

        registerf = findViewById(R.id.registerf);
        registerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rusernameTxt = rusername.getText().toString();
                final String remailTxt = remail.getText().toString();
                final String rnicTxt = rnic.getText().toString();
                final String rphoneTxt = rphone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String cpasswordTxt = cpassword.getText().toString();

                if (rusernameTxt.isEmpty() || remailTxt.isEmpty() || rnicTxt.isEmpty() || rphoneTxt.isEmpty() || passwordTxt.isEmpty() || cpasswordTxt.isEmpty()) {
                    Toast.makeText(UserRegister.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(cpasswordTxt)) {
                    Toast.makeText(UserRegister.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(remailTxt).matches()) {
                    Toast.makeText(UserRegister.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else if (rnicTxt.length() != 12 && (!rnicTxt.endsWith("V"))) {
                    Toast.makeText(UserRegister.this, "Invalid NIC format", Toast.LENGTH_SHORT).show();
                } else if (rphoneTxt.length() != 10) {
                    Toast.makeText(UserRegister.this, "Phone number must have 10 digits", Toast.LENGTH_SHORT).show();
                } else {
                    final String registrationDate = getCurrentDate(); // Get current registration date

                    databaseReference.child(rnicTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(UserRegister.this, "NIC is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create user node and set user details including registration date
                                DatabaseReference userRef = databaseReference.child(rnicTxt);

                                userRef.child("username").setValue(rusernameTxt);
                                userRef.child("Email").setValue(remailTxt);
                                userRef.child("phonenumber").setValue(rphoneTxt);
                                userRef.child("password").setValue(passwordTxt);
                                userRef.child("registration_date").setValue(registrationDate); // Push registration date

                                Toast.makeText(UserRegister.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                                openactivity_user_login();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(UserRegister.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void openactivity_user_login() {
        Intent intent = new Intent(this, User_Login.class);
        startActivity(intent);
    }

    // Helper method to get current date
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
