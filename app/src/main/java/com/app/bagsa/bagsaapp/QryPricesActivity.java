package com.app.bagsa.bagsaapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class QryPricesActivity extends ActionBarActivity {

    private Spinner spinMarket;
    private Spinner spinProduct;
    private Spinner spinBolsa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qry_prices);

        getViewElements();
        loadSpinners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qry_prices, menu);
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
        spinMarket = (Spinner)findViewById(R.id.spinnerMarket);
        spinProduct = (Spinner)findViewById(R.id.spinnerProd);
        spinBolsa = (Spinner)findViewById(R.id.spinnerBolsa);
    }

    public void loadSpinners(){
        String[] market = new String[] { "Nacional", "Internacional"};
        ArrayAdapter<String> adapterMarket = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, market);
        spinMarket.setAdapter(adapterMarket);

        String[] prod = new String[] { "Prod1", "Prod2", "Prod3", "Prod4"};
        ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prod);
        spinProduct.setAdapter(adapterProd);

        String[] bolsa = new String[] { "NY", "Chicago", "otras..."};
        ArrayAdapter<String> adapterBolsa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bolsa);
        spinBolsa.setAdapter(adapterBolsa);
    }
}
