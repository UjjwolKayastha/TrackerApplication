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

import kayastha.ujjwol.atrackerapp.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String str;
    double dLat, dLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(dLat, dLong);

        MarkerOptions marker = new MarkerOptions().position(sydney).title("Location");
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.userplaceholder));
        mMap.addMarker(marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }
}
