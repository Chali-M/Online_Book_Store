package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bookstore.Database.DBHandler;

import java.util.List;

public class EditProfile extends AppCompatActivity {

    EditText username,birthday,address,phone,password,email;
    Button edit,delete,search;
    RadioButton userr , author;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        username = findViewById(R.id.usernameep);
        birthday = findViewById(R.id.editTextTextPersonName2);
        address = findViewById(R.id.addressep);
        phone = findViewById(R.id.phoneep);
        password = findViewById(R.id.passwordep);
        email = findViewById(R.id.editTextTextPersonName8);
        edit= findViewById(R.id.buteditep);
        delete = findViewById(R.id.btndeleteep);
        search = findViewById(R.id.btnsearchep);
        userr = findViewById(R.id.radioButton3);
        author = findViewById(R.id.radioButton4);

       search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DBHandler dbHandler = new DBHandler(getApplicationContext());
              List user = dbHandler.readAllInfo(username.getText().toString());
              
              if(user.isEmpty()){
                  Toast.makeText(EditProfile.this, "NO User", Toast.LENGTH_SHORT).show();
                  username.setText(null);
              }
                 else{

                  Toast.makeText(EditProfile.this, "User Found!  ", Toast.LENGTH_SHORT).show();

                     username.setText(user.get(0).toString());
                     birthday.setText(user.get(1).toString());
                  address.setText(user.get(2).toString());
                  phone.setText(user.get(3).toString());
                  password.setText(user.get(4).toString());
                  email.setText(user.get(5).toString());
                  if(user.get(6).toString().equals("user")) {
                      userr.setChecked(true);
                  }
                  else{
                      author.setChecked(true);
                  }
              }
           }
       });



       edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(userr.isChecked()){
                   type = "user";

               }
               else{
                   type = "female";
               }

               DBHandler dbHandler = new DBHandler(getApplicationContext());

               Boolean steus = dbHandler.updateInfo(username.getText().toString(),birthday.getText().toString(),address.getText().toString(),phone.getText().toString(),password.getText().toString(),email.getText().toString(),type);
               if(steus){
                   Toast.makeText(EditProfile.this, "user updated", Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(EditProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
               }



           }
       });

       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DBHandler dbHandler = new DBHandler(getApplicationContext());
               dbHandler.deleteInfo(username.getText().toString());

               Toast.makeText(EditProfile.this, "User Deleted", Toast.LENGTH_SHORT).show();

               username.setText(null);
               birthday.setText(null);
               address.setText(null);
               phone.setText(null);
               password.setText(null);
               email.setText(null);
               userr.setChecked(false);
               author.setChecked(false);


           }
       });


    }
}