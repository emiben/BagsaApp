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
    private Button newslet;
    private Button consEBag;
    private Button transact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

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
        newslet = (Button) findViewById(R.id.button4);
        consEBag = (Button) findViewById(R.id.button5);
        transact = (Button) findViewById(R.id.button6);
    }

    private void setElementsEvents() {
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReportsActivity();
            }
        });
        newslet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReportsActivity();
            }
        });
    }

    private void startReportsActivity() {
        Intent i = new Intent(this, ReportsActivity.class);
        startActivity(i);
    }
}
