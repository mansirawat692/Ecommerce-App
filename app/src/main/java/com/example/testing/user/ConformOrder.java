package com.example.testing.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testing.Prevalent.Prevalent;
import com.example.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConformOrder extends AppCompatActivity {
public Button con;
public EditText conadd,conname,concity,conphone;
public String TotalAmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conform_order);

        TotalAmount=getIntent().getStringExtra("Product Total Price");
        con=findViewById(R.id.con);
        conadd=findViewById(R.id.conadd);
        concity=findViewById(R.id.concity);
        conname=findViewById(R.id.conname);
        conphone=findViewById(R.id.conphone);


        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {

        if (TextUtils.isEmpty(conname.getText().toString())) {
            conname.setError("Provide Full Name");
        } else if (TextUtils.isEmpty(conadd.getText().toString())) {
            conadd.setError("Provide Address");
        } else if (TextUtils.isEmpty(concity.getText().toString())) {
            concity.setError("Provide City");
        } else if (TextUtils.isEmpty(conphone.getText().toString())) {
            conphone.setError("Provide Phone Number");
        } else {
            conformOrder();
        }
    }



    private void conformOrder() {

        final String curdate,curtime;

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat dateFormat=new SimpleDateFormat("MM dd, yyyy");
        curdate=dateFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat1=new SimpleDateFormat("HH:mm:ss a");
        curtime=dateFormat1.format(calendar.getTime());


        final DatabaseReference order= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("TotalAmount", TotalAmount);
        hashMap.put("pname",conname.getText().toString() );
        hashMap.put("address",conadd.getText().toString() );
        hashMap.put("city",concity.getText().toString() );
        hashMap.put("phone",conphone.getText().toString() );
        hashMap.put("time",curtime );
        hashMap.put("date",curdate );
        hashMap.put("state", "not shipped");

        order.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart")
                            .child("User Cart")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(ConformOrder.this, "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(ConformOrder.this, HomeActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();

                                    }
                                }
                            });
                }
            }
        });
    }
}