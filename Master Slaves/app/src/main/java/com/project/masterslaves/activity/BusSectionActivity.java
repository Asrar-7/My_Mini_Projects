package com.project.masterslaves.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
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
 * Created by Intkonsys on 24/02/2017.
 */

public class BusSectionActivity extends AppCompatActivity implements View.OnClickListener
{
    ProgressDialog pDialog;
    String URL_Response="";

    Button b1;
    EditText bus_token;
    String branch="";
    String URL_Link=Config.Main_URL+"/link_bus.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbus);

        SQLiteDatabase db=openOrCreateDatabase("MASTER", MODE_PRIVATE, null);
        db.execSQL("create table if not exists buses (token varchar(50));");
        Cursor c=db.rawQuery("select * from buses",null);
        if(c.moveToFirst())
        {
            Intent in=new Intent(BusSectionActivity.this,BusDashboardActivity.class);
            startActivity(in);
        }
        db.close();

        bus_token=(EditText)findViewById(R.id.token);
        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==b1)
        {
            if(bus_token.getText().toString().equals(""))
            {
                Toast.makeText(this,"Please Specify Bus Token",Toast.LENGTH_LONG).show();
            }
            else
            {
                new link_bus().execute(bus_token.getText().toString());
            }
        }
    }


    private class link_bus extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusSectionActivity.this);
            pDialog.setMessage("Linking Bus..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Link);
            try {


                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("bus_token",arg0[0]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient.execute(httppost, responseHandler);

                //This is the response from a php application
                // String reverseString = response;
                // server_message_list=response;
                //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
                Log.d("TAP","URL RES IS : "+response);
                if(response.indexOf("VALID")>=0) {
                    URL_Response = "VALID";
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
            if(URL_Response.equals("VALID"))
            {
                Toast.makeText(BusSectionActivity.this, "Link Successful", Toast.LENGTH_LONG).show();

                SQLiteDatabase db=openOrCreateDatabase("MASTER", MODE_PRIVATE, null);
                db.execSQL("create table if not exists buses (token varchar(50));");
                String reg=bus_token.getText().toString();
                db.execSQL("insert into buses values('"+reg+"');");
                db.close();

                Intent in=new Intent(BusSectionActivity.this,BusDashboardActivity.class);
                in.putExtra("token",bus_token.getText().toString());
                startActivity(in);


            }
            else
            {
                Toast.makeText(BusSectionActivity.this, "Authentication/Network Error ", Toast.LENGTH_LONG).show();
            }
        }

    }
}
