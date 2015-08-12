package com.app.bagsa.bagsaapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class BoletinActivity extends ActionBarActivity {

    private int userID=0;
    TableLayout table_layout;
    private ImageView filter;
    private TableRow trHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletin);
        // get the Intent that started this Activity
        Intent in = getIntent();
        // get the Bundle that stores the data of this Activity
        Bundle b = in.getExtras();
        if(null!=b){
            userID = b.getInt("Type");//si es 1 -->Reporte 0-->Boletin
            if(0<userID){
                setTitle(R.string.title_activity_boletinR);
            }else{
                setTitle(R.string.title_activity_boletinB);
            }
        }
        getViewElements();
        BuildTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_boletin, menu);
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
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        filter = (ImageView) findViewById(R.id.ivFilterBoletin);
        trHeaders = (TableRow) findViewById(R.id.tableRowHeadersBoletin);
    }

    private void BuildTable() {
        int rows = 20, cols = 7;
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= cols; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                if(i % 2 == 0)
                    tv.setBackgroundResource(R.drawable.celda_cuerpo1);
                else
                    tv.setBackgroundResource(R.drawable.celda_cuerpo3);
                tv.setPadding(5, 5, 5, 5);
                tv.setGravity(Gravity.CENTER);
//                tv.setTextColor(Integer.parseInt("#FF000000"));
                tv.setTypeface(null, Typeface.BOLD);
                tv.setText("R " + i + ", C" + j);

                row.addView(tv);
            }
            table_layout.addView(row);
        }
    }
}
