package com.example.user.locateshop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VendorSetting extends AppCompatActivity {


    private  static  final int Gallery_Pick=1;
    private EditText mNamefield,mStatusfield,mPhonefield,mEmailfield,mWebsitefield,mLocationfield;
    private Button mSave,mBack;

    private FirebaseAuth mAuth;
    private DatabaseReference mDriverDatabase;

    private String userID;
    private String MName;
    private  String  MPhone;
    private  String mEmail;
    private  String mLoc;
    private  String mWeb;
    private  String mStatus;


    private ImageView mProfileimage;

    private Uri resultUri;
    private  String mProfileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_setting);


        mNamefield=findViewById(R.id.vname);
        mPhonefield=findViewById(R.id.vcontact);
        mEmailfield=findViewById(R.id.vemail);
        mWebsitefield=findViewById(R.id.vwebsite);
        mLocationfield=findViewById(R.id.vlocation);
        mStatusfield =findViewById(R.id.vstatus);




        mSave=findViewById(R.id.vsave);
        mBack=findViewById(R.id.vback);




        mProfileimage=findViewById(R.id.vprofile_image);


        mAuth=FirebaseAuth.getInstance();
        userID=mAuth.getCurrentUser().getUid();
        mDriverDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        getUserinformation();



        mProfileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);

            }
        });


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                SaveUserInfo();

//                backbuttonactivity();

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                backbuttonactivity();

            }
        });


    }

    private  void getUserinformation()
    {

        mDriverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {
                    Map<String,Object> map=(Map<String, Object>) dataSnapshot.getValue();


                    if (map.get("name")!= null)
                    {
                        MName=map.get("name").toString();
                        mNamefield.setText(MName);
                    }


                    if (map.get("phone")!= null)
                    {
                        MPhone=map.get("phone").toString();
                        mPhonefield.setText(MPhone);

                    }

                    if (map.get("status")!= null)
                    {
                        mStatus=map.get("status").toString();
                        mStatusfield.setText(mStatus);

                    }


                    if (map.get("email")!= null)
                    {
                        mEmail=map.get("email").toString();
                        mEmailfield.setText(mEmail);
                    }
                    if (map.get("website")!= null)
                    {
                        mWeb=map.get("website").toString();
                        mWebsitefield.setText(mWeb);
                    }

                    if (map.get("location")!= null)
                    {
                        mLoc=map.get("location").toString();
                        mLocationfield.setText(mLoc);
                    }


                    if (map.get("profileimage")!= null)
                    {
                        mProfileImageUrl=map.get("profileimage").toString();
                        Glide.with(getApplication()).load(mProfileImageUrl).into(mProfileimage);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void SaveUserInfo()
    {



        MName=mNamefield.getText().toString();
        MPhone=mPhonefield.getText().toString();
        mEmail=mEmailfield.getText().toString();
        mLoc=mLocationfield.getText().toString();
        mWeb=mWebsitefield.getText().toString();
        mStatus=mStatusfield.getText().toString();


        Map userinfo=new HashMap();
        userinfo.put("name", MName);
        userinfo.put("phone", MPhone);
        userinfo.put("email", mEmail);
        userinfo.put("website", mWeb);
        userinfo.put("location", mLoc);
        userinfo.put("status", mStatus);
        userinfo.put("UserID", userID);


         mDriverDatabase.updateChildren(userinfo);

        if (resultUri!=null)
        {
            StorageReference filepath= FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
            Bitmap bitmap =null;
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos =new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);

            byte[] data =baos.toByteArray();
            UploadTask uploadTask=filepath.putBytes(data);


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                    finish();
                    return;

                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUri=taskSnapshot.getDownloadUrl();
                    Map newImage =new HashMap();
                    newImage.put("profileImages",downloadUri.toString());
                    mDriverDatabase.updateChildren(newImage);


                    finish();
                    return;



                }
            });

        } else
        {
            finish();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == Activity.RESULT_OK)
        {
            final Uri imageUri= data.getData();
            resultUri=imageUri;
            mProfileimage.setImageURI(resultUri);

        }
    }


    private void backbuttonactivity()
    {
        Intent mainintent=new Intent(VendorSetting.this,VendorMain.class);
        startActivity(mainintent);
        finish();
    }


    public static class products
    {
        private String  pname,description,price,category,image,pid,date,time;

        public products()
        {

        }

        public products(String pname, String description, String price, String category, String image, String pid, String date, String time) {
            this.pname = pname;
            this.description = description;
            this.price = price;
            this.category = category;
            this.image = image;
            this.pid = pid;
            this.date = date;
            this.time = time;
        }


        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}







