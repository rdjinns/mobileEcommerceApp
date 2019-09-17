package com.example.projetcommerce;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projetcommerce.Model.Orders;
import com.example.projetcommerce.viewHolder.OrderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class AdminOrderActivity extends AppCompatActivity {

    private RecyclerView view;
    private RecyclerView.LayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        view = findViewById(R.id.itemOrderAdmin);
        view.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        view.setLayoutManager(manager);
    }

    @Override
    protected void onStart() {
        super.onStart();


        final Query orderList = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>().setQuery(orderList, Orders.class).build(); // on remplit la classe Orders avec la orders de la DB

        for (Orders snapshot : options.getSnapshots()) {
            Log.d("LOG", snapshot.toString());
        }

        FirebaseRecyclerAdapter<Orders, OrderView> adapter = new FirebaseRecyclerAdapter<Orders, OrderView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderView holder, int position, @NonNull final Orders model)
            {
                Log.d("LOG", model.toString());
                holder.phone.setText(model.getPhone());
                holder.date.setText(model.getDate());
                holder.totalAmount.setText(model.getTotalAmount()+" €");
                holder.adresse.setText(model.getAdresse());
                holder.name.setText(model.getName());

                holder.checkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(AdminOrderActivity.this, AdminOrderProductActivity.class);
                        intent.putExtra("order", model);
                        startActivity(intent);
                    }
                });

                holder.sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        final DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("Orders");
                        final Map<String,Object> ordersMap = new HashMap<>();
                        
                        ordersMap.put("etat", "envoyé!");


                        updateRef.child(model.getPid()).updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(!task.isSuccessful())
                                {
                                    Toast.makeText(AdminOrderActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                                
                            }
                        });
                    }



                });
            }
            @NonNull
            @Override
            public OrderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout, viewGroup, false); // on accède à notre layout
                return new OrderView(view);
            }
        };
        view.setAdapter(adapter);
        adapter.startListening();

    }


}
