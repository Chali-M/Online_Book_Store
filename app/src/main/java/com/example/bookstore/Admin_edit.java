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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class Admin_edit extends BaseActivity {

    private static final String TAG = "Admin_edit";
    private static final int REQUEST_CODE = 1002;
    private static final int GET_FROM_GALLERY = 1003;
    private EditText bookCategoryET;
    private EditText bookIdET;
    private EditText bookPriceET;
    private EditText bookAuthorET;
    private EditText bookNameET;
    private EditText bookSearchET;
    private ImageButton uploadBookIB;
    private String bookFirebaseId;
    private Uri bookImageUri = null;

    private View.OnClickListener handleDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            db.collection("inventory").document(bookFirebaseId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            clearFields();
                            Toast.makeText(getApplicationContext(), "Book successfully deleted", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Book deletion failed", Toast.LENGTH_LONG).show();

                        }
                    });

        }
    };
    private View.OnClickListener handleUpdate = new View.OnClickListener() {
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

            if (errorCount == 0 && !bookFirebaseId.isEmpty()) {
                final DocumentReference inventoryDoc = db.collection("inventory").document(bookFirebaseId);
                HashMap<String, Object> book = new HashMap<>();
                book.put("bookId", bookId);
                book.put("bookPrice", bookPrice);
                book.put("bookAuthor", bookAuthor);
                book.put("bookCategory", bookCategory);
                book.put("bookName", bookName);

                inventoryDoc.update(book).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (bookImageUri != null) {
                            final StorageReference imageRef = storage.child("inventory/" + bookFirebaseId);
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
                                        inventoryDoc.update("imageUrl", String.valueOf(downloadUri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                clearFields();
                                                Toast.makeText(getApplicationContext(), "Book details successfully updated", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        // Handle failures
                                        Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            clearFields();
                            Toast.makeText(getApplicationContext(), "Book details successfully updated", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        }
    };
    private View.OnClickListener handleSearch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clearFields();
            String searchQuery = bookSearchET.getText().toString().trim();
            CollectionReference userRef = db.collection("inventory");
            Query query = userRef.whereEqualTo("bookName", searchQuery);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    HashMap<String, Object> book;
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0) {
                            Toast.makeText(Admin_edit.this, "Book not found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    book = new HashMap<>(document.getData());
                                    bookFirebaseId = document.getId();
                                    bookNameET.setText(Objects.requireNonNull(book.get("bookName")).toString());
                                    bookAuthorET.setText(Objects.requireNonNull(book.get("bookAuthor")).toString());
                                    bookCategoryET.setText(Objects.requireNonNull(book.get("bookCategory")).toString());
                                    bookIdET.setText(Objects.requireNonNull(book.get("bookId")).toString());
                                    bookPriceET.setText(Objects.requireNonNull(book.get("bookPrice")).toString());
                                    if (book.get("imageUrl") != null) {
                                        Log.d(TAG, "onComplete: " + Objects.requireNonNull(book.get("imageUrl")).toString());
                                        Glide.with(Admin_edit.this)
                                                .load(Objects.requireNonNull(book.get("imageUrl")).toString())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .fitCenter()
                                                .into(uploadBookIB);
                                    }

                                } else {
                                    Toast.makeText(Admin_edit.this, "Book not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(Admin_edit.this, "Book not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    private View.OnClickListener handleImageUpload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            requestPermission();
        }
    };

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(
                Admin_edit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            uploadImage();

        } else {
            ActivityCompat.requestPermissions(Admin_edit.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }

    private void uploadImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    private void clearFields() {
        bookNameET.setText("");
        bookAuthorET.setText("");
        bookCategoryET.setText("");
        bookIdET.setText("");
        bookPriceET.setText("");
        uploadBookIB.setImageResource(R.drawable.ic_image_72);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);
        initUI();
    }

    private void initUI() {
        bookCategoryET = findViewById(R.id.book_category_admin_edit);
        bookIdET = findViewById(R.id.book_id_admin_edit);
        bookPriceET = findViewById(R.id.book_price_admin_edit);
        bookNameET = findViewById(R.id.book_name_admin_edit);
        bookSearchET = findViewById(R.id.book_search_admin_edit);
        bookAuthorET = findViewById(R.id.book_author_admin_edit);
        uploadBookIB = findViewById(R.id.book_upload_admin_edit);

        Button deleteButton = findViewById(R.id.book_delete_admin_edit);
        Button updateButton = findViewById(R.id.book_update_admin_edit);
        Button searchButton = findViewById(R.id.admin_search_button_edit);

        deleteButton.setOnClickListener(handleDelete);
        updateButton.setOnClickListener(handleUpdate);
        searchButton.setOnClickListener(handleSearch);
        uploadBookIB.setOnClickListener(handleImageUpload);

        bookFirebaseId = "";
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

                Glide.with(Admin_edit.this)
                        .load(bookImageUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(uploadBookIB);
            }
        }
    }
}