package com.project.masterslaves.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.project.masterslaves.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Intkonsys on 25/02/2017.
 */

public class TrackBusActivity extends AppCompatActivity
{
    Location current_location;
    LocationManager manager;
    String Provider_name="";
    boolean isGpsEnabled,isNetworkEnabled;
    Double latitude,longitude;
    String Bus_token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        SQLiteDatabase db=openOrCreateDatabase("MASTER", MODE_PRIVATE, null);
        Cursor c=db.rawQuery("select * from bus",null);
        if(c.moveToFirst())
        {
            Bus_token=c.getString(c.getColumnIndex("token"));
        }


        manager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled=manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(isGpsEnabled||isNetworkEnabled)
        {
            if(isGpsEnabled)
            {
                Provider_name=LocationManager.GPS_PROVIDER;
            }
            if(isNetworkEnabled)
            {
                Provider_name=LocationManager.NETWORK_PROVIDER;
            }
        }
        else
        {
            Toast.makeText(this,"No Location Data Available",Toast.LENGTH_LONG).show();
        }

        Log.d("TAP","THE PROVIDER NAME IS : "+Provider_name);
        current_location=manager.getLastKnownLocation(Provider_name);
        android.location.LocationListener listener=new UserLocationListener();
        manager.requestLocationUpdates(Provider_name, 5000, 0, (android.location.LocationListener) listener);

        Log.d("TAP","THE CURRENT LOC IS : "+current_location);
        if(current_location!=null)
        {

            latitude=current_location.getLatitude();
            longitude=current_location.getLongitude();
            Toast.makeText(this,"Latitude Is : "+latitude+"\n Longitude Is :"+longitude+"",Toast.LENGTH_LONG).show();
            senddata(Bus_token,Double.toString(latitude),Double.toString(longitude));
        }
        else
        {
            Toast.makeText(this,"No Stored Location Are Available",Toast.LENGTH_LONG).show();
        }
    }

    public int senddata(String username,String lat,String lon){

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.Main_URL+"/update_bus_loc.php");
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("bus_token", Bus_token));
            nameValuePairs.add(new BasicNameValuePair("lat", lat));
            nameValuePairs.add(new BasicNameValuePair("lon", lon));



            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            //This is the response from a php application

            // server_mesg_list=response;
            // Toast.makeText(this, "response" + server_mesg_list, Toast.LENGTH_LONG).show();
            return 1;

        }
        catch (ClientProtocolException e)
        {
            Toast.makeText(this, "CPE response " + e.toString(), Toast.LENGTH_LONG).show();
            // TODO Auto-generated catch block
            return 0;
        }
        catch (IOException e)
        {
            Toast.makeText(this, "IOE response " + e.toString(), Toast.LENGTH_LONG).show();
            // TODO Auto-generated catch block
            return 0;
        }


    }

    public class UserLocationListener implements android.location.LocationListener
    {


        @Override
        public void onLocationChanged(Location location) {
            current_location=location;
            latitude=current_location.getLatitude();
            longitude=current_location.getLongitude();
            senddata(Bus_token,Double.toString(latitude),Double.toString(longitude));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

}
