package com.example.user.locateshop;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartMainActivity extends AppCompatActivity {


    private Toolbar Chattoolbar;
    private RecyclerView userCartList;
    private  final List<Messages> messagesList =new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private  MessagesAdapter messagesAdapter;

    private String BuyerID, BuyerName, SellerID,SellerName,saveCurrentDate,saveCurrentTime;

    private TextView Buyername,Buyerlocation,Buyercontact,Sellername,SellerLocation,Sellercontacts;


    private CircleImageView sellerProfileImage;
    private TextView  userLocation,userContact,userOrdernumber;

    private DatabaseReference RootRef,UserRef;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_main);


        mAuth = FirebaseAuth.getInstance();
        BuyerID = mAuth.getCurrentUser().getUid();


        RootRef = FirebaseDatabase.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        SellerID = getIntent().getExtras().get("visit_user_id").toString();
        SellerName =  getIntent().getExtras().get("userName").toString();



        InitializeFields();

//        DisplayReceiverInfo();
//



    }

    private void InitializeFields()

    {

        Chattoolbar= (Toolbar) findViewById(R.id.cart_page_toolbar);
        setSupportActionBar(Chattoolbar);

        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater  = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view  = layoutInflater.inflate(R.layout.cart_custom_bar, null);
        actionBar.setCustomView(action_bar_view);





        Sellername = (TextView) findViewById(R.id.cartcustom_profile_name);
        sellerProfileImage = (CircleImageView)findViewById(R.id.cartcustom_profile_image);
        userLocation= findViewById(R.id.cartcustom_user_location);
        userOrdernumber= findViewById(R.id.cartcustom_user_ordernumber);
        userContact= findViewById(R.id.cartcustom_user_phonenumber);



        messagesAdapter = new MessagesAdapter(messagesList);
        userCartList =  (RecyclerView)findViewById(R.id.cart_list_users);

        linearLayoutManager = new LinearLayoutManager(this);
        userCartList.setHasFixedSize(true);
        userCartList.setLayoutManager(linearLayoutManager);
        userCartList.setAdapter(messagesAdapter);






    }
}
