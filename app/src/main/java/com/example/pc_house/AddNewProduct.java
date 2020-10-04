package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;


public class AddNewProduct extends AppCompatActivity {

    //private static ArrayList list;
    EditText addProductID,addProductname,addProductCategory,addProdQuantity, addProductPrice,addProductURL;
    Button btnConfirmProduct;
    DatabaseReference dbRef;
    Item item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        //list=new ArrayList<Integer>();
        addProductID = findViewById(R.id.productid);
        addProductname = findViewById(R.id.productname);
        addProductPrice = findViewById(R.id.price);
        addProductCategory = findViewById(R.id.prodcategory);
        addProdQuantity = findViewById(R.id.productquantity);
        addProductURL = findViewById(R.id.productURL);

        btnConfirmProduct = findViewById(R.id.confirmProduct);


        item = new Item();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Item");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        Item item=dataSnapshot1.getValue(Item.class);
                        // list.add(item.getID());


                    }


                }
                // else{}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnConfirmProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodID = addProductID.getText().toString().trim();
                String prodName = addProductname.getText().toString().trim();
                String prodCategory = addProductCategory.getText().toString().trim();
                String prodQuantity = addProdQuantity.getText().toString().trim();
                String prodPrice = addProductPrice.getText().toString().trim();
                String prodURL = addProductURL.getText().toString().trim();


                try {
                    if (prodID.isEmpty()) {

                        addProductID.setError("Please enter your product ID");
                        return;


                    }
                    if (prodName.isEmpty()) {

                        addProductname.setError("Please enter your product name");
                        return;


                    }
                    if (prodCategory.isEmpty()) {

                        addProductCategory.setError("Please enter your product category");
                        return;

                    }
                    if (prodQuantity.isEmpty()) {

                        addProdQuantity.setError("Please enter your product quantity");
                        return;

                    }
                    if (prodPrice.isEmpty()) {

                        addProductPrice.setError("Please enter your product price");
                        return;

                    }
                    if (prodURL.isEmpty()) {

                        addProductURL.setError("Please enter your product URL");
                        return;

                    }


                    item.setID(addProductID.getText().toString().trim());
                    item.setName(addProductname.getText().toString().trim());
                    item.setCategory(addProductCategory.getText().toString().trim());
                    item.setQty(Integer.parseInt(addProdQuantity.getText().toString().trim()));
                    item.setPrice(Double.parseDouble(addProductPrice.getText().toString().trim()));
                    item.setUrl(addProductURL.getText().toString().trim());

                    dbRef.child(String.valueOf(item.getID())).setValue(item);

                    Toast.makeText(getApplicationContext(), "Successfully Added !!!", Toast.LENGTH_SHORT).show();
                    clearControls();
                    // Intent intent = new Intent(AddNewProduct.this, ShowProductDetals.class);
                    //  startActivity(intent);

                }catch(NumberFormatException e){

                    Toast.makeText(getApplicationContext(), "Something Wrong, Please Check Details Again !!!", Toast.LENGTH_SHORT).show();

                }


            }

        });

    }


    private void clearControls(){

        addProductID.setText("");
        addProductname.setText("");
        addProductCategory.setText("");
        addProdQuantity.setText("");
        addProductPrice.setText("");
        addProductURL.setText("");

    }




    //=====================


/*    public static int generateProductIDs() {

        int id;
        int next =list.size();
        next++;
        id = CommonConstants.Product_ID_Prefix+ next;
        if (list.contains(id)) {
            next++;
            id = CommonConstants.Product_ID_Prefix + next;
        }
        return id;
    }*/
}
