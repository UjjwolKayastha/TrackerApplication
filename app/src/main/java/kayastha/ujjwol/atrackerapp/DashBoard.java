package kayastha.ujjwol.atrackerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import kayastha.ujjwol.atrackerapp.maps.GPSTracker;
import kayastha.ujjwol.atrackerapp.maps.MapsActivity;
import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Button btnShowLocation, btnshowMap;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    TextView location, navEmail, navUsername;
    ImageButton navImage;
    FirebaseDatabase database;
    DatabaseReference myRef, myRefClient;

    Firebase_method firebase_method;
    private FirebaseAuth mAuth;

    String uID, uEmail;

    String value = null;

    String currentUserEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        firebase_method = new Firebase_method(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        uEmail = mUser.getEmail();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //sets toolbar as actionbar

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Chat with Friends", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(getApplicationContext(), Message.class));
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //checking if the permission is given
        try{
            if (ActivityCompat.checkSelfPermission(this,mPermission)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        navImage =navigationView.getHeaderView(0).findViewById(R.id.navImage);
        navEmail =navigationView.getHeaderView(0).findViewById(R.id.navEmail);
        navUsername =navigationView.getHeaderView(0).findViewById(R.id.navUsername);

        currentUserEmail = mAuth.getCurrentUser().getEmail();
        firebase_method.searchEmail(currentUserEmail, new Firebase_method.ResultCallBack<UserData>() {
            @Override
            public void onResult(UserData data) {
//                Picasso.with(DashBoard.this).load(data.getProfileImage()).into(navImage);
                navUsername.setText(data.getName());
                navEmail.setText(data.getEmail());
            }
        });

            gps = new GPSTracker(DashBoard.this);
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitide();
                double longitude = gps.getLongitude();


//                  FirebaseApp.initializeApp(MainActivity.this);
                database = FirebaseDatabase.getInstance();

                myRef = database.getReference("Users");
                //                myRef = database.getReference();


//                    if (myRef == null) {
//                        Log.d(TAG, "firebase ref is null");
//                    } else {
//                        Log.d(TAG, "firebase ref is not null");
//                    }
                myRef.child(uID).child("location").setValue(latitude + "," + longitude);
            } else {
                gps.showSettingsAlert();
            }


        final FirebaseDatabase databaseClient;
        databaseClient= FirebaseDatabase.getInstance();
        myRefClient = databaseClient.getReference("Users");
        myRefClient.child(uID).child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(String.class);
//                TextView textView = findViewById(R.id.LocationClient);
//                textView.setText(value);
                String [] seperator = value.split(",");
                String latPos = seperator[0].trim();
                String longPos = seperator[1].trim();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Map(View view){

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("LocationValue", value);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            Intent intent=new Intent(getApplicationContext(), Contact.class);
            startActivity(intent);

        } else if (id == R.id.nav_message) {
            Intent intent=new Intent(getApplicationContext(), Message.class);
            startActivity(intent);

        } else if (id == R.id.nav_alarm) {
//            Intent intent=new Intent(getApplicationContext(), Alarm.class);
//            startActivity(intent);

        } else if (id == R.id.nav_shareLocation) {

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));


        } else if (id == R.id.nav_logout) {
            firebase_method.signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
