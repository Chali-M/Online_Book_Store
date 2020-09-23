package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bookstore.Database.DBHandler;

public class ProfileManagement extends AppCompatActivity {

    EditText username,birthday,address,phone,password,email;
    Button add,updateProfile;
    RadioButton user , author;
    String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

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

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),EditProfile.class );
                startActivity(i);

            }
        });

          add.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  if(user.isChecked()){
                      type = "user";

                  }
                  else{
                      type = "female";
                  }

                  DBHandler dbHandler = new DBHandler(getApplicationContext());
                  long newID = dbHandler.addInfo(username.getText().toString(),birthday.getText().toString(),address.getText().toString(),phone.getText().toString(),password.getText().toString(),email.getText().toString(),type);
                  Toast.makeText(ProfileManagement.this, "User Added. User ID: "+ newID , Toast.LENGTH_SHORT).show();

                  Intent i = new Intent(getApplicationContext(),EditProfile.class );
                  startActivity(i);

                  username.setText(null);
                  birthday.setText(null);
                  address.setText(null);
                  phone.setText(null);
                  password.setText(null);
                  email.setText(null);
                  user.setChecked(true);
                  author.setChecked(false);
              }
          });

    }
}