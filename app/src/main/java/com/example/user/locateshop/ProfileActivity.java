package com.example.user.locateshop;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView mNamefield,mUserstatus,mPhonefield,mEmailfield,mWebsitefield,mLocationfield;
    private CircleImageView userProfileImage;

    private DatabaseReference profileUserRef;
    private FirebaseAuth  mAuth;

    private String  currentUserId;

    private Toolbar profiletoolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profiletoolbar= (Toolbar)findViewById(R.id.profile_page_toolbar);
        setSupportActionBar(profiletoolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Profile");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);



        mNamefield=findViewById(R.id.my_profile_full_name);
        mUserstatus =findViewById(R.id.my_profile_status);
        mPhonefield=findViewById(R.id.my_Phone);
        mEmailfield=findViewById(R.id.my_Email);
        mWebsitefield=findViewById(R.id.my_Website);
        mLocationfield=findViewById(R.id.my_Location);

        userProfileImage=findViewById(R.id.my_profile_pic);



        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String myProfileimage =dataSnapshot.child("profileImages").getValue().toString();
                    String myUsername =dataSnapshot.child("name").getValue().toString();
                    String myStatus =dataSnapshot.child("status").getValue().toString();
                    String myLocation =dataSnapshot.child("location").getValue().toString();
                    String myemail =dataSnapshot.child("email").getValue().toString();
                    String myPhone =dataSnapshot.child("phone").getValue().toString();
                    String myWebsite =dataSnapshot.child("website").getValue().toString();


                    Picasso.with(ProfileActivity.this).load(myProfileimage).placeholder(R.drawable.profile).into(userProfileImage);

                    mNamefield.setText(myUsername);
                    mUserstatus.setText("Selling/Offering: " + myStatus);
                    mLocationfield.setText("In:  " + myLocation);
                    mEmailfield.setText("Email:  " + myemail);
                    mPhonefield.setText("Phone number:  " + myPhone);
                    mWebsitefield.setText("Website:  " + myWebsite);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
