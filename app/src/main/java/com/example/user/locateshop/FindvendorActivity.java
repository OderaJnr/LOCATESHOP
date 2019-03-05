package com.example.user.locateshop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindvendorActivity extends AppCompatActivity {

    private ImageButton Searchbuuton;
    private EditText searchInputText;

    private RecyclerView searchResutLists;
    private TextView  switchsearch;

    private  Toolbar Searchtoolbar;

    private DatabaseReference allUsersDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findvendor);

        Searchtoolbar= (Toolbar)findViewById(R.id.search_page_toolbar);
        setSupportActionBar(Searchtoolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Search");


        allUsersDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users");

        searchResutLists= (RecyclerView) findViewById(R.id.search_result_list);
        searchResutLists.setHasFixedSize(true);
        searchResutLists.setLayoutManager(new LinearLayoutManager(this));


        switchsearch=findViewById(R.id.searchbyproducttv);



        Searchbuuton = (ImageButton)findViewById(R.id.search_people_button);
        searchInputText = (EditText) findViewById(R.id.search_productbtn);





        Searchbuuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String searchboxinput= searchInputText.getText().toString();

                SearchPeopleAndFriends(searchboxinput);

            }
        });


        switchsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent searchintent=new Intent(FindvendorActivity.this,ProductsearchActivity.class);
                startActivity(searchintent);

            }
        });





    }



    private void SearchPeopleAndFriends(String searchboxinput)
    {
        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();

        Query  SearchPeoplefriendQuery =allUsersDatabaseRef.orderByChild("name")
                .startAt(searchboxinput).endAt(searchboxinput + "\uf8ff" );




        FirebaseRecyclerAdapter<FriendsActivity.Findfriends,FindFriendsViewHolder > firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FriendsActivity.Findfriends, FindFriendsViewHolder>
                (
                        FriendsActivity.Findfriends.class,
                        R.layout.all_users_display_layout,
                        FindFriendsViewHolder.class,
                        SearchPeoplefriendQuery


                        )


        {
            @Override
            protected void populateViewHolder(FindFriendsViewHolder viewHolder, FriendsActivity.Findfriends model, final int position)
            {
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setProfileImages(getApplicationContext(),model.getProfileImages());


                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String visit_user_id = getRef(position).getKey();

                        Intent profileintent = new Intent(FindvendorActivity.this, PersonprofileActivity.class);
                        profileintent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileintent);



                    }
                });






            }
        };


        searchResutLists.setAdapter(firebaseRecyclerAdapter);

    }



   public  static  class  FindFriendsViewHolder extends RecyclerView.ViewHolder
   {
      View mview;

       public FindFriendsViewHolder(@NonNull View itemView)
       {

           super(itemView);
           mview = itemView;


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






   }





}
