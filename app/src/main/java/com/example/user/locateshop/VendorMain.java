package com.example.user.locateshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VendorMain extends AppCompatActivity {


    private Button Vfood,Vchemist,Velectronic;
    private Button searchbtn,Shouses,Smoney,Srepair,Sfashion,Svisitshop, stimeline;

    private ImageButton  Vgas,Vbeuty;

    private Toolbar mtoolbar;

    private DatabaseReference  UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    private DrawerLayout mDrawerLayout;
    private  NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        mAuth = FirebaseAuth.getInstance();
        currentUserID= mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");



        mtoolbar=findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Home");


        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        mDrawerLayout=findViewById(R.id.drawer_layout);
        mNavigationView=findViewById(R.id.navigationview);









        //Buttons
        Vgas = findViewById(R.id.Vgasbtn);

        searchbtn = findViewById(R.id.searchbtnn);


//
//        Ssalon = findViewById(R.id.Vsalonbtn);
//        Shouses = findViewById(R.id.Vhousesbtn);
//
//        Srepair = findViewById(R.id.Vrepairbtn);
//        Sfashion = findViewById(R.id.Vfashionbtn);





//
//
//        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
//
//            {
//                switch (menuItem.getItemId()) {
//
//                    case R.id.nav_profile:
//
//                        menuItem.setChecked(true);
//                        SendUserToofficialsetting();
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//                    case R.id.nav_home:
//
//                        menuItem.setChecked(true);
//                        displayMessage("HOME");
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//
//                    case R.id.nav_messages:
//
//                        menuItem.setChecked(true);
//                        SendUserToChatActivity();
//                        displayMessage("Messages");
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//                    case R.id.nav_membership:
//
//                        menuItem.setChecked(true);
//                        displayMessage("Profile");
//                        SendUserToProfileActivity();
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//
//                    case R.id.nav_settings:
//
//                        menuItem.setChecked(true);
//                        displayMessage("COMING SOON");
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//                    case R.id.nav_logout:
//
//                        menuItem.setChecked(true);
//                        displayMessage("Signing Out");
//                        updateUserStatus("Offline");
//                        SendUserToMainactivity();
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//
//                    case R.id.nav_searchvendors:
//
//                        menuItem.setChecked(true);
//                        displayMessage("search vendors");
//                        SendUserTosearchvendorsActivity();
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//
//                    case R.id.nav_users:
//
//                        menuItem.setChecked(true);
//                        displayMessage("Vendors");
//                        SendUserToFriendActivity();
//                        mDrawerLayout.closeDrawers();
//                        break;
//
//
//                }
//
//                return false;
//
//
//
//            }
//        });





        Vgas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(VendorMain.this,VgasActivity .class);
                startActivity(intent);
                return;

            }
        });


        Svisitshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(VendorMain.this,VendorPost .class);
                startActivity(intent);
                return;

            }
        });



        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(VendorMain.this,FindvendorActivity .class);
                startActivity(intent);
                return;

            }
        });





    }


    public void updateUserStatus(String state)

    {
        String saveCurrentDate,saveCurrentTime;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());


        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(callForTime.getTime());


        Map currentStateMap =  new HashMap();
        currentStateMap.put("time", saveCurrentTime);
        currentStateMap.put("date", saveCurrentDate);
        currentStateMap.put("type", state);



      UsersRef.child(currentUserID).child("userState")
              .updateChildren(currentStateMap);





    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserStatus("Online");
    }















    private void displayMessage(String message)
    {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    //Intents



    private void SendUserToMainactivity()
    {
        Intent mainintent=new Intent(VendorMain.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }



    private void SendUserTosearchvendorsActivity()
    {
        Intent searchintent=new Intent(VendorMain.this,FindvendorActivity.class);
        startActivity(searchintent);
        finish();
    }


    private void SendUserToofficialsetting()
    {
        Intent loginIntent=new Intent(VendorMain.this,VendorSetting.class);
        startActivity(loginIntent);
        finish();
    }


    private void SendUserToProfileActivity()
    {
        Intent profileintent=new Intent(VendorMain.this,ProfileActivity.class);
        startActivity(profileintent);
        finish();
    }


    private void SendUserToChatActivity()
    {
//        Intent chatintent=new Intent(VendorMain.this,ChatActivity.class);
//        startActivity(chatintent);
//        finish();
    }



    private void SendUserToFriendActivity()
    {
        Intent friendintent=new Intent(VendorMain.this,FriendsActivity.class);
        startActivity(friendintent);
        finish();
    }





}
