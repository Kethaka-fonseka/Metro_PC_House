package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Category_view extends AppCompatActivity implements  ItemAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> itemList;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        Intent intent=getIntent();
        String category=intent.getStringExtra("cat");

        recyclerView = findViewById(R.id.recyclerView_CatView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        itemList = new ArrayList<>();
        adapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(Category_view.this);
        dbRef= FirebaseDatabase.getInstance().getReference().child("Item");
        dbRef.orderByChild("category").equalTo(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Item item1=dataSnapshot1.getValue(Item.class);
                        itemList.add(item1);

                    }
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void OnItemClick(int position) {
        Intent intent=new Intent(this,Item_Display.class);
        Item item1=itemList.get(position);
        intent.putExtra("item",item1);
        startActivity(intent);
    }
}