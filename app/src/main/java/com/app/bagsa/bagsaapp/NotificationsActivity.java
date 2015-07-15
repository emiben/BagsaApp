package com.app.bagsa.bagsaapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.bagsa.bagsaapp.Utils.DBHelper;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class NotificationsActivity extends FragmentActivity {

    TableLayout table_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getViewElements();
        BuildTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
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
        table_layout = (TableLayout) findViewById(R.id.tableLayoutNotif);
    }

    private void BuildTable(){
        int rows = 20, cols = 3;
        DBHelper db = null;
        try {
            db = new DBHelper(getApplicationContext());
            db.openDB(0);
            Cursor rs = db.querySQL("Select * from Notificaciones", null);
            if(rs.moveToFirst()) {
                while (rs.moveToNext()) {
                    int i = 0;
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setGravity(Gravity.CENTER);

                    TextView tv2 = new TextView(this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv2.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv2.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv2.setPadding(5, 5, 5, 5);
                    tv2.setGravity(Gravity.CENTER);

                    TextView tv3 = new TextView(this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv3.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv3.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv3.setPadding(5, 5, 5, 5);
                    tv3.setGravity(Gravity.CENTER);

//                tv.setTextColor(Integer.parseInt("#FF000000"));
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setText(rs.getString(rs.getColumnIndex("descripcion")));
                    row.addView(tv);
                    tv2.setText(rs.getString(rs.getColumnIndex("tipo")));
                    row.addView(tv2);
                    tv3.setText(rs.getString(rs.getColumnIndex("fecha")));
                    row.addView(tv3);
                    i++;
                    table_layout.addView(row);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }finally {
            db.close();
        }
    }
}
