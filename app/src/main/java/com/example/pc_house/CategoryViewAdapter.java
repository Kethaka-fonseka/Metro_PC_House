package com.example.pc_house;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.CatViewHolder> {
    private Context mCtx;
    private List<Item> itemList;
    DatabaseReference dbRef;
    FirebaseAuth fAuth;

    private ItemAdapter.OnItemClickListener mLister;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(ItemAdapter.OnItemClickListener listener){
        mLister=listener;

    }



    public CategoryViewAdapter(Context mCtx, List<Item> itemList) {
        this.mCtx = mCtx;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_categoryview, parent, false);
        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CatViewHolder holder, final int position) {
        final Item item=itemList.get(position);

        fAuth=FirebaseAuth.getInstance();
        Glide.with(mCtx).load(item.getUrl()).into(holder.imageView1);
        holder.textViewName1.setText(item.getName());
        holder.textViewPrice1.setText(String.valueOf(item.getPrice()));


    }

    public void deleteItem(String id){
        dbRef=FirebaseDatabase.getInstance().getReference().child("Cart").child(fAuth.getCurrentUser().getUid()).child(id);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbRef.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class CatViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName1,textViewPrice1;
        ImageView imageView1;
        CardView parent_layout;
        public CatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.category_view_id);
            textViewName1=itemView.findViewById(R.id.category_view_name);
            textViewPrice1=itemView.findViewById(R.id.category_view_price);
            parent_layout=itemView.findViewById(R.id.card_category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mLister!=null) {
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION) {
                            mLister.OnItemClick(position);

                        }
                    }
                }
            });

        }
    }
}
