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
import android.widget.Button;
import android.widget.Toast;

import com.app.bagsa.bagsaapp.Utils.DBHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class qrysEBagsaActivity extends ActionBarActivity {


    private String mensajeWS = "";
    private Activity mCtx = null;
    private String recID = "";
    private Boolean retornoWS = false;
    private ProgressDialog pDialog;
    private Button btnContract;
    private Button btnAuctionsAndBids;
    private Button btnCompraVenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrys_ebagsa);

        getViewElements();
        setElementsActions();
        requestContractWS();
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

    public void getViewElements() {
        btnContract = (Button) findViewById(R.id.buttonContract);
        btnAuctionsAndBids = (Button) findViewById(R.id.buttonAuctionsAndBids);
        btnCompraVenta = (Button) findViewById(R.id.buttonbuyAndSell);
    }

    public void setElementsActions() {
        btnContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    startContractActivity();
                } else {
                    CharSequence text = getResources().getString(R.string.noInternet);
                    Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        btnCompraVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    startCompraVentaActivity();
                } else {
                    CharSequence text = getResources().getString(R.string.noInternet);
                    Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void startContractActivity(){
        Intent intent = new Intent(this, ContractActivity.class);
        startActivity(intent);
    }

    public void startCompraVentaActivity(){
        Intent intent = new Intent(this, CompraVentaActivity.class);
        startActivity(intent);
    }

//    private String[] obtenerDatosContract(int sw) {
//        // TODO Auto-generated method stub
//        String[] ColumYVal = new String[2];
//        int i = 0;
//
//        switch (sw) {
//            case 0:
//                ColumYVal[i++] = "AD_User_ID"; //colum
//                ColumYVal[i++] = "1003360"; //val
//                break;
//            case 1:
//                ColumYVal[i++] = "AD_User_ID_2"; //colum
//                ColumYVal[i++] = "1003360"; //val
//                break;
//        }
//
//        return ColumYVal;
//    }

    private String[] obtenerDatosSubastas() {
        // TODO Auto-generated method stub
        String[] ColumYVal = new String[2];
        int i = 0;
        ColumYVal[i++] = "DateTrx"; //colum
        ColumYVal[i++] = "2015-08-10 00:00:00"; //val

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

    public void requestContractWS() {
        pDialog = ProgressDialog.show(this, null, "Consultando datos...", true);
        new Thread() {
            public void run() {
                try {
                    retornoWS = requestContract();
                    retornoWS = requestProducts();
                } catch (Exception e) {
                    e.getMessage();
                }
                pDialog.dismiss();
            }
        }.start();
    }

    private boolean requestContract(){
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
        serviceType.setValue("ConsultarContratos");
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName = new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue("UY_BG_Contract");
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

//        for (int i = 0; i < ColumYVal.length; i++) {
//            field = new SoapObject(NAMESPACE, "field");
//
//            field.addAttribute("column", ColumYVal[i++]);
//
//            PropertyInfo pi2 = new PropertyInfo();
//            pi2.setName("val");
//            pi2.setValue(ColumYVal[i]);
//            pi2.setType(String.class);
//            pi2.setNamespace(NAMESPACE);
//            field.addProperty(pi2);
//
//            DataRow.addSoapObject(field);
//        }

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
            insertContracts(resultado_xml);
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

    private void insertContracts(SoapObject so){

        SoapObject dataResult = (SoapObject)so.getProperty(0);

        int tam = dataResult.getPropertyCount();
        String delims = "[=;]";

        DBHelper db = new DBHelper(this);
        db.openDB(1);
        db.executeSQL("DELETE FROM UY_BG_Contract");

        try{
            if(tam > 0) {
                for (int i = 0; i < tam; i++) {
                    SoapObject dataRow = (SoapObject) dataResult.getProperty(i);
                    String col1[] = dataRow.getProperty(0).toString().split(delims); //ad_user_id--
                    String col2[] = dataRow.getProperty(1).toString().split(delims); //ad_user_id_2--
                    String col3[] = dataRow.getProperty(2).toString().split(delims); //amt--
                    String col4[] = dataRow.getProperty(3).toString().split(delims); //amtretention--
                    String col5[] = dataRow.getProperty(4).toString().split(delims); //c_doctype_id--
                    String col6[] = dataRow.getProperty(5).toString().split(delims); //c_uom_id--
                    String col7[] = dataRow.getProperty(6).toString().split(delims); //datetrx--
                    String col8[] = dataRow.getProperty(7).toString().split(delims); //documentno--
                    String col9[] = dataRow.getProperty(8).toString().split(delims); //m_product_id--
                    String col10[] = dataRow.getProperty(9).toString().split(delims); //priceentered--
                    String col11[] = dataRow.getProperty(10).toString().split(delims); //projecttype--
                    String col12[] = dataRow.getProperty(11).toString().split(delims); //uy_bg_autionbid_id--
                    String col13[] = dataRow.getProperty(12).toString().split(delims); //uy_bg_autionreq_id--
                    String col14[] = dataRow.getProperty(13).toString().split(delims); //uy_bg_contract_id--
                    String col15[] = dataRow.getProperty(14).toString().split(delims); //volume--

                    String qry = "Insert into UY_BG_Contract values (";
                    qry = qry + col14[1] + ",'" + col8[1] + "'," + col5[1] + ",'"+ col11[1] + "','";
                    qry = qry + col7[1] + "'," + col9[1] + "," + col10[1] + "," + col15[1] + ",";
                    qry = qry + col6[1] + "," + col3[1] + "," + col4[1] + ","+ col1[1] +","+col2[1] + ",";
                    qry = qry + col13[1] + "," + col12[1] +")";

                    db.executeSQL(qry);
                }
            }
        } catch (Exception e){
            System.out.print(e);
        }finally {
            db.close();
        }
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

}
