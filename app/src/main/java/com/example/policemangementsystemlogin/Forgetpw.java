package com.example.policemangementsystemlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;


public class Forgetpw extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://police-fine-management-default-rtdb.asia-southeast1.firebasedatabase.app/users");
    Button fresetpw;
    EditText fpass, cpass, fnic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpw);

        fresetpw = findViewById(R.id.fresetpw);
        fpass = findViewById(R.id.fpass);
        cpass = findViewById(R.id.cpass);
        fnic = findViewById(R.id.fnic);

        fresetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nicNumber = fnic.getText().toString().trim();
                final String newPassword = fpass.getText().toString().trim();
                String confirmPassword = cpass.getText().toString().trim();

                if (!TextUtils.isEmpty(nicNumber) && !TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(confirmPassword)) {
                    if (newPassword.equals(confirmPassword)) {
                        // Update password in Firebase Realtime Database
                        updatePasswordInDatabase(nicNumber, newPassword);
                    } else {
                        Toast.makeText(Forgetpw.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Forgetpw.this, "NIC number and passwords can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePasswordInDatabase(final String nicNumber, final String newPassword) {
        // Check if the user exists in the database
        databaseReference.child(nicNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Update the password
                    dataSnapshot.getRef().child("password").setValue(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Forgetpw.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        openactivity_user_login();
                                    } else {
                                        Toast.makeText(Forgetpw.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Forgetpw.this, "No user found with the provided NIC number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Forgetpw.this, "Error retrieving user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openactivity_user_login() {
        Intent intent = new Intent(this, User_Login.class);
        startActivity(intent);
    }
}
