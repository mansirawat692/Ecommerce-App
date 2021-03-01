package com.example.testing.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.Interface.ItemClickListner;
import com.example.testing.R;

public class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView textpname,textpquantity,textpprice,textdes;
private ItemClickListner itemClickListner;
public ImageView imageViewp;

    public cartViewHolder(@NonNull View itemView) {
        super(itemView);
        textpname=itemView.findViewById(R.id.itemname);
        textpprice=itemView.findViewById(R.id.itemprice);
        textpquantity=itemView.findViewById(R.id.itemquantity);
        textdes=itemView.findViewById(R.id.itemdes);
/*        imageViewp=itemView.findViewById(R.id.imagep);*/
    }



    @Override
    public void onClick(View v) {
itemClickListner.onClick(v, getAdapterPosition(), false);
    }


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
