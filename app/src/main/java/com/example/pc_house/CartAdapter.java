package com.example.pc_house;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context mCtx;
    private List<TheCart> cartList;
    DatabaseReference dbRef;
    FirebaseAuth fAuth;




    public CartAdapter(Context mCtx, List<TheCart> cartList) {
        this.mCtx = mCtx;
        this.cartList = cartList;
    }

    public CartAdapter() {

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final TheCart cart=cartList.get(position);

        fAuth=FirebaseAuth.getInstance();
        Glide.with(mCtx).load(cart.getUrl()).into(holder.imageView1);
        holder.textViewName1.setText(cart.getName());
        holder.textViewPrice1.setText(String.valueOf(cart.getPrice()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(cart.getID());
                Intent intent=new Intent(holder.delete.getContext(),Cart.class);
                holder.delete.getContext().startActivity(intent);
            }
        });
        holder.numberButton.setNumber(String.valueOf(cart.getQty()));
        holder.numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef=FirebaseDatabase.getInstance().getReference().child("Cart").child(fAuth.getCurrentUser().getUid()).child(String.valueOf(cart.getID()));

                notifyItemRangeChanged(position,cartList.size());
                double Unitprice=getUnitPrice(cart.getPrice(),cart.getQty());

                int qty=cart.getQty();
                cart.setQty(Integer.parseInt(holder.numberButton.getNumber()));
                cart.setPrice(cart.getQty()*Unitprice);
                cartList.remove(position);
                dbRef.removeValue();


                dbRef.setValue(cart);
                holder.numberButton.getContext().startActivity(new Intent(holder.numberButton.getContext(),Cart.class));











            }
        });

    }

    public double getUnitPrice(double price,int qty){
        return price/qty;
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
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName1,textViewPrice1;
        ImageView imageView1;
        Button delete;
        ElegantNumberButton numberButton;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.image_view_id1);
            textViewName1=itemView.findViewById(R.id.text_view_name1);
            textViewPrice1=itemView.findViewById(R.id.text_view_price1);
            delete=itemView.findViewById(R.id.btnDelete);
            numberButton=itemView.findViewById(R.id.quantity_btn);

        }
    }
}
