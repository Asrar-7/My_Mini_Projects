package com.example.asrar.assingnment3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    DatabaseBanking myDb;
    EditText acc_no, amt;
    Button   btnDetails, btnDeposit, btnWithdraw, btnCreate, btnviewall;
    int balance=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseBanking(this);
        acc_no= (EditText)findViewById(R.id.editText2);
        amt= (EditText)findViewById(R.id.editText3);
        btnDetails = (Button) findViewById(R.id.button);
        btnDeposit = (Button) findViewById(R.id.button2);
        btnWithdraw = (Button) findViewById(R.id.button3);
        btnCreate = (Button) findViewById(R.id.button4);
        btnviewall = (Button) findViewById(R.id.button5);

        details();
        deposit();
        withdraw();
        create();
        viewAll();
    }

    public void create(){
        btnCreate.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        balance=0;

                        String amount= amt.getText().toString();
                        balance+= parseInt(amount);

                        boolean isUpdate = myDb.create(acc_no.getText().toString(), balance);
                        if(isUpdate == true)
                            Toast.makeText(MainActivity.this,"Congratulations Account Created Successfully",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Error: Unable To Creae Account",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public void deposit(){
        btnDeposit.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String amount= amt.getText().toString();
                        if(amount.equals(String.valueOf("")))
                        {
                            Toast.makeText(MainActivity.this,"Enter Amount To Be Deposited",Toast.LENGTH_LONG).show();
                        }
                        else {
                            balance += parseInt(amount);

                            boolean isUpdate = myDb.updateData(acc_no.getText().toString(), balance);
                            if (isUpdate == true)
                                Toast.makeText(MainActivity.this, "Deposit Successful", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(MainActivity.this, "Deposit Unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    public void withdraw(){
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount= amt.getText().toString();
                if(amount.equals(String.valueOf("")))
                {
                    Toast.makeText(MainActivity.this,"Enter Amount To Be Withdrawn",Toast.LENGTH_LONG).show();
                }
                else {
                    if (balance < parseInt(amount))
                        Toast.makeText(MainActivity.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                    else {
                        balance -= parseInt(amount);

                        boolean isInserted = myDb.updateData(acc_no.getText().toString(), balance);
                        if (isInserted == true)
                            Toast.makeText(MainActivity.this, "Withraw Successful", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Withdraw Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void details() {
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accno = acc_no.getText().toString();

                if(accno.equals(String.valueOf(""))){
                    acc_no.setError("Enter Account Number To Get Details");
                    return;
                }
                Cursor res = myDb.getdetails(accno);
                String data = null;
                if (res.moveToFirst()) {

                    data = "Account Number:"+res.getString(0)+"\n"+
                            "Balance:"+ res.getString(1)+"\n\n";
                }
                showMessage("Details", data);
            }
        });
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void viewAll(){
        btnviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=myDb.getAllData();
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Error","Nothing found");
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Account Number:"+res.getString(0)+"\n");
                    buffer.append("Balance:"+ res.getString(1)+"\n\n");

                }
                showMessage("Data",buffer.toString());
            }
        });
    }

}