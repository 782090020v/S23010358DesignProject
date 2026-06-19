package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RequestDashboardStep2Activity extends AppCompatActivity {

    private EditText etDivision;
    private EditText etResponsiblePerson;
    private EditText etWorkDescription;
    private Button btnAddVisitor;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdashboard_step2);

        // Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Views
        etDivision = findViewById(R.id.etDivision);
        etResponsiblePerson = findViewById(R.id.etResponsiblePerson);
        etWorkDescription = findViewById(R.id.etWorkDescription);
        btnAddVisitor = findViewById(R.id.btnAddVisitor);

        btnAddVisitor.setOnClickListener(v -> saveData());
    }

    private void saveData() {

        String division = etDivision.getText().toString().trim();
        String responsiblePerson = etResponsiblePerson.getText().toString().trim();
        String workDescription = etWorkDescription.getText().toString().trim();

        if (division.isEmpty()) {
            etDivision.setError("Enter division");
            etDivision.requestFocus();
            return;
        }

        if (responsiblePerson.isEmpty()) {
            etResponsiblePerson.setError("Enter responsible person");
            etResponsiblePerson.requestFocus();
            return;
        }

        if (workDescription.isEmpty()) {
            etWorkDescription.setError("Enter work description");
            etWorkDescription.requestFocus();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("userId", currentUser.getUid());
        requestData.put("userEmail", currentUser.getEmail());
        requestData.put("division", division);
        requestData.put("responsiblePerson", responsiblePerson);
        requestData.put("workDescription", workDescription);

        db.collection("AccessRequestsStep2")
                .add(requestData)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(
                            RequestDashboardStep2Activity.this,
                            "Data Saved Successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            RequestDashboardStep2Activity.this,
                            AddVisitorActivity.class
                    );

                    intent.putExtra("requestId",
                            documentReference.getId());

                    startActivity(intent);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                RequestDashboardStep2Activity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show());

        Button btnAddVisitor = findViewById(R.id.btnAddVisitor);
        btnAddVisitor.setOnClickListener(v -> startActivity(
                new Intent(RequestDashboardStep2Activity.this, AddVisitorActivity.class)));
    }
}