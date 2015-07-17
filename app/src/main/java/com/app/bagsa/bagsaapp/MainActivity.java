package com.app.bagsa.bagsaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bagsa.bagsaapp.Utils.BagsaDB;
import com.app.bagsa.bagsaapp.Utils.DBHelper;
import com.app.bagsa.bagsaapp.Utils.Env;
import com.app.bagsa.bagsaapp.Utils.InitialLoad;
import com.app.bagsa.bagsaapp.base.DB;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends ActionBarActivity {

    private String m_deviceID="";
    //Variables login
    private Activity mCtx = null;
    private String userIn="";
    private String pswIn="";
    private Boolean retornoWS=false;
    private ProgressDialog pDialog;

    //variables para insertar regID
    private static String SOAP_ACTION1 = "http://servidor.ws.bagsaBroadcast.com/regGCMUsers";
    private static String NAMESPACE = "http://servidor.ws.bagsaBroadcast.com";
    private static String METHOD_NAME1 = "regGCMUsers";
    private static String URL = "http://200.71.26.66:8080/axis2/services/getGCMUsersService.aar?wsdl";


    private static final String PROPERTY_REG_ID = "PID";
    private static final String PROPERTY_USER = "PU";
    private static final String PROPERTY_APP_VERSION = "PAV";
    private static final String PROPERTY_EXPIRATION_TIME = "PET";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1 ;
    private static final String SENDER_ID = "585544263746";
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
    //private String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            int bgCount = Env.getNotificationsCount();
            //

        }catch (Exception e){
            e.getMessage();
        }
        mCtx = this;
        m_deviceID =  getDeviceID();
        getViewElements();
        setElementsEvents();

        testDataBase();

        registerGCM();// se hace al momento de cargar la base

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
                userIn = txtUser.getText().toString();
                pswIn = txtPsw.getText().toString();
                //if(u.equals("admin") && p.equals("admin")){
                if(isOnline()){
                    if(loginWS(userIn,pswIn)){
                        //startPrincipalActivity(0);
                    }else{

                    }
                }else{

                    CharSequence text =  getResources().getString(R.string.noInternet);
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();                }

                }
        });
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPrincipalActivity(1);
            }
        });
    }

    private boolean loginWS(String u, String p) {

        pDialog = ProgressDialog.show(this,null, "Consultando..",true);
        new Thread(){
            public void run(){
                try{
                    retornoWS = loginWebServer(userIn,pswIn);
                }catch (Exception e){
                    e.getMessage();
                }
                pDialog.dismiss();
                (mCtx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retornoWS) {
                            startPrincipalActivity(0);
                        } else {
                            Context context = getApplicationContext();
                            CharSequence text =  getResources().getString(R.string.user_pws_error);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });
            }
        }.start();

        return retornoWS;
    }

    /**Iniciar activity para realizar resgitro
     * @Author sbouissa 30-06-2015 Issue#
     */
    private void startRegisterActivity() {
       this.finish();
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
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

    public void registerGCM() {
        context = getApplicationContext();

        //Chequemos si está instalado Google Play Services
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(MainActivity.this);

            //Obtenemos el Registration ID guardado
            regid = getRegistrationId(context);

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                TareaRegistroGCM tarea = new TareaRegistroGCM();
                tarea.execute(txtUser.getText().toString());
            } else {
                boolean ret = DBHelper.exists("UY_BG_GCMDevice", "TokenID = '" + regid + "'", getBaseContext());
                if (!ret) {
                    DBHelper db = new DBHelper(getBaseContext());
                    db.executeSQL("UPDATE UY_BG_GCMDevice set TokenID = '" + regid + "' WHERE DeviceID = '"+m_deviceID+"'");
                }
            }
            //new RegGCMUsersTask().execute();
        } else {
            Log.i(TAG, "No se ha encontrado Google Play Services.");
        }
    }

