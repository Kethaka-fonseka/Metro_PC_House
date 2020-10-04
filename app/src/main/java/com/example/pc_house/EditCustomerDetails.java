package com.example.pc_house;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class EditCustomerDetails extends AppCompatActivity {

    EditText EnterCustomerName, EnterCustomerID, EnterCustomerEmail, EnterCustomerPhone;
    Button CustSaveBtn;
    DatabaseReference dbRef;
    User user;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_details);

        EnterCustomerName = findViewById(R.id.EnterCustomerName);
        EnterCustomerID = findViewById(R.id.EnterCustomerID);
        EnterCustomerEmail = findViewById(R.id.EnterCustomerEmail);
        EnterCustomerPhone = findViewById(R.id.EnterCustomerPhone);
        firebaseAuth = FirebaseAuth.getInstance();
        CustSaveBtn = findViewById(R.id.CustSaveBtn);




        //get passed object
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        EnterCustomerName.setText(user.getName());
        EnterCustomerID.setText(user.getId());
        EnterCustomerEmail.setText(user.getEmail());
        EnterCustomerPhone.setText(String.valueOf(user.getPhone()));



        CustSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                dbRef = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getCurrentUser().getUid());
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(user.getId())){

                            user.setName(EnterCustomerName.getText().toString().trim());
                            user.setId(EnterCustomerID.getText().toString().trim());
                            user.setEmail(EnterCustomerEmail.getText().toString().trim());
                            user.setPhone(Integer.parseInt(EnterCustomerPhone.getText().toString().trim()));

                            dbRef.child(user.getId()).setValue(user);

                            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(EditCustomerDetails.this,CustomerProfile.class);
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