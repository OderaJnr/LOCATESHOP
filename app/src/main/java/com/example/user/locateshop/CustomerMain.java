package com.example.user.locateshop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.model.Products;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class CustomerMain extends AppCompatActivity {


    private Button Ugas,Ubeuty,Ufood,Uchemist,Uelectronic;
    private Button Usalon,Uhouses,Umoney,Urepair,Ufashion,Ubodaboda;

    private Toolbar mtoolbar;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);


//        mtoolbar=findViewById(R.id.main_page_toolbar);
//        setSupportActionBar(mtoolbar);
//        final ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout=findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigationview);





        //Buttons
        Ugas = findViewById(R.id.ugasbtn);
        Ubodaboda= findViewById(R.id.bodaboda);





        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)

            {
                switch (menuItem.getItemId()) {

//                    case R.id.nav_profile:
//
//                        menuItem.setChecked(true);
//                        SendUserToofficialsetting();
//                        mDrawerLayout.closeDrawers();
//                        break;

                    case R.id.nav_home:

                        menuItem.setChecked(true);
                        displayMessage("HOME");
                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.nav_messages:

                        menuItem.setChecked(true);
                        displayMessage("COMING SOON");
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_membership:

                        menuItem.setChecked(true);
                        displayMessage("COMING SOON");
                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.nav_settings:

                        menuItem.setChecked(true);
                        displayMessage("COMING SOON");
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_logout:

                        menuItem.setChecked(true);
                        displayMessage("Signing Out");
                        SendUserToMainactivity();
                        mDrawerLayout.closeDrawers();
                        break;

                }

                return false;



            }
        });



        Ugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(CustomerMain.this,UsergasActivity .class);
                startActivity(intent);
                return;

            }
        });


        Ubodaboda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(CustomerMain.this,ProductsearchActivity .class);
                startActivity(intent);

            }
        });



//
//        Medical.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent=new Intent(UserchooseActivity.this,UsermedicalmapActivity .class);
//                startActivity(intent);
//                return;
//
//            }
//        });
//
//
//        Police.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent=new Intent(UserchooseActivity.this,UserpolicemapActivity .class);
//                startActivity(intent);
//                return;
//
//            }
//        });
//
//
//        Fire.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent=new Intent(UserchooseActivity.this,UserfiremapsActivity .class);
//                startActivity(intent);
//                return;
//            }
//        });
//




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
        Intent mainintent=new Intent(CustomerMain.this,MainActivity.class);
        startActivity(mainintent);
        finish();
    }


//    private void SendUserToofficialsetting()
//    {
//        Intent loginIntent=new Intent(CustomerMain.this,UsersettingActivity.class);
//        startActivity(loginIntent);
//        finish();
//    }



}
