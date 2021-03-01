package com.example.testing.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testing.R;
import com.example.testing.user.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisteration extends AppCompatActivity {
private EditText a1,a2,a3,a4,a5;
private TextView a7;
private Button a6;
private FirebaseAuth auth;
private DatabaseReference databaseReference;
private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registeration);

        auth=FirebaseAuth.getInstance();
        a1=findViewById(R.id.sellername);
        a2=findViewById(R.id.sellerno);
        a3=findViewById(R.id.sellermail);
        a4=findViewById(R.id.sellerpass);
        a5=findViewById(R.id.selleradd);
        a6=findViewById(R.id.sellerregister);
        a7=findViewById(R.id.textbutton);
        loadingBar = new ProgressDialog(this);
databaseReference=FirebaseDatabase.getInstance().getReference().child("Seller");


        a7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerRegisteration.this,SellerLogin.class));
            }
        });



        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerseller();
            }
        });
    }

    private void registerseller() {

        final String sellername = a1.getText().toString();
        final String sellerphone = a2.getText().toString();
        final String sellermail = a3.getText().toString().trim();
        final String sellerpassword = a4.getText().toString().trim();
        final String selleraddress = a5.getText().toString();


        if (!sellername.equals("") && !sellerphone.equals("") && !sellermail.equals("") && !sellerpassword.equals("") && !selleraddress.equals("")) {


            loadingBar.setTitle("Create Seller Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            auth.createUserWithEmailAndPassword(sellermail, sellerpassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String sellerid=auth.getCurrentUser().getUid();
                                HashMap<String,Object> hm=new HashMap<>();
                                hm.put("sellername", sellername);
                                hm.put("sellerid", sellerid);
                                hm.put("sellerphone", sellerphone);
                                hm.put("sellermail", sellermail);
                                hm.put("sellerpassword", sellerpassword);
                                hm.put("selleraddress", selleraddress);

                                databaseReference.child(sellerid).updateChildren(hm);

                                Toast.makeText(SellerRegisteration.this, "Account has been created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SellerRegisteration.this,sellerhomepage.class));
                            }

                            else {
                                Toast.makeText(SellerRegisteration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else
        {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }
}