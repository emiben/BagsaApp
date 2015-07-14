package com.app.bagsa.bagsaapp;

import android.app.ProgressDialog;
import android.content.Intent;
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
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class RegisterActivity extends ActionBarActivity {

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
                String mje = verficarDataIn();
                if(mje.equals("OK")){
                    if(sendDataWS()){
                        startMainActivity();
                    }
                }else{
                    Toast toast = Toast.makeText(getBaseContext(), mje, Toast.LENGTH_LONG);
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
            }
        }.start();

        return retornoWS;


    }

    private Boolean sendRequestWebServer() {

        boolean reg = false;

        final String NAMESPACE = Env.NAMESPACE;
        final String URL=Env.URL;
        final String METHOD_NAME = "createData";
        final String SOAP_ACTION = "http://3e.pl/ADInterface/ModelADServicePortType/createDataRequest";
        /* <adin:ModelCRUDRequest>
            <adin:ModelCRUD>
               <adin:serviceType>CreateUsuarioMovil</adin:serviceType>
               <adin:TableName>UY_UserReq</adin:TableName>
               <adin:RecordID>1</adin:RecordID>
               <!--<adin:Filter>?</adin:Filter>
-->
               <adin:Action>Create</adin:Action>
               <!--Optional:-->
               <adin:DataRow>
                  <!--Zero or more repetitions:-->
                  <adin:field column="Name" >
                     <adin:val>SBT_TEST</adin:val>
                  </adin:field>
                  <adin:field column="Phone" >
                     <adin:val>123456789</adin:val>
                  </adin:field>
			   <adin:field column="Address1" >
                     <adin:val>Calle Test 1111</adin:val>
                  </adin:field>
                  <adin:field column="EMail" >
                     <adin:val>mimail@gmail.com</adin:val>
                  </adin:field>
                  <adin:field column="C_DocType_ID" >
                     <adin:val>1001042</adin:val>
                  </adin:field>
                  <adin:field column="Code" >
                     <adin:val>123456789</adin:val>
                  </adin:field>
			   <adin:field column="FirstName" >
                     <adin:val>SBT</adin:val>
                  </adin:field>
                  <adin:field column="FirstSurname" >
                     <adin:val>SBT_TEST</adin:val>
                  </adin:field>
                  <adin:field column="DocStatus" >
                     <adin:val>DR</adin:val>
                  </adin:field>
                  <adin:field column="DateTrx" >
                  <!-- yyyy-mm-dd hh:mm:ss-->
                     <adin:val>2015-07-10 00:00:00</adin:val>
                  </adin:field>
               </adin:DataRow>
            </adin:ModelCRUD>
            <adin:ADLoginRequest>
               <adin:user>sbouissa</adin:user>
               <adin:pass>sbouissa</adin:pass>
               <adin:lang>143</adin:lang>
               <adin:ClientID>1000006</adin:ClientID>
               <adin:RoleID>1000022</adin:RoleID>
               <adin:OrgID>1000007</adin:OrgID>
               <adin:WarehouseID>1000052</adin:WarehouseID>
               <adin:stage>0</adin:stage>
            </adin:ADLoginRequest>
         </adin:ModelCRUDRequest>
      </adin:createData>
*/
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapObject adLoginRequest = new SoapObject(NAMESPACE,"ADLoginRequest");
        PropertyInfo usrPI= new PropertyInfo();
        usrPI.setName("user");
        usrPI.setValue("sbouissa");
        usrPI.setNamespace(NAMESPACE);
        usrPI.setType(String.class);
        adLoginRequest.addProperty(usrPI);

        PropertyInfo pswPI= new PropertyInfo();
        pswPI.setName("pass");
        pswPI.setValue("sbouissa");
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
              //  Log.d(TAG, "Registrado en mi servidor.");
                reg = true;
            }
        }
        catch (Exception e)
        {
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
        }else return "Las contraseñas no coinciden";
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
}
