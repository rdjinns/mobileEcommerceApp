package com.example.projetcommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetcommerce.Model.Cart;
import com.example.projetcommerce.Model.Orders;
import com.example.projetcommerce.Model.Products;
import com.example.projetcommerce.connectedUser.connected;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ConfirmFinalOrderActivity extends AppCompatActivity {


    private TextView totalprice;
    private EditText nameShip, adressShip, numberShip, cityShip;
    private Button confirmbtn;
    private String totalAmount="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        totalAmount = getIntent().getStringExtra("totalprice");



        totalprice = findViewById(R.id.totalPrice);
        confirmbtn = findViewById(R.id.nextCommand);
        nameShip = findViewById(R.id.shipName);
        adressShip = findViewById(R.id.shipAdress);
        numberShip = findViewById(R.id.shipNumber);
        cityShip = findViewById(R.id.shipCity);

        totalprice.setText("Montant à payer : "+totalAmount+" €");



        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Check();
            }
        });


    }

    private void Check() {



        if (TextUtils.isEmpty(nameShip.getText().toString())) // si vide alors on envoi un toast à l'utilisateur
        {
            Toast.makeText(this, "Rentrez votre nom s'il vous plait", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(adressShip.getText().toString()))
        {
            Toast.makeText(this, "Rentrez votre adresse s'il vous plait", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(numberShip.getText().toString()))
        {
            Toast.makeText(this, "Indiquez un numéro de téléphone de contact", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityShip.getText().toString())) {
            Toast.makeText(this, "Indiquez un une ville de livraison s'il vous plait", Toast.LENGTH_SHORT).show();
        }
        else {

            ConfirmOrder();
        }


    }

    private void ConfirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;
        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("CartList");


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        cartList.child("User view").child(connected.currentOnlineUser.getPhone()).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                List<Cart> listProduct = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Cart ap = snapshot.getValue(Cart.class);
                    listProduct.add(ap);
                }

                String key = UUID.randomUUID().toString();
              /*ordersMap.put("productOrder", listProduct);
                ordersMap.put("totalamount", totalAmount);
                ordersMap.put("name", nameShip.getText().toString());
                ordersMap.put("phone", numberShip.getText().toString());
                ordersMap.put("adresse", adressShip.getText().toString());
                ordersMap.put("city", cityShip.getText().toString());
                ordersMap.put("date", saveCurrentDate);
                ordersMap.put("time", saveCurrentTime);
                ordersMap.put("etat", "non envoyé");*/

                Map<String,Object> orders = new Orders(key,adressShip.getText().toString(), cityShip.getText().toString(), saveCurrentDate, "pas envoyé", nameShip.getText().toString(), numberShip.getText().toString(), saveCurrentTime, totalAmount,connected.currentOnlineUser.getPhone(), listProduct).toMap();


                orderRef.child(key).updateChildren(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference().child("CartList").child("User view").
                                    child(connected.currentOnlineUser.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Votre commande a bien été enregistré ! ", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
