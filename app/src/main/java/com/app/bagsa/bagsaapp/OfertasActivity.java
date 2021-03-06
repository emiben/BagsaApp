package com.app.bagsa.bagsaapp;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
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


public class OfertasActivity extends ActionBarActivity {

    private TableLayout table_layout;
    private ImageView filter;
    private TableRow trHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);

        getViewElements();
        setElementsEvents();
        BuildTable("","Fecha de Trx.","Fecha de Trx.",2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ofertas, menu);
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
        table_layout = (TableLayout) findViewById(R.id.tableLayoutOffer);
        filter = (ImageView) findViewById(R.id.ivFilterOffer);
        trHeaders = (TableRow) findViewById(R.id.tableRowHeadersOffer);
    }

    private void setElementsEvents() {
        final OfertasActivity finalThis = this;
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] cols = {"Fecha de Trx.", "Instrumento", "Producto", "Volumen",
                        "Precio", "Tipo", "Transado"};
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
            db = new DBHelper(this);
            db.openDB(0);
            String qry = getQuery(txt, col, colOrder, orderBy);
            Cursor rs = db.querySQL(qry, null);
            //int tama = rs.getCount();
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

                    TextView tv5 = new TextView(this);
                    tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv5.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv5.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv5.setPadding(5, 5, 5, 5);
                    tv5.setGravity(Gravity.CENTER);

                    TextView tv6 = new TextView(this);
                    tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv6.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv6.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv6.setPadding(5, 5, 5, 5);
                    tv6.setGravity(Gravity.CENTER);

                    TextView tv7 = new TextView(this);
                    tv7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv7.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv7.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv7.setPadding(5, 5, 5, 5);
                    tv7.setGravity(Gravity.CENTER);

                    TextView tv8 = new TextView(this);
                    tv8.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    if (i % 2 == 0)
                        tv8.setBackgroundResource(R.drawable.celda_cuerpo1);
                    else
                        tv8.setBackgroundResource(R.drawable.celda_cuerpo3);
                    tv8.setPadding(5, 5, 5, 5);
                    tv8.setGravity(Gravity.CENTER);

                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setText(rs.getString(rs.getColumnIndex("o.updated")));
                    row.addView(tv);
                    tv2.setText(rs.getString(rs.getColumnIndex("i.code")));
                    row.addView(tv2);
                    tv3.setText(rs.getString(rs.getColumnIndex("p.name")));
                    row.addView(tv3);
                    tv4.setText(Float.toString(rs.getFloat(rs.getColumnIndex("o.volume"))));
                    row.addView(tv4);
                    tv7.setText(Float.toString(rs.getFloat(rs.getColumnIndex("o.priceentered"))));
                    row.addView(tv7);
                    tv8.setText(rs.getString(rs.getColumnIndex("o.buySell")));
                    row.addView(tv8);
                    tv5.setText(rs.getString(rs.getColumnIndex("o.type")));
                    row.addView(tv5);
                    tv6.setText(rs.getString(rs.getColumnIndex("o.isMatched")));
                    row.addView(tv6);
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


    private String getQuery(String txt, String colName, String colNameOrd, int type) { //Obligatorio pasarle las columnas
        Env e = new Env();
        int usr = Integer.parseInt(e.getAdUsr());

        switch (colName) {
            case "Fecha de Trx.":
                colName = "o.updated";
                break;
            case "Instrumento":
                colName = "i.code";
                break;
            case "Producto":
                colName = "p.name";
                break;
            case "Volumen":
                colName = "o.volume";
                break;
            case "Precio":
                colName = "o.priceentered";
                break;
            case "Tipo":
                colName = "o.type";
                break;
            case "Transado":
                colName = "o.isMatched";
                break;
            case "Oferta":
                colName = "o.buySell";
                break;
        }

        String qry = "SELECT o.updated, i.code, p.name, o.volume, o.priceentered," +
                        " o.buySell, o.type, o.isMatched" +
                        " FROM UY_BG_Offer o" +
                        " LEFT JOIN m_product p ON o.m_product_id = p.m_product_id" +
                        " LEFT JOIN VUY_Bagsa_Instrument i ON o.uy_bg_instrument_id = i.uy_bg_instrument_id" +
                        " WHERE o.ad_user_id = " + usr +
                        " AND "+ colName + " like '%" + txt + "%'";

        switch (colNameOrd) {
            case "Fecha de Trx.":
                colNameOrd = "o.updated";
                break;
            case "Instrumento":
                colNameOrd = "i.code";
                break;
            case "Producto":
                colNameOrd = "p.name";
                break;
            case "Volumen":
                colNameOrd = "o.volume";
                break;
            case "Precio":
                colNameOrd = "o.priceentered";
                break;
            case "Tipo":
                colNameOrd = "o.type";
                break;
            case "Transado":
                colNameOrd = "o.isMatched";
                break;
            case "Oferta":
                colNameOrd = "o.buySell";
                break;
        }

        switch (colNameOrd) {
            case "o.updated":
                qry = qry + " ORDER BY o.updated";
                break;
            case "i.code":
                qry = qry + " ORDER BY i.code";
                break;
            case "p.name":
                qry = qry + " ORDER BY p.name";
                break;
            case "o.volume":
                qry = qry + " ORDER BY o.volume";
                break;
            case "o.priceentered":
                qry = qry + " ORDER BY o.priceentered";
                break;
            case "o.type":
                qry = qry + " ORDER BY o.type";
                break;
            case "o.isMatched":
                qry = qry + " ORDER BY o.isMatched";
                break;
            case "o.buySell":
                qry = qry + " ORDER BY o.buySell";
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
