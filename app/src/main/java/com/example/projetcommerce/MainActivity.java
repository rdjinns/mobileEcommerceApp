package com.example.projetcommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.projetcommerce.connectedUser.connected;
import com.example.projetcommerce.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn, joinNowBtn;
    private ProgressDialog loadingBar;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);


       joinNowBtn =  findViewById(R.id.join_btn);
       loginBtn =  findViewById(R.id.login_btn);
       loadingBar = new ProgressDialog(this);
       Paper.init(this);

       loginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, LoginActivity.class);
               startActivity(intent);
           }
       });

       joinNowBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
               startActivity(intent);
           }
       });


       ///////// On va utiliser Paper pour garder en mémoire téléphone les indentifiants que l'user a décidé de se souvenir
       String UserPhoneKey = Paper.book().read(connected.UserPhoneKey);
       String UserPasswordKey = Paper.book().read(connected.UserPasswordKey);

       // si les deux variables de classe ne sont pas vide ça veut dire que quelqu'un a check le bouton
       if (UserPhoneKey != "" && UserPasswordKey != "")
       {
           if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
           {
               AllowAccess(UserPhoneKey, UserPasswordKey);

               loadingBar.setTitle("Déjà connecté");
               loadingBar.setMessage("Patientez...");
               loadingBar.setCanceledOnTouchOutside(false);
               loadingBar.show();
           }
       }
   }

   // On laisse donc la connexion allumé
    private void AllowAccess(final String phone, final String password)
    {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Patientez... Vous êtes déjà connecté ! ", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            connected.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Mot de passe incorrect !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Compte introuvable...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}



