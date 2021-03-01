package com.example.testing.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testing.Interface.ItemClickListner;
import com.example.testing.Model.Products;
import com.example.testing.R;
import com.example.testing.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CheckApproveProducts extends AppCompatActivity {
private RecyclerView rv5;
RecyclerView.LayoutManager layoutManager;
private DatabaseReference approval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_approve_products);


        approval= FirebaseDatabase.getInstance().getReference().child("Products");
        rv5=findViewById(R.id.rv5);
        layoutManager=new LinearLayoutManager(this);
        rv5.setLayoutManager(layoutManager);
       rv5.setHasFixedSize(true);
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(approval.orderByChild("productstate").equalTo("not approved"), Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.txtProductName.setText(products.getPname());
                productViewHolder.txtProductDescription.setText(products.getDescription());
                productViewHolder.txtProductPrice.setText("Rs. " + products.getPrice());
                Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String pid =products.getPid();

                        CharSequence op[]=new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CheckApproveProducts.this);
                        builder.setTitle("Do you want to approve this product ?");
                        builder.setItems(op, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    checkproducts(pid);
                                }

                                else if(which==1){

                                }
                            }
                        });
                        builder.show();
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
        rv5.setAdapter(adapter);
        adapter.startListening();
    }


    private void checkproducts(String pid){
        approval.child(pid).child("productstate").setValue("approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CheckApproveProducts.this, "This product has been approved for selling ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}