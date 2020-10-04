package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class AddNewAddress extends AppCompatActivity {

    private static ArrayList list;
    EditText addCountry,addStreet,addCity,addProvince,addCode,addNumber;
    Button btnConfirm,btnSkip;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    Address add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        list=new ArrayList<Integer>();
        addCountry = findViewById(R.id.country);
        addStreet = findViewById(R.id.street);
        addCity = findViewById(R.id.city);
        addProvince = findViewById(R.id.province);
        addCode = findViewById(R.id.code);
        addNumber = findViewById(R.id.phone);
        btnConfirm = findViewById(R.id.confirmAddress);
        btnSkip = findViewById(R.id.skipAdd);
        firebaseAuth=FirebaseAuth.getInstance();


        add = new Address();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Delivery Details").child(firebaseAuth.getCurrentUser().getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        Address add=dataSnapshot1.getValue(Address.class);
                        list.add(add.getAddressId());


                    }


                }
                else{}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String country = addCountry.getText().toString().trim();
                String street = addStreet.getText().toString().trim();
                String city = addCity.getText().toString().trim();
                String province = addProvince.getText().toString().trim();
                String code = addCode.getText().toString().trim();
                String phone = addNumber.getText().toString().trim();

                try {
                    if (country.isEmpty()) {

                        addCountry.setError("Please enter your country");
                        return;


                    }
                    if (street.isEmpty()) {

                        addStreet.setError("Please enter your street");
                        return;

                    }
                    if (city.isEmpty()) {

                        addCity.setError("Please enter your city");
                        return;

                    }
                    if (province.isEmpty()) {

                        addProvince.setError("Please enter your province");
                        return;

                    }
                    if (code.isEmpty()) {

                        addCode.setError("Please enter your postal code");
                        return;


                    }
                    if (phone.isEmpty()) {

                        addNumber.setError("Please enter phone number");
                        return;


                    }


                    add.setAddressId(generateAddressIDs());
                    add.setCountry(addCountry.getText().toString().trim());
                    add.setStreet_Address(addStreet.getText().toString().trim());
                    add.setCty(addCity.getText().toString().trim());
                    add.setProvince(addProvince.getText().toString().trim());
                    add.setPostalCode(Integer.parseInt(addCode.getText().toString().trim()));
                    add.setTelephone(Integer.parseInt(addNumber.getText().toString().trim()));


                    dbRef.child(String.valueOf(add.getAddressId())).setValue(add);

                    Toast.makeText(getApplicationContext(), "Successfully Added !!!", Toast.LENGTH_SHORT).show();
                    clearControls();
                    Intent intent = new Intent(AddNewAddress.this, MainActivity.class);
                    startActivity(intent);

                }catch(NumberFormatException e){

                    Toast.makeText(getApplicationContext(), "Something Wrong, Please Check Details Again !!!", Toast.LENGTH_SHORT).show();

                }


            }

        });

    }


    private void clearControls(){


        addCountry.setText("");
        addStreet.setText("");
        addCity.setText("");
        addProvince.setText("");
        addCode.setText("");
        addNumber.setText("");

    }




    //=====================


    public static int generateAddressIDs() {

        int id;
        int next =list.size();
        next++;
        id = CommonConstants.Address_ID_Prefix+ next;
        if (list.contains(id)) {
            next++;
            id = CommonConstants.Address_ID_Prefix + next;
        }
        return id;
    }
}