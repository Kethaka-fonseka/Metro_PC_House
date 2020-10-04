package com.example.pc_house;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Item_Display extends AppCompatActivity {
    TextView title,price,qty,description;
    ImageView image;
    Button add,plus,minus,checkout;
    ImageView cart,profile,category;
    ImageView home;
    FirebaseAuth fAuth;
    int val1=0;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__display);


        cart=findViewById(R.id.cart);
        category=findViewById(R.id.categoryBtn);
        home=findViewById(R.id.homeImage);
        profile=findViewById(R.id.btnProfile);
        title=findViewById(R.id.text_title);
        price=findViewById(R.id.text_price);
        image=findViewById(R.id.imageView);
        add=findViewById(R.id.buttonCart);
        qty=findViewById(R.id.text_qty);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        checkout=findViewById(R.id.buttonCheck);
        fAuth=FirebaseAuth.getInstance();





        Intent intent=getIntent();

        final Item i1=intent.getParcelableExtra("item");


        Glide.with(this).load(i1.getUrl()).into(image);
        title.setText(i1.getName());
        price.setText("RS"+String.valueOf(i1.getPrice()));



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(val1==i1.getQty()){
                    plus.setEnabled(false);
                }
                else {
                    val1++;
                    minus.setEnabled(true);
                    qty.setText(String.valueOf(val1));
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                val1--;
                if(val1!=-1) {
                    plus.setEnabled(true);
                    qty.setText(String.valueOf(val1));
                }
                else{
                    minus.setEnabled(false);
                }
            }
        });
        qty.setText(String.valueOf(val1));

//add items to the cart
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(String.valueOf(fAuth.getCurrentUser().getUid()));
                TheCart cart=new TheCart();
                cart.setID(i1.getID());
                cart.setName(i1.getName());
                cart.setPrice(i1.getPrice()*val1);
                cart.setUrl(i1.getUrl());
                cart.setQty(val1);

                Toast.makeText(getApplicationContext(),"Item successfully added",Toast.LENGTH_SHORT).show();
                dbRef.child(String.valueOf(cart.getID())).setValue(cart);
            }
        });
        //add one item to the order
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef=FirebaseDatabase.getInstance().getReference().child("Order").child(fAuth.getCurrentUser().getUid());

                      int  quantity=Integer.parseInt(qty.getText().toString().trim());
                        TheCart cart1=new TheCart();
                        cart1.setID(i1.getID());
                        cart1.setName(i1.getName());
                        cart1.setPrice(i1.getPrice()*quantity);
                        cart1.setUrl(i1.getUrl());
                        cart1.setQty(quantity);
                        String key=dbRef.push().getKey();
                        dbRef.child(key).child(String.valueOf(cart1.getID())).setValue(cart1);

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Cart.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerProfile.class );
                startActivity(intent);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Category.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }





}