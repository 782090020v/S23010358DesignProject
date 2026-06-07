package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tabNewRequest = findViewById(R.id.L03);
        tabNewRequest.setOnClickListener(v -> startActivity(
                new Intent(HomeActivity.this, RequestdashboardActivity.class)));
    }
}
