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


public class ContractActivity extends ActionBarActivity {

    private TableLayout table_layout;
    private ImageView filter;
    private TableRow trHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        getViewElements();
        setElementsEvents();
        BuildTable("","datetrx","datetrx",2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contract, menu);
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
        table_layout = (TableLayout) findViewById(R.id.tableLayoutContract);
        filter = (ImageView) findViewById(R.id.ivFilterContract);
        trHeaders = (TableRow) findViewById(R.id.tableRowHeadersContract);
    }

    private void setElementsEvents() {
        final ContractActivity finalThis = this;
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] cols = {"Nro. de Contrato", "Fecha de Trx.", "Tipo de Contrato", "Vol. Transado",
                                    "Producto Transado", "Monto Total", "Prima"};
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

                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setText(rs.getString(rs.getColumnIndex("uy_bg_contract_id")));
                    row.addView(tv);
                    tv2.setText(rs.getString(rs.getColumnIndex("datetrx")));
                    row.addView(tv2);
                    tv3.setText(rs.getString(rs.getColumnIndex("c_doctype_id")));
                    row.addView(tv3);
                    tv4.setText(Integer.toString(rs.getInt(rs.getColumnIndex("volume"))));
                    row.addView(tv4);
                    tv5.setText(Integer.toString(rs.getInt(rs.getColumnIndex("amt"))));
                    row.addView(tv5);
                    tv6.setText(Integer.toString(rs.getInt(rs.getColumnIndex("amtretention"))));
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
            case "Nro. de Contrato":
                colName = "uy_bg_contract_id";
                break;
            case "Fecha de Trx.":
                colName = "datetrx";
                break;
            case "Tipo de Contrato":
                colName = "c_doctype_id";
                break;
            case "Vol. Transado":
                colName = "volume";
                break;
            case "Producto Transado":
                colName = "m_product_id";
                break;
            case "Monto Total":
                colName = "amt";
                break;
            case "Prima":
                colName = "amtretention";
                break;
        }

        String qry = "SELECT * FROM UY_BG_Contract WHERE uy_bg_autionreq_id is null AND (ad_user_id = "+usr+" OR ad_user_id_2 = "+usr+")"
                        +" AND "+ colName + " like '%" + txt + "%'";

        switch (colNameOrd) {
            case "Nro. de Contrato":
                colNameOrd = "uy_bg_contract_id";
                break;
            case "Fecha de Trx.":
                colNameOrd = "datetrx";
                break;
            case "Tipo de Contrato":
                colNameOrd = "c_doctype_id";
                break;
            case "Vol. Transado":
                colNameOrd = "volume";
                break;
            case "Producto Transado":
                colNameOrd = "m_product_id";
                break;
            case "Monto Total":
                colNameOrd = "amt";
                break;
            case "Prima":
                colNameOrd = "amtretention";
                break;
        }

        switch (colNameOrd) {
            case "uy_bg_contract_id":
                qry = qry + " ORDER BY uy_bg_contract_id";
                break;
            case "datetrx":
                qry = qry + " ORDER BY datetrx";
                break;
            case "c_doctype_id":
                qry = qry + " ORDER BY c_doctype_id";
                break;
            case "volume":
                qry = qry + " ORDER BY volume";
                break;
            case "m_product_id":
                qry = qry + " ORDER BY m_product_id";
                break;
            case "amt":
                qry = qry + " ORDER BY amt";
                break;
            case "amtretention":
                qry = qry + " ORDER BY amtretention";
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
