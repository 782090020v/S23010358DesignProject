package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddVisitorActivity extends AppCompatActivity {

    private EditText etName, etNicNo, etCompany, etVehicleNo, etContactNo;
    private Button btnAddVisitorTable, btnUpdate;
    private LinearLayout visitorTableBody;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        etName = findViewById(R.id.etName);
        etNicNo = findViewById(R.id.etNicNo);
        etCompany = findViewById(R.id.etCompany);
        etVehicleNo = findViewById(R.id.etVehicleNo);
        etContactNo = findViewById(R.id.etContactNo);

        btnAddVisitorTable = findViewById(R.id.btnAddVisitorTable);
        btnUpdate = findViewById(R.id.btnUpdate);
        visitorTableBody = findViewById(R.id.visitorTableBody);

        requestId = getIntent().getStringExtra("requestId");

        if (currentUser == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddVisitorActivity.this, LoginActivity.class));
            finish();
            return;
        }

        loadVisitorsFromFirestore();

        btnAddVisitorTable.setOnClickListener(v -> saveVisitorData());

        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(AddVisitorActivity.this, ApprovalDashboardActivity.class);
            intent.putExtra("requestId", requestId);
            startActivity(intent);
        });
    }

    private void saveVisitorData() {
        String name = etName.getText().toString().trim();
        String nicNo = etNicNo.getText().toString().trim();
        String company = etCompany.getText().toString().trim();
        String vehicleNo = etVehicleNo.getText().toString().trim();
        String contactNo = etContactNo.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Enter visitor name");
            etName.requestFocus();
            return;
        }

        if (nicNo.isEmpty()) {
            etNicNo.setError("Enter NIC number");
            etNicNo.requestFocus();
            return;
        }

        if (company.isEmpty()) {
            etCompany.setError("Enter company");
            etCompany.requestFocus();
            return;
        }

        if (vehicleNo.isEmpty()) {
            etVehicleNo.setError("Enter vehicle number");
            etVehicleNo.requestFocus();
            return;
        }

        if (contactNo.isEmpty()) {
            etContactNo.setError("Enter contact number");
            etContactNo.requestFocus();
            return;
        }

        btnAddVisitorTable.setEnabled(false);

        Map<String, Object> visitorData = new HashMap<>();
        visitorData.put("userId", currentUser.getUid());
        visitorData.put("userEmail", currentUser.getEmail());
        visitorData.put("requestId", requestId);
        visitorData.put("name", name);
        visitorData.put("nicNo", nicNo);
        visitorData.put("company", company);
        visitorData.put("vehicleNo", vehicleNo);
        visitorData.put("contactNo", contactNo);
        visitorData.put("createdAt", FieldValue.serverTimestamp());

        db.collection("Visitors")
                .add(visitorData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Visitor added successfully", Toast.LENGTH_SHORT).show();

                    addVisitorRow(name, company, nicNo, contactNo);

                    btnAddVisitorTable.setEnabled(true);
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    btnAddVisitorTable.setEnabled(true);
                    Toast.makeText(this, "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void loadVisitorsFromFirestore() {
        db.collection("Visitors")
                .whereEqualTo("userId", currentUser.getUid())
                .whereEqualTo("requestId", requestId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    visitorTableBody.removeAllViews();

                    for (var document : queryDocumentSnapshots) {
                        String name = document.getString("name");
                        String company = document.getString("company");
                        String nicNo = document.getString("nicNo");
                        String contactNo = document.getString("contactNo");

                        addVisitorRow(name, company, nicNo, contactNo);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load visitors: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void addVisitorRow(String name, String company, String nicNo, String contactNo) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setPadding(8, 8, 8, 8);
        row.setBackgroundColor(Color.parseColor("#FAFCFF"));

        row.addView(createCell(name, 1.8f));
        row.addView(createCell(company, 1.2f));
        row.addView(createCell(nicNo, 1.2f));
        row.addView(createCell(contactNo, 1.5f));

        visitorTableBody.addView(row);
    }

    private TextView createCell(String text, float weight) {
        TextView tv = new TextView(this);
        tv.setText(text == null ? "" : text);
        tv.setTextSize(11);
        tv.setTextColor(Color.parseColor("#222222"));
        tv.setSingleLine(false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                weight
        );

        tv.setLayoutParams(params);
        return tv;
    }

    private void clearFields() {
        etName.setText("");
        etNicNo.setText("");
        etCompany.setText("");
        etVehicleNo.setText("");
        etContactNo.setText("");
        etName.requestFocus();
    }
}