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


public class ConsultaPreciosActivity extends ActionBarActivity {

    private TableLayout table_layout;
    private ImageView filter;
    private TableRow trHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_precios);

        getViewElements();
        setElementsEvents();
        BuildTable("","Fecha","Fecha",2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consulta_precios, menu);
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
        table_layout = (TableLayout) findViewById(R.id.tableLayoutPrecios);
        filter = (ImageView) findViewById(R.id.ivFilterPrecios);
        trHeaders = (TableRow) findViewById(R.id.tableRowHeadersPrecios);
    }

    private void setElementsEvents() {
        final ConsultaPreciosActivity finalThis = this;
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] cols = {"Producto", "Precio de Cmpa.", "Precio de Vta.", "Fecha"};
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

                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setText(rs.getString(rs.getColumnIndex("prod.name")));
                    row.addView(tv);
                    tv2.setText("BAGSA");//rs.getString(rs.getColumnIndex("c.datetrx")));
                    row.addView(tv2);
                    tv3.setText(rs.getString(rs.getColumnIndex("p.price")));
                    row.addView(tv3);
                    tv4.setText(rs.getString(rs.getColumnIndex("p.priceEntered")));
                    row.addView(tv4);
                    tv7.setText("");//rs.getString(rs.getColumnIndex("p.name")));
                    row.addView(tv7);
                    tv5.setText("");//(Integer.toString(rs.getInt(rs.getColumnIndex("c.amt"))));
                    row.addView(tv5);
                    tv6.setText(rs.getString(rs.getColumnIndex("p.updated")));
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

        switch (colName) {
            case "Producto":
                colName = "prod.name";
                break;
            case "Precio de Cmpa.":
                colName = "p.price";
                break;
            case "Precio de Vta.":
                colName = "p.priceEntered";
                break;
            case "Fecha":
                colName = "p.updated";
                break;
        }

        String qry = "SELECT prod.name, p.price, p.priceEntered, p.updated" +
                        " FROM UY_BG_dailypriceline p LEFT JOIN m_product prod" +
                            " ON p.m_product_id = prod.m_product_id" +
                        " WHERE "+ colName + " like '%" + txt + "%'";

        switch (colNameOrd) {
            case "Producto":
                colNameOrd = "prod.name";
                break;
            case "Precio de Cmpa.":
                colNameOrd = "p.price";
                break;
            case "Precio de Vta.":
                colNameOrd = "p.priceEntered";
                break;
            case "Fecha":
                colNameOrd = "p.updated";
                break;
        }

        switch (colNameOrd) {
            case "prod.name":
                qry = qry + " ORDER BY prod.name";
                break;
            case "p.price":
                qry = qry + " ORDER BY p.price";
                break;
            case "p.priceEntered":
                qry = qry + " ORDER BY p.priceEntered";
                break;
            case "p.updated":
                qry = qry + " ORDER BY p.updated";
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
