package com.example.user.locateshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class AddnewProductActivity extends AppCompatActivity {

    private  String Categoryname;
    private Button AddNewproductButton;
    private StorageReference ProductImagesRefrence;
    private EditText Inputproductname,inputProductDescription,inputproductPrice;

    private ImageView selectproductimage,selectproductimage2,selectproductimage3;

    private ProgressDialog loadingBar;
    private static final int Gallery_Pick = 1;


    private String Price,Name,Description;
    private Uri ImageUri;
    private DatabaseReference productsRef;

    private Toolbar Addproducttoolbar;
    private String saveCurrentDate, saveCurrentTime, productRandomKey, downloadImageUrl, current_user_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_product);




        Addproducttoolbar= (Toolbar)findViewById(R.id.addproduct_page_toolbar);
        setSupportActionBar(Addproducttoolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Add New Product");



        ProductImagesRefrence = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        Categoryname = getIntent().getExtras().get("category").toString();


        AddNewproductButton =  findViewById(R.id.add_product_button);
        Inputproductname   = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputproductPrice  = findViewById(R.id.product_price);

        loadingBar= new ProgressDialog(this);

        selectproductimage= findViewById(R.id.select_product_image);

        selectproductimage2= findViewById(R.id.select_product_image2);
        selectproductimage3= findViewById(R.id.select_product_image3);





        selectproductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                OpenGallery();
            }
        });

        selectproductimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                OpenGallery();
            }
        });


        selectproductimage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                OpenGallery();
            }
        });



        AddNewproductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validatepostdetails();

            }
        });


    }

    private void validatepostdetails()


    {   Name =  Inputproductname.getText().toString();
        Description =  inputProductDescription.getText().toString();
        Price =  inputproductPrice.getText().toString();


        if (ImageUri==null)
        {
            Toast.makeText(this, "Please select a photo ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please enter your product details", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Name))
        {
            Toast.makeText(this, "Please enter your product Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please enter your product Price", Toast.LENGTH_SHORT).show();
        }



        else
        {
            StoreProductInformation();

        }




    }

    private void StoreProductInformation()

    {
         loadingBar.setTitle("Adding New Product");
         loadingBar.setMessage("Please Wait");
         loadingBar.setCanceledOnTouchOutside(false);
         loadingBar.show();

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRefrence.child("Post Images").child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();

                Toast.makeText(AddnewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

                Toast.makeText(AddnewProductActivity.this, "Product Image Uploaded successfully ", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())

                        {
                            throw task.getException();
                        }
                        downloadImageUrl=  filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {

                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AddnewProductActivity.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }

                    }
                });

            }
        });




    }

    private void saveProductInfoToDatabase()
    {



        HashMap<String,Object> productMap  = new HashMap<>();


        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",Categoryname);
        productMap.put("price",Price);
        productMap.put("pname",Name);


     productsRef.child(productRandomKey).updateChildren(productMap)
     .addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task)
         {
             if (task.isSuccessful())
             {


                 Intent intent = new Intent(AddnewProductActivity.this, AdmincategoryActivity .class);
                 startActivity(intent);


                 loadingBar.dismiss();

                 Toast.makeText(AddnewProductActivity.this, "Product added Successfully", Toast.LENGTH_SHORT).show();
                 loadingBar.dismiss();
             }
             else
                 {
                     String message = task.getException().toString();

                     Toast.makeText(AddnewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                     loadingBar.dismiss();

                 }

         }
     });


    }


    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);


    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Pick && resultCode == RESULT_OK && data!= null)
        {
            ImageUri = data.getData();
            selectproductimage.setImageURI(ImageUri);


        }

    }


}
