package com.example.hubbyandroid.views;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hubbyandroid.DetailEventActivity;
import com.example.hubbyandroid.R;
import com.example.hubbyandroid.adapter.EventsAdapter;
import com.example.hubbyandroid.models.Evento;
import com.example.hubbyandroid.service.Geocode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity implements OnMapReadyCallback,EventsAdapter.OnItemClickListener {

    private DatabaseReference mUsersRef;
    private TextView textUser;
    private ImageButton addEventButton;
    private ImageButton logOut;

     RecyclerView recyclerView;
     DatabaseReference database;
     EventsAdapter eventsAdapter;
     ArrayList<Evento> list;

    public static final String SHARED_PREFS = "shared_pref";

    private View view;
    private GoogleMap myMap;
    private Marker mark;
    private static final String TAG = MainMenuActivity.class.getSimpleName();
    private final LatLng defaultLocation = new LatLng(-15.789007411749221, -47.89741273385346);
    private static final int DEFAULT_ZOOM = 3;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocode geocode = new Geocode();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initInterface();
    }

    private void initInterface()
    {
        database = FirebaseDatabase.getInstance().getReference("Eventos");

        pegaUsername();

        configRecyclerView();
        configAddEventButton();
        configLogOutButton();
        initGoogleMaps();
    }

    private void initGoogleMaps()
    {
        getLocationPermission();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        view = mapFragment.getView();
    }

    private void configAddEventButton()
    {
        addEventButton = findViewById(R.id.imageButtonOpenAddEvent);

        addEventButton.setClickable(false);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenuActivity.this, CreateEventActivity.class);
                intent.putExtra( "latitude", mark.getPosition().latitude );
                intent.putExtra( "longitude", mark.getPosition().longitude );

                startActivity( intent );

                finish();
            }
        });
    }

    private void configLogOutButton()
    {
        logOut = findViewById(R.id.imagemButtonLogout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pref_check", " ");
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void configRecyclerView()
    {
        recyclerView = findViewById(R.id.recyclerViewListOfLocations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Evento>();
        eventsAdapter = new EventsAdapter(this, list);
        recyclerView.setAdapter(eventsAdapter);

        eventsAdapter.setOnItemClickListener(this);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    list.add(evento);
                }
                eventsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        recyclerView.forceLayout();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
                locationPermissionGranted = true;
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (myMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                myMap.setMyLocationEnabled(true);
                myMap.getUiSettings().setMyLocationButtonEnabled(true);

            }
            else {
                myMap.setMyLocationEnabled(false);
                myMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            LatLng latLng = new LatLng(lastKnownLocation.getLatitude(),
                                    lastKnownLocation.getLongitude());
                            if (lastKnownLocation != null) {
                                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latLng
                                        , DEFAULT_ZOOM));
                            }

                        }
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            myMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            myMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
            else
            {
                myMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                myMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (myMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, myMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;
        myMap.setOnMarkerClickListener(marker -> {
            return false;
        });

        myMap.setOnMapLoadedCallback(() -> {
            for (int i = 0; i < list.size(); i++)
            {
                Evento event = list.get(i);
                String local = event.getLocal().toString();
                String[] latLng = local.split(";");

                myMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng( new Double(latLng[0]), new Double(latLng[1]) ))
                                .title(event.getTitulo()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            }

            recyclerView.forceLayout();
        });

        myMap.getUiSettings().setMapToolbarEnabled(false);

        myMap.setOnMapClickListener( click -> {

            if ( mark != null )
            {
                mark.remove();
            }

            LatLng latLng = new LatLng(click.latitude, click.longitude);

            mark = myMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .title(geocode.getAddressByLatLng(latLng)));

            addEventButton.setClickable(true);

        } );

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        this.myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
    }

    private void pegaUsername(){
        textUser = findViewById(R.id.textUsername);
        mUsersRef = FirebaseDatabase.getInstance().getReference("Usuarios");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference userRef = mUsersRef.child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String username = snapshot.child("Username").getValue(String.class);

                    textUser.setText(username);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(String id) {
        Intent intent = new Intent(MainMenuActivity.this, DetailEventActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}