package com.example.projetcommerce;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetcommerce.Model.Cart;
import com.example.projetcommerce.Model.Orders;
import com.example.projetcommerce.connectedUser.connected;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {


    private TextView myorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        myorder = findViewById(R.id.myorder);

        final DatabaseReference orderList = FirebaseDatabase.getInstance().getReference().child("Orders");


        orderList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {


                final List<Orders> list = new ArrayList<>();
                final int i = 0;
                for (DataSnapshot snap : dataSnapshot.getChildren())
                {

                    Orders orders = snap.getValue(Orders.class);

                    if(orders.getPhoneInitial().equals(connected.currentOnlineUser.getPhone())){

                        list.add(snap.getValue(Orders.class));
                    }

                }

                String var="";

                for (Orders orders : list) {

                    var+= "Nom : "+orders.getName()+"\nEtat : "+orders.getEtat()+"\nMontant : "+orders.getTotalAmount()+" €"+"\n"+"-------------------\n";

                }

                myorder.setText(var);


                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
