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
import android.widget.ImageView;
import android.widget.Toast;

public class AdmincategoryActivity extends AppCompatActivity
{
    private ImageView cookinggas,vacanthouses,groceries,pharmacy,generalshop,foodjoint,salon,fashion,mpesa,boutique;
    private Button searchbtn;

    private Toolbar mtoolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincategory);


        mtoolbar=findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Home");
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);




        cookinggas =  findViewById(R.id.gasimage);
        vacanthouses =  findViewById(R.id.houses);
        groceries =  findViewById(R.id.groceries);
        pharmacy =  findViewById(R.id.pharmacy);
        generalshop =  findViewById(R.id.generalshop);
        salon =  findViewById(R.id.salon);
        fashion =  findViewById(R.id.fashion);
        mpesa =  findViewById(R.id.mpesa);
        boutique =  findViewById(R.id.boutique);
        foodjoint = findViewById(R.id.foodjoints);


        searchbtn  = findViewById(R.id.searchbtnn);


        mDrawerLayout=findViewById(R.id.admin_drawer);
        mNavigationView=findViewById(R.id.navigationview);







        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)

            {
                switch (menuItem.getItemId()) {

                    case R.id.nav_profile:

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_home:

                        menuItem.setChecked(true);
                        displayMessage("HOME");
                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.nav_messages:

                        menuItem.setChecked(true);
                        displayMessage("Messages");
                        SendUserToChatActivity();
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_membership:

                        menuItem.setChecked(true);
                        displayMessage("Profile");
                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.nav_settings:

                        menuItem.setChecked(true);
                        SendUserToofficialsetting();
                        displayMessage("COMING SOON");
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_logout:

                        menuItem.setChecked(true);
                        displayMessage("Signing Out");
                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.nav_searchvendors:

                        menuItem.setChecked(true);
                        SendUserTosearchActivity();
                        displayMessage("search vendors");
                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.nav_users:

                        menuItem.setChecked(true);
                        displayMessage("Vendors");
                        mDrawerLayout.closeDrawers();
                        break;


                }

                return false;



            }
        });





        cookinggas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Gasvendors");
                startActivity(intent);

            }
        });





        vacanthouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Vacanthouses");
                startActivity(intent);

            }
        });


        groceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Groceries");
                startActivity(intent);

            }
        });


        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Pharmaceuticals");
                startActivity(intent);

            }
        });

        generalshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Generalshops");
                startActivity(intent);

            }
        });

        salon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Salon");
                startActivity(intent);

            }
        });

        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "FashionANDdesigns");
                startActivity(intent);

            }
        });

        mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "FinancialServices");
                startActivity(intent);

            }
        });

        boutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Boutique");
                startActivity(intent);

            }
        });

        foodjoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,AddnewProductActivity .class);
                intent.putExtra("category", "Foodjoints");
                startActivity(intent);

            }
        });


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdmincategoryActivity.this,FindvendorActivity .class);
                startActivity(intent);

            }
        });


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





    private void displayMessage(String message)
    {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }






//    intents


    private void SendUserToofficialsetting()
    {
        Intent loginIntent=new Intent(AdmincategoryActivity.this,VendorSetting.class);
        startActivity(loginIntent);
        finish();
    }




    private void SendUserTosearchActivity()
    {
        Intent searchintent=new Intent(AdmincategoryActivity.this,FindvendorActivity.class);
        startActivity(searchintent);
        finish();
    }


    private void SendUserToChatActivity()
    {
        Intent usersintent=new Intent(AdmincategoryActivity.this,FriendsActivity.class);
        startActivity(usersintent);
        finish();
    }



}
