package com.example.user.locateshop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VgasActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,RoutingListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Boolean currentLogoutDriverStatus=false;
    private DatabaseReference AssignedCustomerRef;
    private String driverId;
    private  String customerId="";

    private LinearLayout mCustomerinfo;
    private TextView mCustomername,mCustomerphone,mCustomerlocation;
    private SupportMapFragment mapframent;
    private ImageView mCustomerprofileimage;

    private Switch mDriverworking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vgas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polylines = new ArrayList<>();
        // use who needs the police details

        mCustomerinfo= findViewById(R.id.customerInfo);
        mCustomername= findViewById(R.id.customerName);
        mCustomerphone= findViewById(R.id.customerPhone);
        mCustomerlocation=findViewById(R.id.customerlocation);
        mCustomerprofileimage= findViewById(R.id.customerprofileimage);





        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        GetAssignedCustomerRequest();


        mDriverworking=findViewById(R.id.driverworking);

        mDriverworking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    connecttheDriver();
                }

                else
                {
                    DisconectTheDriver();
                }

            }
        });




    }

    private void GetAssignedCustomerRequest()

    {      driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        AssignedCustomerRef= FirebaseDatabase.getInstance().getReference().child("Users").child("PoliceDrivers").child(driverId).child("PoliceCustomerRideID");
        AssignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            { if (dataSnapshot.exists())

            {
                customerId=dataSnapshot.getValue().toString();


                GetAssignedCustomerPickupLocation();
                GetAssignedCustomerinfo();

            }else
            {
                erasePolylines();
                customerId="";
                if (pickupMarker!=null)

                {
                    pickupMarker.remove();
                }

                if (assignedCustomerPickupLocationRefListener!=null)
                {
                    assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
                }







                mCustomerinfo.setVisibility(View.GONE);
                mCustomerphone.setText("");
                mCustomerlocation.setText("");
                mCustomername.setText("");
                mCustomerprofileimage.setImageResource(R.drawable.profile);


            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    Marker pickupMarker;
    private  DatabaseReference assignedCustomerPickupLocationRef;
    private  ValueEventListener assignedCustomerPickupLocationRefListener;

    private  void  GetAssignedCustomerPickupLocation()
    {

        assignedCustomerPickupLocationRef=FirebaseDatabase.getInstance().getReference().child("PoliceCustomers Requests").child(customerId).child("l");
        assignedCustomerPickupLocationRefListener = assignedCustomerPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            { if (dataSnapshot.exists() && !customerId.equals(""))
            {
                List<Object> map=(List<Object>) dataSnapshot.getValue();
                double LocationLat=0;
                double LocationLng=0;

                if (map.get(0) !=null)
                {
                    LocationLat=Double.parseDouble(map.get(0).toString());

                } if (map.get(1) !=null)

            {
                LocationLng=Double.parseDouble(map.get(1).toString());
            }

                LatLng pickupLatLng=new LatLng(LocationLat,LocationLng);

                pickupMarker= mMap.addMarker(new MarkerOptions().position(pickupLatLng).title("Pickup Location"));

                getRouteToMarker(pickupLatLng);

            }
            else
            {
                erasePolylines();
                pickupMarker.remove();

            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getRouteToMarker(LatLng pickupLatLng)
    {

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()), pickupLatLng)
                .key("AIzaSyDgwPi72r3-raLM5lzXUS7czCKcYgLDyME")
                .build();
        routing.execute();
    }


    private  void GetAssignedCustomerinfo()
    {
        mCustomerinfo.setVisibility(View.VISIBLE);

        DatabaseReference mCustomerDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {
                    Map<String,Object> map=(Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name")!= null)
                    {
                        mCustomername.setText(map.get("name").toString());
                    }
                    if (map.get("phone")!= null)
                    {
                        mCustomerphone.setText(map.get("phone").toString());
                    }
                    if (map.get("location")!= null) {
                        mCustomerlocation.setText(map.get("location").toString());
                    }


                    if (map.get("profileImageUrl")!= null)
                    {
                        Glide.with(getApplication()).load(map.get("profileImageUrl")).into(mCustomerprofileimage);
                    }
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

        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);


    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    { locationRequest = new LocationRequest();
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


        if (getApplicationContext()!=null)
        {
            lastLocation=location;

            LatLng latlng=new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


            String userID= currentUser.getUid();
            DatabaseReference DriverAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("PoliceDrivers Available");
            GeoFire geoFireavailability = new GeoFire(DriverAvailabilityRef);
            geoFireavailability.setLocation(userID,new GeoLocation(location.getLatitude(),location.getLongitude()));


            DatabaseReference DriverworkingRef=FirebaseDatabase.getInstance().getReference().child("PoliceDrivers Working");
            GeoFire geoFireworking = new GeoFire(DriverworkingRef);


            switch (customerId)
            {
                case"":
                    geoFireworking.removeLocation(userID);
                    geoFireavailability.setLocation(userID,new GeoLocation(location.getLatitude(),location.getLongitude()));
                    break;

                default:
                    geoFireavailability.removeLocation(userID);
                    geoFireworking.setLocation(userID,new GeoLocation(location.getLatitude(),location.getLongitude()));
                    break;


            }
        }

    }

    protected synchronized void buildGoogleApiClient()

    {
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

    }

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    public void onRoutingFailure(RouteException e)
    {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart()
    {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex)
    {


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled()
    {

    }


    private void erasePolylines()
    {
        for (Polyline line : polylines )
        {
            line.remove();
        }
        polylines.clear();




    }

    private void connecttheDriver()
    {
        if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }





    private void DisconectTheDriver()


    {  LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DriverAvailabilityRef= FirebaseDatabase.getInstance().getReference().child("Drivers Available");

        GeoFire geoFire = new GeoFire(DriverAvailabilityRef);
        geoFire.removeLocation(userID);
    }

    private void LogOutDriver()
    {

//        Intent welcomeintent=new Intent(PoliceActivity.this,ChooseActivity.class);
//        welcomeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(welcomeintent);
//        finish();
    }










}
