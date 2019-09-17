package com.example.projetcommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.projetcommerce.Model.Products;
import com.example.projetcommerce.connectedUser.connected;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class ProductDetailsActivity extends AppCompatActivity {


    private Button addcart;
    private ImageView productImage;
    private ElegantNumberButton numberProduct;
    private TextView priceProduct, nameProduct, descriptionProduct;
    private String productID;
    private DatabaseReference ProductsRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products"); // contient tout nos produits

        productID = getIntent().getStringExtra("pid");

        addcart = findViewById(R.id.addCart);
        productImage = findViewById(R.id.productDetailsImage);
        numberProduct = findViewById(R.id.numberDetails);
        priceProduct = findViewById(R.id.priceDetails);
        nameProduct = findViewById(R.id.NameDetails);
        descriptionProduct = findViewById(R.id.descriptionDetails);



        getProduct(productID);


        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                addtoCart();

            }
        });

    }



    private void getProduct(final String productID) {


        ProductsRef.child(productID).addValueEventListener(new ValueEventListener() { // on recherche via l'ID
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    Products products = dataSnapshot.getValue(Products.class); // on stock dans une class les produits

                    nameProduct.setText(products.getPname());
                    priceProduct.setText(products.getPrice() + " €");
                    descriptionProduct.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addtoCart() {

        String saveCurrentTime, saveCurrentDate;

        Calendar date = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(date.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(date.getTime());


       final DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("CartList");
       final DatabaseReference cartUser = FirebaseDatabase.getInstance().getReference().child("CartList").child("User view");

        final HashMap<String, Object> cartMap = new HashMap<>();



        cartMap.put("pid", productID);
        cartMap.put("pname", nameProduct.getText().toString());
        cartMap.put("price", priceProduct.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberProduct.getNumber());
        cartMap.put("discount", "");




        cartlist.child("User view").child(connected.currentOnlineUser.getPhone())
                .child("Products").child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())

                            Toast.makeText(ProductDetailsActivity.this, "Produit ajouté au panier ! ", Toast.LENGTH_SHORT).show();

                    }
                    });
        }

}
