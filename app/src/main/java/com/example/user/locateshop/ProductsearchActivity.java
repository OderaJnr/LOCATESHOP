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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ProductsearchActivity extends AppCompatActivity
{
    private RecyclerView searchproductsResutLists;

    private DatabaseReference AllProductsRef;
    private TextView Switchtext2;
    private Toolbar Searchtoolbar;

    private ImageButton Searchproductbutton;
    private EditText searchproductInputText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productsearch);

        Searchtoolbar= (Toolbar)findViewById(R.id.searchproduct_page_toolbar);
        setSupportActionBar(Searchtoolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Search");



        searchproductsResutLists= (RecyclerView) findViewById(R.id.product_search_result_list);
        searchproductsResutLists.setHasFixedSize(true);
        searchproductsResutLists.setLayoutManager(new LinearLayoutManager(this));




        AllProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");




        Searchproductbutton =  findViewById(R.id.search_productbtn);
        searchproductInputText =findViewById(R.id.search_product);

        Switchtext2 =findViewById(R.id.switchsearch2);




        Searchproductbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String searchboxinput= searchproductInputText.getText().toString();

                searchProductsbyCutomers(searchboxinput);

            }
        });


        Switchtext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent searchintent=new Intent(ProductsearchActivity.this,FindvendorActivity.class);
                startActivity(searchintent);

            }
        });




    }

    private void searchProductsbyCutomers(String searchboxinput)
    {
        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();

        Query SearchProductsQuery =AllProductsRef.orderByChild("pname")
                .startAt(searchboxinput).endAt(searchboxinput + "\uf8ff" );

        FirebaseRecyclerAdapter<Products,FindProductsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Products, FindProductsViewHolder>
                (
                        Products.class,
                        R.layout.product_item_layout,
                        FindProductsViewHolder.class,
                        SearchProductsQuery

                )
        {
            @Override
            protected void populateViewHolder(FindProductsViewHolder viewHolder, final Products model, int position)
            {
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPname(model.getPname());
                viewHolder.setPrice(model.getPrice());

                viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                Intent intent=new Intent(ProductsearchActivity.this,ProductDetailsActivity .class);
                intent.putExtra("pid",model.getPid());
                startActivity(intent);


                    }
                });

            }
        };

        searchproductsResutLists.setAdapter(firebaseRecyclerAdapter);



    }


    public static  class FindProductsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FindProductsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mView = itemView;
        }


        public void setImage(Context ctx,String image)

        {
            ImageView productImage = (ImageView) mView.findViewById(R.id.product_image);
            Picasso.with(ctx).load(image).placeholder(R.drawable.imageicon).into(productImage);


        }


        public void setPname(String pname)

        {
            TextView productName = (TextView) mView.findViewById(R.id.product_name);
            productName.setText(pname);

        }


        public void setDescription(String description)
        {
            TextView productDescription = (TextView) mView.findViewById(R.id.product_description);
            productDescription.setText(description);

        }

        public void setPrice(String price)
        {
            TextView productPrice = (TextView) mView.findViewById(R.id.product_price);
            productPrice.setText(price);

        }

    }







}
