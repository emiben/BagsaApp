package com.app.bagsa.bagsaapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;


public class ReportsActivity extends ActionBarActivity {

    private Spinner spinYear;
    private Spinner spinMonth;
    private RadioButton chkReports;
    private RadioButton chkNewsLet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        getViewElements();
        setElementsEvents();
        loadSpinners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reports, menu);
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
        spinYear = (Spinner)findViewById(R.id.spinnerYear);
        spinMonth = (Spinner)findViewById(R.id.spinnerMonth);
        chkReports = (RadioButton) findViewById(R.id.checkboxReports);
        chkNewsLet = (RadioButton) findViewById(R.id.checkboxNewsLet);
    }

    private void setElementsEvents() {
        chkReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chckRepAction(v);
            }
        });
        chkNewsLet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chckNwsLetAction(v);
            }
        });

    }

    public void chckRepAction(View v){
        RadioButton radioBtn = (RadioButton)v;
        if(radioBtn.isChecked()){
            chkNewsLet.setChecked(false);
        }
    }

    public void chckNwsLetAction(View v){
        RadioButton radioBtn = (RadioButton)v;
        if(radioBtn.isChecked()){
            chkReports.setChecked(false);
        }
    }

    public void loadSpinners(){
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1950 ; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYear.setAdapter(adapter);

        String[] Months = new String[] { "Enero", "Febrero",
                "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };

        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Months);
        spinMonth.setAdapter(adapterMonth);
    }


}
