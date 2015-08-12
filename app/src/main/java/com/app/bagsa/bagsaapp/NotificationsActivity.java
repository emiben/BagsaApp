package com.app.bagsa.bagsaapp;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.bagsa.bagsaapp.Utils.DBHelper;
import com.app.bagsa.bagsaapp.Utils.Env;


import me.leolin.shortcutbadger.ShortcutBadger;


public class NotificationsActivity extends FragmentActivity {

    private TableLayout table_layout;
    private ImageView filter;
    private TableRow trHeaders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ShortcutBadger.with(getApplicationContext()).count(0);
        Env.setNotificationsCount(0);
        getViewElements();
        BuildTable("", "Fecha", "Fecha", 2);
        crearNorifications();
        setElementsEvents();
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

    public void getViewElements() {
        table_layout = (TableLayout) findViewById(R.id.tableLayoutNotif);
        filter = (ImageView) findViewById(R.id.ivFilter);
        trHeaders = (TableRow) findViewById(R.id.tableRowHeaders);
    }

    private void setElementsEvents() {
        final NotificationsActivity finalThis = this;
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] cols = {"Descripcion", "Tipo", "Fecha", "ID"};
                new myDialog(finalThis, cols) {
                    @Override
                    public void onOKButton(String txt, String col, String colOrder, int orderBy) {
                        BuildTable(txt, col, colOrder, orderBy);
                        this.dismiss();
                    }

                    @Override
                    public void onCancellButton() {
                        this.dismiss();
                    }
                }.show();
            }
        });
    }


    private void BuildTable(String txt, String col, String colOrder, int orderBy) {
        DBHelper db = null;
        try {
            db = new DBHelper(getApplicationContext());
            db.openDB(0);
            String qry = getQuery(txt, col, colOrder, orderBy);
            Cursor rs = db.querySQL(qry, null);
            table_layout.removeAllViews();
            table_layout.addView(trHeaders);


            if (rs.moveToFirst()) {
                do {
                    int i = 0;
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    row.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            TableRow t = (TableRow) view;
                            TextView firstTextView = (TextView) t.getChildAt(3);
                            int noticeID = Integer.parseInt(firstTextView.getText().toString());

                            startNotifDescActivity(noticeID);
                        }
                    });

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

                    TextView tv4 = new TextView(this);
                    tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv4.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv4.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv4.setPadding(5, 5, 5, 5);
                    tv4.setGravity(Gravity.CENTER);

                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setText(rs.getString(rs.getColumnIndex("descripcion")));
                    row.addView(tv);
                    tv2.setText(rs.getString(rs.getColumnIndex("tipo")));
                    row.addView(tv2);
                    tv3.setText(rs.getString(rs.getColumnIndex("fecha")));
                    row.addView(tv3);
                    tv4.setText(Integer.toString(rs.getInt(rs.getColumnIndex("ID"))));
                    row.addView(tv4);
                    i++;
                    table_layout.addView(row);
                } while (rs.moveToNext());
            }

        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
    }

    private void startNotifDescActivity(int notifID) {
        Intent i = new Intent(this, NotifDescActivity.class);
        Bundle b = new Bundle();
        b.putInt("notifID", notifID);
        i.putExtras(b);
        this.finish();
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Env.setNotificationsCount(0);
    }

    public void crearNorifications() {
        NotificationManager notifManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    private String getQuery(String txt, String colName, String colNameOrd, int type) { //Obligatorio pasarle las columnas
        String qry = "SELECT * FROM Notificaciones WHERE "+ colName + " like '%" + txt + "%'";

        switch (colNameOrd) {
            case "Descripcion":
                qry = qry + " ORDER BY descripcion";
                break;
            case "Tipo":
                qry = qry + " ORDER BY tipo";
                break;
            case "Fecha":
                qry = qry + " ORDER BY fecha";
                break;
            case "ID":
                qry = qry + " ORDER BY ID";
                break;
        }

        switch (type) {
            case 1:
                qry = qry + " asc";
                break;
            case 2:
                qry = qry + " desc";
                break;
        }
        return qry;
    }



}

