package com.example.kkwbustracking;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
public class driver_tracklocation extends AppCompatActivity implements OnMapReadyCallback
{
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseDatabase database;
    String licence_no,email,phone,name;
    DatabaseHelper databaseHelper;
    Location location;

    int route=0;
    boolean route_selected = false;

    private final String CHANNEL_ID = "simple_notification";
    private final int NOTIFICATION_ID = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_tracklocation);

        database = FirebaseDatabase.getInstance();
        databaseHelper = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Current Location");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white,getTheme()));
        setSupportActionBar(toolbar);

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue2, this.getTheme()));
        route_dialog();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFrag != null;
        mapFrag.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.route)
            route_dialog();
        else if(id == R.id.share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else
        {
            Intent intent = new Intent(driver_tracklocation.this,about.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void route_dialog()
    {

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose a Route");
        builder1.setCancelable(false);
        String[] routes ={"Route - A (1)", "Route - B (2)","Route - C (3)","Route - D (4)","Route - E (5)"};
        int checkedItem = route-1;
        builder1.setSingleChoiceItems(routes, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                
                switch(which)
                {
                    case 0: route = 1;  break;
                    case 1: route = 2;  break;
                    case 2: route = 3;  break;
                    case 3: route = 4;  break;
                    case 4: route = 5;  break;
                }
            }
        });

        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                route_selected = true;
                Cursor res = databaseHelper.getData("1");
                if(res.moveToFirst())
                {
                    name = res.getString(1);
                    phone = res.getString(2);
                    licence_no = res.getString(3);
                    email = res.getString(4);
                }

            }
        });
        builder1.setNegativeButton("Cancel", null);
        final AlertDialog dialog1 = builder1.create();
        dialog1.show();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
        else
        {
            checkLocationPermission();
        }
    }

    LocationCallback mLocationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0)
            {
                location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null)
                {
                    mCurrLocationMarker.remove();
                }


                if(route_selected)
                {
                    Toast.makeText(driver_tracklocation.this, "Location Sent : " + location.getLatitude() + "   " + location.getLongitude(), Toast.LENGTH_LONG).show();

                    final DatabaseReference reference = database.getReference();      //return the path to the root

                    reference.child(route + "").child("Latitude").setValue(location.getLatitude()).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                reference.child(route + "").child("Longitude").setValue(location.getLongitude());
                                reference.child(route + "").child("Name").setValue(name);
                                reference.child(route + "").child("Phone").setValue(phone);
                                reference.child(route + "").child("Licence_no").setValue(licence_no);
                                reference.child(route + "").child("Email").setValue(email);
                            }
                        }
                    });
                }



                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                DisplayNotification();
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(driver_tracklocation.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }
            else
            {
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void DisplayNotification()
    {
        Toast.makeText(this, "Driver you are here", Toast.LENGTH_SHORT).show();
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.location);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.phone));
        builder.setContentTitle("Sharing Driver Location");
        builder.setContentText(location.getLatitude()+" , "+location.getLongitude());
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "Simple Notification";
            String description = "Include all the simple notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}