package com.example.testing.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.Interface.ItemClickListner;
import com.example.testing.R;

public class SellerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView ttProductName, ttProductDescription, ttProductPrice,ttProductStatus;
    public ImageView imageView4;
    public ItemClickListner listner;


    public SellerItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView4 = (ImageView) itemView.findViewById(R.id.product_imag);
        ttProductName = (TextView) itemView.findViewById(R.id.product_nam);
        ttProductDescription = (TextView) itemView.findViewById(R.id.product_descriptio);
        ttProductPrice = (TextView) itemView.findViewById(R.id.product_pric);
        ttProductStatus=itemView.findViewById(R.id.product_status);
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
