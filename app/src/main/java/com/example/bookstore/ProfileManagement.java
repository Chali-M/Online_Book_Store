package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileManagement extends AppCompatActivity {

    private static final String TAG = "ProfileManagement";
    EditText username, birthday, address, phone, password, email;
    Button add, updateProfile;
    RadioButton user, author;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private View.OnClickListener handleUpdateProfile = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mAuth.getCurrentUser() != null) {
                Intent i = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Please create an account first", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener handleAddUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String userType = "author";
            if (user.isChecked()) {
                userType = "user";
            }

            String passwordText = password.getText().toString().trim();
            String emailText = email.getText().toString().trim();

            if (emailText.isEmpty()) {
                email.setError("Email is required");
            } else if (passwordText.isEmpty()) {
                password.setError("Password is required");
            } else if (passwordText.length() < 6) {
                password.setError("Password must contain at least 6 characters");
            } else {
                final Map<String, Object> user = new HashMap<>();
                user.put("username", username.getText().toString().trim());
                user.put("birthday", birthday.getText().toString().trim());
                user.put("address", address.getText().toString().trim());
                user.put("phone", phone.getText().toString().trim());
                user.put("password", passwordText);
                user.put("email", emailText);
                user.put("userType", userType);

                mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String firebaseUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            DocumentReference userRef = db.collection("users").document(firebaseUserId);
                            userRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "User profile successfully created", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(ProfileManagement.this, EditProfile.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                        }
                    }
                });
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        initUI();
    }

    private void initUI() {
        username = findViewById(R.id.namepm);
        birthday = findViewById(R.id.datepm);
        address = findViewById(R.id.addresspm);
        phone = findViewById(R.id.Phonepm);
        password = findViewById(R.id.passwordpm);
        email = findViewById(R.id.emailpm);
        add = findViewById(R.id.addpm);
        updateProfile = findViewById(R.id.updatepm);
        user = findViewById(R.id.radioButton);
        author = findViewById(R.id.radioButton2);

        updateProfile.setOnClickListener(handleUpdateProfile);
        add.setOnClickListener(handleAddUserClick);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
}