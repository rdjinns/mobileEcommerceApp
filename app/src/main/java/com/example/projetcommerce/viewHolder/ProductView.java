package com.example.projetcommerce.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetcommerce.Interface.ItemClickListener;
import com.example.projetcommerce.R;

public class ProductView extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtName, txtDescription, txtPrice;
    public ImageView productImage;
    public ItemClickListener listener;



    public ProductView(@NonNull View itemView) {
        super(itemView);


        productImage = itemView.findViewById(R.id.productImage);
        txtName = itemView.findViewById(R.id.productName);
        txtDescription = itemView.findViewById(R.id.productDescr);
        txtPrice = itemView.findViewById(R.id.productPrice);


    }

    public void setItemClickListner(ItemClickListener listener)
    {
        this.listener = listener;
    }


    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
