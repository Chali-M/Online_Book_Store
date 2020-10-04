package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sell_Book extends BaseActivity {

    //button routing create variable first
    private Button nextButn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell__book);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.dashbaord);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {


                    case R.id.about:
                        startActivity(new Intent(getApplicationContext()
                                , AboutUs.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , BookStOrE.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.dashbaord:

                }
                return false;
            }
        });


//button routing second step

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sell your book");

        final EditText oAuthorEt = findViewById(R.id.authorEt);
        final EditText oAddressEt = findViewById(R.id.addressEt);
        final EditText oPhoneEt = findViewById(R.id.phoneEt);
        final EditText oEmailEt = findViewById(R.id.emailEt);
        final EditText oBookEt = findViewById(R.id.bookEt);
        final EditText oPublisherEt = findViewById(R.id.publisherEt);
        final EditText oTypeEt = findViewById(R.id.typeEt);
        final EditText oPriceEt = findViewById(R.id.priceEt);
        final EditText oLanguageEt = findViewById(R.id.languageEt);

        Button mSaveBtn = findViewById(R.id.saveBtn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String author = oAuthorEt.getText().toString();
                String address = oAddressEt.getText().toString();
                String phone = oPhoneEt.getText().toString();
                String email = oEmailEt.getText().toString();
                String book = oBookEt.getText().toString();
                String publisher = oPublisherEt.getText().toString();
                String type = oTypeEt.getText().toString();
                String price = oPriceEt.getText().toString();
                String language = oLanguageEt.getText().toString();

                Map<String, Object> bookDetails = new HashMap<>();
                bookDetails.put("author", author);
                bookDetails.put("address", address);
                bookDetails.put("phone", phone);
                bookDetails.put("email", email);
                bookDetails.put("book", book);
                bookDetails.put("publisher", publisher);
                bookDetails.put("type", type);
                bookDetails.put("price", price);
                bookDetails.put("language", language);

                DocumentReference docRef = db.collection("selling_in_progress")
                        .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                docRef.set(bookDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(Sell_Book.this, SellBooks1.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }
}

