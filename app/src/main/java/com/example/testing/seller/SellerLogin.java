package com.example.testing.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLogin extends AppCompatActivity {
    private EditText b1, b2;
    Button b3;
    ProgressDialog loadingBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        b1 = findViewById(R.id.sellermaillog);
        b2 = findViewById(R.id.sellerpasslog);
        b3=findViewById(R.id.sellerbutton);
        auth = FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginseller();
            }
        });

    }


    private void loginseller() {

        final String email = b1.getText().toString();
        final String password = b2.getText().toString();


        if (!email.equals("") && !password.equals("")) {


            loadingBar.setTitle("Login in Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loadingBar.dismiss();
                        Toast.makeText(SellerLogin.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(SellerLogin.this, sellerhomepage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    else{
                        loadingBar.dismiss();
                        Toast.makeText(SellerLogin.this, "This userid do not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        else{
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }

    }
}