package com.example.projetcommerce.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetcommerce.Interface.ItemClickListener;
import com.example.projetcommerce.R;

public class OrderView extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView name, phone, adresse,totalAmount,date;
    public Button checkBtn,sendBtn;
    private ItemClickListener itemClickListener;

    public OrderView(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.layoutNameOrder);
        phone = itemView.findViewById(R.id.layoutPhoneOrder);
        adresse = itemView.findViewById(R.id.layoutAdressOrder);
        totalAmount = itemView.findViewById(R.id.layoutTotalAmount);
        checkBtn = itemView.findViewById(R.id.checkProductBtn);
        date = itemView.findViewById(R.id.layoutDateOrder);
        sendBtn = itemView.findViewById(R.id.sendBtn);
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
