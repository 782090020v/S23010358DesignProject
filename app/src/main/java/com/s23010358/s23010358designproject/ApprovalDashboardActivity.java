package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ApprovalDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_dashboard);

        Intent toPermit = new Intent(this, ApprovedWorkPermitActivity.class);

        Button btnApprove = findViewById(R.id.btnApprove);
        btnApprove.setOnClickListener(v -> startActivity(toPermit));

        Button btnViewTicket = findViewById(R.id.btnViewTicket);
        btnViewTicket.setOnClickListener(v -> startActivity(toPermit));
    }
}
