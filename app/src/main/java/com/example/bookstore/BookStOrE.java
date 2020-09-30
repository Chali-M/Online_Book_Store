package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookStOrE extends AppCompatActivity {
      Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_st_or_e);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.dashbaord:
                        startActivity(new Intent(getApplicationContext()
                                ,Sell_Book.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext()
                                ,AboutUs.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        button = findViewById(R.id.btn222);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Booklist.class);
                startActivity(i);
            }
        });
    }
}