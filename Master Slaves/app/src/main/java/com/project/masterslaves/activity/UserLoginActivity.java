package com.project.masterslaves.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Intkonsys on 24/02/2017.
 */

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener
{
    AutoCompleteTextView t1;
    EditText t2;
    Button b1,b2;



    ProgressDialog pDialog;
    String URL_Response="";
    String URL_LOGIN =Config.Main_URL+"/login_check.php";
    String logged_user="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        t1=(AutoCompleteTextView)findViewById(R.id.email);
        t2=(EditText) findViewById(R.id.password);

        b1=(Button)findViewById(R.id.email_sign_in_button);
        b1.setOnClickListener(this);

        b2=(Button)findViewById(R.id.button2);
        b2.setOnClickListener(this);
    }



    @Override
    public void onClick(View view)
    {
        if(view==b1)
        {
            String email,pass;
            email=t1.getText().toString();
            pass=t2.getText().toString();
            if(Config.is_email(email))
            {
                if(email.equals("")||pass.equals(""))
                {
                    Toast.makeText(UserLoginActivity.this,"Invalid Inputs Provided",Toast.LENGTH_LONG).show();
                }
                else
                {
                    new Authentication().execute(email,pass);
                }
            }
            else
                Toast.makeText(UserLoginActivity.this,"The Email Is Invalid",Toast.LENGTH_LONG).show();
        }
        if(view==b2)
        {
            Intent in=new Intent(UserLoginActivity.this,RegisterActivity.class);
            startActivity(in);
        }


    }



    private class Authentication extends AsyncTask<String, String, String> {



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(UserLoginActivity.this);
        pDialog.setMessage("Authenticating User..");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected String doInBackground(String... arg0) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL_LOGIN);
        try {


            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("email",arg0[0]));
            nameValuePairs.add(new BasicNameValuePair("password",arg0[1]));




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
                logged_user = arg0[0];


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
            Toast.makeText(UserLoginActivity.this, "Login Successful ", Toast.LENGTH_LONG).show();

            Intent in=new Intent(UserLoginActivity.this,DashboardActivity.class);
            in.putExtra("logged_user",logged_user);
            startActivity(in);


        }
        else
        {
            Toast.makeText(UserLoginActivity.this, "Authentication/Network Error ", Toast.LENGTH_LONG).show();
        }
    }

}
}
