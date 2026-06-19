package com.s23010358.s23010358designproject;
import android.annotation.SuppressLint;
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


public class SignupActivity extends AppCompatActivity {

    EditText name, nic, organization,email,username, password, repeatPassword;
    Button btnSignUpOriginal;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.etName);
        nic = findViewById(R.id.etNicNo);
        organization = findViewById(R.id.etOrganization);
        email = findViewById(R.id.etEmail);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        repeatPassword = findViewById(R.id.etRepeatPassword);
        btnSignUpOriginal = findViewById(R.id.btnSignUpOriginal);
        mAuth = FirebaseAuth.getInstance();

        btnSignUpOriginal.setOnClickListener(v -> handleSignUp());
    }


    private boolean validateInputs() {
        String nameVal = name.getText().toString().trim();
        String nicVal = nic.getText().toString().trim();
        String orgVal = organization.getText().toString().trim();
        String emailVal = email.getText().toString().trim();
        String usernameVal = username.getText().toString().trim();
        String passwordVal = password.getText().toString().trim();
        String repeatPasswordVal = repeatPassword.getText().toString().trim();

        if (nameVal.isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            return false;
        }
        if (nicVal.isEmpty()) {
            nic.setError("NIC is required");
            nic.requestFocus();
            return false;
        }
        if (orgVal.isEmpty()) {
            organization.setError("Organization is required");
            organization.requestFocus();
            return false;
        }
        if (emailVal.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()) {
            email.setError("Enter a valid email address");
            email.requestFocus();
            return false;
        }
        if (usernameVal.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return false;
        }
        if (passwordVal.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return false;
        }
        if (passwordVal.length() < 6) {
            password.setError("Password must be at least 6 characters");
            password.requestFocus();
            return false;
        }
        if (!passwordVal.equals(repeatPasswordVal)) {
            repeatPassword.setError("Passwords do not match");
            repeatPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void handleSignUp() {
        if (!validateInputs()) return;

        String emailVal = email.getText().toString().trim();
        String passwordVal = password.getText().toString().trim();
        String nameVal = name.getText().toString().trim();
        String nicVal = nic.getText().toString().trim();
        String orgVal = organization.getText().toString().trim();
        String usernameVal = username.getText().toString().trim();

        btnSignUpOriginal.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(emailVal, passwordVal)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            createUserProfile(firebaseUser.getUid(), nameVal, nicVal, orgVal, emailVal, usernameVal);
                        }
                    } else {
                        btnSignUpOriginal.setEnabled(true);
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : "Unknown error";
                        Toast.makeText(SignupActivity.this,
                                "Sign up failed: " + errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserProfile(String uid, String name, String nic, String organization, String email, String username) {
        Map<String, String> user = new HashMap<>();
        user.put("name", name);
        user.put("nic", nic);
        user.put("organization", organization);
        user.put("email", email);
        user.put("username", username);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(SignupActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnSignUpOriginal.setEnabled(true);
                    Toast.makeText(SignupActivity.this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
