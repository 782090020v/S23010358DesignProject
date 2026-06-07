package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RequestDashboardStep2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdashboard_step2);

        Button btnAddVisitor = findViewById(R.id.btnAddVisitor);
        btnAddVisitor.setOnClickListener(v -> startActivity(
                new Intent(RequestDashboardStep2Activity.this, AddVisitorActivity.class)));
    }
}
