package com.example.bookstore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class Admin_insert extends BaseActivity {

    private static final String TAG = "Admin_insert";
    private static final int REQUEST_CODE = 1000;
    private static final int GET_FROM_GALLERY = 1001;

    private EditText bookCategoryET;
    private EditText bookIdET;
    private EditText bookPriceET;
    private EditText bookAuthorET;
    private EditText bookNameET;
    private ImageButton uploadBookIB;
    private Uri bookImageUri = null;

    private View.OnClickListener handleBookInsert = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String bookId = bookIdET.getText().toString().trim();
            String bookPrice = bookPriceET.getText().toString().trim();
            String bookAuthor = bookAuthorET.getText().toString().trim();
            String bookCategory = bookCategoryET.getText().toString().trim();
            String bookName = bookNameET.getText().toString().trim();
            int errorCount = 0;
            if (bookAuthor.isEmpty()) {
                bookAuthorET.setError("Author is required");
                errorCount += 1;
            }
            if (bookName.isEmpty()) {
                bookNameET.setError("Name is required");
                errorCount += 1;
            }
            if (bookCategory.isEmpty()) {
                bookCategoryET.setError("Book category is required");
                errorCount += 1;
            }
            if (bookPrice.isEmpty()) {
                bookPriceET.setError("Book price is required");
                errorCount += 1;
            }
            if (bookId.isEmpty()) {
                bookIdET.setError("Book ID is required");
                errorCount += 1;
            }

            if (errorCount == 0) {
                final CollectionReference inventoryCollection = db.collection("inventory");
                HashMap<String, String> book = new HashMap<>();
                book.put("bookId", bookId);
                book.put("bookPrice", bookPrice);
                book.put("bookAuthor", bookAuthor);
                book.put("bookCategory", bookCategory);
                book.put("bookName", bookName);

                inventoryCollection.add(book).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        final String inventoryId = documentReference.getId();

                        if (bookImageUri != null) {
                            final StorageReference imageRef = storage.child("inventory/" + inventoryId);
                            UploadTask uploadTask = imageRef.putFile(bookImageUri);
                            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw Objects.requireNonNull(task.getException());
                                    }

                                    return imageRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        inventoryCollection.document(inventoryId).update("imageUrl", String.valueOf(downloadUri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                bookIdET.setText("");
                                                bookPriceET.setText("");
                                                bookAuthorET.setText("");
                                                bookCategoryET.setText("");
                                                bookNameET.setText("");
                                                Toast.makeText(getApplicationContext(), "New book successfully added to the inventory", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        // Handle failures
                                        Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
            }
        }
    };
    private View.OnClickListener handleImageUpload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            requestPermission();
        }
    };
    private View.OnClickListener handleBookEdit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(Admin_insert.this, Admin_edit.class));
        }
    };


    private void uploadImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(
                Admin_insert.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            uploadImage();

        } else {
            ActivityCompat.requestPermissions(Admin_insert.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_insert);
        initUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadImage();
            } else {
                Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                bookImageUri = data.getData();

                Glide.with(Admin_insert.this)
                        .load(bookImageUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(uploadBookIB);
            }
        }

    }

    private void initUI() {
        bookAuthorET = findViewById(R.id.book_author_admin);
        bookCategoryET = findViewById(R.id.book_category_admin);
        bookPriceET = findViewById(R.id.book_price_admin);
        bookIdET = findViewById(R.id.book_id_admin);
        bookNameET = findViewById(R.id.book_name_admin);
        uploadBookIB = findViewById(R.id.book_upload_admin);
        Button insertButton = findViewById(R.id.book_insert_admin);
        Button editButton = findViewById(R.id.book_edit_admin);

        insertButton.setOnClickListener(handleBookInsert);
        editButton.setOnClickListener(handleBookEdit);
        uploadBookIB.setOnClickListener(handleImageUpload);
    }
}