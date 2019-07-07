package kayastha.ujjwol.atrackerapp.maps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String str;
    double dLat, dLong;

    String uID;
    Firebase_method firebase_method;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth = FirebaseAuth.getInstance();
        firebase_method = new Firebase_method(this);


        FirebaseUser mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        str = getIntent().getExtras().getString("LocationValue");
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        String [] seperator = str.split(",");
        String latPos = seperator[0].trim();
        String longPos = seperator[1].trim();

        dLat = Double.parseDouble(latPos);
        dLong = Double.parseDouble(longPos);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        firebase_method.userFriends(uID, new Firebase_method.ResultCallBack<UserData>(){
            @Override
            public void onResult(UserData data) {
                plotInMap(data);
            }
        });

    }

    private void plotInMap(UserData data){

        String[] arr = data.getLocation().split(",");
        double lat = Double.parseDouble(arr[0]);
        double lng = Double.parseDouble(arr[1].trim());

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);

        MarkerOptions marker = new MarkerOptions().position(sydney).title(data.getName());
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.userplaceholder));
        mMap.addMarker(marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(2f));
    }
}
