package com.example.bookstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Payment3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment3);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Payment Successful");

        Button button = findViewById(R.id.go_to_home);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Payment3.this, BookStOrE.class));
                finish();
            }
        });
    }
}
