package com.example.pc_house;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProductDetails extends AppCompatActivity {

    EditText editproductid,editproductname,editproductcategory,editproductquantity,editprice,editproductURL;
    Button btnSaveProduct;
    DatabaseReference dbRef;
    Item item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_details);

        editproductid = findViewById(R.id.productid);
        editproductname = findViewById(R.id.productname);
        editprice = findViewById(R.id.price);
        editproductcategory = findViewById(R.id.prodcategory);
        editproductquantity = findViewById(R.id.productquantity);
        editproductURL = findViewById(R.id.productURL);


        btnSaveProduct = findViewById(R.id.editconfirmProduct);

        item= new Item();


        //get passed object
        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");

        editproductid.setText(item.getID());
        editproductname.setText(item.getName());
        editprice.setText((int) item.getPrice());
        editproductcategory.setText(item.getCategory());
        editproductquantity.setText(item.getQty());
        editproductURL.setText(item.getUrl());



        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                dbRef = FirebaseDatabase.getInstance().getReference().child("Product Details");
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(String.valueOf(item.getID()))){

                            item.setName(editproductname.getText().toString().trim());
                            item.setPrice(Integer.parseInt(editprice.getText().toString().trim()));
                            item.setCategory(editproductcategory.getText().toString().trim());
                            item.setQty(Integer.parseInt(editproductquantity.getText().toString().trim()));
                            item.setUrl(editproductURL.getText().toString().trim());

                            dbRef.child(String.valueOf(item.getID())).setValue(item);

                            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(EditProductDetails.this, ShowProductDetails.class);
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