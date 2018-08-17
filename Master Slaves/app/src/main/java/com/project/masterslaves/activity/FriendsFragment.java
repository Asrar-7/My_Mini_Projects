package com.project.masterslaves.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Ravi on 29/07/15.
 */
public class FriendsFragment extends Fragment implements OnMapReadyCallback,View.OnClickListener {

    MapView mMapView;
    private GoogleMap googleMap;
    private boolean mapsSupported = true;

    EditText t_num;
    Button b1;

    ProgressDialog pDialog;
    String URL_Response="";
    String URL_Search =Config.Main_URL+"/user_search.php";
    String logged_user="";

    public FriendsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());

        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }
        initializeMap();
    }

    private void initializeMap() {
        if (googleMap == null && mapsSupported) {
            mMapView = (MapView) getActivity().findViewById(R.id.mapView);
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
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        t_num= (EditText) rootView.findViewById(R.id.editText1);
        b1= (Button) rootView.findViewById(R.id.button1);
        b1.setOnClickListener((View.OnClickListener) this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onClick(View v) {
        if(v==b1)
        {
            String num=t_num.getText().toString();
            if(Config.is_num(num))
            {
                new Find_friend().execute(num);
            }
            else
            {
                Toast.makeText(getActivity(),"Invalid Number Provided",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Find_friend extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching User Location..");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected String doInBackground(String... arg0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Search);
            try {


                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("num",arg0[0]));





                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Log.d("MS","the URL VALU IS : "+URL_Response);
            if(URL_Response.indexOf("DONE")>=0)
            {
                String [] data=URL_Response.split("#");
                double latitude = Double.parseDouble(data[1]);
                double longitude = Double.parseDouble(data[2]);

                // create marker
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latitude, longitude)).title(data[3]);

                // Changing marker icon
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                // adding marker
                googleMap.addMarker(marker);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude)).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
            else
            {
                Toast.makeText(getActivity(), "User Not Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}
