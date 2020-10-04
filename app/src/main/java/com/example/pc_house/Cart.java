package com.example.pc_house;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<TheCart> cartList;
    DatabaseReference dbRef;
    ImageView home;
    Button checkout;
    Button searchBtn;
    EditText searchText;
    ImageView cart,profile,category;
    FirebaseAuth firebaseAuth;
    TextView total;
    double sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        firebaseAuth=FirebaseAuth.getInstance();
        searchText=findViewById(R.id.searching_text);
        searchBtn=findViewById(R.id.search_button_main);

        total=findViewById(R.id.PriceView);
        cart=findViewById(R.id.cart);
        category=findViewById(R.id.categoryBtn);
        profile=findViewById(R.id.btnProfile);
        sum=0.0;
        checkout=findViewById(R.id.cartCheck);
        home=findViewById(R.id.homeImage);
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartList = new ArrayList<>();
        adapter = new CartAdapter(this, cartList);
        recyclerView.setAdapter(adapter);

        //retrive list of items in the cart
        dbRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(firebaseAuth.getCurrentUser().getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        TheCart cart =dataSnapshot1.getValue(TheCart.class);
                        cartList.add(cart);
                    }
                    adapter.notifyDataSetChanged();
                }
                else{}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart.this,MainActivity.class);
                startActivity(intent);
            }
        });


        //get the total count of the cart
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        TheCart cart1=dataSnapshot1.getValue(TheCart.class);
                        sum+=cart1.getPrice();
                    }
                    total.setText(String.valueOf(sum));

                }
                else{}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//add items from items  from cart to order
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbRef2=FirebaseDatabase.getInstance().getReference().child("Order").child(firebaseAuth.getCurrentUser().getUid());
                String Orderid=dbRef2.push().getKey();
                for(int i=0;i<cartList.size();++i){
                    DatabaseReference  dbRef1=FirebaseDatabase.getInstance().getReference().child("Cart").child(firebaseAuth.getCurrentUser().getUid()).child(cartList.get(i).getID());

                    dbRef2.child(Orderid).child(cartList.get(i).getID()).setValue(cartList.get(i));
                    dbRef1.removeValue();
                    startActivity(new Intent(getApplicationContext(),Cart.class));

                }
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


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=searchText.getText().toString();
                Intent intent=new Intent(getApplicationContext(),ShowSearchDetails.class);
                intent.putExtra("msg",msg);
                startActivity(intent);
            }
        });


    }


}