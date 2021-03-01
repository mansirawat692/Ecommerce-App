package com.example.testing.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.testing.Model.Products;
import com.example.testing.R;
import com.example.testing.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
public EditText search;
public Button searchop;
public RecyclerView rv4;
public RecyclerView.LayoutManager layoutManager;
public String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rv4=findViewById(R.id.rv4);
        layoutManager=new LinearLayoutManager(SearchActivity.this);
        rv4.setLayoutManager(layoutManager);
        search=findViewById(R.id.search);
        searchop=findViewById(R.id.searchop);


        searchop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=search.getText().toString();
                onStart();
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference db2;
        db2= FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(db2.orderByChild("pname").startAt(text), Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products products) {
                holder.txtProductName.setText(products.getPname());
                holder.txtProductDescription.setText(products.getDescription());
                holder.txtProductPrice.setText("Rs. " + products.getPrice());
                Picasso.get().load(products.getImage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(SearchActivity.this, ProductDetails.class);
                        i.putExtra("pid",products.getPid());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        rv4.setAdapter(adapter);
        adapter.startListening();
    }
}