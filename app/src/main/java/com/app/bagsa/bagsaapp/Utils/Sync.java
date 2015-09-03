package com.app.bagsa.bagsaapp.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Emilino on 01/09/2015.
 */
public class Sync {

    public static final String URL = "http://200.71.26.66:6050/ADInterface-1.0/services/ModelADService";
    public static final String NAMESPACE = "http://3e.pl/ADInterface";

    public String[] obtenerDatos() {
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


    public Boolean getFromWS(String method, String table, String user, String pass) {

        String[] ColumYVal = obtenerDatos();
        boolean reg = false;

        //final String NAMESPACE = "http://3e.pl/ADInterface";
        //final String URL = "http://200.71.26.66:6050/ADInterface-1.0/services/ModelADService";
        final String METHOD_NAME = "queryData";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ModelADServicePortType/queryDataRequest";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapObject ModelCRUDRequest = new SoapObject(NAMESPACE, "ModelCRUDRequest");
        SoapObject ModelCRUD =  new SoapObject(NAMESPACE, "ModelCRUD");

        PropertyInfo serviceType= new PropertyInfo();
        serviceType.setName("serviceType");
        serviceType.setValue(method);
        serviceType.setNamespace(NAMESPACE);
        serviceType.setType(String.class);
        ModelCRUD.addProperty(serviceType);
        //ModelCRUD.addProperty("serviceType", "RegisterMobileUser");
        PropertyInfo TableName= new PropertyInfo();
        TableName.setName("TableName");
        TableName.setValue(table);
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
        String usr = user;
        PropertyInfo usrPI= new PropertyInfo();
        usrPI.setName("user");
        usrPI.setValue(user);
        usrPI.setNamespace(NAMESPACE);
        usrPI.setType(String.class);
        ADLoginRequest.addProperty(usrPI);
        PropertyInfo pswPI= new PropertyInfo();
        pswPI.setName("pass");
        pswPI.setValue(pass);
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
}
