package com.s23010358.s23010358designproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AddVisitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> startActivity(
                new Intent(AddVisitorActivity.this, OverallRequestFormActivity.class)));
    }
}
