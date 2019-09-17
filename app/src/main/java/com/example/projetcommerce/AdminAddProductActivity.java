package com.example.projetcommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class AdminAddProductActivity extends AppCompatActivity {


    private String categoryName, Descrip, Price, Pname, saveCurrentDate, saveCurrentTime;;
    private Button AddProduct;
    private EditText inputProductName, inputProductDescription, inputProductPrice;
    private ImageView inputProductImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri; // tableau d'imageview
    private ProgressDialog loadingBar;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        categoryName = getIntent().getExtras().get("category").toString(); // va contenir le putExtra provenant du adminCategory
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images"); // cloud storage pour les images
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);



        AddProduct = findViewById(R.id.addProduct);
        inputProductName = findViewById(R.id.productName);
        inputProductDescription = findViewById(R.id.productDescription);
        inputProductPrice = findViewById(R.id.productPrice);
        inputProductImage= findViewById(R.id.productImage);

        // Quand l'user clique pour rajouter une image au produit
        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });

        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Valider();

            }
        });

    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null) { // On check ce que l'activity nous renvoi et on store dans la variable input

            ImageUri = data.getData();
            inputProductImage.setImageURI(ImageUri);
        }

    }


    private void Valider(){

        Descrip = inputProductDescription.getText().toString();
        Price = inputProductPrice.getText().toString();
        Pname = inputProductName.getText().toString();


        if(ImageUri == null) {

            Toast.makeText(this, "Veuillez upload une image", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Descrip)) {

            Toast.makeText(this, "Rentrez une description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)) {

            Toast.makeText(this, "Rentrez un prix", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname)) {

            Toast.makeText(this, "Rentrez un nom de produit", Toast.LENGTH_SHORT).show();
        }
        else {

            StoreProduct();

        }

    }

    // firebase storage
    private void StoreProduct() {

        loadingBar.setTitle("Ajout du nouveau produit");
        loadingBar.setMessage("Patientez pendant l'ajout...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = UUID.randomUUID().toString(); // random ID

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg"); // url de l'image dans la db

        final UploadTask uploadTask = filePath.putFile(ImageUri); // classe qui permet de gérer les uploads, on ajoute l'imageUri dans le file path

        // En cas d'erreur d'upload
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show(); // on va renvoyer l'exception dans un toast
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() { // en cas de succès
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "L'image a été upload", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() { // on continue la tache d'upload
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddProductActivity.this, "URL de l'image succes...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Descrip);
        productMap.put("image", downloadImageUrl); // l'image est sauvegarder grace à la méthode ci dessus
        productMap.put("category", categoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);


        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddProductActivity.this, "Produit ajouté avec succès ! ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
