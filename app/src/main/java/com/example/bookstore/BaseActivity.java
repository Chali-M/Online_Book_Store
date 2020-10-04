package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BaseActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance().getReference();
        this.mAuth.addAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(BaseActivity.this, Home.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mAuth.removeAuthStateListener(this);
    }
}