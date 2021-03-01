package com.example.testing.admin;

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

import com.example.testing.Model.Orders;
import com.example.testing.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ADmin_New_Orders extends AppCompatActivity {

public RecyclerView rv2;
private DatabaseReference orderDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_dmin__new__orders);




        orderDisplay = FirebaseDatabase.getInstance().getReference().child("Orders");

        rv2=findViewById(R.id.rv2);
        rv2.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> ordersFirebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderDisplay,Orders.class )
                .build();


        FirebaseRecyclerAdapter<Orders,orderviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Orders, orderviewholder>(ordersFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull orderviewholder orderviewholder, final int i, @NonNull final Orders orders) {
                orderviewholder.txt1.setText("Name : "+orders.getPname());
                orderviewholder.txt2.setText("Phone : "+orders.getPhone());
                orderviewholder.txt3.setText("Address/City : "+orders.getAddress()+", "+orders.getCity());
                orderviewholder.txt4.setText("Date : "+orders.getDate());
                orderviewholder.txt5.setText("Time : "+orders.getTime());
                orderviewholder.txt6.setText("Total Amount :Rs. "+orders.getTotalAmount());


                orderviewholder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid=getRef(i).getKey();
                        Intent j=new Intent(ADmin_New_Orders.this, AdminUserproduct.class);
                        j.putExtra("PID", uid);
                        startActivity(j);
                    }
                });



                orderviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(ADmin_New_Orders.this);
                        builder.setTitle("Order has been shipped ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    String uid=getRef(i).getKey();
                                    RemoveOder(uid);
                                }
                                else if(which==1){
                                    finish();
                                }
                            }
                        }).show();

                    }
                });
            }

            @NonNull
            @Override
            public orderviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adminorder, parent,false);

                return new orderviewholder(v);
            }
        };
        rv2.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }




    public static class orderviewholder extends RecyclerView.ViewHolder {

    private TextView txt1,txt2,txt3,txt4,txt5,txt6;
    private Button button;


    public orderviewholder(@NonNull View itemView) {
        super(itemView);

        txt1=itemView.findViewById(R.id.username);
        txt2=itemView.findViewById(R.id.orderno);
        txt3=itemView.findViewById(R.id.orderaddcity);
        txt4=itemView.findViewById(R.id.orderdate);
        txt5=itemView.findViewById(R.id.ordertime);
        txt6=itemView.findViewById(R.id.orderamount);
        button=itemView.findViewById(R.id.button);

    }
}

    private void RemoveOder(String uid) {
        orderDisplay.child(uid).removeValue();

    }
}