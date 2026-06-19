package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private EditText ticketId, location, workType, startDate, endDate;
    private TextView status;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // XML Controls
        ticketId = findViewById(R.id.x);
        location = findViewById(R.id.Y02);
        workType = findViewById(R.id.Y03);
        startDate = findViewById(R.id.Y04);
        endDate = findViewById(R.id.Y05);
        status = findViewById(R.id.Y06);

        // Navigation
        TextView tabNewRequest = findViewById(R.id.L03);

        tabNewRequest.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                navigateToRequestdashboard();
            } else {
                Toast.makeText(HomeActivity.this,
                        "User not logged in",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToRequestdashboard() {
        Intent intent = new Intent(HomeActivity.this, RequestdashboardActivity.class);
        startActivity(intent);
    }
}