package com.project.masterslaves.activity;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.location.LocationListener;
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

import static android.content.Intent.getIntent;


public class HomeFragment extends Fragment {

    TextView content;

    Location current_location;
    LocationManager manager;
    String Provider_name="";
    boolean isGpsEnabled,isNetworkEnabled;
    Double latitude,longitude;

    String logged_user="";


    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // logged_user = getActivity().getIntent().getExtras().getString("name");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            logged_user = bundle.getString("logged_user");
            System.out.println(logged_user);
        }

        manager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            Toast.makeText(getActivity(),"No Location Data Available",Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(),"Latitude Is : "+latitude+"\n Longitude Is :"+longitude+"",Toast.LENGTH_LONG).show();
            senddata(logged_user,Double.toString(latitude),Double.toString(longitude));
        }
        else
        {
            Toast.makeText(getActivity(),"No Stored Location Are Available",Toast.LENGTH_LONG).show();
        }
    }


    public int senddata(String username,String lat,String lon){

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.Main_URL+"/update_user_loc.php");
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("username", logged_user));
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
            Toast.makeText(getActivity(), "CPE response " + e.toString(), Toast.LENGTH_LONG).show();
            // TODO Auto-generated catch block
            return 0;
        }
        catch (IOException e)
        {
            Toast.makeText(getActivity(), "IOE response " + e.toString(), Toast.LENGTH_LONG).show();
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
            senddata(logged_user,Double.toString(latitude),Double.toString(longitude));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);



        content= (TextView) rootView.findViewById(R.id.content);
        content.setText("Before Using This Applicaton Make Sure That, The Number Which You Want To Track Should Be Registered With The Master Slave Application And By Installing This Application You Agree Our Terms And Conditions(That Is Your Installed This Application To Share Your Location With Others)                                                                                                                             \n Contact Us Here\n                                                     " +
                "\nEmail: contactus@masterslavesapp.com                                                                           Web:www.masterslavesapp.com");
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
