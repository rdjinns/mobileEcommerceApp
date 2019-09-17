package com.example.projetcommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.example.projetcommerce.connectedUser.connected;
import com.example.projetcommerce.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText getInputPhone;
    private EditText getInputPswd;
    private ProgressDialog loadingBar;
    private TextView admin, notadmin;

    private String usernode = "Users";

    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.confirmlogin_btn);
        getInputPhone = (EditText) findViewById(R.id.login_phone_input);
        getInputPswd = (EditText) findViewById(R.id.login_password_input);
        admin = findViewById(R.id.admin_mode);
        notadmin = findViewById(R.id.notadmin);
        remember = findViewById(R.id.remember);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login Admin");
                admin.setVisibility(View.INVISIBLE);
                notadmin.setVisibility(View.VISIBLE);
                usernode = "Admins";
            }
        });

        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                admin.setVisibility(View.VISIBLE);
                notadmin.setVisibility(View.INVISIBLE);
                usernode = "Users";
            }
        });




    }

    private void Login(){

        String phone = getInputPhone.getText().toString();
        String password = getInputPswd.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Rentrez votre numéro de téléphone s'il vous plait...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Indiquez un mot de passe s'il vous plait...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Connexion en cours");
            loadingBar.setMessage("Un peu de patience...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccess(phone,password);

        }

     }

     private void allowAccess(final String phone, final String password) {



         if(remember.isChecked())
         {
             Paper.book().write(connected.UserPhoneKey, phone);
             Paper.book().write(connected.UserPasswordKey, password);
         }

         final DatabaseReference refData;
         refData = FirebaseDatabase.getInstance().getReference(); // accès à la base de donnée


        refData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(usernode).child(phone).exists()) { // on check si l'utilisateur existe

                    Users usersData = dataSnapshot.child(usernode).child(phone).getValue(Users.class); // on remplit la class user ( POO oklm )

                    if (usersData.getPhone().equals(phone)) {
                        // si le phone de class user provenant de la DB est égale au editext

                        if (usersData.getPassword().equals(password)) // check le pswd
                        {
                            if (usernode.equals("Admins")) { // connexion pour la partie ADMIN
                                Toast.makeText(LoginActivity.this, "Bienvenue ADMIN !", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            } else if (usernode.equals("Users")) { // connexion pour la partie client
                                Toast.makeText(LoginActivity.this, "Connexion autorisé...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                connected.currentOnlineUser = usersData; // session
                                startActivity(intent);
                            }
                        } else { // si le password ne correspond pas
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Données incorrectes", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else { // si le phone est faux
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Données incorrectes", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });
        }
     }