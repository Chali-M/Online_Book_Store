package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    EditText email, password;
    Button login, register;
    private FirebaseAuth mAuth;

    private View.OnClickListener handleRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), ProfileManagement.class);
            startActivity(i);
        }
    };

    private View.OnClickListener handleLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (isInputValid(emailText, true) && isInputValid(passwordText, false)) {
                mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login is successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Home.this, BookStOrE.class);
                            if (emailText.equals("admin@bookstore.com")) {
                                intent = new Intent(Home.this, Admin_insert.class);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    private boolean isInputValid(String text, Boolean isEmail) {
        if (!text.isEmpty()) {
            return true;
        }
        if (isEmail) {
            email.setError("Email " + getString(R.string.is_required));
        } else {
            password.setError("Password " + getString(R.string.is_required));
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
    }

    private void initUI() {
        email = findViewById(R.id.etUserName);
        password = findViewById(R.id.etPasswordH);
        login = findViewById(R.id.btnlogin);
        register = findViewById(R.id.btnregister);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(handleRegisterClick);
        login.setOnClickListener(handleLoginClick);
    }
}