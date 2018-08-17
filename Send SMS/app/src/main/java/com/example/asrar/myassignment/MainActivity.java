package com.example.asrar.myassignment;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.example.asrar.myassignment.R.*;

public class MainActivity extends AppCompatActivity {
    Button sendBtn;
    EditText password;
    EditText cpassword;
    String pwd1;
    String pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void doTask(View view) {
        sendBtn = (Button) findViewById(R.id.button);
        password = (EditText) findViewById(R.id.editText);
        cpassword = (EditText) findViewById(R.id.editText2);
        pwd1 = password.getText().toString();
        pwd2 = cpassword.getText().toString();

        if (pwd1.equals(pwd2))
        {
            Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(this,Main2Activity.class);

            startActivity(intent);
        } else {

            Toast.makeText(this, " ERROR:Your Password & Confirm Password Do Not Match", Toast.LENGTH_SHORT).show();
        }
    }
}
