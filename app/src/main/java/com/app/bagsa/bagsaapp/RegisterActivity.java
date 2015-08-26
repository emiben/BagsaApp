package com.app.bagsa.bagsaapp;

import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bagsa.bagsaapp.Utils.Env;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;


public class RegisterActivity extends ActionBarActivity {

    private String mensajeWS = "";
    private Activity mCtx = null;
    private String recID = "";
    private Boolean retornoWS=false;
    private ProgressDialog pDialog;

    private EditText eTxtFullName;
    private String in_FullName = "";
    private EditText eTxtPhone;
    private String in_Phone = "";
    private EditText eTxtLocalidad;
    private String in_Localidad = "";
    private EditText eTxtEmail;
    private String in_Email = "";
    private EditText eTxtUserName;
    private String in_UserName = "";
    private EditText eTxtPsw;
    private String in_Psw= "";
    private EditText eTxtConfirmPsw;
    private String in_ConfPsw= "";
    private CheckBox chBTransac;
    private Boolean in_Transac = false;
    private CheckBox getChBTransacBgsa;
    private Boolean in_TransacBgsa = false;
    private String in_TypoUsuario = "";



    private Spinner userTypes;
    private TextView linkLogIn;
    private Button BtnRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_register);
        mCtx = this;

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

        eTxtFullName = (EditText) findViewById(R.id.reg_fullname);
        eTxtPhone = (EditText) findViewById(R.id.reg_CellPhone);
        eTxtLocalidad = (EditText) findViewById(R.id.reg_localidad);
        eTxtEmail = (EditText) findViewById(R.id.reg_email);
        eTxtUserName = (EditText) findViewById(R.id.reg_username);
        eTxtPsw = (EditText) findViewById(R.id.reg_password);
        eTxtConfirmPsw = (EditText) findViewById(R.id.reg_conf_pass);

        chBTransac = (CheckBox) findViewById(R.id.checkBoxTransact);
        getChBTransacBgsa = (CheckBox) findViewById(R.id.checkBoxTransact);
    }

    private void setElementsEvents() {
        BtnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    String mje = verficarDataIn();
                    if(mje.equals("OK")){
                        if(sendDataWS()){
                          //  startMainActivity();
                        }
                    }else{
                        //Toast toast = Toast.makeText(getBaseContext(), mje, Toast.LENGTH_SHORT);
                        //toast.show();
                    }
                }else {
                    CharSequence text =  getResources().getString(R.string.noInternet);
                    Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        linkLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
    }

    private boolean sendDataWS() {
        pDialog = ProgressDialog.show(this, null, "Consultando..", true);
        new Thread(){
            public void run(){
                try{
                    retornoWS = sendRequestWebServer();
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
                            startMainActivity();
                        }
                    }
                });
            }
        }.start();

        return retornoWS;

    }

    private Boolean sendRequestWebServer() {

        String[] ColumYVal = obtenerDatos();
        boolean reg = false;

        final String NAMESPACE = "http://3e.pl/ADInterface";
        final String URL = "http://200.71.26.66:6050/ADInterface-1.0/services/ModelADService";
        final String METHOD_NAME = "createData";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ModelADServicePortType/createDataRequest";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapObject ModelCRUDRequest = new SoapObject(NAMESPACE, "ModelCRUDRequest");
        SoapObject ModelCRUD =  new SoapObject(NAMESPACE, "ModelCRUD");

        PropertyInfo serviceType= new PropertyInfo();
        serviceType.setName("serviceType");
        serviceType.setValue("RegisterMobileUser");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName= new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("UY_UserReq");
        TableName.setNamespace(NAMESPACE);
        TableName.setType(String.class);
        ModelCRUD.addProperty(TableName);
       // ModelCRUD.addProperty("TableName", "UY_UserReq");
        PropertyInfo RecordID= new PropertyInfo();
        RecordID.setName("RecordID");
        RecordID.setValue("1");
        RecordID.setNamespace(NAMESPACE);
        RecordID.setType(String.class);
        ModelCRUD.addProperty(RecordID);
        //ModelCRUD.addProperty("RecordID", "1");
        PropertyInfo Action= new PropertyInfo();
        Action.setName("Action");
        Action.setValue("Create");
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

    private String verficarDataIn() {

        in_FullName = ((eTxtFullName.getText()!=null)?eTxtFullName.getText().toString():"");
        in_Phone = ((eTxtPhone.getText()!=null)?eTxtPhone.getText().toString():"");
        in_Localidad = ((eTxtLocalidad.getText()!=null)?eTxtLocalidad.getText().toString():"");
        in_Email = ((eTxtEmail.getText()!=null)? eTxtEmail.getText().toString():"");
        in_UserName = ((eTxtUserName.getText()!=null)?eTxtUserName.getText().toString():"");
        in_Psw = ((eTxtPsw.getText()!=null)?eTxtPsw.getText().toString():"");
        in_ConfPsw = ((eTxtConfirmPsw.getText()!=null)?eTxtConfirmPsw.getText().toString():"");
        in_TypoUsuario =  userTypes.getSelectedItem().toString();
        in_Transac =  chBTransac.isChecked();
        in_TransacBgsa = getChBTransacBgsa.isChecked();

        if("".equals(in_FullName)||"".equals(in_Phone)||"".equals(in_Localidad)||
                "".equals(in_Email)||"".equals(in_UserName)||"".equals(in_Psw)||
                "".equals(in_ConfPsw)||"".equals(in_TypoUsuario)){
            return "Se requieren todos los datos";

        }else if(in_Psw.equals(in_ConfPsw)){
            return "OK";
        }else return "Las contrasenas no coinciden";
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
        userTyp.add(getResources().getString(R.string.exportador));
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

    private String[] obtenerDatos() {
        // TODO Auto-generated method stub
        String[] ColumYVal = new String[24];
        int i=0;
        ColumYVal[i++] = "Name"; //colum
        ColumYVal[i++] = in_FullName; //val

        ColumYVal[i++] = "Code"; //colum
        ColumYVal[i++] = in_UserName; //val

        ColumYVal[i++] = "C_DocType_ID"; //colum
        ColumYVal[i++] = "1001051"; //val

        ColumYVal[i++] = "EMail"; //colum
        ColumYVal[i++] = in_Email; //val

        ColumYVal[i++] = "Address1"; //colum
        ColumYVal[i++] = in_Localidad; //val

        ColumYVal[i++] = "DateTrx"; //colum
        ColumYVal[i++] = "2015-07-15 00:00:00"; //val

        ColumYVal[i++] = "Phone"; //colum
        ColumYVal[i++] = in_Phone;

        ColumYVal[i++] = "UserLevel"; //colum
        ColumYVal[i++] = in_TypoUsuario; //val

        ColumYVal[i++] = "DocStatus"; //colum
        ColumYVal[i++] = "DR"; //val

        ColumYVal[i++] = "Password"; //colum
        ColumYVal[i++] = in_Psw; //val

        ColumYVal[i++] = "HaveAttach1"; //colum Envio siempre 0 y en document no  configuro id de ruta
        ColumYVal[i++] = ((in_Transac)?"Y":"N"); //val
        //ordenEnvio.getDocumentNo()
        ColumYVal[i++] = "HaveAttach2"; //colum
        ColumYVal[i++] = ((in_TransacBgsa)?"Y":"N"); //val //val

        return ColumYVal;
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
