package com.example.bookstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bookstore.Payment;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InventoryListAdapter extends RecyclerView.Adapter<InventoryListAdapter.ViewHolder> {
    private static final int INVENTORY_LIST_POSITION = 1004;
    private ArrayList<Book> bookArrayList;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public InventoryListAdapter(Context context, ArrayList<Book> bookArrayList, FirebaseAuth mAuth, FirebaseFirestore db) {
        this.bookArrayList = new ArrayList<>(bookArrayList);
        this.context = context;
        this.mAuth = mAuth;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTv.setText(this.bookArrayList.get(position).getBookName());
        holder.authorTv.setText(this.bookArrayList.get(position).getBookAuthor());
        holder.priceTv.setText(this.bookArrayList.get(position).getBookPrice());
        holder.categoryTv.setText(this.bookArrayList.get(position).getBookCategory());
        holder.buyButton.setTag(position);

        Glide.with(this.context)
                .load(this.bookArrayList.get(position).getBookUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return this.bookArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button buyButton;
        private TextView nameTv;
        private TextView authorTv;
        private TextView priceTv;
        private TextView categoryTv;
        private ImageView bookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.list_item_name);
            authorTv = itemView.findViewById(R.id.list_item_author);
            priceTv = itemView.findViewById(R.id.list_item_price);
            categoryTv = itemView.findViewById(R.id.list_item_category);
            bookImage = itemView.findViewById(R.id.list_item_image);
            buyButton = itemView.findViewById(R.id.buy_book_button);

            View.OnClickListener handleBuyBook = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = (int) view.getTag();
                    DocumentReference cartRef = db.collection("active_carts").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                    HashMap<String, String> book = new HashMap<>();
                    book.put("bookId", bookArrayList.get(position).getBookId());
                    book.put("bookPrice", bookArrayList.get(position).getBookPrice());
                    book.put("bookAuthor", bookArrayList.get(position).getBookAuthor());
                    book.put("bookCategory", bookArrayList.get(position).getBookCategory());
                    book.put("bookName", bookArrayList.get(position).getBookName());
                    book.put("imageUrl", bookArrayList.get(position).getBookUrl());
                    cartRef.set(book).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Book " + bookArrayList.get(position).getBookName() + " added to cart", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(((AppCompatActivity)context), Payment.class);
                            ((AppCompatActivity)context).startActivity(intent);
                        }
                    });
                }
            };
            buyButton.setOnClickListener(handleBuyBook);
        }

    }
}
