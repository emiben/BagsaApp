package com.app.bagsa.bagsaapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.app.bagsa.bagsaapp.Utils.Env;
import com.readystatesoftware.viewbadger.BadgeView;


public class PrincipalActivity extends ActionBarActivity {

    private Button notif;
    private Button consPrices;
    private Button reports;
    private Button consEBag;
    private Button transact;
    private View target;
    private BadgeView badge;

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
        setQuantityOfNotif();

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
        target = findViewById(R.id.button);
        badge = new BadgeView(this, target);
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

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNotificationsActivity();
            }
        });


    }

    private void setQuantityOfNotif() {
        int number = Env.getNotificationsCount();
        if(0<number){
            badge.setText(String.valueOf(number));
            badge.show();
        }else{
            badge.hide();
        }
    }

    private void startNotificationsActivity() {
        Intent i = new Intent(this, NotificationsActivity.class);
        startActivity(i);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Env.setNotificationsCount(0);
        startMainActivity();
    }
    private void startMainActivity() {
        this.finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setQuantityOfNotif();
    }

}
