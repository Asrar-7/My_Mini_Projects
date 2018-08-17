package com.project.masterslaves.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class BusDashboardActivity extends AppCompatActivity implements View.OnClickListener
{
    String bus_token="";
    Location current_location;
    LocationManager manager;
    String Provider_name="";
    boolean isGpsEnabled,isNetworkEnabled;
    Double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busdashboard);
        SQLiteDatabase db=openOrCreateDatabase("MASTER", MODE_PRIVATE, null);
        db.execSQL("create table if not exists buses (token varchar(50));");
        Cursor c=db.rawQuery("select * from buses",null);
        if(c.moveToFirst())
        {
            bus_token=c.getString(c.getColumnIndex("token"));
        }


        db.close();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        manager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled=manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        LocationListener listener=new UserLocationListener();
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
            Toast.makeText(this,"No Location Data Is Available",Toast.LENGTH_LONG).show();
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
        }

        Log.d("TAP","THE PROVIDER NAME IS : "+Provider_name);
        current_location=manager.getLastKnownLocation(Provider_name);
        listener=new UserLocationListener();
        manager.requestLocationUpdates(Provider_name, 5000, 0,listener);

        Log.d("TAP","THE CURRENT LOC IS : "+current_location);
        if(current_location!=null)
        {

            latitude=current_location.getLatitude();
            longitude=current_location.getLongitude();
            Toast.makeText(this,"Latitude Is : "+latitude+"\n Longitude Is :"+longitude+"",Toast.LENGTH_LONG).show();
            senddata(bus_token,Double.toString(latitude),Double.toString(longitude));
        }
        else
        {
            Toast.makeText(this,"No Stored Location Available",Toast.LENGTH_LONG).show();
        }

    }

    public class UserLocationListener implements android.location.LocationListener
    {


        @Override
        public void onLocationChanged(Location location) {
            current_location=location;
            latitude=current_location.getLatitude();
            longitude=current_location.getLongitude();
            senddata(bus_token,Double.toString(latitude),Double.toString(longitude));
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

    public int senddata(String bus_token,String lat,String lon){

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.Main_URL+"/update_bus_loc.php");
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("bus_token", bus_token));
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

    @Override
    public void onClick(View v) {

    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
