package com.example.policemangementsystemlogin;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Stats extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Fetch data from Firebase
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Loop through each user data
                Map<String, Integer> userCountMap = new TreeMap<>(); // Using TreeMap to automatically sort by keys (dates)
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String registrationDate = userSnapshot.child("registration_date").getValue(String.class);
                    if (registrationDate != null) {
                        try {
                            // Convert registration date string to Date object
                            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(registrationDate);
                            String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
                            // Count users registered on each date
                            if (userCountMap.containsKey(formattedDate)) {
                                userCountMap.put(formattedDate, userCountMap.get(formattedDate) + 1);
                            } else {
                                userCountMap.put(formattedDate, 1);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Convert user count map to AnyChart data entries
                for (Map.Entry<String, Integer> entry : userCountMap.entrySet()) {
                    data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                }

                Column column = cartesian.column(data);

                column.color("#093660"); // Change bar graph color

                column.tooltip()
                        .titleFormat("{%X}")
                        .position(Position.CENTER_BOTTOM)
                        .anchor(String.valueOf(Anchor.CENTER_BOTTOM))
                        .offsetX(0d)
                        .offsetY(5d)
                        .format("{%Value}");

                cartesian.animation(true);
                cartesian.title("User Registration Count by Date");

                cartesian.yScale().ticks().interval(2); // Y-axis increments by 2

                cartesian.yAxis(0).title("User Count");

                cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                cartesian.interactivity().hoverMode(HoverMode.BY_X);

                cartesian.xAxis(0).title("Registration Date");

                anyChartView.setChart(cartesian);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
