
package com.example.testing.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testing.Model.CartList;
import com.example.testing.Prevalent.Prevalent;
import com.example.testing.R;
import com.example.testing.ViewHolder.cartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Cart extends AppCompatActivity {
public Button next;
public RecyclerView r1;
public RecyclerView.LayoutManager layoutManager1;
public TextView total,confirmsg;
private int totalprice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        next=findViewById(R.id.next);
        r1=findViewById(R.id.rv1);
        total=findViewById(R.id.total);
        layoutManager1=new LinearLayoutManager(this);
        r1.setLayoutManager(layoutManager1);
        confirmsg=findViewById(R.id.conformmsg);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total.setText("Total Amount = Rs."+String.valueOf(totalprice));
                Intent i=new Intent(Cart.this, ConformOrder.class);
                i.putExtra("Product Total Price", String.valueOf(totalprice));
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
orderState();
        final DatabaseReference db1= FirebaseDatabase.getInstance().getReference().child("Cart");

        FirebaseRecyclerOptions<CartList> option=new FirebaseRecyclerOptions.Builder<CartList>()
                .setQuery(db1.child("User Cart").child(Prevalent.currentOnlineUser.getPhone()).child("Products"),CartList.class)
                .build();

        FirebaseRecyclerAdapter<CartList, cartViewHolder> adapter=new FirebaseRecyclerAdapter<CartList, cartViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull cartViewHolder cartViewHolder, int i, @NonNull final CartList cartList) {
                cartViewHolder.textpname.setText(cartList.getPname());
                cartViewHolder.textpprice.setText("Rs."+cartList.getPprice());
                cartViewHolder.textpquantity.setText("Quantity : "+cartList.getquantity());
                cartViewHolder.textdes.setText(cartList.getPdescription());


                int productpricetotal=((Integer.valueOf(cartList.getPprice()))*Integer.valueOf(cartList.getquantity()));
                totalprice=totalprice+productpricetotal;



                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence option[]=new CharSequence[]
                        {
                            "Edit",
                            "Remove"
                        };

                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
                        alertDialog.setTitle("Cart Options");
                        alertDialog.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {
                                    Intent i=new Intent(Cart.this, ProductDetails.class);
                                    i.putExtra("pid",cartList.getPid());
                                    startActivity(i);
                                }

                                if(which==1){
                                    db1.child("User Cart").child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products").child(cartList.getPid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Cart.this, "Item Removed", Toast.LENGTH_SHORT).show();
                                                        Intent i=new Intent(Cart.this, HomeActivity.class);
                                                        startActivity(i);
                                                    }
                                                }
                                            });
                                }

                            }
                        });
                        alertDialog.show();
                    }
                });
               /*Picasso.get().load(cartList.getImage()).into(cartViewHolder.imageViewp);*/
            }

            @NonNull
            @Override
            public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitems, parent,false);
                cartViewHolder holder=new cartViewHolder(view);
                return holder;
            }
        };
        r1.setAdapter(adapter);
        adapter.startListening();
    }


    private void orderState(){
        DatabaseReference orderstate;
        orderstate=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());


        orderstate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String Shipping_State=dataSnapshot.child("state").getValue().toString();
                    String Name=dataSnapshot.child("pname").getValue().toString();


                    if(Shipping_State.equals("shipped")){

                        total.setText("Your order has been shipped");
                        confirmsg.setVisibility(View.VISIBLE);
                        confirmsg.setText("Congratulations, your order has been shipped successfully.Soon you will receive your order");
                        r1.setVisibility(View.GONE);
                        next.setVisibility(View.GONE);

                    }


                    else if(Shipping_State.equals("not shipped")){

                        total.setText(Name + " Your order has not been shipped yet");
                        confirmsg.setVisibility(View.VISIBLE);
                        confirmsg.setText("Congratulations, your order has been shipped successfully.Soon you will be verified");
                        r1.setVisibility(View.GONE);
                        next.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}