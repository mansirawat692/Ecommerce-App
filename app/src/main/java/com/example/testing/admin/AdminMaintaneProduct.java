package com.example.testing.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testing.seller.SellerCategoryActivity;
import com.example.testing.user.HomeActivity;
import com.example.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.text.TextUtils.isEmpty;

public class AdminMaintaneProduct extends AppCompatActivity {
public EditText et1,et2,et3;
public ImageView iv1;
public Button bt1,delete;
public String PID="";
public DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintane_product);

        PID=getIntent().getStringExtra("pid");
        bt1=findViewById(R.id.upload);
        et1=findViewById(R.id.product_nameupdate);
        et2=findViewById(R.id.product_priceupdate);
        et3=findViewById(R.id.product_descriptionupdate);
        iv1=findViewById(R.id.product_imageupdate);
        delete=findViewById(R.id.delete);


        databaseReference= FirebaseDatabase.getInstance().getReference().child("Products").child(PID);


        displayProduct();


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applychanges();
            }
        });



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletepro();
            }
        });

    }


    private void displayProduct(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String Productname=dataSnapshot.child("pname").getValue().toString();
                    String Productprice=dataSnapshot.child("price").getValue().toString();
                    String Productdescription=dataSnapshot.child("description").getValue().toString();
                    String Productimage=dataSnapshot.child("image").getValue().toString();

                    et1.setText(Productname);
                    et2.setText(Productprice);
                    et3.setText(Productdescription);
                    Picasso.get().load(Productimage).into(iv1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void applychanges(){

        String name=et1.getText().toString();
        String price=et2.getText().toString();
        String description=et3.getText().toString();

        if(isEmpty(name))
            et1.setError("Product Name Missing");


        else if(isEmpty(price))
            et2.setError("Product Price Missing");


       else if(isEmpty(description))
            et3.setError("Product Description Missing");




       else {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("pname", name);
            hashMap.put("price", price);
            hashMap.put("description", description);
            hashMap.put("pid",PID);


            databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintaneProduct.this, "Changes are done", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminMaintaneProduct.this, SellerCategoryActivity.class));
                        finish();
                    }
                }
            });
        }


    }




    private void deletepro(){

        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(AdminMaintaneProduct.this, HomeActivity.class));
                    finish();
                    Toast.makeText(AdminMaintaneProduct.this, "This products has been deleted!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}