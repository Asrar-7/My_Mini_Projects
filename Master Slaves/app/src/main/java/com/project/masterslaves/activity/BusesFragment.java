package com.project.masterslaves.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ravi on 29/07/15.
 */
public class BusesFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap=null;
    private boolean mapsSupported = true;

    ProgressDialog pDialog;
    String URL_Response="";
    String URL_Location =Config.Main_URL+"/get_bus_locations.php";
    String logged_user="";

    public BusesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());

        new GetBusLocation().execute();

        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }
        initializeMap();
       /* Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                update_locations();
            }

        },0,90000);//Update text every second*/
    }

    public void update_locations()
    {
        if(googleMap!=null)
         googleMap.clear();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL_Location);
        try {


            // Execute HTTP Post Request

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            //This is the response from a php application
            // String reverseString = response;
            // server_message_list=response;
            //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
            Log.d("TAP","URL RES IS : "+response);
            if(response.indexOf("DONE")>=0)
            {
                URL_Response = response;
                String [] data=URL_Response.split(",");
                for(int i=1;i<data.length;i++)
                {
                    String each_loc[]=data[i].split("#");
                    double latitude = Double.parseDouble(data[1]);
                    double longitude = Double.parseDouble(data[2]);

                    // create marker
                    MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(latitude, longitude)).title(data[3]);

                    // Changing marker icon
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    // adding marker
                    googleMap.addMarker(marker);
                }
            }

        }
        catch (ClientProtocolException e)
        {

            Log.d("APMC","CPE response " + e.toString());

        }
        catch (IOException e)
        {

            Log.d("QRCODE","IOE response " + e.toString());
            // TODO Aut return 0
            URL_Response="INVALID";
        }

    }

    private void initializeMap() {
        if (googleMap == null && mapsSupported) {
            mMapView = (MapView) getActivity().findViewById(R.id.mapView1);
            googleMap = mMapView.getMap();

            double latitude = 0.00;
            double longitude = 0.00;



            googleMap=mMapView.getMap();
            googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buses, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView1);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        initializeMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private class GetBusLocation extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Location);
            try {


                 // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient.execute(httppost, responseHandler);

                //This is the response from a php application
                // String reverseString = response;
                // server_message_list=response;
                //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
                Log.d("TAP","URL RES IS : "+response);
                if(response.indexOf("DONE")>=0) {
                    URL_Response = response;
                }
                else
                    URL_Response="INVALID";
            }
            catch (ClientProtocolException e)
            {

                Log.d("APMC","CPE response " + e.toString());
                // TODO Auto-generated catch block
                URL_Response="INVALID";
            }
            catch (IOException e)
            {

                Log.d("QRCODE","IOE response " + e.toString());
                // TODO Aut return 0
                URL_Response="INVALID";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Locations..");
            pDialog.setCancelable(false);
            pDialog.show();

        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Log.d("MS","the URL VALU IS : "+URL_Response);
            if(URL_Response.indexOf("DONE")>=0)
            {
               String [] data=URL_Response.split(",");
                for(int i=1;i<data.length;i++)
                {
                    String each_loc[]=data[i].split("#");
                    double latitude = Double.parseDouble(each_loc[1]);
                    double longitude = Double.parseDouble(each_loc[2]);

                    // create marker
                    MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(latitude, longitude)).title(each_loc[3]);

                    // Changing marker icon
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    // adding marker
                    googleMap.addMarker(marker);
                    if((i+1)==data.length)
                    {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(latitude, longitude)).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    }
                }


            }
            else
            {
                Toast.makeText(getActivity(), "Authentication/Network Error ", Toast.LENGTH_LONG).show();
            }
        }

    }



}
