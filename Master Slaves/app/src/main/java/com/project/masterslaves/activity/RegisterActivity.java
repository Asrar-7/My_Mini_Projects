package com.project.masterslaves.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.masterslaves.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{


    ProgressDialog pDialog;
    String URL_Response="";
    MaterialBetterSpinner materialDesignSpinner;

    Button b1;
    EditText fname,lname,email,contact,password;
    String branch="";
    String URL_REGISTER=Config.Main_URL+"/register_user.php";

    String[] SPINNERLIST = {"Automobile Engineering", "Civil Engineering", "Computer Science & Engineering", "Electronics and Communication Engineering","Electrical and Electronics Engineering","Mechanical Engineering","Mechatronics Engineering"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        materialDesignSpinner = (MaterialBetterSpinner) findViewById(R.id.material_spinner1);
        materialDesignSpinner.setAdapter(arrayAdapter);


        fname=(EditText)findViewById(R.id.fname);
        lname=(EditText)findViewById(R.id.lname);
        email=(EditText)findViewById(R.id.email);
        contact=(EditText)findViewById(R.id.contact);
        password=(EditText)findViewById(R.id.password);


        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(this);
    }




    @Override
    public void onClick(View v)
    {
        if(v==b1)
        {
            branch=materialDesignSpinner.getText().toString();
            if(fname.getText().toString().equals("")||lname.getText().toString().equals("")||email.getText().toString().equals("")||contact.getText().toString().equals("")||password.getText().toString().equals(""))
            {
                Toast.makeText(RegisterActivity.this,"Invalid Details Are Provided",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(!Config.validateName(fname.getText().toString())||!Config.validateName(lname.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this,"Please Provide Valid First/Lastname",Toast.LENGTH_LONG).show();
                }
                else
                {
                   if(Config.is_email(email.getText().toString()))
                   {
                       if(Config.is_num(contact.getText().toString()))
                       {
                           new Regsiter_user().execute(fname.getText().toString(),lname.getText().toString(),contact.getText().toString(),password.getText().toString(),email.getText().toString(),branch);
                       }
                       else
                       {
                           Toast.makeText(RegisterActivity.this,"Invalid Mobile Number",Toast.LENGTH_LONG).show();
                       }
                   }
                    else
                       Toast.makeText(RegisterActivity.this,"Invalid Email ID",Toast.LENGTH_LONG).show();

                }

            }

        }
    }


    private class Regsiter_user extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Registering User..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_REGISTER);
            try {


                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("fname",arg0[0]));
                nameValuePairs.add(new BasicNameValuePair("lname",arg0[1]));
                nameValuePairs.add(new BasicNameValuePair("contact",arg0[2]));
                nameValuePairs.add(new BasicNameValuePair("password",arg0[3]));
                nameValuePairs.add(new BasicNameValuePair("email",arg0[4]));
                nameValuePairs.add(new BasicNameValuePair("branch",branch));



                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient.execute(httppost, responseHandler);

                //This is the response from a php application
                // String reverseString = response;
                // server_message_list=response;
                //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
                Log.d("TAP","URL RES IS : "+response);
                if(response.equals("DONE"))
                    URL_Response="DONE";
                else if(response.equals("EXISTS"))
                    URL_Response="EXISTS";
                else
                    URL_Response="INVALID";
            }
            catch (ClientProtocolException e)
            {

                Log.d("TAP","CPE response " + e.toString());
                // TODO Auto-generated catch block
                URL_Response="INVALID";
            }
            catch (IOException e)
            {

                Log.d("TAP","IOE response " + e.toString());
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
            Log.d("TAP","the URL VALU IS : "+URL_Response);
            if(URL_Response.equals("DONE"))
            {
                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                SQLiteDatabase db=openOrCreateDatabase("MASTER", MODE_PRIVATE, null);
                db.execSQL("create table if not exists users (registered varchar(5));");
                String reg="YES";
                db.execSQL("insert into users values('"+reg+"');");
                db.close();

                Intent in=new Intent(RegisterActivity.this,UserLoginActivity.class);
                startActivity(in);
            }
            else
            {
                if(URL_Response.equals("EXISTS"))
                    Toast.makeText(RegisterActivity.this, "User Already Registered", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
            }
        }

    }
}
