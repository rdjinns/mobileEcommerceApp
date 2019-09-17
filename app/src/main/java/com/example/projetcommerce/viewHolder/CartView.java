package com.example.projetcommerce.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetcommerce.Interface.ItemClickListener;
import com.example.projetcommerce.R;

public class CartView extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView productName, productPrice, productQuantity;
    private ItemClickListener itemClickListener;

    public CartView(@NonNull View itemView) {
        super(itemView);


        productName = itemView.findViewById(R.id.layoutNameProduct);
        productPrice = itemView.findViewById(R.id.layoutPriceProduct);
        productQuantity = itemView.findViewById(R.id.layoutQuantityProduct);
    }

    public void setItemClickListner(ItemClickListener listener)
    {
        this.itemClickListener = listener;
    }


    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);


    }
}
