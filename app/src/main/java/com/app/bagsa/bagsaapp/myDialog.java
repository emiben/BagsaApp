package com.app.bagsa.bagsaapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Emilino on 07/08/2015.
 */
public abstract class myDialog extends AlertDialog {


    protected myDialog(Context context, String[] items) {
        super(context);
        View v = View.inflate(context, R.layout.filter_layout, null);
        this.setView(v);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinColumn);
        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinColumn2);
        Button btnCancell = (Button) v.findViewById(R.id.btnCancell);
        Button btnOK = (Button) v.findViewById(R.id.btnOK);
        final EditText txt2Filter = (EditText) v.findViewById(R.id.etTxt2Filter);
        final Spinner column = (Spinner) v.findViewById(R.id.spinColumn);
        final Spinner column2 = (Spinner) v.findViewById(R.id.spinColumn2);
        final RadioButton rbAZ = (RadioButton) v.findViewById(R.id.rbAz);
        final RadioButton rbZA = (RadioButton) v.findViewById(R.id.rbZa);

        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);

        btnCancell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(rbAZ.isChecked()){
                    onOKButton(txt2Filter.getText().toString(), column.getSelectedItem().toString(),
                            column2.getSelectedItem().toString(), 1);
                }else{
                    onOKButton(txt2Filter.getText().toString(), column.getSelectedItem().toString(),
                            column2.getSelectedItem().toString(), 2);
                }
            }
        });

        btnCancell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCancellButton();
            }
        });
    }

    public abstract void onOKButton(String txt, String col, String colOrder, int orderBy);
    public abstract void onCancellButton();

}
