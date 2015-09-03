package com.app.bagsa.bagsaapp;

import android.app.ProgressDialog;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SubastasActivity extends ActionBarActivity {

    private String mensajeWS = "";
    private ProgressDialog pDialog;
    private Boolean retornoWS = false;
    private TableLayout table_layout;
    private ImageView filter;
    private TableRow trHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subastas);

        getViewElements();
        setElementsEvents();
        BuildTable("", "DocNo", "DocNo",2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subastas, menu);
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
        table_layout = (TableLayout) findViewById(R.id.tableLayoutSubasta);
        filter = (ImageView) findViewById(R.id.ivFilterSubasta);
        trHeaders = (TableRow) findViewById(R.id.tableRowHeadersSubasta);
    }

    private void setElementsEvents() {
        final SubastasActivity finalThis = this;
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] cols = {"DocNo", "Producto", "Volumen", "Precio",
                        "Tipo", "DocStatus", "Mejor Oferta", "Hora"};
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
            int cant=rs.getCount();

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
                    String aux = rs.getColumnName(0);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setText(rs.getString(0));
                    row.addView(tv);
                    tv2.setText(rs.getString(1));
                    row.addView(tv2);
                    tv3.setText(rs.getString(2));
                    row.addView(tv3);
                    tv4.setText(Float.toString(rs.getFloat(3)));
                    row.addView(tv4);
                    tv7.setText(rs.getString(4));
                    row.addView(tv7);
                    tv8.setText(rs.getString(5));
                    row.addView(tv8);
                    tv5.setText(rs.getString(6));
                    row.addView(tv5);
                    tv6.setText(rs.getString(7));
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
            case "DocNo":
                colName = "r.documentno";
                break;
            case "Producto":
                colName = "p.name";
                break;
            case "Volumen":
                colName = "r.qty";
                break;
            case "Precio":
                colName = "r.price";
                break;
            case "Tipo":
                colName = "r.buysell";
                break;
            case "DocStatus":
                colName = "r.docstatus";
                break;
            case "Mejor Oferta":
                colName = "oferta";
                break;
            case "Hora":
                colName = "fechaOfer";
                break;
        }

        String qry = "SELECT r.documentno, p.name, r.qty," +
                " r.price, r.buysell, r.docstatus, max(b.price) as oferta, max(b.created) as fechaOfer" +
                " FROM VUY_Bagsa_Autionreq r" +
                " LEFT JOIN VUY_Bagsa_Autionbid b" +
                " ON r.uy_bg_autionreq_id = b.uy_bg_autionreq_id" +
                " LEFT JOIN m_product p ON r.m_product_id = p.m_product_id" +
                " WHERE "+ colName + " like '%" + txt + "%'" +
                " AND r.buysell = 'VENTA' " +
                "group by r.updated, r.documentno, p.name, r.qty, r.price, r.buysell, r.docstatus " +
                "UNION " +
                "SELECT r.documentno, p.name, r.qty, " +
                " r.price, r.buysell, r.docstatus, min(b.price) as oferta, max(b.created) as fechaOfer " +
                " FROM VUY_Bagsa_Autionreq r " +
                " LEFT JOIN VUY_Bagsa_Autionbid b " +
                " ON r.uy_bg_autionreq_id = b.uy_bg_autionreq_id " +
                " LEFT JOIN m_product p ON r.m_product_id = p.m_product_id" +
                " WHERE "+ colName + " like '%" + txt + "%'" +
                " AND r.buysell = 'COMPRA' " +
                "group by r.updated, r.documentno, p.name, r.qty, r.price, r.buysell, r.docstatus";


        switch (colNameOrd) {
            case "DocNo":
                colNameOrd = "r.documentno";
                break;
            case "Producto":
                colNameOrd = "p.name";
                break;
            case "Volumen":
                colNameOrd = "r.qty";
                break;
            case "Precio":
                colNameOrd = "r.price";
                break;
            case "Tipo":
                colNameOrd = "r.buysell";
                break;
            case "DocStatus":
                colNameOrd = "r.docstatus";
                break;
            case "Mejor Oferta":
                colNameOrd = "oferta";
                break;
            case "Hora":
                colNameOrd = "fechaOfer";
                break;
        }

        switch (colNameOrd) {
            case "r.documentno":
                qry = qry + " ORDER BY r.documentno";
                break;
            case "p.name":
                qry = qry + " ORDER BY p.name";
                break;
            case "r.qty":
                qry = qry + " ORDER BY r.qty";
                break;
            case "r.price":
                qry = qry + " ORDER BY r.price";
                break;
            case "r.buysell":
                qry = qry + " ORDER BY r.buysell";
                break;
            case "r.docstatus":
                qry = qry + " ORDER BY r.docstatus";
                break;
            case "oferta":
                qry = qry + " ORDER BY oferta";
                break;
            case "fechaOfer":
                qry = qry + " ORDER BY fechaOfer";
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
