package com.example.projetcommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton;
    private EditText inputName;
    private EditText inputPhone;
    private EditText inputPswd;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton = (Button) findViewById(R.id.confirmregister_btn);
        inputName = (EditText) findViewById(R.id.register_name_input);
        inputPhone = (EditText) findViewById(R.id.register_phone_input);
        inputPswd = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);



        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount(){

        String name = inputName.getText().toString();
        String phone = inputPhone.getText().toString();
        String pswd = inputPswd.getText().toString();

        if (TextUtils.isEmpty(name)) // si vide alors on envoi un toast à l'utilisateur
        {
            Toast.makeText(this, "Rentrez votre pseudo s'il vous plait...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Rentrez votre numéro de téléphone s'il vous plait...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pswd))
        {
            Toast.makeText(this, "Indiquez un mot de passe s'il vous plait...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Création du compte en cours");
            loadingBar.setMessage("Un peu de patience...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            phoneNumberValidation(name,phone,pswd);

        }

    }

    private void phoneNumberValidation(final String name, final String phone, final String pswd) {

        final DatabaseReference refData;
        refData = FirebaseDatabase.getInstance().getReference(); // accès à la base de donnée

        refData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists())) // si dans le noeud utlisateur il y'a une variable phone qui n'existe pas
                {
                    HashMap<String, Object> userdataMap = new HashMap<>(); // on doit mettre les données dans une hashmap
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", pswd);
                    userdataMap.put("name", name);

                    refData.child("Users").child(phone).updateChildren(userdataMap)  // si l'utilisateur n'existe pas alors on rentre les données
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Felicitation votre compte a été créé ! ", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); // on redirige vers le login activité
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Erreur réseau... Veuillez réessayer plus tard", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Numéro de téléphone déjà existant !", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class); // on redirige vers la page d'accueil
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

