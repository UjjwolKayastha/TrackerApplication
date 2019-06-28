package kayastha.ujjwol.atrackerapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Messenger;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Button btnShowLocation,btnshowMap;

    Firebase_method firebase_method;


    private static final  int REQUEST_CODE_PERMISSION =2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        firebase_method = new Firebase_method(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //sets toolbar as actionbar

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try{
            if (ActivityCompat.checkSelfPermission(this,mPermission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        btnShowLocation=findViewById(R.id.button);
        btnshowMap=findViewById(R.id.button2);

    }

    public void Map(View view){


        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
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
