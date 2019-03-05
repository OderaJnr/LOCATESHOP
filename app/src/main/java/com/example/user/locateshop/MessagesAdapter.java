package com.example.user.locateshop;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>
{
   private List<Messages> usermessagesList;
   private FirebaseAuth mAuth;
   private DatabaseReference usersDatabaseRef;




   public  MessagesAdapter (List<Messages>usermessagesList)
   {
       this.usermessagesList = usermessagesList;
   }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {

        public TextView SenderMessagegText,ReceiverMessagegText;
        public CircleImageView receiverProfileImage;

        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            SenderMessagegText = itemView.findViewById(R.id.sender_message_text);
            ReceiverMessagegText = itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = itemView.findViewById(R.id.message_profile_image);

        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View V = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meesage_layout_of_users,parent,false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(V);


    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int position)
    {
        String messageSenderID = mAuth.getCurrentUser().getUid();
        Messages messages = usermessagesList.get(position);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        usersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        usersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {if (dataSnapshot.exists())
            {
                String image =dataSnapshot.child("profileImages").getValue().toString();

                Picasso.with(messageViewHolder.receiverProfileImage.getContext()).load(image)
                        .placeholder(R.drawable.profile).into(messageViewHolder.receiverProfileImage);
            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        if (fromMessageType.equals("text"))

        {
            messageViewHolder.ReceiverMessagegText.setVisibility(View.INVISIBLE);
            messageViewHolder.receiverProfileImage.setVisibility(View.INVISIBLE);

            if (fromUserID.equals(messageSenderID))
             {
                 messageViewHolder.SenderMessagegText.setBackgroundResource(R.drawable.sender_message_background);
                 messageViewHolder.SenderMessagegText.setTextColor(Color.WHITE);
                 messageViewHolder.SenderMessagegText.setGravity(Gravity.LEFT);
                 messageViewHolder.SenderMessagegText.setText(messages.getMessage());


             }
             else
                 {
                     messageViewHolder.SenderMessagegText.setVisibility(View.INVISIBLE);

                     messageViewHolder.ReceiverMessagegText.setVisibility(View.VISIBLE);
                     messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);


                     messageViewHolder.ReceiverMessagegText.setBackgroundResource(R.drawable.receiver_message_text_backgrud);
                     messageViewHolder.ReceiverMessagegText.setTextColor(Color.WHITE);
                     messageViewHolder.ReceiverMessagegText.setGravity(Gravity.LEFT);
                     messageViewHolder.ReceiverMessagegText.setText(messages.getMessage());




                 }

        }



    }

    @Override
    public int getItemCount()
    {
        return usermessagesList.size();
    }
}
