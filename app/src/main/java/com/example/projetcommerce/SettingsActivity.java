package com.example.projetcommerce;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetcommerce.connectedUser.connected;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    private TextView  closeSettings, updateSettings,  deleteAccount;;
    private EditText updateName, updateAdress;
    private ProgressDialog loadingbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loadingbar = new ProgressDialog(this);


        closeSettings = findViewById(R.id.closeSettings);
        updateSettings = findViewById(R.id.updateSettings);
        updateName = findViewById(R.id.nameSettings);
        updateAdress = findViewById(R.id.adressSettings);
        deleteAccount = findViewById(R.id.deleteAccount);


        userInfoDisplay(updateName, updateAdress);

        closeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOnlyUserInfo();

            }

        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAccount();
            }
        });

    }


    private void deleteAccount()
    {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation");
        builder.setMessage("Voulez vous vraiment supprimer votre compte ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                ref.child(connected.currentOnlineUser.getPhone()).removeValue();
                Intent home = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(home);
                Toast.makeText(SettingsActivity.this, "Vous avez supprimé ! ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", updateName.getText().toString());
        userMap.put("adresse", updateAdress.getText().toString());
        ref.child(connected.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
        Toast.makeText(SettingsActivity.this, "Profil mis à jours", Toast.LENGTH_SHORT).show();
        finish();


    }



    private void userInfoDisplay(final EditText updateName, final EditText updateAdress) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(connected.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("adresse").exists()) {

                            String name = dataSnapshot.child("name").getValue().toString();
                            String adresse = dataSnapshot.child("adresse").getValue().toString();
                            updateName.setText(name);
                            updateAdress.setText(adresse);
                        }
                    }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}