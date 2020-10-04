package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SellBooks1 extends BaseActivity {

    private static final String TAG = "SellBooks1";
    private EditText authorNameEt;
    private EditText authorAddressEt;
    private EditText authorEmailEt;
    private EditText authorNumberEt;
    private EditText bookNameEt;
    private EditText publisherEt;
    private EditText bookTypeEt;
    private EditText unitPriceEt;
    private EditText languageEt;
    private ProgressBar progressBar;

    private View.OnClickListener handleConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DocumentReference documentReference = db.collection("selling_books")
                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            Map<String, Object> bookDetails = new HashMap<>();

            String author = authorNameEt.getText().toString().trim();
            String address = authorAddressEt.getText().toString().trim();
            String phone = authorNumberEt.getText().toString().trim();
            String email = authorEmailEt.getText().toString().trim();
            String book = bookNameEt.getText().toString().trim();
            String publisher = publisherEt.getText().toString().trim();
            String type = bookTypeEt.getText().toString().trim();
            String price = unitPriceEt.getText().toString().trim();
            String language = languageEt.getText().toString().trim();

            bookDetails.put("author", author);
            bookDetails.put("address", address);
            bookDetails.put("phone", phone);
            bookDetails.put("email", email);
            bookDetails.put("book", book);
            bookDetails.put("publisher", publisher);
            bookDetails.put("type", type);
            bookDetails.put("price", price);
            bookDetails.put("language", language);

            documentReference.set(bookDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    openThirdActivity();
                }
            });
        }
    };
    private View.OnClickListener handleUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DocumentReference documentReference = db.collection("selling_in_progress")
                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

            Map<String, Object> bookDetails = new HashMap<>();

            String author = authorNameEt.getText().toString().trim();
            String address = authorAddressEt.getText().toString().trim();
            String phone = authorNumberEt.getText().toString().trim();
            String email = authorEmailEt.getText().toString().trim();
            String book = bookNameEt.getText().toString().trim();
            String publisher = publisherEt.getText().toString().trim();
            String type = bookTypeEt.getText().toString().trim();
            String price = unitPriceEt.getText().toString().trim();
            String language = languageEt.getText().toString().trim();

            bookDetails.put("author", author);
            bookDetails.put("address", address);
            bookDetails.put("phone", phone);
            bookDetails.put("email", email);
            bookDetails.put("book", book);
            bookDetails.put("publisher", publisher);
            bookDetails.put("type", type);
            bookDetails.put("price", price);
            bookDetails.put("language", language);

            documentReference.update(bookDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Book details successfully updated", Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    private View.OnClickListener handleDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DocumentReference documentReference = db.collection("selling_books")
                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Book details successfully deleted", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SellBooks1.this, Sell_Book.class));
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_books1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detail Confirmation");
        initUI();
        fetchData();
    }

    private void fetchData() {
        DocumentReference docRef = db.collection("selling_in_progress")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        HashMap<String, Object> cartDetails = new HashMap<>(Objects.requireNonNull(document.getData()));
                        authorNameEt.setText(cartDetails.get("author").toString());
                        authorAddressEt.setText(cartDetails.get("address").toString());
                        authorNumberEt.setText(cartDetails.get("phone").toString());
                        authorEmailEt.setText(cartDetails.get("email").toString());
                        bookNameEt.setText(cartDetails.get("book").toString());
                        publisherEt.setText(cartDetails.get("publisher").toString());
                        bookTypeEt.setText(cartDetails.get("type").toString());
                        unitPriceEt.setText(cartDetails.get("price").toString());
                        languageEt.setText(cartDetails.get("language").toString());

                        progressBar.setVisibility(View.GONE);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void initUI() {
        Button confirmBtn = findViewById(R.id.confirmBtn);
        Button updateButton = findViewById(R.id.updateBtn);
        Button deleteButton = findViewById(R.id.DeleteBtn);
        authorAddressEt = findViewById(R.id.selling_address);
        authorNameEt = findViewById(R.id.selling_author);
        authorEmailEt = findViewById(R.id.selling_email);
        authorNumberEt = findViewById(R.id.selling_contact_number);
        bookNameEt = findViewById(R.id.selling_book_name);
        publisherEt = findViewById(R.id.selling_publisher);
        bookTypeEt = findViewById(R.id.selling_book_type);
        languageEt = findViewById(R.id.selling_book_language);
        unitPriceEt = findViewById(R.id.selling_book_price);
        progressBar = findViewById(R.id.spinner);

        confirmBtn.setOnClickListener(handleConfirm);
        updateButton.setOnClickListener(handleUpdate);
        deleteButton.setOnClickListener(handleDelete);
    }

    public void openThirdActivity() {
        Intent intent = new Intent(this, sellBook2.class);
        startActivity(intent);
    }
}