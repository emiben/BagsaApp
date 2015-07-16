package com.app.bagsa.bagsaapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;

import com.app.bagsa.bagsaapp.Utils.DBHelper;


public class NotifDescActivity extends ActionBarActivity {

    private int notifID = 0;
    private String typeDesc;
    private String dateDesc;
    private String desc;
    private String infoDesc;

    private TextView tvTypeDesc;
    private TextView tvDateDesc;
    private TextView tvDesc;
    private TextView tvInfoDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_desc);

        // get the Intent that started this Activity
        Intent in = getIntent();
        // get the Bundle that stores the data of this Activity
        Bundle b = in.getExtras();
        if(null!=b){
            notifID = b.getInt("notifID");
        }

        getViewElements();
        getNoticeData();
        loadElementsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notif_desc, menu);
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

    public void getViewElements(){
        tvTypeDesc = (TextView) findViewById(R.id.typeDesc);
        tvDateDesc = (TextView) findViewById(R.id.dateDesc);
        tvDesc = (TextView) findViewById(R.id.desc);
        tvInfoDesc = (TextView) findViewById(R.id.data);
    }

    public void loadElementsData(){
        tvTypeDesc.setText(typeDesc);
        tvDateDesc.setText(dateDesc);
        tvDesc.setText(desc);
        tvInfoDesc.setText(infoDesc);
        tvInfoDesc.setBackgroundResource(R.drawable.celda_cuerpo3);
    }

    private void getNoticeData(){

        DBHelper db = null;
        try {
            db = new DBHelper(getApplicationContext());
            db.openDB(0);
            Cursor rs = db.querySQL("Select * from Notificaciones where ID = " + notifID + ";", null);
            if(rs.moveToFirst()) {
                typeDesc = rs.getString(rs.getColumnIndex("tipo"));
                dateDesc = rs.getString(rs.getColumnIndex("fecha"));
                desc = rs.getString(rs.getColumnIndex("descripcion"));
                infoDesc = rs.getString(rs.getColumnIndex("informacion"));
            }

        } catch (Exception e) {
            e.getMessage();
        }finally {
            db.close();
        }
    }
}
