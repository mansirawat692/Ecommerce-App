package com.example.testing.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.Model.Products;
import com.example.testing.R;
import com.example.testing.ViewHolder.ProductViewHolder;
import com.example.testing.ViewHolder.SellerItemViewHolder;
import com.example.testing.admin.AdminMaintaneProduct;
import com.example.testing.admin.CheckApproveProducts;
import com.example.testing.user.HomeActivity;
import com.example.testing.user.MainActivity;
import com.example.testing.user.ProductDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class sellerhomepage extends AppCompatActivity {
public TextView textView;
RecyclerView rv6;
RecyclerView.LayoutManager layoutManager;
private DatabaseReference databaseReference;

     private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent j = new Intent(sellerhomepage.this, sellerhomepage.class);
                    startActivity(j);
                    return true;

                case R.id.navigation_add:

                    Intent intent = new Intent(sellerhomepage.this, SellerCategoryActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.navigation_out:

                    final FirebaseAuth auth;
                    auth = FirebaseAuth.getInstance();
                    auth.signOut();

                    Intent i = new Intent(sellerhomepage.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                    return true;
            }
            return false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerhomepage);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        textView=findViewById(R.id.txtvi);
        rv6=findViewById(R.id.rv6);
        layoutManager=new LinearLayoutManager(this);
        rv6.setHasFixedSize(true);
        rv6.setLayoutManager(layoutManager);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");



        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_out)
                .build();
 /*

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    */

    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, SellerItemViewHolder> adapter=new FirebaseRecyclerAdapter<Products, SellerItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerItemViewHolder productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.ttProductName.setText(products.getPname());
                productViewHolder.ttProductDescription.setText(products.getDescription());
                productViewHolder.ttProductDescription.setText(products.getProductstate());
                productViewHolder.ttProductPrice.setText("Rs. " + products.getPrice());
                Picasso.get().load(products.getImage()).into(productViewHolder.imageView4);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String pid =products.getPid();

                        CharSequence op[]=new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(sellerhomepage.this);
                        builder.setTitle("Do you want to delete this product ?");
                        builder.setItems(op, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    deleteproducts(pid);
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
            public SellerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_items, parent, false);
                SellerItemViewHolder holder = new SellerItemViewHolder(view);
                return holder;
            }
        };
        rv6.setAdapter(adapter);
        adapter.startListening();
    }


    public void deleteproducts(String pid){
        databaseReference.child(pid).removeValue()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(sellerhomepage.this, "Product has been deleted successfully!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}