package com.example.projetcommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView sucette, jelly, chocolat;
    private ImageView gummy, cookie, marshmallow;
    private Button checkorder, logout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        sucette = findViewById(R.id.sucette);
        jelly = findViewById(R.id.jelly);
        chocolat = findViewById(R.id.chocolat);

        gummy = findViewById(R.id.gummy);
        cookie = findViewById(R.id.cookie);
        marshmallow = findViewById(R.id.marshmallow);
        logout = findViewById(R.id.logoutAdmin);
        checkorder = findViewById(R.id.checkOrdersAdmin);

        // Quand on va cliquer sur un produit à rajouter on redireger avec la valeur du produit
        sucette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "sucette");
                startActivity(intent);
            }
        });

        jelly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "jelly");
                startActivity(intent);
            }
        });

        chocolat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "chocolat");
                startActivity(intent);
            }
        });

        gummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "gummy");
                startActivity(intent);
            }
        });

        cookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "cookie");
                startActivity(intent);
            }
        });

        marshmallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "marshmallow");
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        checkorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}
