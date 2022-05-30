
package com.example.asim.rest_man;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class crm extends Activity {


    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    SimpleAdapter myCursorAdapter;
    CheckBox chkBreakFast,chkLunch,chkDinner,chkOther;
    CheckBox chkFoodAverage,chkFoodGood,chkFoodExellent;
    CheckBox chkPAverage,chkPGood,chkPExellent;
    CheckBox chkSAverage,chkSGood,chkSExellent;
    CheckBox chkDAverage,chkDGood,chkDExellent;
    CheckBox chkAAverage,chkAGood,chkAExellent;
    Context context;
    static EditText  txtDate;
    Button cmdCancel,cmdUpdate;
    EditText txtTableNo,txtName,txtProfession,txtContact,txtAddress,txtEmail,txtSuggestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.crm);
        context = this.getBaseContext();

        cmdCancel=(Button) findViewById(R.id.cmdCancel);
        cmdUpdate=(Button) findViewById(R.id.cmdUpdate);

        chkBreakFast=(CheckBox) findViewById(R.id.Breakfast);
        chkLunch=(CheckBox) findViewById(R.id.Lunch);
        chkDinner=(CheckBox) findViewById(R.id.Dinner);
        chkOther=(CheckBox) findViewById(R.id.Other);

        chkFoodAverage=(CheckBox) findViewById(R.id.Average);
        chkFoodGood=(CheckBox) findViewById(R.id.Good);
        chkFoodExellent=(CheckBox) findViewById(R.id.Excellent);

        chkPAverage=(CheckBox) findViewById(R.id.PAverage);
        chkPGood=(CheckBox) findViewById(R.id.PGood);
        chkPExellent=(CheckBox) findViewById(R.id.PExcellent);

        chkSAverage=(CheckBox) findViewById(R.id.SAverage);
        chkSGood=(CheckBox) findViewById(R.id.SGood);
        chkSExellent=(CheckBox) findViewById(R.id.SExcellent);

        chkDAverage=(CheckBox) findViewById(R.id.DAverage);
        chkDGood=(CheckBox) findViewById(R.id.DGood);
        chkDExellent=(CheckBox) findViewById(R.id.DExcellent);

        chkAAverage=(CheckBox) findViewById(R.id.AAverage);
        chkAGood=(CheckBox) findViewById(R.id.AGood);
        chkAExellent=(CheckBox) findViewById(R.id.AExcellent);

        txtDate=(EditText) findViewById(R.id.txtDate);
        txtDate.setText(utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"yyyy-dd-MM"));
        txtTableNo=(EditText) findViewById(R.id.txtTableNo);
        txtName=(EditText) findViewById(R.id.txtName);
        txtProfession=(EditText) findViewById(R.id.txtProfession);
        txtContact=(EditText) findViewById(R.id.txtContact);
        txtAddress=(EditText) findViewById(R.id.txtAddress);
        txtEmail=(EditText) findViewById(R.id.txtEmail);
        txtSuggestion=(EditText) findViewById(R.id.txtSuggestion);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"");

            }
        });
        chkBreakFast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkLunch.setChecked(false);
                    chkDinner.setChecked(false);
                    chkOther.setChecked(false);
                }
            }
        });
        chkLunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkBreakFast.setChecked(false);
                    chkDinner.setChecked(false);
                    chkOther.setChecked(false);
                }
            }
        });
        chkDinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkBreakFast.setChecked(false);
                    chkLunch.setChecked(false);
                    chkOther.setChecked(false);
                }
            }
        });
        chkOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkBreakFast.setChecked(false);
                    chkLunch.setChecked(false);
                    chkDinner.setChecked(false);
                }
            }
        });

        chkFoodAverage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkFoodGood.setChecked(false);
                    chkFoodExellent.setChecked(false);
                }
            }
        });
        chkFoodGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkFoodAverage.setChecked(false);
                    chkFoodExellent.setChecked(false);
                }
            }
        });
        chkFoodExellent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkFoodAverage.setChecked(false);
                    chkFoodGood.setChecked(false);
                }
            }
        });
        chkPAverage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkPGood.setChecked(false);
                    chkPExellent.setChecked(false);
                }
            }
        });
        chkPGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkPAverage.setChecked(false);
                    chkPExellent.setChecked(false);
                }
            }
        });
        chkPExellent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkPAverage.setChecked(false);
                    chkPGood.setChecked(false);
                }
            }
        });
        chkSAverage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkSGood.setChecked(false);
                    chkSExellent.setChecked(false);
                }
            }
        });
        chkSGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkSAverage.setChecked(false);
                    chkSExellent.setChecked(false);
                }
            }
        });
        chkSExellent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkSAverage.setChecked(false);
                    chkSGood.setChecked(false);
                }
            }
        });
        chkDAverage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkDGood.setChecked(false);
                    chkDExellent.setChecked(false);
                }
            }
        });
        chkDGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkDAverage.setChecked(false);
                    chkDExellent.setChecked(false);
                }
            }
        });
        chkDExellent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkDAverage.setChecked(false);
                    chkDGood.setChecked(false);
                }
            }
        });
        chkAAverage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkAGood.setChecked(false);
                    chkAExellent.setChecked(false);
                }
            }
        });
        chkAGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkAAverage.setChecked(false);
                    chkAExellent.setChecked(false);
                }
            }
        });
        chkAExellent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chkAAverage.setChecked(false);
                    chkAGood.setChecked(false);
                }
            }
        });
        cmdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper myDBH = new DBHelper();
                Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
                String strQuery="";
                PreparedStatement stmt;
                try {
                    MyCon.setAutoCommit(false);
                    strQuery = "INSERT INTO Customer_Feedback(DT,Name,Profession,Contact,Address,Email,Suggestion,Breakfast,Lunch,Dinner,Other,Food_Average" +
                            ",Food_Good,Food_Excellent,Portion_Average,Portion_Good,Portion_Excellent,Service_Average,Service_Good,Service_Excellent" +
                            ",Decor_Average,Decor_Good,Decor_Excellent,Atmosphere_Average,Atmosphere_Good,Atmosphere_Excellent,UserName,MachineName,Table_No) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = MyCon.prepareStatement(strQuery);
                    java.sql.Date myDT=java.sql.Date.valueOf(txtDate.getText().toString());//utility_functions.convertStringToSQLDate(txtDate.getText().toString(),context);//null;//java.sql.Date.valueOf(txtDate.getText().toString());
                    //utility_functions.convertStringToSQLDate();
                    stmt.setDate(1, myDT);
                    stmt.setString(2, txtName.getText().toString());
                    stmt.setString(3, txtProfession.getText().toString());
                    stmt.setString(4, txtContact.getText().toString());
                    stmt.setString(5, txtAddress.getText().toString());
                    stmt.setString(6, txtEmail.getText().toString());
                    stmt.setString(7, txtSuggestion.getText().toString());
                    stmt.setBoolean(8,chkBreakFast.isChecked());
                    stmt.setBoolean(9,chkLunch.isChecked());
                    stmt.setBoolean(10,chkDinner.isChecked());
                    stmt.setBoolean(11,chkOther.isChecked());
                    stmt.setBoolean(12,chkFoodAverage.isChecked());
                    stmt.setBoolean(13,chkFoodGood.isChecked());
                    stmt.setBoolean(14,chkFoodExellent.isChecked());

                    stmt.setBoolean(15,chkPAverage.isChecked());
                    stmt.setBoolean(16,chkPGood.isChecked());
                    stmt.setBoolean(17,chkPExellent.isChecked());

                    stmt.setBoolean(18,chkSAverage.isChecked());
                    stmt.setBoolean(19,chkSGood.isChecked());
                    stmt.setBoolean(20,chkSExellent.isChecked());

                    stmt.setBoolean(21,chkDAverage.isChecked());
                    stmt.setBoolean(22,chkDGood.isChecked());
                    stmt.setBoolean(23,chkDExellent.isChecked());

                    stmt.setBoolean(24,chkAAverage.isChecked());
                    stmt.setBoolean(25,chkAGood.isChecked());
                    stmt.setBoolean(26,chkAExellent.isChecked());

                    stmt.setString(27,"Android");
                    stmt.setString(28,"Android");
                    stmt.setString(29,txtTableNo.getText().toString());
                    stmt.addBatch();
                    stmt.executeBatch();
                    MyCon.commit();
                    Message.message(context,"Successfully Saved.");
                    onBackPressed();
                } catch (SQLException e) {

                    e.printStackTrace();
                    Message.message(context,e.toString());
                }

            }
        });
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final java.util.Calendar c=java.util.Calendar.getInstance();
            //final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c=Calendar.getInstance();
            c.set(year,month,day);

            String myDT;
            month=month+1;
            //myDT=Integer.toString(month)+'/'+Integer.toString(day)+'/'+Integer.toString(year);
            myDT=Integer.toString(year)+'-'+Integer.toString(month)+'-'+Integer.toString(day);
            txtDate.setText(myDT);

        }
    }
}
