package com.example.user.locateshop;

import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonprofileActivity extends AppCompatActivity {

    private TextView mNamefield,mUserstatus,mPhonefield,mEmailfield,mWebsitefield,mLocationfield;
    private CircleImageView userProfileImage;
    private Button sendfriendRequest,cancelrequest;

    private Toolbar personProfiletoolbar;


   private DatabaseReference  profileUserRef, Usersref,FriendRequestRef,FriendsRef;
   private FirebaseAuth  mAuth;

   private  String  senderUserId, receiverUserId,Currentstate;
   private  String  saveCurrentDate;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personprofile);

        personProfiletoolbar= (Toolbar)findViewById(R.id.personprofile_page_toolbar);
        setSupportActionBar(personProfiletoolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Vendor's Profile");


        mAuth =  FirebaseAuth.getInstance();
        senderUserId= mAuth.getCurrentUser().getUid();

        receiverUserId= getIntent().getExtras().get("visit_user_id").toString();
        Usersref  = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestRef  = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        FriendsRef     = FirebaseDatabase.getInstance().getReference().child("Friends");





        InitializeFields();


        Usersref.child(receiverUserId).addValueEventListener(new ValueEventListener() {
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


                    Picasso.with(PersonprofileActivity.this).load(myProfileimage).placeholder(R.drawable.profile).into(userProfileImage);

                    mNamefield.setText(myUsername);
                    mUserstatus.setText(":" + myStatus);
                    mLocationfield.setText(":" + myLocation);
                    mEmailfield.setText(":" + myemail);
                    mPhonefield.setText(":" + myPhone);
                    mWebsitefield.setText(":" + myWebsite);




                    MaintananceofButton();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        cancelrequest.setVisibility(View.INVISIBLE);
        cancelrequest.setEnabled(false);





        if (!senderUserId.equals(receiverUserId))
         {
             sendfriendRequest.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v)
                 {
                     sendfriendRequest.setEnabled(false);

                     if (Currentstate.equals("not_friends"))
                     {
                         sendFriendRequestToperson();
                     }

                     if (Currentstate.equals("request_sent"))
                     {
                         CancelFriendRequest();
                     }

                     if (Currentstate.equals("request_received"))
                     {
                       AcceptFriendRequest();
                     }

                     if (Currentstate.equals("friends"))
                     {
                         UnfriendExistingFriend();
                     }



                 }
             });

         }

          else
              {
                  cancelrequest.setVisibility(View.INVISIBLE);
                  sendfriendRequest.setVisibility(View.INVISIBLE);


              }




    }

    private void UnfriendExistingFriend()
    {

        FriendsRef.child(senderUserId).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            FriendsRef.child(receiverUserId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                sendfriendRequest.setEnabled(true);
                                                Currentstate = "not_friends";
                                                sendfriendRequest.setText("SEND FOLLOW REQUEST");
                                                cancelrequest.setVisibility(View.INVISIBLE);
                                                cancelrequest.setEnabled(false );

                                            }


                                        }
                                    });
                        }

                    }
                });

    }





    private void AcceptFriendRequest()
    {
        Calendar  callfordate = Calendar.getInstance();
        SimpleDateFormat currentDate =new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(callfordate.getTime());

        FriendsRef.child(senderUserId).child(receiverUserId).child("date").setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {

                            FriendsRef.child(receiverUserId).child(senderUserId).child("date").setValue(saveCurrentDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {

                                                FriendRequestRef.child(senderUserId).child(receiverUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    FriendRequestRef.child(receiverUserId).child(senderUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        sendfriendRequest.setEnabled(true);
                                                                                        Currentstate = "friends";
                                                                                        sendfriendRequest.setText("Unfollow");
                                                                                        cancelrequest.setVisibility(View.INVISIBLE);
                                                                                        cancelrequest.setEnabled(false );

                                                                                    }


                                                                                }
                                                                            });
                                                                }

                                                            }
                                                        });


                                            }

                                        }
                                    });

                        }

                    }
                });



    }









    private void CancelFriendRequest()

        {

            FriendRequestRef.child(senderUserId).child(receiverUserId)
                  .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                FriendRequestRef.child(receiverUserId).child(senderUserId)
                                      .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    sendfriendRequest.setEnabled(true);
                                                    Currentstate = "not_friends";
                                                    sendfriendRequest.setText("SEND FOLLOW REQUEST");
                                                    cancelrequest.setVisibility(View.INVISIBLE);
                                                    cancelrequest.setEnabled(false );

                                                }


                                            }
                                        });
                            }

                        }
                    });

        }






    private void MaintananceofButton()
    {



        FriendRequestRef.child(senderUserId)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild(receiverUserId))
                  {
                      String request_type =  dataSnapshot.child(receiverUserId).child("request_type").getValue().toString();

                      if (request_type.equals("sent"))
                        {
                            Currentstate = "request_sent";
                            sendfriendRequest.setText("CANCEL FOLLOW REQUEST");


                            cancelrequest.setVisibility(View.INVISIBLE);
                            cancelrequest.setEnabled(false );
                        } else if (request_type.equals("received"))

                         {
                             Currentstate = "request_received";
                             sendfriendRequest.setText("Accept Follow Request");

                             cancelrequest.setVisibility(View.VISIBLE);
                             cancelrequest.setEnabled(true);

                             cancelrequest.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v)
                                 {
                                     CancelFriendRequest();

                                 }
                             });


                         }

                  } else
                      {
                          FriendsRef.child(senderUserId)
                          .addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot)
                              {if (dataSnapshot.hasChild(receiverUserId))
                               {
                                   Currentstate = "friends";
                                   sendfriendRequest.setText("Unfollow");
                                   cancelrequest.setVisibility(View.INVISIBLE);
                                   cancelrequest.setEnabled(false);
                               }

                              }

                              @Override
                              public void onCancelled(DatabaseError databaseError)
                              {

                              }
                          });
                      }


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }







    private void sendFriendRequestToperson()
    {
       FriendRequestRef.child(senderUserId).child(receiverUserId)
       .child("request_type").setValue("sent")
       .addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task)
           {
               if (task.isSuccessful())
                {
                    FriendRequestRef.child(receiverUserId).child(senderUserId)
                            .child("request_type").setValue("received")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        sendfriendRequest.setEnabled(true);
                                        Currentstate = "request_sent";
                                        sendfriendRequest.setText("CANCEL FOLLOW REQUEST");

                                        cancelrequest.setVisibility(View.INVISIBLE);
                                        cancelrequest.setEnabled(false );

                                    }


                                }
                            });
                }

           }
       });

    }






    private void InitializeFields()
    {

        mNamefield=findViewById(R.id.person_full_name);
        mUserstatus =findViewById(R.id.person_profile_status);
        mPhonefield=findViewById(R.id.person_Phone);
        mEmailfield=findViewById(R.id.person_Email);
        mWebsitefield=findViewById(R.id.person_Website);
        mLocationfield=findViewById(R.id.person_Location);

        sendfriendRequest =findViewById(R.id.send_friend_requestbtn);
        cancelrequest= findViewById(R.id.cancelrequest);

        Currentstate  = "not_friends";

        userProfileImage=findViewById(R.id.person_profile_pic);

    }


}
