package kayastha.ujjwol.atrackerapp.maps;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

public class GPSTracker extends Service implements LocationListener {

    //reference
    private final Context mContext;

    //setting up flag for GPS status when we start the app its default is off
    boolean isGPSEnabled = false;

    //setting up flag for netwok connection status
    boolean isNetworkEnabled = false;

    //setting up location
    boolean canGetLocation = false;

    //variables for location
    Location location;
    double latitide;
    double longitude;

    //setting up min distance to change update and min time betn update
    private static final long MIN_DISTANCE_FOR_UPDATES = 10;
    private  static final long MIN_TIME_BTN_UPDATES = 1000 * 60 * 1;

    //location manager to get the location
    protected LocationManager locationManager;

    public GPSTracker(Context context){
        this.mContext = context;
        //to check whether the GPS and Network is enabled and also the permission to access the location
        getLocation();
    }
    //get the current location
    //might go wrong so use try and catch
    public Location getLocation(){
        try{
            //get reference to location manager
            //get system service using the context
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //get the GPS status  true if enabled
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){

            }else{
                this.canGetLocation = true;

                //attempt to get location from network provider
                if(isNetworkEnabled){
                    //checking for location access and access coarse location
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED);
                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BTN_UPDATES, MIN_DISTANCE_FOR_UPDATES, this);

                if(locationManager != null){
                    //give access to the location
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null){
                        latitide = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }

            }

            if (isGPSEnabled){
                if (location == null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BTN_UPDATES, MIN_DISTANCE_FOR_UPDATES, this);

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location != null){
                            latitide = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    //to stop GPS
    public void stopUsingGPS(){
        if (locationManager != null){
            //check permission
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED);
            return;
        }
        locationManager.removeUpdates(GPSTracker.this);
    }

    //get latitude
    public double getLatitide(){
        if (location != null){
            latitide = location.getLatitude();
        }
        return latitide;
    }

    public double getLongitude(){
        if (location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    //checking if we can get the location
    public boolean canGetLocation(){
        return this.canGetLocation;
    }


    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
