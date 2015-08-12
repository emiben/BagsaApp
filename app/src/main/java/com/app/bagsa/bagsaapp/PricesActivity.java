package com.app.bagsa.bagsaapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bagsa.bagsaapp.R;

public class PricesActivity extends ActionBarActivity {

    private TableLayout table_layout;
    private Button      btnExport;
    private Button      btnEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prices);

        getViewElements();
        BuildTable();
        setActions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prices, menu);
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
        table_layout = (TableLayout) findViewById(R.id.tableLayoutPrices);
        btnEmail = (Button) findViewById(R.id.btnEmail);
    }

    public void setActions(){
        btnEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"emiben1@hotmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Test Android");
                i.putExtra(Intent.EXTRA_TEXT   , "Test Android Body");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(PricesActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void BuildTable(){
        int rows = 20, cols = 5;
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
