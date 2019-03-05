package com.example.user.locateshop;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsergasActivity extends FragmentActivity implements OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;



    private Button CallcarButton,mcancelride;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String customerID;
    private DatabaseReference CustomerDatabaseRef;
    private  LatLng CustomerpickupLocation;
    private DatabaseReference DriverAvailableRef;
    private int radius=1;
    private Boolean driverFound=false;
    private String driverFoundID;
    private DatabaseReference DriversRef;

    private ProgressDialog loadingbar;

    Marker DriverMarker;



    private  Boolean requestbol=false;
    GeoQuery geoQuery;

    private LinearLayout mDriverinfo;
    private TextView mDrivername,mDriverphone,mDriverId,mDriverlocation,mdriverdistance;

    private ImageView mDriverprofileimage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usergas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        loadingbar=new ProgressDialog(this);

        mDrivername=findViewById(R.id.driverName);
        mDriverphone=findViewById(R.id.driverPhone);
        mDriverlocation=findViewById(R.id.driverlocation);
        mDriverId=findViewById(R.id.driverid);
        mdriverdistance=findViewById(R.id.driverdistance);

        CustomerDatabaseRef= FirebaseDatabase.getInstance().getReference().child("RescueCustomers Requests");



        currentUser=mAuth.getCurrentUser();
        CallcarButton=findViewById(R.id.customer_call_car);
        mcancelride=findViewById(R.id.cancelride);




        mcancelride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                cancelride();
            }
        });







        CallcarButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if  (requestbol)
                {   requestbol=false;
                    geoQuery.removeAllListeners();
                    DriverLocationRef.removeEventListener(DriverLocationRefListener);

                    if (driverFound!=null)
                    {
                        DatabaseReference driverRef= FirebaseDatabase.getInstance().getReference().child("Users").child("RescueDrivers").child(driverFoundID).child("RescueCustomers Requests");
                        driverRef.removeValue();
                        driverFoundID = null;
                    }

                    driverFound=false;
                    radius=1;
                    String userID =FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref =FirebaseDatabase.getInstance().getReference("RescueCustomers Requests");
                    GeoFire geofire =new GeoFire(ref);
                    geofire.removeLocation(userID);



                    if (DriverMarker!=null)
                    {
                        DriverMarker.remove();
                    }

                    CallcarButton.setText("Call Rescue");





                }

                else
                {  requestbol=true;
                    String userID =FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref =FirebaseDatabase.getInstance().getReference("RescueCustomers Requests");
                    GeoFire geofire =new GeoFire(ref);
                    geofire.setLocation(userID,new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()));

                    CustomerpickupLocation=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(CustomerpickupLocation).title("Rescue Client from Here"));


                    CallcarButton.setText("Finding Rescue Team");


                    GetClosestDriverCab();

                }



            }
        });






    } private void GetClosestDriverCab()
    {
        DriverAvailableRef=FirebaseDatabase.getInstance().getReference().child("RescueDrivers Available");
        GeoFire geoFire=new GeoFire(DriverAvailableRef);
        geoQuery=geoFire.queryAtLocation(new GeoLocation(CustomerpickupLocation.latitude,CustomerpickupLocation.longitude),radius);
        geoQuery.removeAllListeners();


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location)
            {
                if(!driverFound && requestbol)
                {
                    driverFound=true;
                    driverFoundID=key;

                    DriversRef=FirebaseDatabase.getInstance().getReference().child("Users").child("RescueDrivers").child(driverFoundID);
                    customerID=FirebaseAuth.getInstance().getCurrentUser().getUid();

                    HashMap drivermap=new HashMap();
                    drivermap.put("RescueCustomerRideID", customerID);
                    DriversRef.updateChildren(drivermap);

                    GettingDriverLocation();


                    getDriverInfo();


                    CallcarButton.setText("Searching for the rescue Team near You..");


                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady()
            {
                if (!driverFound)
                {
                    radius++;
                    GetClosestDriverCab();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }









    private DatabaseReference DriverLocationRef;
    private ValueEventListener DriverLocationRefListener;

    private void GettingDriverLocation()

    { DriverLocationRef=FirebaseDatabase.getInstance().getReference().child("RescueDrivers Working").child(driverFoundID).child("l");

        DriverLocationRefListener=DriverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {if (dataSnapshot.exists()&& requestbol)
            {
                List<Object> driverLocationmap=(List<Object>)dataSnapshot.getValue();
                double LocationLat=0;
                double LocationLng=0;
                CallcarButton.setText("Call Rescue");

                if (driverLocationmap.get(0) !=null)
                {
                    LocationLat=Double.parseDouble(driverLocationmap.get(0).toString());

                } if (driverLocationmap.get(1) !=null)

            {
                LocationLng=Double.parseDouble(driverLocationmap.get(1).toString());
            }

                LatLng driverLatLng=new LatLng(LocationLat,LocationLng);
                if (DriverMarker!=null)
                {
                    DriverMarker.remove();
                }


                Location location1=new Location("");
                location1.setLatitude(CustomerpickupLocation.latitude);
                location1.setLongitude(CustomerpickupLocation.longitude);


                Location location2=new Location("");
                location2.setLatitude(driverLatLng.latitude);
                location2.setLongitude(driverLatLng.longitude);

                float Distance =location1.distanceTo(location2);




                if (Distance<20)
                {
                    CallcarButton.setText("Rescue Team Is Here");
                }else


                { CallcarButton.setVisibility(View.INVISIBLE);

                    mcancelride.setVisibility(View.VISIBLE);}


                DriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Rescuer Location."));

                mdriverdistance.setVisibility(View.VISIBLE);

                mdriverdistance.setText("Rescuer Assigned is" + " " +String.valueOf(Distance/1000) + " " +"Kilometers from you");



            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        CallcarButton.setVisibility(View.INVISIBLE);
        mcancelride.setVisibility(View.INVISIBLE);
        loadingbar.setTitle("LOADING YOUR LOCATION");
        loadingbar.setMessage("please wait..");
        loadingbar.show();

        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);


    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location)
    {

        CallcarButton.setVisibility(View.VISIBLE);
        loadingbar.dismiss();
        lastLocation=location;

        LatLng latlng=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


        if (!getDriveraroundStarted)

            getDriversAround();



    }

    protected synchronized void buildGoogleApiClient()

    {
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

    }



    boolean getDriveraroundStarted=false;

    List<Marker> Markerlist =new ArrayList<Marker>();

    private void getDriversAround()


    {
        getDriveraroundStarted=true;
        DatabaseReference driverslocation = FirebaseDatabase.getInstance().getReference().child(("RescueDrivers Available"));

        GeoFire geoFire=new GeoFire(driverslocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()),radius=100);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {



            @Override
            public void onKeyEntered(String key, GeoLocation location)
            {

                for (Marker MarkerIt :  Markerlist)
                {
                    if (MarkerIt.getTag().equals(key))

                        return;
                }

                LatLng driverLocation=new LatLng(location.latitude,location.longitude);

                Marker DriverMarker = mMap.addMarker(new MarkerOptions().position(driverLocation).title(key));

                DriverMarker.setTag(key);

                Markerlist.add(DriverMarker);


            }

            @Override
            public void onKeyExited(String key)

            {
                for (Marker MarkerIt :  Markerlist)
                {
                    if (MarkerIt.getTag().equals(key))
                    {
                        MarkerIt.remove();
                        Markerlist.remove(MarkerIt);
                        return;
                    }

                }


            }

            @Override
            public void onKeyMoved(String key, GeoLocation location)
            {
                for (Marker MarkerIt :  Markerlist)
                {
                    if (MarkerIt.getTag().equals(key))
                    {

                        MarkerIt.setPosition(new LatLng(location.latitude,location.longitude));
                    }


                }

            }

            @Override
            public void onGeoQueryReady()
            {


            }

            @Override
            public void onGeoQueryError(DatabaseError error)
            {


            }
        });

    }



    private  void getDriverInfo()
    {


        DatabaseReference mCustomerDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {
                    Map<String,Object> map=(Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name")!= null)
                    {
                        mDrivername.setText(map.get("name").toString());
                    }

                    if (map.get("phone")!= null) {
                        mDriverphone.setText(map.get("phone").toString());
                    }
                    if (map.get("driverid")!= null) {
                        mDriverId.setText(map.get("driverid").toString());
                    }
                    if (map.get("location")!= null) {
                        mDriverlocation.setText(map.get("location").toString());
                    }



                    if (map.get("profileImageUrl")!= null)
                    {
                        Glide.with(getApplication()).load(map.get("profileImageUrl")).into(mDriverprofileimage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  void cancelride()
    {


        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference disconectuser= FirebaseDatabase.getInstance().getReference().child("RescueCustomers Requests");

        GeoFire geoFire = new GeoFire(disconectuser);
        geoFire.removeLocation(userID);



        mcancelride.setVisibility(View.INVISIBLE);
        CallcarButton.setVisibility(View.VISIBLE);
        mdriverdistance.setVisibility(View.INVISIBLE);







    }




}
