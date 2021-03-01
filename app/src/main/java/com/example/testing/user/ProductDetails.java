package com.example.testing.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.testing.Model.Products;
import com.example.testing.Prevalent.Prevalent;
import com.example.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
    private Button flll;
   private ElegantNumberButton elegent;
    private ImageView imageView2;
    private TextView t1,t2,t3;
    public String productid="",state="normal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        flll=findViewById(R.id.flll);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        imageView2=findViewById(R.id.productdimage);
        elegent=findViewById(R.id.elegant);


        productid=getIntent().getStringExtra("pid");

        getproductDetails(productid);
        
        
        
        flll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state=="Order Shipped" || state=="Order Placed"){

                    Toast.makeText(ProductDetails.this, "You can purchase other products ones the previous orders will be shipped at your door", Toast.LENGTH_LONG).show();
                }
                else
                    addtoCart();
            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        orderState();
    }

    private void addtoCart() {
        String currentDate,currentTime;

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat dateFormat=new SimpleDateFormat("MM dd,yy");
        currentDate=dateFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat1=new SimpleDateFormat("HH:mm:ss a");
        currentTime=dateFormat1.format(calendar.getTime());


        final DatabaseReference database=FirebaseDatabase.getInstance().getReference().child("Cart");

        final HashMap<String,Object> hash=new HashMap<>();
        hash.put("pid",productid);
        hash.put("pname",t1.getText().toString());
        hash.put("pprice",t3.getText().toString());
        hash.put("quantity",elegent.getNumber());
        hash.put("time",currentTime);
        hash.put("date",currentDate);
        hash.put("discount","");
      /*  hash.put("image", imageView2);*/
        hash.put("pdescription", t2.getText().toString());

        database.child("User Cart").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productid).updateChildren(hash)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    database.child("Admin Cart").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productid).updateChildren(hash)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ProductDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProductDetails.this, HomeActivity.class));
                    }


                    else {
                        Toast.makeText(ProductDetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
}
            }
        });
    }



    private void getproductDetails(final String productid) {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");
        databaseReference.child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products pro=dataSnapshot.getValue(Products.class);
                    t1.setText(pro.getPname());
                    t2.setText(pro.getDescription());
                    t3.setText(pro.getPrice());
                    Picasso.get().load(pro.getImage()).into(imageView2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void orderState(){
        DatabaseReference orderstate;
        orderstate=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());


        orderstate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String Shipping_State=dataSnapshot.child("state").getValue().toString();


                    if(Shipping_State.equals("shipped")){

                        state="Order Shipped";

                    }


                    else if(Shipping_State.equals("not shipped")){

                        state="Order Placed";

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
