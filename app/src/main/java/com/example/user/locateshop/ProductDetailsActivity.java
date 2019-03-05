package com.example.user.locateshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.user.locateshop.Prevalent.Prevalent;
import com.example.user.model.Products;
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
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton mycartbtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productName,productDescription;
    private Button addToCartBtn;

    private String productID = "";

    private  Toolbar detailsToolbar;


    private String buyerID, vendorID,buyerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        detailsToolbar= (Toolbar)findViewById(R.id.detailsp_page_toolbar);
        setSupportActionBar(detailsToolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Back");

        productID = getIntent().getStringExtra("pid");


        buyerID = getIntent().getExtras().get("visit_user_id").toString();
        buyerName =  getIntent().getExtras().get("userName").toString();




        mycartbtn= (FloatingActionButton)findViewById(R.id.add_product_to_cart_btn);
        productImage= (ImageView)findViewById(R.id.product_image_details);

        numberButton= (ElegantNumberButton)findViewById(R.id.number_btn);

        addToCartBtn= (Button)findViewById(R.id.add_product_cart_btn);

        productPrice= (TextView)findViewById(R.id.product_price_details);
        productName= (TextView)findViewById(R.id.product_name_details);
        productDescription= (TextView)findViewById(R.id.product_description_details);





        getProductsDetails(productID);




        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                addingToCartList();

            }
        });


        mycartbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ProductDetailsActivity.this,CartMainActivity.class);
                startActivity(intent);

            }
        });


    }

    private void addingToCartList()
    {
        String saveCurrentDate,saveCurrentTime;


        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());


        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(callForTime.getTime());



        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap <String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User view").child("Products").child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                 {
                     cartListRef.child("Admin view").child("Products").child(productID).updateChildren(cartMap);
                     Toast.makeText(ProductDetailsActivity.this, "Product added to cart Successfully ", Toast.LENGTH_SHORT).show();
                 }
                 else
                     {
                         Toast.makeText(ProductDetailsActivity.this, "error Occurred", Toast.LENGTH_SHORT).show();
                     }

            }
        });



//        cartListRef.child("user view").child(Prevalent.currentOnlineUser.getEmail()).child("Products").child(productID)
//                .updateChildren(cartMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>()
//                {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        cartListRef.child("Admin view").child(Prevalent.currentOnlineUser.getEmail()).child("Products").child(productID)
//                                .updateChildren(cartMap)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task)
//                                    {
//                                        if (task.isSuccessful())
//                                        {
//                                            Toast.makeText(ProductDetailsActivity.this, "Added to your cart", Toast.LENGTH_SHORT).show();
//
//                                            Intent intent = new Intent(ProductDetailsActivity.this,ProductsearchActivity.class);
//                                            startActivity(intent);
//                                        }
//
//                                    }
//                                });
//
//
//                    }
//                });
//

    }






    private void getProductsDetails(String productID)
    {
        DatabaseReference productsRef  = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.with(ProductDetailsActivity.this).load(products.getImage()).placeholder(R.drawable.imageicon).into(productImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }



}
