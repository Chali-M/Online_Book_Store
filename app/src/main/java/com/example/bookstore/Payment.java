package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Objects;

public class Payment extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Payment Details");

        final EditText mNameEt = findViewById(R.id.payment_name);
        final EditText mPhoneEt = findViewById(R.id.payment_phone);
        final EditText mAddressEt = findViewById(R.id.addressEt);
        final EditText mHolderEt = findViewById(R.id.holderEt);
        final EditText mNumberEt = findViewById(R.id.numberEt);
        final EditText mExpireEt = findViewById(R.id.expireEt);
        final EditText mCvvEt = findViewById(R.id.cvvEt);
        final EditText mTypeEt = findViewById(R.id.typeEt);

        Button mSaveBtn = findViewById(R.id.saveBtn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String name = mNameEt.getText().toString().trim();
                String phone = mPhoneEt.getText().toString().trim();
                String address = mAddressEt.getText().toString().trim();
                String holder = mHolderEt.getText().toString().trim();
                String number = mNumberEt.getText().toString().trim();
                String expire = mExpireEt.getText().toString().trim();
                String cvv = mCvvEt.getText().toString().trim();
                String type = mTypeEt.getText().toString().trim();
                int errorCount = 0;
                if (name.isEmpty()) {
                    mNameEt.setError("Name is required");
                    errorCount += 1;
                }
                if (phone.isEmpty()) {
                    mPhoneEt.setError("Phone is required");
                    errorCount += 1;
                }
                if (address.isEmpty()) {
                    mAddressEt.setError("Address is required");
                    errorCount += 1;
                }
                if (holder.isEmpty()) {
                    mHolderEt.setError("Name on Card is required");
                    errorCount += 1;
                }
                if (number.isEmpty()) {
                    mNumberEt.setError("Card number is required");
                    errorCount += 1;
                }
                if (expire.isEmpty()) {
                    mExpireEt.setError("Expiry date is required");
                    errorCount += 1;
                }
                if (cvv.isEmpty()) {
                    mCvvEt.setError("CVV is required");
                    errorCount += 1;
                }
                if (type.isEmpty()) {
                    mTypeEt.setError("Card type is required");
                    errorCount += 1;
                }

                if (errorCount == 0) {
                    HashMap<String, Object> cartUserDetails = new HashMap<>();
                    cartUserDetails.put("name", name);
                    cartUserDetails.put("phone", phone);
                    cartUserDetails.put("address", address);
                    cartUserDetails.put("holder", holder);
                    cartUserDetails.put("number", number);
                    cartUserDetails.put("expire", expire);
                    cartUserDetails.put("cvv", cvv);
                    cartUserDetails.put("type", type);

                    DocumentReference cartRef = db.collection("active_carts")
                            .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

                    cartRef.update("userData", cartUserDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Payment.this, "Payment details successfully added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Payment.this, Payment2.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(Payment.this, "Enter required details", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
