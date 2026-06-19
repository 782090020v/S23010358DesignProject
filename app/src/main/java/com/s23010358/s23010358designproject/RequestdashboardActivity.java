package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RequestdashboardActivity extends AppCompatActivity {

    private EditText etRequester, etResponsiblePerson, etLocation, etAccessStart, etAccessEnd;
    private RadioGroup rgWorkType;
    private Button btnNext;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdashboard);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        etRequester = findViewById(R.id.etRequester);
        etResponsiblePerson = findViewById(R.id.etResponsiblePerson);
        etLocation = findViewById(R.id.etLocation);
        etAccessStart = findViewById(R.id.etAccessStart);
        etAccessEnd = findViewById(R.id.etAccessEnd);
        rgWorkType = findViewById(R.id.rgWorkType);
        btnNext = findViewById(R.id.btnNext);

        if (currentUser == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RequestdashboardActivity.this, LoginActivity.class));
            finish();
            return;
        }

        btnNext.setOnClickListener(v -> saveRequestData());
    }

    private void saveRequestData() {
        String requester = etRequester.getText().toString().trim();
        String responsiblePerson = etResponsiblePerson.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String accessStart = etAccessStart.getText().toString().trim();
        String accessEnd = etAccessEnd.getText().toString().trim();

        int selectedWorkTypeId = rgWorkType.getCheckedRadioButtonId();

        if (requester.isEmpty()) {
            etRequester.setError("Requester is required");
            etRequester.requestFocus();
            return;
        }

        if (responsiblePerson.isEmpty()) {
            etResponsiblePerson.setError("Responsible person is required");
            etResponsiblePerson.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            etLocation.setError("Location is required");
            etLocation.requestFocus();
            return;
        }

        if (accessStart.isEmpty()) {
            etAccessStart.setError("Access start date is required");
            etAccessStart.requestFocus();
            return;
        }

        if (accessEnd.isEmpty()) {
            etAccessEnd.setError("Access end date is required");
            etAccessEnd.requestFocus();
            return;
        }

        if (selectedWorkTypeId == -1) {
            Toast.makeText(this, "Please select work type", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedWorkTypeId);
        String workType = selectedRadioButton.getText().toString();

        btnNext.setEnabled(false);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("requester", requester);
        requestData.put("responsiblePerson", responsiblePerson);
        requestData.put("location", location);
        requestData.put("accessStart", accessStart);
        requestData.put("accessEnd", accessEnd);
        requestData.put("workType", workType);
        requestData.put("status", "Pending");
        requestData.put("createdAt", FieldValue.serverTimestamp());

        db.collection("RequestDashboard")
                .add(requestData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Request saved successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RequestdashboardActivity.this, RequestDashboardStep2Activity.class);
                    intent.putExtra("requestId", documentReference.getId());
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    btnNext.setEnabled(true);
                    Toast.makeText(this, "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}