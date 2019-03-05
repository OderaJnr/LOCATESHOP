package com.example.user.locateshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VendorLogin extends AppCompatActivity {

    private Button Driverloginbtn,DriverRegisterbtn;
    private TextView DriverRegisterlink,Driverstatus;
    private EditText Emaildriver,Passworddriver;

    private FirebaseAuth mAuth;
    private DatabaseReference DriverDatabaseRef;
    private String onlineDriverID;

    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);



        Driverloginbtn=findViewById(R.id.login_driver);
        DriverRegisterbtn=findViewById(R.id.driver_reg_button);
        Driverstatus=findViewById(R.id.driver_status);
        DriverRegisterlink=findViewById(R.id.driver_register_link2);
        Emaildriver=findViewById(R.id.email_driver);
        Passworddriver=findViewById(R.id.password_driver);


        loadingbar=new ProgressDialog(this);



        mAuth=FirebaseAuth.getInstance();

        mAuth=FirebaseAuth.getInstance();

        DriverRegisterbtn.setVisibility(View.INVISIBLE);
        DriverRegisterbtn.setEnabled(false);

        DriverRegisterlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Driverloginbtn.setVisibility(View.INVISIBLE);
                DriverRegisterlink.setVisibility(View.INVISIBLE);
                Driverstatus.setText("REGISTER VENDOR");
                DriverRegisterbtn.setVisibility(View.VISIBLE);
                DriverRegisterbtn.setEnabled(true);



            }
        });


        DriverRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email=Emaildriver.getText().toString();
                String password=Passworddriver.getText().toString();

                RegisterDriver(email,password);


            }
        });

        Driverloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { String email=Emaildriver.getText().toString();
                String password=Passworddriver.getText().toString();

                LoginDriver(email,password);

            }
        });






    }

    private void LoginDriver(String email, String password)
    {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(VendorLogin.this,"PLease enter your email",Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(VendorLogin.this,"PLease enter your password",Toast.LENGTH_SHORT).show();
        }else
        {
            loadingbar.setTitle("SIGNING IN AS A VENDOR");
            loadingbar.setMessage("please wait..");
            loadingbar.show();


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Intent driverIntent=new Intent(VendorLogin.this,AdmincategoryActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(VendorLogin.this,"Vendor logged in successfully ",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();


                    }

                    else
                    {
                        Toast.makeText(VendorLogin.this,"Login Unsuccessful..please try again",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }

                }
            });
        }



    }


    private void RegisterDriver(final String email, String password)
    {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(VendorLogin.this,"PLease enter your email",Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(VendorLogin.this,"PLease enter your password",Toast.LENGTH_SHORT).show();
        }else
        {
            loadingbar.setTitle("REGISTERING AS A VENDOR ");
            loadingbar.setMessage("please wait..");
            loadingbar.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {


                        onlineDriverID=mAuth.getCurrentUser().getUid();
                        DriverDatabaseRef= FirebaseDatabase.getInstance()
                                .getReference().child("Users").child("Vendors").child(onlineDriverID).child("name");

                        DriverDatabaseRef.setValue(email);

                        Intent driverIntent=new Intent(VendorLogin.this,AdmincategoryActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(VendorLogin.this," Vendor registered successfully ",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();



                    }

                    else
                    {
                        Toast.makeText(VendorLogin.this,"Registration Unsuccessful..please try again",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }

                }
            });
        }



    }
}








