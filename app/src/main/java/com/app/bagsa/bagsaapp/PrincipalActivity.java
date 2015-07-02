package com.app.bagsa.bagsaapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class PrincipalActivity extends ActionBarActivity {

    private Button notif;
    private Button consPrices;
    private Button reports;
    private Button consEBag;
    private Button transact;

    //sbouissa 02-07-2015
    private int userID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // get the Intent that started this Activity
        Intent in = getIntent();
        // get the Bundle that stores the data of this Activity
        Bundle b = in.getExtras();
        if(null!=b){
           userID = b.getInt("UserID");
        }
        getViewElements();
        setElementsEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getViewElements() {
        notif = (Button) findViewById(R.id.button);
        consPrices = (Button) findViewById(R.id.button2);
        reports = (Button) findViewById(R.id.button3);
        consEBag = (Button) findViewById(R.id.button5);
        transact = (Button) findViewById(R.id.button6);
        if(userID>0){
            transact.setVisibility(View.GONE);
            consEBag.setVisibility(View.GONE);
        }
    }

    private void setElementsEvents() {
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReportsActivity();
            }
        });
        consEBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrysEBagsaActivity();
            }
        });
        consPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrysPricesActivity();
            }
        });
    }

    private void startReportsActivity() {
        Intent i = new Intent(this, ReportsActivity.class);
        startActivity(i);
    }

    private void startQrysEBagsaActivity() {
        Intent i = new Intent(this, qrysEBagsaActivity.class);
        startActivity(i);
    }

    private void startQrysPricesActivity() {
        Intent i = new Intent(this, QryPricesActivity.class);
        startActivity(i);
    }
}
