package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookstore.Database.DBHandler;

public class Home extends AppCompatActivity {

    EditText username ,password;
    Button login ,register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = findViewById(R.id.etUserName);
        password = findViewById(R.id.etPasswordH);
        login= findViewById(R.id.btnlogin);
        register = findViewById(R.id.btnregister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),ProfileManagement.class );
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DBHandler dbHandler = new DBHandler(getApplicationContext());
                if(dbHandler.loginUser(username.getText().toString(),password.getText().toString())){

                    Toast.makeText(Home.this, "Loging Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),BookStOrE.class );
                    startActivity(i);
                }
                else {
                    Toast.makeText(Home.this, "No User OR Invalid User Details", Toast.LENGTH_SHORT).show();
                    username.setText(null);
                    password.setText(null);
                }

            }
        });



    }
}