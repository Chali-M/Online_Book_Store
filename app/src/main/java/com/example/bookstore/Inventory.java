package com.example.bookstore;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.adapters.InventoryListAdapter;
import com.example.bookstore.models.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory extends BaseActivity {

    private ProgressBar spinner;
    private ArrayList<Book> bookArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initUI();
        fetchInventory();
    }

    private void fetchInventory() {
        CollectionReference inventoryCollection = db.collection("inventory");
        inventoryCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = new Book();
                                HashMap<String, Object> bookMap = new HashMap<>(document.getData());
                                book.setFirebaseBookId(document.getId());
                                book.setBookName(bookMap.get("bookName").toString());
                                book.setBookAuthor(bookMap.get("bookAuthor").toString());
                                book.setBookCategory(bookMap.get("bookCategory").toString());
                                book.setBookId(bookMap.get("bookId").toString());
                                book.setBookPrice(bookMap.get("bookPrice").toString());
                                book.setBookUrl(bookMap.get("imageUrl").toString());
                                bookArrayList.add(book);
                            }
                            initRecyclerView();
                        }
                    }
                });

    }

    private void initUI() {
        spinner = findViewById(R.id.list_spinner);
        bookArrayList = new ArrayList<>();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.inventory_rv);
        InventoryListAdapter adapter = new InventoryListAdapter(Inventory.this, bookArrayList, mAuth, db);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        spinner.setVisibility(View.GONE);
    }

}