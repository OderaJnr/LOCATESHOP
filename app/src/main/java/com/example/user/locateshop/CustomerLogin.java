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

public class CustomerLogin extends AppCompatActivity {

    private Button Customerloginbtn;
    private Button CustomerRegisterbtn;
    private TextView CustomerRegisterlink;
    private TextView Customerstatus;
    private EditText Emailcustomer,Passwordcustomer;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    private DatabaseReference CustomerDatabaseRef;
    private String onlineCustomerID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);


        mAuth=FirebaseAuth.getInstance();

        Customerloginbtn=findViewById(R.id.login_customer);
        CustomerRegisterbtn=findViewById(R.id.customer_reg_button);
        Customerstatus=findViewById(R.id.customer_status);
        CustomerRegisterlink=findViewById(R.id.customer_register_link);
        Emailcustomer=findViewById(R.id.email_customer);
        Passwordcustomer=findViewById(R.id.password_customer);
        loadingbar=new ProgressDialog(this);








        CustomerRegisterbtn.setVisibility(View.INVISIBLE);
        CustomerRegisterbtn.setEnabled(false);

        CustomerRegisterlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Customerloginbtn.setVisibility(View.INVISIBLE);
                CustomerRegisterlink.setVisibility(View.INVISIBLE);
                Customerstatus.setText("REGISTER AS A USER");


                CustomerRegisterbtn.setVisibility(View.VISIBLE);
                CustomerRegisterbtn.setEnabled(true);



            }
        });

        CustomerRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email=Emailcustomer.getText().toString();
                String password=Passwordcustomer.getText().toString();

//                register customer method

                RegisterCustomer(email,password);


            }
        });


        Customerloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email=Emailcustomer.getText().toString();
                String password=Passwordcustomer.getText().toString();

                SignInCustomer(email,password);


            }
        });



    }

    private void SignInCustomer(String email, String password)
    { if (TextUtils.isEmpty(email))
    {
        Toast.makeText(CustomerLogin.this,"PLease enter your email",Toast.LENGTH_SHORT).show();
    }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(CustomerLogin.this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }else
        {
            loadingbar.setTitle("SIGNING IN AS A USER");
            loadingbar.setMessage("please wait..");
            loadingbar.show();


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {

                        Intent customerIntent=new Intent(CustomerLogin.this,AdmincategoryActivity.class);
                        startActivity(customerIntent);

                        Toast.makeText(CustomerLogin.this,"User signed in successfully ",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();



                    }

                    else
                    {
                        Toast.makeText(CustomerLogin.this,"Sign in Unsuccessful..please try again",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }

                }
            });
        }

    }





    private void RegisterCustomer(String email, String password)
    { if (TextUtils.isEmpty(email))
    {
        Toast.makeText(CustomerLogin.this,"Please enter your email",Toast.LENGTH_SHORT).show();
    }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(CustomerLogin.this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }else
        {
            loadingbar.setTitle("REGISTERING AS A MEMBER");
            loadingbar.setMessage("please wait..");
            loadingbar.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())

                    {onlineCustomerID=mAuth.getCurrentUser().getUid();
                        CustomerDatabaseRef= FirebaseDatabase.getInstance().getReference()
                                .child("Customers").child(onlineCustomerID);

                        CustomerDatabaseRef.setValue(true);

                        Intent driverIntent=new Intent(CustomerLogin.this, AdmincategoryActivity.class);
                        startActivity(driverIntent);


                        Toast.makeText(CustomerLogin.this,"Member registered successfully ",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();


                    }

                    else
                    {
                        Toast.makeText(CustomerLogin.this,"Registration Unsuccessful..please try again",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }

                }
            });
        }



    }
}


