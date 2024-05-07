package com.example.policemangementsystemlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
private Button driver1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driver1 = (Button) findViewById(R.id.driver1);
        driver1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openactivity_user_login();//methan gahnna thiyenne open venna onna ekke open kiyala gahla xml ekke nama

            }
        });

    }
    public void openactivity_user_login(){
        Intent intent =new Intent(this, User_Login.class);//User_Login kiyanne login kiyann ekke class ekka
        startActivity(intent);
    }
}