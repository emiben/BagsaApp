package com.app.bagsa.bagsaapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private EditText txtUser;
    private EditText txtPsw;
    private TextView txtRegister;
    //Buttons
    private Button btnGuest;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewElements();
        setElementsEvents();
    }

    /**Inicializo los componentes del Activity
     * @Author sbouissa 30-06-2015 Issue#
     */
    private void getViewElements() {
        txtUser = (EditText) findViewById(R.id.eTxtUserName);
        txtUser = (EditText) findViewById(R.id.eTxtPassword);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        btnGuest = (Button) findViewById(R.id.btnGuest);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    /**Inicializo los eventos de cada componentes del Activity
     * @Author sbouissa 30-06-2015 Issue#
     */
    private void setElementsEvents() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterActivity();
            }
        });
    }

    /**Iniciar activity para realizar resgitro
     * @Author sbouissa 30-06-2015 Issue#
     */
    private void startRegisterActivity() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
