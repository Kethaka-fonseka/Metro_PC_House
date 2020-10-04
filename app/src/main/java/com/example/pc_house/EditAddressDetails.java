package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class EditAddressDetails extends AppCompatActivity {


    EditText editCountry,editStreet,editCity,editProvince,editCode,editNumber;
    Button btnSave;
    DatabaseReference dbRef;
    Address add;
    FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address_details);

        editCountry = findViewById(R.id.editcountry);
        editStreet = findViewById(R.id.editstreet);
        editCity = findViewById(R.id.editcity);
        editProvince = findViewById(R.id.editprovince);
        editCode = findViewById(R.id.editcode);
        editNumber = findViewById(R.id.editphone);
        btnSave = findViewById(R.id.editconfirmAddress);
        firebaseAuth=FirebaseAuth.getInstance();

        add= new Address();


        //get passed object
        Intent intent = getIntent();
        add = intent.getParcelableExtra("addrr");
        editCountry.setText(add.getCountry());
        editStreet.setText(add.getStreet_Address());
        editCity.setText(add.getCty());
        editProvince.setText(add.getProvince());
        editCode.setText(String.valueOf(add.getPostalCode()));
        editNumber.setText(String.valueOf(add.getTelephone()));

        //Updating Delivery Details
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                dbRef = FirebaseDatabase.getInstance().getReference().child("Delivery Details").child(firebaseAuth.getCurrentUser().getUid());
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(String.valueOf(add.getAddressId()))){

                            add.setCountry(editCountry.getText().toString().trim());
                            add.setStreet_Address(editStreet.getText().toString().trim());
                            add.setCty(editCity.getText().toString().trim());
                            add.setProvince(editProvince.getText().toString().trim());
                            add.setPostalCode(Integer.parseInt(editCode.getText().toString().trim()));
                            add.setTelephone(Integer.parseInt(editNumber.getText().toString().trim()));

                            dbRef.child(String.valueOf(add.getAddressId())).setValue(add);

                            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(EditAddressDetails.this,CustomerProfile.class);
                            startActivity(intent1);

                        }else{

                            Toast.makeText(getApplicationContext(), "Data Updated Unsuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}