package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class ShowSearchDetails extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<Item> searchList;
    String queryText;
    DatabaseReference dbRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search_details);

        Intent intent=getIntent();
        queryText=intent.getStringExtra("msg");
        recyclerView = findViewById(R.id.recycler_view_show_search_details);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchList = new ArrayList<>();
        adapter = new SearchAdapter(this, searchList);
        recyclerView.setAdapter(adapter);
        dbRef= FirebaseDatabase.getInstance().getReference().child("Item");

        //search given item and retrive
        dbRef.orderByChild("name")
                .startAt(queryText)
                .endAt(queryText+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Item item_search=dataSnapshot1.getValue(Item.class);
                    searchList.add(item_search);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
dbRef.orderByChild("category") .startAt(queryText)
        .endAt(queryText+"\uf8ff").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
            Item item_search=dataSnapshot1.getValue(Item.class);
            searchList.add(item_search);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }
}