package com.example.testing.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testing.Model.CartList;
import com.example.testing.Prevalent.Prevalent;
import com.example.testing.R;
import com.example.testing.ViewHolder.cartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserproduct extends AppCompatActivity {
    public RecyclerView rv3;
    public RecyclerView.LayoutManager layoutManager;
    public DatabaseReference cartist;
    public String UID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_userproduct);

        UID=getIntent().getStringExtra("PID");
        rv3=findViewById(R.id.rv3);
        cartist= FirebaseDatabase.getInstance().getReference().child("Cart").child("Admin Cart").child(UID).child("Products");
        layoutManager=new LinearLayoutManager(this);
        rv3.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<CartList> cartListFirebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<CartList>()
                .setQuery(cartist, CartList.class)
                .build();

        FirebaseRecyclerAdapter<CartList, cartViewHolder> adapter=new FirebaseRecyclerAdapter<CartList, cartViewHolder>(cartListFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull cartViewHolder cartViewHolder, int i, @NonNull CartList cartList) {
                cartViewHolder.textpname.setText(cartList.getPname());
                cartViewHolder.textpprice.setText("Rs."+cartList.getPprice());
                cartViewHolder.textpquantity.setText("Quantity : "+cartList.getquantity());
                cartViewHolder.textdes.setText(cartList.getPdescription());
            }

            @NonNull
            @Override
            public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitems, parent,false);
                return new cartViewHolder(view);
            }
        };
        rv3.setAdapter(adapter);
        adapter.startListening();



    }
}