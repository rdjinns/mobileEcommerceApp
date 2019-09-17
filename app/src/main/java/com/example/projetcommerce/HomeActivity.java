package com.example.projetcommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetcommerce.Model.Products;
import com.example.projetcommerce.connectedUser.connected;
import com.example.projetcommerce.viewHolder.ProductView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products"); // contient tout nos produits
        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Accueil");


        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView userNameText = headerView.findViewById(R.id.userProfil);
        ImageView profilImage = headerView.findViewById(R.id.profile_image);

        userNameText.setText(connected.currentOnlineUser.getName()); // Quand on s'est loggé on a stocké les données du user dans la classe connected.

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductView> adapter =
                new FirebaseRecyclerAdapter<Products, ProductView>(options) {
                    @Override
                    // On va afficher les données contenu dans la BD dans les textview
                    protected void onBindViewHolder(@NonNull ProductView holder, int position, @NonNull final Products model)
                    {
                        holder.txtName.setText(model.getPname());
                        holder.txtDescription.setText(model.getDescription());
                        holder.txtPrice.setText("Prix = " + model.getPrice() + "€");
                        Picasso.get().load(model.getImage()).into(holder.productImage); // picasso sert à afficher des images contenu dans la DB


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid()); // on envoi l'ID
                                startActivity(intent);
                            }
                        });


                    }
                    @NonNull
                    @Override
                    public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent, false); // on accède à notre layout
                        ProductView holder = new ProductView(view); // on le link à notre class viewProduct
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_panier)
        {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_commande)
        {
            Intent intent = new Intent(HomeActivity.this, OrderActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
            Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(settings);


        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_credit)
        {

            Intent intent = new Intent(HomeActivity.this, CreditActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
