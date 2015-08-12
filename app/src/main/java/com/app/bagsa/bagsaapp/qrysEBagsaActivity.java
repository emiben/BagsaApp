package com.app.bagsa.bagsaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.bagsa.bagsaapp.Utils.Env;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class qrysEBagsaActivity extends ActionBarActivity {


    private String mensajeWS = "";
    private Activity mCtx = null;
    private String recID = "";
    private Boolean retornoWS=false;
    private ProgressDialog pDialog;
    private Button btnContract;
    private Button btnAuctionsAndBids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrys_ebagsa);

        getViewElements();
        setElementsActions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrys_ebagsa, menu);
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
        btnContract = (Button) findViewById(R.id.buttonContract);
        btnAuctionsAndBids = (Button) findViewById(R.id.buttonAuctionsAndBids);
    }

    public void setElementsActions(){
        btnContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    getContractDataWS();
                }else {
                    CharSequence text =  getResources().getString(R.string.noInternet);
                    Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnAuctionsAndBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    getsubastasDataWS();
                }else {
                    CharSequence text =  getResources().getString(R.string.noInternet);
                    Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private String[] obtenerDatosContract() {
        // TODO Auto-generated method stub
        String[] ColumYVal = new String[2];
        int i=0;
        ColumYVal[i++] = "AD_User_ID_2"; //colum
        ColumYVal[i++] = "1003360"; //val

//        ColumYVal[i++] = "Code"; //colum
//        ColumYVal[i++] = in_UserName; //val
//
//        ColumYVal[i++] = "C_DocType_ID"; //colum
//        ColumYVal[i++] = "1001051"; //val

        return ColumYVal;
    }

    private String[] obtenerDatosSubastas() {
        // TODO Auto-generated method stub
        String[] ColumYVal = new String[2];
        int i=0;
        ColumYVal[i++] = "DateTrx"; //colum
        ColumYVal[i++] = "2015-08-10 00:00:00"; //val

        return ColumYVal;
    }

    private boolean getContractDataWS() {
        pDialog = ProgressDialog.show(this, null, "Consultando..", true);
        new Thread(){
            public void run(){
                try{
                    retornoWS = sendRequestContract();
                }catch (Exception e){
                    e.getMessage();
                }
                pDialog.dismiss();
                (mCtx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!retornoWS){
                            Toast toast = Toast.makeText(getBaseContext(), mensajeWS, Toast.LENGTH_LONG);
                            toast.show();
                        }else{
                            Toast toast = Toast.makeText(getBaseContext(), mensajeWS, Toast.LENGTH_LONG);
                            toast.show();
                            //startMainActivity();
                        }
                    }
                });
            }
        }.start();

        return retornoWS;

    }

    private boolean getsubastasDataWS() {
        pDialog = ProgressDialog.show(this, null, "Consultando..", true);
        new Thread(){
            public void run(){
                try{
                    retornoWS = sendRequestSubastasPujas();
                }catch (Exception e){
                    e.getMessage();
                }
                pDialog.dismiss();
                (mCtx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!retornoWS){
                            Toast toast = Toast.makeText(getBaseContext(), mensajeWS, Toast.LENGTH_LONG);
                            toast.show();
                        }else{
                            Toast toast = Toast.makeText(getBaseContext(), mensajeWS, Toast.LENGTH_LONG);
                            toast.show();
                            //startMainActivity();
                        }
                    }
                });
            }
        }.start();

        return retornoWS;

    }

    private Boolean sendRequestContract() {

        String[] ColumYVal = obtenerDatosContract();
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
        serviceType.setValue("ConsultarContratos");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName= new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("UY_BG_Contract");
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

            // PropertyInfo pi= new PropertyInfo();
            //  pi.setName("column");
            //  pi.setValue(ColumYVal[i++]);
            //  pi.setType(String.class);
            //   pi.setNamespace(NAMESPACE);
            //   field.addProperty(pi);
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
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml =(SoapObject)envelope.getResponse();
            Object val = resultado_xml.getAttribute(0);

            int tam = resultado_xml.getPropertyCount();
            String resp = resultado_xml.toString();

            System.out.print(resp);

            resultado_xml.getPropertyCount();

            String col0 = resultado_xml.getAttribute(0).toString();
            String col1 = resultado_xml.getAttribute(1).toString();
            String col2 = resultado_xml.getAttribute(2).toString();


            if(val.toString().contains("true")){
                Object total = resultado_xml.getProperty(0);
                if(total.toString().contains("codeunique_uy_userreq")) {
                    mensajeWS = "USER NO DISPONIBLE";
                }
            }else{
                //String status = resultado_xml.getProperty("StandardResponse").toString();

                //SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                recID = val.toString();

                if(val!= null){
                    try{
                        int a = Integer.valueOf(recID);
                        if(0<a){
                            mensajeWS = "REGISTRO ENVIADO !!";
                            reg = true;
                        }
                    }catch (Exception e){
                        mensajeWS = "No se ha resitrado,intente nuevamente";
                    }
                }
            }

        }
        catch (Exception e)
        {
            mensajeWS = "No se ha resitrado,intente nuevamente";
            e.getMessage();
            //Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }
        return reg;
    }

    private Boolean sendRequestSubastasPujas() {

        String[] ColumYVal = obtenerDatosSubastas();
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
        serviceType.setValue("getSubastasPujas");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName= new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("UY_BG_Offer");
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

            // PropertyInfo pi= new PropertyInfo();
            //  pi.setName("column");
            //  pi.setValue(ColumYVal[i++]);
            //  pi.setType(String.class);
            //   pi.setNamespace(NAMESPACE);
            //   field.addProperty(pi);
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
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml =(SoapObject)envelope.getResponse();
            Object val = resultado_xml.getAttribute(0);

            int tam = resultado_xml.getPropertyCount();
            String resp = resultado_xml.toString();

            String prop1 = resultado_xml.getProperty(0).toString();
            String prop2 = resultado_xml.getProperty(1).toString();
            String prop3 = resultado_xml.getProperty(2).toString();

            System.out.print(resp);

            String col0 = resultado_xml.getAttribute(0).toString();
            String col1 = resultado_xml.getAttribute(1).toString();
            String col2 = resultado_xml.getAttribute(2).toString();


            if(val.toString().contains("true")){
                Object total = resultado_xml.getProperty(0);
                if(total.toString().contains("codeunique_uy_userreq")) {
                    mensajeWS = "USER NO DISPONIBLE";
                }
            }else{
                //String status = resultado_xml.getProperty("StandardResponse").toString();

                //SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                recID = val.toString();

                if(val!= null){
                    try{
                        int a = Integer.valueOf(recID);
                        if(0<a){
                            mensajeWS = "REGISTRO ENVIADO !!";
                            reg = true;
                        }
                    }catch (Exception e){
                        mensajeWS = "No se ha resitrado,intente nuevamente";
                    }
                }
            }

        }
        catch (Exception e)
        {
            mensajeWS = "No se ha resitrado,intente nuevamente";
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

}
