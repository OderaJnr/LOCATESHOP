package com.example.user.locateshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button WelcomeDriverButton,WelcomeCustomerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WelcomeCustomerButton=findViewById(R.id.user_welcome_btn);
        WelcomeDriverButton=findViewById(R.id.driver_welcome_btn);

        startService(new Intent(MainActivity.this,onappkilled.class));


        WelcomeCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent loginregistercustomerintent=new Intent(MainActivity.this,CustomerLogin.class);
                startActivity(loginregistercustomerintent);

            }
        });
        WelcomeDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent loginregistercustomerintent=new Intent(MainActivity.this,VendorLogin.class);
                startActivity(loginregistercustomerintent);

            }
        });

    }




}