//    private class RegGCMUsersTask extends AsyncTask<String,Integer,String> {
//        @Override
//        protected String doInBackground(String... params) {
//            String resIds = "";
//
//            regGCMUser();
//            return null;
//        }
//
//        public void regGCMUser() {
//            //Initialize soap request + add parameters
//            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
//            //Use this to add parameters
//            request.addProperty("regID", regid);
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
//
//            try {
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//
//                //this is the actual part that will call the webservice
//                androidHttpTransport.call(SOAP_ACTION1, envelope);
//
//                // Get the SoapResult from the envelope body.
//                SoapObject result = (SoapObject) envelope.bodyIn;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
//        else if (System.currentTimeMillis() > expirationTime)
//        {
//            Log.d(TAG, "Registro GCM expirado.");
//            return "";
//        }
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
            else
            {
                Log.i(TAG, "Dispositivo no soportado.");
                finish();
            }
            return false;
        }
        return true;
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
            //registerGCM();
        }
    }

    /**
     * Sbouissa 15-07-2015
     * @return
     */
    public String getDeviceID() {
        String id="";

            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            id =  telephonyManager.getDeviceId();

//            try{
//                DB con = new DB(this);
//                con.openDB(DB.READ_WRITE);
//                con.executeSQL("UPDATE UY_MB_Device SET IsActive='N' WHERE UY_MB_Device.DeviceCode <> "+id);
//                con.close();
//            }catch (Exception e){
//                System.out.println(e.toString());
//            }
        return id.toString();
    }


    //FIN CLASE SYNC
    private class TareaRegistroGCM extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";

            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);

                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);

                //Nos registramos en nuestro servidor

                boolean registrado = false;
                DBHelper db = new DBHelper(getBaseContext());
                if(regid!=null){
                    boolean ret = DBHelper.exists("UY_BG_GCMDevice", " DeviceID = '" + m_deviceID + "' ", getBaseContext());
                    if (!ret) {
                        db.inserting("UY_BG_GCMDevice","(TokenID,DeviceID)","'"+regid+"','"+m_deviceID+"'",getBaseContext());
                       // db.executeSQL("INSERT UY_BG_GCMDevice set TokenID = '" + regid + "', DeviceID = '"+m_deviceID+"'");
                        registrado = registroServidor(params[0], regid);
                    }else{
                        boolean ret1 = DBHelper.exists("UY_BG_GCMDevice", "TokenID = '" + regid + "' AND DeviceID = '"+m_deviceID+"' ", getBaseContext());
                        if(!ret1){
                            db.executeSQL("UPDATE UY_BG_GCMDevice set TokenID = '" + regid + "' WHERE DeviceID = '"+m_deviceID+"'");
                            registrado = registroServidor(params[0], regid);
                        }
                    }
                }
                db.close();

                //Guardamos los datos del registro
                if(registrado)
                {
                    setRegistrationId(context, params[0], regid);
                }
            }
            catch (IOException ex)
            {
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
        }

        private boolean registroServidor(String usuario, String regId)
        {
            boolean reg = false;

            //Initialize soap request + add parameters
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            //Use this to add parameters
            request.addProperty("regID", regid);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                //this is the actual part that will call the webservice
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                // Get the SoapResult from the envelope body.
                SoapObject o = (SoapObject)envelope.bodyIn;
                SoapObject result = (SoapObject) envelope.bodyIn;
                Objects results = (Objects) envelope.getResponse();
                if(result!=null){
                    reg = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return reg;
        }

        private void setRegistrationId(Context context, String user, String regId)
        {
            SharedPreferences prefs = getSharedPreferences(
                    MainActivity.class.getSimpleName(),
                    Context.MODE_PRIVATE);

            int appVersion = getAppVersion(context);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PROPERTY_USER, user);
            editor.putString(PROPERTY_REG_ID, regId);
            editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.putLong(PROPERTY_EXPIRATION_TIME,
                    System.currentTimeMillis() + EXPIRATION_TIME_MS);

            editor.commit();
        }
    }//FIN CLASE SYNC


    //metodo para consultar WebService para realizar login
    private boolean loginWebServer(String userIn, String pswIn)
    {
        boolean reg = false;

        final String NAMESPACE = Env.NAMESPACE;
        final String URL=Env.URL;
        final String METHOD_NAME = "login";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ADServicePortType/loginRequest";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapObject adLoginRequest = new SoapObject(NAMESPACE,"ADLoginRequest");
        PropertyInfo usrPI= new PropertyInfo();
        usrPI.setName("user");
        usrPI.setValue(userIn);
        usrPI.setNamespace(NAMESPACE);
        usrPI.setType(String.class);
        adLoginRequest.addProperty(usrPI);

        PropertyInfo pswPI= new PropertyInfo();
        pswPI.setName("pass");
        pswPI.setValue(pswIn);
        pswPI.setNamespace(NAMESPACE);
        pswPI.setType(String.class);
        adLoginRequest.addProperty(pswPI);

        request.addSoapObject(adLoginRequest);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;

        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        try
        {
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml =(SoapObject)envelope.getResponse();
            String status = resultado_xml.getProperty("status").toString();

            //SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
            String res = resultado_xml.toString();

            if(status.equals("1000006"))
            {
                Log.d(TAG, "Registrado en mi servidor.");
                reg = true;
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }
        return reg;
    }

    private boolean isOnline(){
        Boolean ret = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                ret = true;
            }
        }catch (Exception e){
            e.getMessage();
        }
        return ret;
    }


}
