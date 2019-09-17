package com.example.projetcommerce;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.projetcommerce.Model.Cart;
import com.example.projetcommerce.Model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderProductActivity extends AppCompatActivity {

    private TextView products;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_product);

        products =findViewById(R.id.products);



        final DatabaseReference orderList = FirebaseDatabase.getInstance().getReference().child("Orders");


        orderList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                Orders order = (Orders) getIntent().getSerializableExtra("order");

                String var="";

                for (Cart product : order.getProducts())
                {
                    var +=" "+product.getPname()+" : "+product.getQuantity()+" ";
                }

                products.setText(var);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {



            }
        });

    }


}
