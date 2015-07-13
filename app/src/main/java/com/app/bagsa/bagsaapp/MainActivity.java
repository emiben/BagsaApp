package com.app.bagsa.bagsaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bagsa.bagsaapp.Utils.BagsaDB;
import com.app.bagsa.bagsaapp.Utils.Env;
import com.app.bagsa.bagsaapp.Utils.InitialLoad;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    private static final String PROPERTY_REG_ID = "PID";
    private static final String PROPERTY_USER = "PU";
    private static final String PROPERTY_APP_VERSION = "PAV";
    private static final String PROPERTY_EXPIRATION_TIME = "PET";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1 ;
    private static final String SENDER_ID = "123456789";
    private static final long EXPIRATION_TIME_MS = 1;
    private String TAG  = "SBT";
    private Context context;
    private GoogleCloudMessaging gcm;
    private String regid;

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

        testDataBase();

        registerGCM();
       //registerGCM();

    }

    /**Inicializo los componentes del Activity
     * @Author sbouissa 30-06-2015 Issue#
     */
    private void getViewElements() {
        txtUser = (EditText) findViewById(R.id.eTxtUserName);
        txtPsw = (EditText) findViewById(R.id.eTxtPassword);
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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = txtUser.getText().toString();
                String p = txtPsw.getText().toString();
                if(u.equals("admin") && p.equals("admin")){
                    startPrincipalActivity(0);
                }else{
                    Context context = getApplicationContext();
                    CharSequence text =  getResources().getString(R.string.user_pws_error);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                 }
            }
        });
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPrincipalActivity(1);
            }
        });
    }

    /**Iniciar activity para realizar resgitro
     * @Author sbouissa 30-06-2015 Issue#
     */
    private void startRegisterActivity() {
        context = getApplicationContext();

        //Chequemos si está instalado Google Play Services
        if(checkPlayServices())
        {
        gcm = GoogleCloudMessaging.getInstance(MainActivity.this);

        //Obtenemos el Registration ID guardado
        regid = getRegistrationId(context);

        //Si no disponemos de Registration ID comenzamos el registro
        if (regid.equals("")) {
            TareaRegistroGCM tarea = new TareaRegistroGCM();
            tarea.execute(txtUser.getText().toString());
        }
        }
        else
        {
            Log.i(TAG, "No se ha encontrado Google Play Services.");
        }

       //this.finish();
       // Intent i = new Intent(this, RegisterActivity.class);
       // startActivity(i);
    }

    /**
     * Inicia ventana principal, depende del tipo de login si se envia usuario o no
     * 0 no envia usuario indica LOGIN INVITADO 1 envia usuario.
     * @param type
     */
    private void startPrincipalActivity(int type) {
        Intent i = new Intent(this, PrincipalActivity.class);
        if(0==type){
            this.finish();
            startActivity(i);
        }else{
            Bundle b = new Bundle();
            b.putString("User", "ADMIN");
            b.putInt("UserID",1);
            i.putExtras(b);
            this.finish();
            startActivity(i);
        }

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

    public void registerGCM(){
       // Thread thread = new Thread() {
         //   public void run() {
                MyInstanceIDListenerService myListenerSer = new MyInstanceIDListenerService();
                myListenerSer.onTokenRefresh();
           // }
       // };
       // thread.start();
    }


    private String getRegistrationId(Context context)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.length() == 0)
        {
            Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }

        String registeredUser =
                prefs.getString(PROPERTY_USER, "user");

        int registeredVersion =
                prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");

        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion)
        {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        }
        else if (System.currentTimeMillis() > expirationTime)
        {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        }
        else if (!txtUser.getText().toString().equals(registeredUser))
        {
            Log.d(TAG, "Nuevo nombre de usuario.");
            return "";
        }

        return registrationId;
    }

    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
        };
        thread.start();
    }

    private void testDataBase() {
        // Validate SD
        if(Env.isEnvLoad(this)){
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                if(!Env.getDB_PathName(this).equals(BagsaDB.DB_NAME)){
                    finish();
                }
            }
        } else {
            InitialLoad initData = new InitialLoad(this);
            initData.initialLoad_copyDB();
        }
    }
}
