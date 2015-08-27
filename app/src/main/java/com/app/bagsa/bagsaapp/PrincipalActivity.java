package com.app.bagsa.bagsaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.bagsa.bagsaapp.Utils.DBHelper;
import com.app.bagsa.bagsaapp.Utils.Env;
import com.readystatesoftware.viewbadger.BadgeView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class PrincipalActivity extends ActionBarActivity {

    private Button notif;
    private Button consPrices;
    private Button reports;
    private Button consEBag;
    private Button transact;
    private View target;
    private BadgeView badge;
    private Boolean retornoWS = false;
    private String mensajeWS = "";

    private ProgressDialog pDialog;

    //sbouissa 02-07-2015
    private int userID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // get the Intent that started this Activity
        Intent in = getIntent();
        // get the Bundle that stores the data of this Activity
        Bundle b = in.getExtras();
        if(null!=b){
           userID = b.getInt("UserID");
        }
        getViewElements();
        setElementsEvents();
        setQuantityOfNotif();
        if(isOnline() && null==b){
            runGetAdUser();
        }
        runGetData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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
        notif = (Button) findViewById(R.id.button);
        consPrices = (Button) findViewById(R.id.button2);
        reports = (Button) findViewById(R.id.button3);
        consEBag = (Button) findViewById(R.id.button5);
        transact = (Button) findViewById(R.id.button6);
        if(userID>0){
            transact.setVisibility(View.GONE);
            consEBag.setVisibility(View.GONE);
        }
        target = findViewById(R.id.button);
        badge = new BadgeView(this, target);
    }

    private void setElementsEvents() {
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReportsActivity();
            }
        });
        consEBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrysEBagsaActivity();
            }
        });
        consPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrysPricesActivity();
            }
        });

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNotificationsActivity();
            }
        });


    }

    private void setQuantityOfNotif() {
        int number = Env.getNotificationsCount();
        if(0<number){
            badge.setText(String.valueOf(number));
            badge.show();
        }else{
            badge.hide();
        }
    }

    private void startNotificationsActivity() {
        Intent i = new Intent(this, NotificationsActivity.class);
        startActivity(i);
    }

    private void startReportsActivity() {
        Intent i = new Intent(this, ReportsActivity.class);
        startActivity(i);
    }

    private void startQrysEBagsaActivity() {
        Intent i = new Intent(this, qrysEBagsaActivity.class);
        startActivity(i);
    }

    private void startQrysPricesActivity() {
        Intent i = new Intent(this, QryPricesActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Env.setNotificationsCount(0);
        startMainActivity();
    }
    private void startMainActivity() {
        this.finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setQuantityOfNotif();
    }

    private String[] obtenerDatos() {
        // TODO Auto-generated method stub
        Env e = new Env();
        String[] ColumYVal = new String[6];
        int i=0;
        ColumYVal[i++] = "Name"; //colum
        ColumYVal[i++] = e.getUser(); //val "emiben"; //

        ColumYVal[i++] = "Password"; //colum
        ColumYVal[i++] = e.getPass(); //val "emiben"; //

        ColumYVal[i++] = "IsActive"; //colum
        ColumYVal[i++] = "Y"; //e.getPass(); //val

        return ColumYVal;
    }

    public void runGetAdUser() {
        pDialog = ProgressDialog.show(this, null, "Consultando datos...", true);
        new Thread() {
            public void run() {
                try {
                    getAdUserID();
                } catch (Exception e) {
                    e.getMessage();
                }
                pDialog.dismiss();
            }
        }.start();
    }



    public void runGetData() {
        final ProgressDialog pDialog2 = ProgressDialog.show(this, null, "Consultando datos...", true);
        new Thread() {
            public void run() {
                try {
                    retornoWS = requestProducts();
                    retornoWS = getDocTypes();
                    retornoWS = getPrices();
                } catch (Exception e) {
                    e.getMessage();
                }
                pDialog2.dismiss();
            }
        }.start();
    }

    private Boolean getAdUserID() {

        String[] ColumYVal = obtenerDatos();
        boolean reg = false;

        final String NAMESPACE = "http://3e.pl/ADInterface";
        final String URL = "http://200.71.26.66:6050/ADInterface-1.0/services/ModelADService";
        final String METHOD_NAME = "queryData";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ModelADServicePortType/queryDataRequest";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapObject ModelCRUDRequest = new SoapObject(NAMESPACE, "ModelCRUDRequest");
        SoapObject ModelCRUD =  new SoapObject(NAMESPACE, "ModelCRUD");

        PropertyInfo serviceType= new PropertyInfo();
        serviceType.setName("serviceType");
        serviceType.setValue("getAdUserId");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName= new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("AD_User");
        TableName.setNamespace(NAMESPACE);
        TableName.setType(String.class);
        ModelCRUD.addProperty(TableName);
        // ModelCRUD.addProperty("TableName", "UY_UserReq");
        PropertyInfo RecordID= new PropertyInfo();
        RecordID.setName("RecordID");
        RecordID.setValue("0");
        RecordID.setNamespace(NAMESPACE);
        RecordID.setType(String.class);
        ModelCRUD.addProperty(RecordID);
        //ModelCRUD.addProperty("RecordID", "1");
        PropertyInfo Action= new PropertyInfo();
        Action.setName("Action");
        Action.setValue("Read");
        Action.setNamespace(NAMESPACE);
        Action.setType(String.class);
        ModelCRUD.addProperty(Action);
        // ModelCRUD.addProperty("Action", "Create");

        SoapObject DataRow = new SoapObject(NAMESPACE, "DataRow");
        SoapObject field;

        for(int i = 0;i<ColumYVal.length;i++){
            field = new SoapObject(NAMESPACE, "field");

            field.addAttribute("column", ColumYVal[i++]);

            PropertyInfo pi2 = new PropertyInfo();
            //SoapObject pi2 = new SoapObject(NAMESPACE,"val");
            //PropertyInfo pi= new PropertyInfo();
            pi2.setName("val");
            pi2.setValue(ColumYVal[i]);
            pi2.setType(String.class);
            pi2.setNamespace(NAMESPACE);
            field.addProperty(pi2);

            //field.addSoapObject(pi2);
            // field.addProperty("val", ColumYVal[i]);
            DataRow.addSoapObject(field);
        }

        ModelCRUD.addSoapObject(DataRow);
        ModelCRUDRequest.addSoapObject(ModelCRUD);

        SoapObject ADLoginRequest = new SoapObject(NAMESPACE, "ADLoginRequest");
        String usr = "sbouissa";
        PropertyInfo usrPI= new PropertyInfo();
        usrPI.setName("user");
        usrPI.setValue("sbouissa");
        usrPI.setNamespace(NAMESPACE);
        usrPI.setType(String.class);
        ADLoginRequest.addProperty(usrPI);
        PropertyInfo pswPI= new PropertyInfo();
        pswPI.setName("pass");
        pswPI.setValue("sbouissa");
        pswPI.setNamespace(NAMESPACE);
        pswPI.setType(String.class);
        ADLoginRequest.addProperty(pswPI);
        //ADLoginRequest.addProperty("user",  String.valueOf(usr));
        //ADLoginRequest.addProperty("pass", String.valueOf(usr));
        PropertyInfo lang= new PropertyInfo();
        lang.setName("lang");
        lang.setValue("143");
        lang.setNamespace(NAMESPACE);
        lang.setType(String.class);
        ADLoginRequest.addProperty(lang);
        //ADLoginRequest.addProperty("lang","143");
        // ADLoginRequest.addProperty("lang", String.valueOf(Env.getAD_Language(m_Ctx))));
        PropertyInfo cli= new PropertyInfo();
        cli.setName("ClientID");
        cli.setValue("1000006");
        cli.setNamespace(NAMESPACE);
        cli.setType(String.class);
        ADLoginRequest.addProperty(cli);
        //ADLoginRequest.addProperty("ClientID", "1000006");
        //  ADLoginRequest.addProperty("RoleID", String.valueOf(Env.getAD_Role_ID(m_Ctx)));
        PropertyInfo rol= new PropertyInfo();
        rol.setName("RoleID");
        rol.setValue("1000022");
        rol.setNamespace(NAMESPACE);
        rol.setType(String.class);
        ADLoginRequest.addProperty(rol);
        //ADLoginRequest.addProperty("RoleID", "1000022");
        PropertyInfo org= new PropertyInfo();
        org.setName("OrgID");
        org.setValue("1000007");
        org.setNamespace(NAMESPACE);
        org.setType(String.class);
        ADLoginRequest.addProperty(org);
        // ADLoginRequest.addProperty("OrgID", "1000007");
        //   ADLoginRequest.addProperty("WarehouseID",String.valueOf(Env.getM_Warehouse_ID(m_Ctx)));
        PropertyInfo ware= new PropertyInfo();
        ware.setName("WarehouseID");
        ware.setValue("1000052");
        ware.setNamespace(NAMESPACE);
        ware.setType(String.class);
        ADLoginRequest.addProperty(ware);
        //ADLoginRequest.addProperty("WarehouseID","1000052");
        ModelCRUDRequest.addSoapObject(ADLoginRequest);

        request.addSoapObject(ModelCRUDRequest);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;

        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        try
        {
            Env e = new Env();
            String delims = "[=;]";
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml =(SoapObject) envelope.getResponse();
            SoapObject val = (SoapObject)resultado_xml.getProperty(0);
            String col[] = val.getProperty(0).toString().split(delims);
            e.setadusr(col[2]);
        }
        catch (Exception e)
        {
            e.getMessage();
            //Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
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

    private boolean requestProducts(){
        boolean reg = false;

        //String[] ColumYVal = obtenerDatosContract(j);

        final String NAMESPACE = "http://3e.pl/ADInterface";
        final String URL = "http://200.71.26.66:6050/ADInterface-1.0/services/ModelADService";
        final String METHOD_NAME = "queryData";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ModelADServicePortType/queryDataRequest";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapObject ModelCRUDRequest = new SoapObject(NAMESPACE, "ModelCRUDRequest");
        SoapObject ModelCRUD = new SoapObject(NAMESPACE, "ModelCRUD");

        PropertyInfo serviceType = new PropertyInfo();
        serviceType.setName("serviceType");
        serviceType.setValue("getProducts");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName = new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("M_Product");
        TableName.setNamespace(NAMESPACE);
        TableName.setType(String.class);
        ModelCRUD.addProperty(TableName);
        // ModelCRUD.addProperty("TableName", "UY_UserReq");
        PropertyInfo RecordID = new PropertyInfo();
        RecordID.setName("RecordID");
        RecordID.setValue("0");
        RecordID.setNamespace(NAMESPACE);
        RecordID.setType(String.class);
        ModelCRUD.addProperty(RecordID);
        //ModelCRUD.addProperty("RecordID", "1");
        PropertyInfo Action = new PropertyInfo();
        Action.setName("Action");
        Action.setValue("Read");
        Action.setNamespace(NAMESPACE);
        Action.setType(String.class);
        ModelCRUD.addProperty(Action);
        // ModelCRUD.addProperty("Action", "Create");

        SoapObject DataRow = new SoapObject(NAMESPACE, "DataRow");
        SoapObject field;


        ModelCRUD.addSoapObject(DataRow);
        ModelCRUDRequest.addSoapObject(ModelCRUD);

        SoapObject ADLoginRequest = new SoapObject(NAMESPACE, "ADLoginRequest");
        String usr = "sbouissa";
        PropertyInfo usrPI = new PropertyInfo();
        usrPI.setName("user");
        usrPI.setValue("sbouissa");
        usrPI.setNamespace(NAMESPACE);
        usrPI.setType(String.class);
        ADLoginRequest.addProperty(usrPI);
        PropertyInfo pswPI = new PropertyInfo();
        pswPI.setName("pass");
        pswPI.setValue("sbouissa");
        pswPI.setNamespace(NAMESPACE);
        pswPI.setType(String.class);
        ADLoginRequest.addProperty(pswPI);
        //ADLoginRequest.addProperty("user",  String.valueOf(usr));
        //ADLoginRequest.addProperty("pass", String.valueOf(usr));
        PropertyInfo lang = new PropertyInfo();
        lang.setName("lang");
        lang.setValue("143");
        lang.setNamespace(NAMESPACE);
        lang.setType(String.class);
        ADLoginRequest.addProperty(lang);
        //ADLoginRequest.addProperty("lang","143");
        // ADLoginRequest.addProperty("lang", String.valueOf(Env.getAD_Language(m_Ctx))));
        PropertyInfo cli = new PropertyInfo();
        cli.setName("ClientID");
        cli.setValue("1000006");
        cli.setNamespace(NAMESPACE);
        cli.setType(String.class);
        ADLoginRequest.addProperty(cli);
        //ADLoginRequest.addProperty("ClientID", "1000006");
        //  ADLoginRequest.addProperty("RoleID", String.valueOf(Env.getAD_Role_ID(m_Ctx)));
        PropertyInfo rol = new PropertyInfo();
        rol.setName("RoleID");
        rol.setValue("1000022");
        rol.setNamespace(NAMESPACE);
        rol.setType(String.class);
        ADLoginRequest.addProperty(rol);
        //ADLoginRequest.addProperty("RoleID", "1000022");
        PropertyInfo org = new PropertyInfo();
        org.setName("OrgID");
        org.setValue("1000007");
        org.setNamespace(NAMESPACE);
        org.setType(String.class);
        ADLoginRequest.addProperty(org);
        // ADLoginRequest.addProperty("OrgID", "1000007");
        //   ADLoginRequest.addProperty("WarehouseID",String.valueOf(Env.getM_Warehouse_ID(m_Ctx)));
        PropertyInfo ware = new PropertyInfo();
        ware.setName("WarehouseID");
        ware.setValue("1000052");
        ware.setNamespace(NAMESPACE);
        ware.setType(String.class);
        ADLoginRequest.addProperty(ware);
        //ADLoginRequest.addProperty("WarehouseID","1000052");
        ModelCRUDRequest.addSoapObject(ADLoginRequest);

        request.addSoapObject(ModelCRUDRequest);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;

        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();
            insertProducts(resultado_xml);
            reg = true;
            transporte.reset();
        } catch (Exception e) {
            reg = false;
            mensajeWS = "Error al consultar los contrtos, por favor intente nuevamente.";
            e.getMessage();
            //Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }
        return reg;
    }

    private void insertProducts(SoapObject so){

        SoapObject dataResult = (SoapObject)so.getProperty(0);

        int tam = dataResult.getPropertyCount();
        String delims = "[=;]";

        DBHelper db = new DBHelper(this);
        db.openDB(1);
        db.executeSQL("DELETE FROM m_product");

        try{
            if(tam > 0) {
                for (int i = 0; i < tam; i++) {
                    SoapObject dataRow = (SoapObject) dataResult.getProperty(i);
                    String col1[] = dataRow.getProperty(0).toString().split(delims); //ad_user_id--
                    String col2[] = dataRow.getProperty(1).toString().split(delims); //ad_user_id_2--

                    String qry = "Insert into m_product values (";
                    qry = qry + col1[1] + ",'" + col2[1] +"')";

                    db.executeSQL(qry);
                }
            }
        } catch (Exception e){
            System.out.print(e);
        }finally {
            db.close();
        }
    }


    private boolean getDocTypes(){
        boolean reg = false;

        //String[] ColumYVal = obtenerDatosContract(j);

        final String NAMESPACE = "http://3e.pl/ADInterface";
        final String URL = "http://200.71.26.66:6050/ADInterface-1.0/services/ModelADService";
        final String METHOD_NAME = "queryData";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ModelADServicePortType/queryDataRequest";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapObject ModelCRUDRequest = new SoapObject(NAMESPACE, "ModelCRUDRequest");
        SoapObject ModelCRUD = new SoapObject(NAMESPACE, "ModelCRUD");

        PropertyInfo serviceType = new PropertyInfo();
        serviceType.setName("serviceType");
        serviceType.setValue("getBagsaDocType");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName = new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("VUY_Bagsa_doctype_vw");
        TableName.setNamespace(NAMESPACE);
        TableName.setType(String.class);
        ModelCRUD.addProperty(TableName);
        // ModelCRUD.addProperty("TableName", "UY_UserReq");
        PropertyInfo RecordID = new PropertyInfo();
        RecordID.setName("RecordID");
        RecordID.setValue("0");
        RecordID.setNamespace(NAMESPACE);
        RecordID.setType(String.class);
        ModelCRUD.addProperty(RecordID);
        //ModelCRUD.addProperty("RecordID", "1");
        PropertyInfo Action = new PropertyInfo();
        Action.setName("Action");
        Action.setValue("Read");
        Action.setNamespace(NAMESPACE);
        Action.setType(String.class);
        ModelCRUD.addProperty(Action);
        // ModelCRUD.addProperty("Action", "Create");

        SoapObject DataRow = new SoapObject(NAMESPACE, "DataRow");
        SoapObject field;


        ModelCRUD.addSoapObject(DataRow);
        ModelCRUDRequest.addSoapObject(ModelCRUD);

        SoapObject ADLoginRequest = new SoapObject(NAMESPACE, "ADLoginRequest");
        String usr = "sbouissa";
        PropertyInfo usrPI = new PropertyInfo();
        usrPI.setName("user");
        usrPI.setValue("sbouissa");
        usrPI.setNamespace(NAMESPACE);
        usrPI.setType(String.class);
        ADLoginRequest.addProperty(usrPI);
        PropertyInfo pswPI = new PropertyInfo();
        pswPI.setName("pass");
        pswPI.setValue("sbouissa");
        pswPI.setNamespace(NAMESPACE);
        pswPI.setType(String.class);
        ADLoginRequest.addProperty(pswPI);
        //ADLoginRequest.addProperty("user",  String.valueOf(usr));
        //ADLoginRequest.addProperty("pass", String.valueOf(usr));
        PropertyInfo lang = new PropertyInfo();
        lang.setName("lang");
        lang.setValue("143");
        lang.setNamespace(NAMESPACE);
        lang.setType(String.class);
        ADLoginRequest.addProperty(lang);
        //ADLoginRequest.addProperty("lang","143");
        // ADLoginRequest.addProperty("lang", String.valueOf(Env.getAD_Language(m_Ctx))));
        PropertyInfo cli = new PropertyInfo();
        cli.setName("ClientID");
        cli.setValue("1000006");
        cli.setNamespace(NAMESPACE);
        cli.setType(String.class);
        ADLoginRequest.addProperty(cli);
        //ADLoginRequest.addProperty("ClientID", "1000006");
        //  ADLoginRequest.addProperty("RoleID", String.valueOf(Env.getAD_Role_ID(m_Ctx)));
        PropertyInfo rol = new PropertyInfo();
        rol.setName("RoleID");
        rol.setValue("1000022");
        rol.setNamespace(NAMESPACE);
        rol.setType(String.class);
        ADLoginRequest.addProperty(rol);
        //ADLoginRequest.addProperty("RoleID", "1000022");
        PropertyInfo org = new PropertyInfo();
        org.setName("OrgID");
        org.setValue("1000007");
        org.setNamespace(NAMESPACE);
        org.setType(String.class);
        ADLoginRequest.addProperty(org);
        // ADLoginRequest.addProperty("OrgID", "1000007");
        //   ADLoginRequest.addProperty("WarehouseID",String.valueOf(Env.getM_Warehouse_ID(m_Ctx)));
        PropertyInfo ware = new PropertyInfo();
        ware.setName("WarehouseID");
        ware.setValue("1000052");
        ware.setNamespace(NAMESPACE);
        ware.setType(String.class);
        ADLoginRequest.addProperty(ware);
        //ADLoginRequest.addProperty("WarehouseID","1000052");
        ModelCRUDRequest.addSoapObject(ADLoginRequest);

        request.addSoapObject(ModelCRUDRequest);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;

        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();
            insertDocTypes(resultado_xml);
            reg = true;
            transporte.reset();
        } catch (Exception e) {
            reg = false;
            mensajeWS = "Error al consultar los contrtos, por favor intente nuevamente.";
            e.getMessage();
            //Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }
        return reg;
    }

    private void insertDocTypes(SoapObject so){

        SoapObject dataResult = (SoapObject)so.getProperty(0);

        int tam = dataResult.getPropertyCount();
        String delims = "[=;]";

        DBHelper db = new DBHelper(this);
        db.openDB(1);
        db.executeSQL("DELETE FROM VUY_Bagsa_doctype");

        try{
            if(tam > 0) {
                for (int i = 0; i < tam; i++) {
                    SoapObject dataRow = (SoapObject) dataResult.getProperty(i);
                    String col1[] = dataRow.getProperty(0).toString().split(delims); //ad_user_id--
                    String col2[] = dataRow.getProperty(1).toString().split(delims); //ad_user_id_2--

                    String qry = "Insert into VUY_Bagsa_doctype values (";
                    qry = qry + col1[1] + ",'" + col2[1] +"')";

                    db.executeSQL(qry);
                }
            }
        } catch (Exception e){
            System.out.print(e);
        }finally {
            db.close();
        }
    }


    private boolean getPrices(){
        boolean reg = false;

        //String[] ColumYVal = obtenerDatosContract(j);

        final String NAMESPACE = "http://3e.pl/ADInterface";
        final String URL = "http://200.71.26.66:6050/ADInterface-1.0/services/ModelADService";
        final String METHOD_NAME = "queryData";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ModelADServicePortType/queryDataRequest";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapObject ModelCRUDRequest = new SoapObject(NAMESPACE, "ModelCRUDRequest");
        SoapObject ModelCRUD = new SoapObject(NAMESPACE, "ModelCRUD");

        PropertyInfo serviceType = new PropertyInfo();
        serviceType.setName("serviceType");
        serviceType.setValue("getPrices");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName = new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("UY_BG_DailyLinePrice");
        TableName.setNamespace(NAMESPACE);
        TableName.setType(String.class);
        ModelCRUD.addProperty(TableName);
        // ModelCRUD.addProperty("TableName", "UY_UserReq");
        PropertyInfo RecordID = new PropertyInfo();
        RecordID.setName("RecordID");
        RecordID.setValue("0");
        RecordID.setNamespace(NAMESPACE);
        RecordID.setType(String.class);
        ModelCRUD.addProperty(RecordID);
        //ModelCRUD.addProperty("RecordID", "1");
        PropertyInfo Action = new PropertyInfo();
        Action.setName("Action");
        Action.setValue("Read");
        Action.setNamespace(NAMESPACE);
        Action.setType(String.class);
        ModelCRUD.addProperty(Action);
        // ModelCRUD.addProperty("Action", "Create");

        SoapObject DataRow = new SoapObject(NAMESPACE, "DataRow");
        SoapObject field;


        ModelCRUD.addSoapObject(DataRow);
        ModelCRUDRequest.addSoapObject(ModelCRUD);

        SoapObject ADLoginRequest = new SoapObject(NAMESPACE, "ADLoginRequest");
        String usr = "sbouissa";
        PropertyInfo usrPI = new PropertyInfo();
        usrPI.setName("user");
        usrPI.setValue("sbouissa");
        usrPI.setNamespace(NAMESPACE);
        usrPI.setType(String.class);
        ADLoginRequest.addProperty(usrPI);
        PropertyInfo pswPI = new PropertyInfo();
        pswPI.setName("pass");
        pswPI.setValue("sbouissa");
        pswPI.setNamespace(NAMESPACE);
        pswPI.setType(String.class);
        ADLoginRequest.addProperty(pswPI);
        //ADLoginRequest.addProperty("user",  String.valueOf(usr));
        //ADLoginRequest.addProperty("pass", String.valueOf(usr));
        PropertyInfo lang = new PropertyInfo();
        lang.setName("lang");
        lang.setValue("143");
        lang.setNamespace(NAMESPACE);
        lang.setType(String.class);
        ADLoginRequest.addProperty(lang);
        //ADLoginRequest.addProperty("lang","143");
        // ADLoginRequest.addProperty("lang", String.valueOf(Env.getAD_Language(m_Ctx))));
        PropertyInfo cli = new PropertyInfo();
        cli.setName("ClientID");
        cli.setValue("1000006");
        cli.setNamespace(NAMESPACE);
        cli.setType(String.class);
        ADLoginRequest.addProperty(cli);
        //ADLoginRequest.addProperty("ClientID", "1000006");
        //  ADLoginRequest.addProperty("RoleID", String.valueOf(Env.getAD_Role_ID(m_Ctx)));
        PropertyInfo rol = new PropertyInfo();
        rol.setName("RoleID");
        rol.setValue("1000022");
        rol.setNamespace(NAMESPACE);
        rol.setType(String.class);
        ADLoginRequest.addProperty(rol);
        //ADLoginRequest.addProperty("RoleID", "1000022");
        PropertyInfo org = new PropertyInfo();
        org.setName("OrgID");
        org.setValue("1000007");
        org.setNamespace(NAMESPACE);
        org.setType(String.class);
        ADLoginRequest.addProperty(org);
        // ADLoginRequest.addProperty("OrgID", "1000007");
        //   ADLoginRequest.addProperty("WarehouseID",String.valueOf(Env.getM_Warehouse_ID(m_Ctx)));
        PropertyInfo ware = new PropertyInfo();
        ware.setName("WarehouseID");
        ware.setValue("1000052");
        ware.setNamespace(NAMESPACE);
        ware.setType(String.class);
        ADLoginRequest.addProperty(ware);
        //ADLoginRequest.addProperty("WarehouseID","1000052");
        ModelCRUDRequest.addSoapObject(ADLoginRequest);

        request.addSoapObject(ModelCRUDRequest);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;

        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();
            insertPrices(resultado_xml);
            reg = true;
            transporte.reset();
        } catch (Exception e) {
            reg = false;
            mensajeWS = "Error al consultar los contrtos, por favor intente nuevamente.";
            e.getMessage();
            //Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }
        return reg;
    }

    private void insertPrices(SoapObject so){

        SoapObject dataResult = (SoapObject)so.getProperty(0);

        int tam = dataResult.getPropertyCount();
        String delims = "[=;]";

        DBHelper db = new DBHelper(this);
        db.openDB(1);
        db.executeSQL("DELETE FROM UY_BG_dailypriceline");

        try{
            if(tam > 0) {
                for (int i = 0; i < tam; i++) {
                    SoapObject dataRow = (SoapObject) dataResult.getProperty(i);
                    String col1[] = dataRow.getProperty(0).toString().split(delims);//product_id
                    String col2[] = dataRow.getProperty(1).toString().split(delims);//Price
                    String col3[] = dataRow.getProperty(2).toString().split(delims);//PriceEntered
                    String col4[] = dataRow.getProperty(3).toString().split(delims);//date

                    String qry = "Insert into UY_BG_dailypriceline values (";
                    qry = qry + col1[1] + "," + col2[1] + "," + col3[1] + ",'" + col4[1] + "')";

                    db.executeSQL(qry);
                }
            }
        } catch (Exception e){
            System.out.print(e);
        }finally {
            db.close();
        }
    }



}
