package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddNewPayment extends AppCompatActivity {

    private static ArrayList list;
    EditText addname,addcard,addcvv,addexpire;
    Button btnConfirm;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    Payment pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_payment);

        list=new ArrayList<Integer>();
        addname= findViewById(R.id.custName);
        addcard = findViewById(R.id.cardNo);
        addcvv = findViewById(R.id.cvv);
        addexpire = findViewById(R.id.expireDate);
        btnConfirm = findViewById(R.id.confirmAddPayment);
        firebaseAuth=FirebaseAuth.getInstance();


        pay = new Payment();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Payment Details").child(firebaseAuth.getCurrentUser().getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Payment add=dataSnapshot1.getValue(Payment.class);
                        list.add(add.getPaymentId());
                    }
                }
                else{}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = addname.getText().toString().trim();
                String card= addcard.getText().toString().trim();
                String cvv = addcvv.getText().toString().trim();
                String expdate = addexpire.getText().toString().trim();

                try {
                    if (card.isEmpty()) {

                        addcard.setError("Please enter valid card number");
                        return;

                    }
                    if (name.isEmpty()) {

                        addname.setError("Please enter card holder name");
                        return;

                    }
                    if (cvv.isEmpty()) {

                        addcvv.setError("Please enter your cvv");
                        return;

                    }
                    if (expdate.isEmpty()) {


                        addexpire.setError("Please enter your expire date");
                        return;

                    }


                    pay.setPaymentId(generatePaymentIDs());
                    pay.setCustomer_name(addname.getText().toString().trim());
                    pay.setCardNo(addcard.getText().toString().trim());
                    pay.setCvv(Integer.parseInt(addcvv.getText().toString().trim()));
                    pay.setExpireDate(addexpire.getText().toString().trim());


                    dbRef.child(String.valueOf(pay.getPaymentId())).setValue(pay);

                    Toast.makeText(getApplicationContext(), "Successfully Added !!!", Toast.LENGTH_SHORT).show();
                    clearControls();
                    Intent intent = new Intent(AddNewPayment.this, CustomerProfile.class);
                    startActivity(intent);

                }catch (NumberFormatException e){

                    Toast.makeText(getApplicationContext(), "Something Wrong, Please Check Details Again !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    private void clearControls(){


        addname.setText("");
        addcard.setText("");
        addcvv.setText("");
        addexpire.setText("");

    };

    public static int generatePaymentIDs() {

        int id;
        int next =list.size();
        next++;
        id = CommonConstants.Payment_ID_Prefix+ next;
        if (list.contains(id)) {
            next++;
            id = CommonConstants.Payment_ID_Prefix + next;
        }
        return id;
    }
}