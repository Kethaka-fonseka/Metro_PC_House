package com.example.pc_house;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loging extends AppCompatActivity {

    EditText  email,password;
    Button  logBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);

        email=findViewById(R.id.logMail);
        password=findViewById(R.id.logPassword);
        fAuth=FirebaseAuth.getInstance();

        logBtn=findViewById(R.id.loginBtn);



        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString().trim();
                String  Password=password.getText().toString().trim();

                if(TextUtils.isEmpty(Email)){
                    email.setError("Please enter the email..");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Please enter the password");
                    return;
                }

                fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"login Unsuccessful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}