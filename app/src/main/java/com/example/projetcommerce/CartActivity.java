package com.example.projetcommerce;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetcommerce.Model.Cart;
import com.example.projetcommerce.Model.ProductNames;
import com.example.projetcommerce.Model.Products;
import com.example.projetcommerce.connectedUser.connected;
import com.example.projetcommerce.viewHolder.CartView;
import com.example.projetcommerce.viewHolder.ProductView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class CartActivity extends AppCompatActivity {

    private RecyclerView view;
    private RecyclerView.LayoutManager manager;
    private Button confirmbtn;
    private TextView priceTotal;
    private SharedPreferences preferences;
    private int totalprice;
    private String product="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        view = findViewById(R.id.itemCart);
        view.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        view.setLayoutManager(manager);
        confirmbtn = findViewById(R.id.confirmCommand);
        priceTotal = findViewById(R.id.totalPrice);




    }


    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("CartList");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartList.child("User view").
                child(connected.currentOnlineUser.getPhone()).child("Products"), Cart.class).build(); // on remplit la classe Cart avec la cartlist de la DB

        FirebaseRecyclerAdapter<Cart, CartView> adapter
                = new FirebaseRecyclerAdapter<Cart, CartView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartView holder, int position, @NonNull final Cart model) {

                // on  remplit les champs de text du layout cart
                int price = Integer.parseInt(model.getPrice().replaceAll("[\\D]",""));
                int quantity = Integer.parseInt(model.getQuantity().replaceAll("[\\D]",""));
                int singlePriceQuantity = price*quantity;
                totalprice += singlePriceQuantity;


                priceTotal.setText(String.valueOf("Prix total : "+totalprice+" €"));
                holder.productName.setText(model.getPname());
                holder.productPrice.setText("Prix "+String.valueOf(singlePriceQuantity)+ " €");
                holder.productQuantity.setText(model.getQuantity()+" pcs");



                confirmbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                        intent.putExtra("totalprice", String.valueOf(totalprice));
                        //intent.putExtra("productName", String.valueOf(product));
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        CharSequence options[] = new CharSequence[] {

                                "Edit",
                                "Delete"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Si égal à edit
                                if(which==0) {
                                    Intent home = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    home.putExtra("pid", model.getPid());
                                    startActivity(home);
                                }
                                if(which==1)
                                {

                                    cartList.child("User view").
                                            child(connected.currentOnlineUser.getPhone()).child("Products").child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this, "Produit supprimé !", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });


                                }

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false); // on accède à notre layout
                CartView holder = new CartView(view); // on le link à notre class viewProduct
                return holder;
            }
        };

        view.setAdapter(adapter);
        adapter.startListening();
    }


    protected void onStop() {
        super.onStop();
        totalprice = 0;
    }


}



