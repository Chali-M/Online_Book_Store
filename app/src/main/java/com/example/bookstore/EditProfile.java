package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    private static final String TAG = "EditProfile";
    EditText username, birthday, address, phone, password, email;
    Button edit, delete, search;
    RadioButton userr, author;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private View.OnClickListener handleSearch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String searchQueryUsername = username.getText().toString().trim();
            CollectionReference userRef = db.collection("users");
            Query query = userRef.whereEqualTo("username", searchQueryUsername);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    HashMap<String, Object> userProfile;
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0) {
                            Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    userProfile = new HashMap<>(document.getData());
                                    String uName = "";
                                    String uBday = "";
                                    String uEmail = "";
                                    String uPword = "";
                                    String uPhone = "";
                                    String uType = "";
                                    String uAddress = "";

                                    if (userProfile.get("username") != null) {
                                        uName = Objects.requireNonNull(userProfile.get("username")).toString();
                                    }
                                    if (userProfile.get("birthday") != null) {
                                        uBday = Objects.requireNonNull(userProfile.get("birthday")).toString();
                                    }
                                    if (userProfile.get("email") != null) {
                                        uEmail = Objects.requireNonNull(userProfile.get("email")).toString();
                                    }
                                    if (userProfile.get("password") != null) {
                                        uPword = Objects.requireNonNull(userProfile.get("password")).toString();
                                    }
                                    if (userProfile.get("phone") != null) {
                                        uPhone = Objects.requireNonNull(userProfile.get("phone")).toString();
                                    }
                                    if (userProfile.get("userType") != null) {
                                        uType = Objects.requireNonNull(userProfile.get("userType")).toString();
                                    }
                                    if (userProfile.get("address") != null) {
                                        uAddress = Objects.requireNonNull(userProfile.get("address")).toString();
                                    }

                                    username.setText(uName);
                                    birthday.setText(uBday);
                                    address.setText(uAddress);
                                    phone.setText(uPhone);
                                    password.setText(uPword);
                                    email.setText(uEmail);
                                    if (uType.equals("user")) {
                                        userr.setChecked(true);
                                    } else {
                                        author.setChecked(true);
                                    }
                                } else {
                                    Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    };
    private View.OnClickListener handleEdit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String passwordText = password.getText().toString().trim();
            String emailText = email.getText().toString().trim();
            String userType = "author";
            if (userr.isChecked()) {
                userType = "user";
            }

            if (emailText.isEmpty()) {
                email.setError("Email is required");
                Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
            } else if (passwordText.isEmpty()) {
                password.setError("Password is required");
                Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
            } else {
                final Map<String, Object> user = new HashMap<>();
                user.put("username", username.getText().toString().trim());
                user.put("birthday", birthday.getText().toString().trim());
                user.put("address", address.getText().toString().trim());
                user.put("phone", phone.getText().toString().trim());
                user.put("password", passwordText);
                user.put("email", emailText);
                user.put("userType", userType);

                String firebaseUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                DocumentReference ref = db.collection("users").document(firebaseUserId);
                ref.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfile.this, "User profile update successful", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(EditProfile.this, Home.class));
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "User profile update failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };
    private View.OnClickListener handleDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (email.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "No user data available", Toast.LENGTH_SHORT).show();
            } else {
                String firebaseUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                DocumentReference ref = db.collection("users").document(firebaseUserId);
                ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfile.this, "User profile delete successful", Toast.LENGTH_SHORT).show();
                            username.setText("");
                            birthday.setText("");
                            address.setText("");
                            phone.setText("");
                            password.setText("");
                            email.setText("");
                            userr.setChecked(true);
                            author.setChecked(false);

                            startActivity(new Intent(EditProfile.this, ProfileManagement.class));
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "User profile delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initUI();
    }

    private void initUI() {
        username = findViewById(R.id.usernameep);
        birthday = findViewById(R.id.editTextTextPersonName2);
        address = findViewById(R.id.addressep);
        phone = findViewById(R.id.phoneep);
        password = findViewById(R.id.passwordep);
        email = findViewById(R.id.editTextTextPersonName8);
        edit = findViewById(R.id.buteditep);
        delete = findViewById(R.id.btndeleteep);
        search = findViewById(R.id.btnsearchep);
        userr = findViewById(R.id.radioButton3);
        author = findViewById(R.id.radioButton4);

        search.setOnClickListener(handleSearch);
        edit.setOnClickListener(handleEdit);
        delete.setOnClickListener(handleDelete);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
}