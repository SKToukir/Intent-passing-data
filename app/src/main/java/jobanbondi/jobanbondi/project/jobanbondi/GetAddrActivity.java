package jobanbondi.jobanbondi.project.jobanbondi;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import jobanbondi.jobanbondi.project.jobanbondi.util.GPSTracker;
import jobanbondi.jobanbondi.project.jobanbondi.util.GetAddress;

public class GetAddrActivity extends AppCompatActivity {

    String userAddr;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_addr);
    }

    public void btnGetAddr(View view) {
   // create class object
        gps = new GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
           // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            if (latitude != 0.0 && longitude != 0.0) {

                GetAddress getAddress = new GetAddress();
                userAddr = getAddress.getCompleteAddressString(latitude, longitude, this);
                Toast.makeText(this,userAddr,Toast.LENGTH_LONG).show();

            }else {

                Toast.makeText(this,"Try again!",Toast.LENGTH_LONG).show();

            }
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }
}
