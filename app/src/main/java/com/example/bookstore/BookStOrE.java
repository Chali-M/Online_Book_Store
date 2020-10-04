package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookStOrE extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener handleBottomNavigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.dashbaord:
                    startActivity(new Intent(getApplicationContext()
                            , Sell_Book.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.home:
                    return true;

                case R.id.about:
                    startActivity(new Intent(getApplicationContext()
                            , AboutUs.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        }
    };
    private View.OnClickListener handleReadButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(BookStOrE.this, Booklist.class);
            startActivity(i);
        }
    };
    private View.OnClickListener handleInventoryButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(BookStOrE.this, Inventory.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_st_or_e);
        initUI();
    }

    private void initUI() {
        Button readButton = (Button) findViewById(R.id.read_button_home);
        Button inventoryButton = findViewById(R.id.inventory_button_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(handleBottomNavigation);
        readButton.setOnClickListener(handleReadButtonClick);
        inventoryButton.setOnClickListener(handleInventoryButtonClick);
    }
}