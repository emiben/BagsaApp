package com.app.bagsa.bagsaapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class RegisterActivity extends ActionBarActivity {

    private Spinner userTypes;
    private TextView linkLogIn;
    private Button BtnRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_register);
        //setTitle(R.string.title_activity_boletin);
        getViewElements();
        setElementsEvents();
        loadUserType();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
        userTypes = (Spinner) findViewById(R.id.spinnerUT);
        BtnRegis = (Button) findViewById(R.id.btnRegister);
        linkLogIn = (TextView) findViewById(R.id.link_to_login);
    }

    private void setElementsEvents() {
        BtnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
        linkLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        this.finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void loadUserType(){

        ArrayList<String> userTyp = new ArrayList<String>();

        userTyp.add(getResources().getString(R.string.producer));
        userTyp.add(getResources().getString(R.string.industrial));
        userTyp.add(getResources().getString(R.string.other));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, userTyp);
        userTypes.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startMainActivity();
    }
}
