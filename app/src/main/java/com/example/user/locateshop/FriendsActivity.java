package com.example.user.locateshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView myfriendlist;

    private Toolbar FriendsToolbar;

    private DatabaseReference  FriendsRef,UsersRef;
    private FirebaseAuth mAuth;

    private String online_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        FriendsToolbar= (Toolbar)findViewById(R.id.followers_page_toolbar);
        setSupportActionBar(FriendsToolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Followers");



        mAuth  =  FirebaseAuth.getInstance();
        online_user_id =mAuth.getCurrentUser().getUid();
        FriendsRef=  FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
        UsersRef  = FirebaseDatabase.getInstance().getReference().child("Users");


        myfriendlist = (RecyclerView)findViewById(R.id.friend_list);
        myfriendlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myfriendlist.setLayoutManager(linearLayoutManager);




        DisplayAllFriends();



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



        UsersRef.child(online_user_id).child("userState")
                .updateChildren(currentStateMap);




    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserStatus("Online");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        updateUserStatus("Offline");

    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        updateUserStatus("Offline");

    }

    private void DisplayAllFriends()
    {
        Toast.makeText(this, "searching", Toast.LENGTH_SHORT).show();

        FirebaseRecyclerAdapter<Messages.Friends,FriendsViewHolder>firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Messages.Friends, FriendsViewHolder>
                (
                        Messages.Friends.class,
                        R.layout.all_users_display_layout,
                        FriendsViewHolder.class,
                        FriendsRef

                )
        {

            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, Messages.Friends model, int position)
            {
                viewHolder.setDate(model.getDate());

                final String userIDS = getRef(position).getKey();

                UsersRef.child(userIDS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            final  String userName =dataSnapshot.child("name").getValue().toString();

                            final  String userStatus =dataSnapshot.child("status").getValue().toString();

                            final  String profileimage =dataSnapshot.child("profileImages").getValue().toString();

                            viewHolder.setName(userName);
                            viewHolder.setStatus(userStatus);
                            viewHolder.setProfileImages(getApplicationContext(),profileimage);

                           viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v)
                               {
                                   CharSequence options[] = new CharSequence[]
                                           {
                                                   userName + " 's Profile",
                                                   "send message"

                                           };
                                   AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                                   builder.setTitle("Select Option");

                                   builder.setItems(options, new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which)
                                       {

                                           if (which==0)
                                       {
                                           Intent profileintent=new Intent(FriendsActivity.this,PersonprofileActivity.class);
                                           profileintent.putExtra("visit_user_id", userIDS);
                                           startActivity(profileintent);

                                       }

                                       if (which==1)

                                        {

                                            Intent messageintent=new Intent(FriendsActivity.this,ChatActivity.class);
                                            messageintent.putExtra("visit_user_id", userIDS);
                                            messageintent.putExtra("userName", userName);
                                            startActivity(messageintent);

                                        }


                                       }
                                   });

                                   builder.show();


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
        };

        myfriendlist.setAdapter(firebaseRecyclerAdapter);

    }



    public static class FriendsViewHolder extends RecyclerView.ViewHolder
   {
       View mview;

       public FriendsViewHolder(@NonNull View itemView)
       {
           super(itemView);

           mview=itemView;
       }

       public void setProfileImages(Context ctx, String profileImages)
       {
           CircleImageView myImage = (CircleImageView)mview.findViewById(R.id.all_users_profile_image);
           Picasso.with(ctx).load(profileImages).placeholder(R.drawable.profile).into(myImage);


       }


       public void setName(String name)
       {
           TextView myname = (TextView) mview.findViewById(R.id.all_users_full_name);
           myname.setText(name);
       }

       public void setStatus(String status)
       {
           TextView mystatus = (TextView) mview.findViewById(R.id.all_users_status);
           mystatus.setText(status);

       }

       public void setDate(String date)
   {
       TextView friendsdate = (TextView) mview.findViewById(R.id.all_users_date);
       friendsdate.setText("Follower since :" + date);

   }
   }












    public static class Findfriends
    {
        public  String name,profileImages,status;

        public Findfriends()
        {

        }




        public Findfriends(String name, String profileImages, String status)

        {
            this.name = name;
            this.profileImages = profileImages;
            this.status = status;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileImages() {
            return profileImages;
        }

        public void setProfileImages(String profileImages) {
            this.profileImages = profileImages;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
