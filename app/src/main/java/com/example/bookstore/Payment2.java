package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Payment2 extends BaseActivity {
    private static final String TAG = "Payment2";

    private TextView bookNameTv;
    private TextView bookAuthorTv;
    private TextView bookPriceTv;
    private TextView bookCategoryTv;

    private EditText receiverNameEt;
    private EditText receiverAddressEt;
    private EditText receiverPhoneEt;

    private EditText cardNameEt;
    private EditText cardNumberEt;
    private EditText cardExpDateEt;
    private EditText cardCvvEt;
    private EditText cardTypeEt;

    private String cardType;
    private String cardName;
    private String cardCvv;
    private String cardExp;
    private String cardNumber;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverName;
    private int errorCount;

    private View.OnClickListener handleUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DocumentReference docRef = db.collection("active_carts")
                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            HashMap<String, Object> details = new HashMap<>();
            errorCount = 0;

            validateInputs();

            if (errorCount == 0) {
                details.put("name", receiverName);
                details.put("address", receiverAddress);
                details.put("phone", receiverPhone);
                details.put("holder", cardName);
                details.put("number", cardNumber);
                details.put("cvv", cardCvv);
                details.put("expire", cardExp);
                details.put("type", cardType);

                docRef.update("userData", details).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Payment details updated successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(Payment2.this, "Enter required fields", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private View.OnClickListener handleConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            errorCount = 0;
            validateInputs();
            DocumentReference docRef = db.collection("purchases")
                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            HashMap<String, Object> userData = new HashMap<>();
            HashMap<String, Object> allData = new HashMap<>();

            if (errorCount == 0) {
                userData.put("name", receiverName);
                userData.put("address", receiverAddress);
                userData.put("phone", receiverPhone);
                userData.put("holder", cardName);
                userData.put("number", cardNumber);
                userData.put("cvv", cardCvv);
                userData.put("expire", cardExp);
                userData.put("type", cardType);

                allData.put("bookName", bookNameTv.getText().toString().trim());
                allData.put("bookAuthor", bookAuthorTv.getText().toString().trim());
                allData.put("bookPrice", bookPriceTv.getText().toString().trim());
                allData.put("bookCategory", bookCategoryTv.getText().toString().trim());
                allData.put("userData", userData);

                docRef.set(allData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        openThirdActivity();
                    }
                });

            } else {
                Toast.makeText(Payment2.this, "Enter required fields", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void validateInputs() {
        receiverName = receiverNameEt.getText().toString().trim();
        receiverAddress = receiverAddressEt.getText().toString().trim();
        receiverPhone = receiverPhoneEt.getText().toString().trim();
        cardName = cardNameEt.getText().toString().trim();
        cardCvv = cardCvvEt.getText().toString().trim();
        cardExp = cardExpDateEt.getText().toString().trim();
        cardNumber = cardNumberEt.getText().toString().trim();
        cardType = cardTypeEt.getText().toString().trim();

        setErrorMessage(receiverNameEt, "Name", receiverName);
        setErrorMessage(receiverAddressEt, "Address", receiverAddress);
        setErrorMessage(receiverPhoneEt, "Phone", receiverPhone);
        setErrorMessage(cardNameEt, "Name on card", cardName);
        setErrorMessage(cardCvvEt, "CVV", cardCvv);
        setErrorMessage(cardExpDateEt, "Expiry date", cardExp);
        setErrorMessage(cardNumberEt, "Card number", cardNumber);
        setErrorMessage(cardTypeEt, "Card type", cardType);
    }

    private void setErrorMessage(EditText et, String name, String value) {
        if (value.isEmpty()) {
            et.setError(name + " " + getString(R.string.is_required));
            errorCount += 1;
        }
    }

    private View.OnClickListener handleDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DocumentReference docRef = db.collection("active_carts")
                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

            docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Order successfully deleted", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Payment2.this, Inventory.class));
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Payment Details");
        initUI();
        fetchData();
    }

    private void initUI() {
        Button confirmBtnn = (Button) findViewById(R.id.confirmBtnn);
        Button update = findViewById(R.id.UpdateBtn);
        Button delete = findViewById(R.id.DeleteBtn);
        bookNameTv = findViewById(R.id.book_name);
        bookAuthorTv = findViewById(R.id.book_author);
        bookPriceTv = findViewById(R.id.book_price);
        bookCategoryTv = findViewById(R.id.book_category);
        receiverNameEt = findViewById(R.id.receiver_name);
        receiverPhoneEt = findViewById(R.id.receiver_phone);
        receiverAddressEt = findViewById(R.id.receiver_address);
        cardNameEt = findViewById(R.id.card_name);
        cardCvvEt = findViewById(R.id.card_cvv);
        cardExpDateEt = findViewById(R.id.card_exp_date);
        cardNumberEt = findViewById(R.id.card_number);
        cardTypeEt = findViewById(R.id.card_type);

        confirmBtnn.setOnClickListener(handleConfirm);
        update.setOnClickListener(handleUpdate);
        delete.setOnClickListener(handleDelete);
    }

    private void fetchData() {
        DocumentReference docRef = db.collection("active_carts")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        HashMap<String, Object> cartDetails = new HashMap<>(Objects.requireNonNull(document.getData()));
                        HashMap<String, Object> cardUserData = (HashMap<String, Object>) cartDetails.get("userData");
                        bookNameTv.setText(cartDetails.get("bookName").toString());
                        bookAuthorTv.setText(cartDetails.get("bookAuthor").toString());
                        bookPriceTv.setText(cartDetails.get("bookPrice").toString());
                        bookCategoryTv.setText(cartDetails.get("bookCategory").toString());

                        if (cardUserData != null) {
                            receiverNameEt.setText(cardUserData.get("name").toString());
                            receiverAddressEt.setText(cardUserData.get("address").toString());
                            receiverPhoneEt.setText(cardUserData.get("phone").toString());
                            cardNameEt.setText(cardUserData.get("holder").toString());
                            cardCvvEt.setText(cardUserData.get("cvv").toString());
                            cardExpDateEt.setText(cardUserData.get("expire").toString());
                            cardNumberEt.setText(cardUserData.get("number").toString());
                            cardTypeEt.setText(cardUserData.get("type").toString());
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    //last part of routing button
    public void openThirdActivity() {
        Intent intent = new Intent(this, Payment3.class);
        startActivity(intent);
    }
}
