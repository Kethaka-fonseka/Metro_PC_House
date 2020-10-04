package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfile extends AppCompatActivity {

    private Button paymentDetails, paymentHistory, shippingDetails, viewMyProducts, editProfile, logoutBtn, myOrders;
    private TextView  customerEmail,customerPhone,customerId, customerName;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        DatabaseReference dbref;
        FirebaseAuth Firebaseauth;

        editProfile =findViewById(R.id.editProfile);
        paymentDetails =findViewById(R.id.paymentDetails);

        shippingDetails =findViewById(R.id.shippingDetails);
        viewMyProducts =findViewById(R.id.viewMyProducts);
        myOrders = findViewById(R.id.myOrdersBtn);
        customerEmail = findViewById(R.id.customerEmail);
        customerPhone = findViewById(R.id.customerPhone);
        customerId = findViewById(R.id.customerId);
        customerName = findViewById(R.id.customerName);
        logoutBtn = findViewById(R.id.logOutbtn);



        Firebaseauth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference().child("User").child(Firebaseauth.getCurrentUser().getUid());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   User user = dataSnapshot.getValue(User.class);
                    customerPhone.setText(String.valueOf(user.getPhone()));
                    customerId.setText(user.getId());
                    customerEmail.setText(user.getEmail());
                    customerName.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        paymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPayments();
            }
        });

        shippingDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressDetails();
            }
        });

        viewMyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewMyProductDetails();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserDetails();
            }
        });

        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowOrderDetails.class );
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),loging.class));
                finish();

            }
        });

    }

    private void openViewMyProductDetails() {
        Intent intent = new Intent(this, ShowProductDetals.class );
        startActivity(intent);
    }

    private void openUserDetails() {
        Intent intent = new Intent(getApplicationContext(), EditCustomerDetails.class );
        startActivity(intent);
    }


    private void openAddressDetails() {
        Intent intent = new Intent(this, ShowAddressDetails.class );
        startActivity(intent);
    }

    private void openPayments() {
        Intent intent = new Intent(this, ShowPaymentDetails.class );
        startActivity(intent);
    }




}