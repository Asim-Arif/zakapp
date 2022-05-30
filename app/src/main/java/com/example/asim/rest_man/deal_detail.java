package com.example.asim.rest_man;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;

import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class deal_detail extends Activity {

    EditText txtDealName,txtPizzaQty,txtSaladsQty,txtPrice;
    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    SimpleAdapter myCursorAdapter;
    Button cmdCancel,cmdOK;
    Context context;
    ArrayList data;
    Map<String, String> datanum;
    Button cmdClear,cmdCRM;
    ListView myLV;
    ScrollView svSubMenus,svSizes;
    int iAmount=0,lPendingSaleEntryID=0;
    TextView txtSubMenu;

    int iSalesTax;
    int iMilliSeconds;
    int iDealID=0;
    boolean bDealGroup=false;
    String strSize="";

    DealsData DD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle b = getIntent().getExtras();
        Bundle args = intent.getBundleExtra("BUNDLE");


        iDealID=b.getInt("DealID");
        //ArrayList<DealsData> DealsData_List_Inner=args.getParcelableArrayList("DealData_List"); When ArrayList is passed
        DD=args.getParcelable("DealData");
        //DD=DealsData_List_Inner.get(0);

        setContentView(R.layout.deal_detail);
        context = this.getBaseContext();

        cmdOK=(Button) findViewById(R.id.cmdOK);
        cmdCancel=(Button) findViewById(R.id.cmdCancel);
        txtDealName=(EditText) findViewById(R.id.txtDealName);
        txtPizzaQty=(EditText) findViewById(R.id.txtPizzas);
        txtSaladsQty=(EditText) findViewById(R.id.txtSalads);
        txtPrice=(EditText) findViewById(R.id.txtPrice);

        txtPrice.setText(Integer.toString(DD.iDealNo));
        data = new ArrayList<Map<String, String>>();

        String[] fromwhere = { "ItemName","Qty","Size"};

        int[] toViewIDs = new int[] {R.id.txtItemName,R.id.txtQty,R.id.txtSize};

        myCursorAdapter=new SimpleAdapter(context,data,R.layout.pos_lv_layout,fromwhere,toViewIDs);
        myCursorAdapter.setDropDownViewResource(R.layout.pos_lv_layout);

        myLV=(ListView) findViewById(R.id.LV);
        myLV.setClickable(true);
        myLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Message.message(context,Integer.toString(i));
            }
        });

        cmdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        cmdOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //First Verify the Qty against required Qty....
                int iPizzas=Integer.parseInt(txtPizzaQty.getText().toString());
                int iSalads=Integer.parseInt(txtSaladsQty.getText().toString());
                int i,iQty=0;
                for (i=0;i<data.size();i++){
                    Map<String, String> datanum=(Map<String, String>) data.get(i);
                    iQty=iQty+Integer.parseInt(datanum.get("Qty"));
                }
                if (iPizzas!=iQty){
                    Message.message(context,"Please Select "+Integer.toString(iPizzas)+"Pizzas");
                    return;
                }
                /*if (iPizzas!=data.size()){
                    Message.message(context,"Please Select "+Integer.toString(iPizzas)+"Pizzas");
                    return;
                }*///do same for Salads if Salads are available. currently not using Salads Selection in Boison Deals.
                int[] iPizzasArr=new int[data.size()];
                int[] iPizzasQty=new int[data.size()];

                for (i=0;i<data.size();i++){
                    Map<String, String> datanum=(Map<String, String>) data.get(i);
                    iPizzasArr[i]=Integer.parseInt(datanum.get("FM_EntryID"));
                    iPizzasQty[i]=Integer.parseInt(datanum.get("Qty"));
                }
                DD.iPizzas= Arrays.copyOf(iPizzasArr,iPizzasArr.length);
                DD.iPizzas_Qty=Arrays.copyOf(iPizzasQty,iPizzasQty.length);
                Intent intent = getIntent();
                Bundle args = new Bundle();
                args.putParcelable("DealData",DD);
                intent.putExtra("BUNDLE",args);

                setResult(RESULT_OK,intent);
                finish();
            }
        });
        myLV.setAdapter(myCursorAdapter);

        showDealDetail();
        loadSubMenus();
    }

    private void showDealDetail() {
        try {
            DBHelper myDBH = new DBHelper();
            Connection MyCon = myDBH.connectionclass(context);        // Connect to database
            String query = "SELECT * FROM Deals WHERE EntryID=" + Integer.toString(iDealID);
            PreparedStatement stmt = MyCon.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                txtDealName.setText(rs.getString("DealName"));
                txtPizzaQty.setText(rs.getString("PizzaQty"));
                txtSaladsQty.setText(rs.getString("SaladQty"));
                //txtPrice.setText(rs.getString("DealPrice"));
                strSize=rs.getString("PizzaSize");
            }
            rs.close();
        } catch (Exception e) {
            Message.message(this.getBaseContext(), "" + e);
        }
    }

    private void loadSubMenus()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT  EntryID,FinalMaterialID,FinalMaterialName,UrduName,Not_Available FROM FinalMaterials WHERE MaterialSize='"+strSize+"' AND GroupID=51";

        String strMenu_Urdu="";
        int iID;
        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                GridLayout menuLayout = (GridLayout) findViewById(R.id.ll_SubMenus);
                Button b1 = new Button(this);
                strMenu_Urdu=rs.getString("UrduName");
                iID=rs.getInt("EntryID");
                b1.setText(strMenu_Urdu);
                b1.setId(iID);

                b1.setSingleLine(true);
                int iFontSize=8;
                String pattern = "^[A-Za-z0-9. ]+$";
                int iHeight=0;
                int iWidht=0;
                Rect bounds = new Rect();
                Paint textPaint = b1.getPaint();
                textPaint.getTextBounds(strMenu_Urdu, 0, strMenu_Urdu.length(), bounds);
                iHeight = bounds.height();
                iWidht = bounds.width();
                String strFirst=strMenu_Urdu.substring(0,1);
                if (strFirst.matches(pattern)) {
                    iFontSize =getFontSize(strMenu_Urdu, strMenu_Urdu.length(),iWidht);
                    //b1.setTextSize(TypedValue.COMPLEX_UNIT_PT, iFontSize);
                    b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, iFontSize);
                }
                else {
                    iFontSize = getFontSize(strMenu_Urdu, strMenu_Urdu.length(),iWidht);
                    //b1.setTextSize(TypedValue.COMPLEX_UNIT_PT,iFontSize);
                    b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,iFontSize);
                    b1.setTypeface(null,Typeface.BOLD);
                }

                b1.setBackgroundColor(Color.BLUE);
                GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                params.rightMargin = 1;
                params.bottomMargin=1;
                //params.height=70;
                params.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.width=0;
                params.rowSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.height=70;
                //params.width=218;

                /*b1.setHeight(70);
                b1.setWidth(218);*/
                b1.setTextColor(Color.WHITE);
                if (rs.getBoolean("Not_Available")){
                    b1.setBackgroundColor(Color.BLACK);
                }
                menuLayout.addView(b1,params);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int iID=v.getId();
                        String strName="";
                        Button myBtn=(Button) v;
                        strName=myBtn.getText().toString();
                        String strSize=utility_functions.getSingleStringValue("MaterialSize","FinalMaterials"," WHERE FinalMaterialName='"+strName+"'",context);
                        //Message.message(context,Integer.toString(v.getTop()));
                        addItemToLV(iID,strName);

                    }
                });
            }
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }
        //svSizes.setVisibility(View.GONE);
        //svSubMenus.setVisibility(View.VISIBLE);

    }
    private int getFontSize(String string,int iLen,int iTextWidth) {
        int iFontSize = 9;
        String pattern = "^[A-Za-z0-9. ]+$";

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int iScreenWidth = size.x;
        int iScreenHeight = size.y;
        int iScreenHeight_Idea = 752;
        int iScreenRatio = iScreenHeight - iScreenHeight_Idea;
        float ydpi=getResources().getDisplayMetrics().ydpi;
        float ydpi_ideal=196;
        String strFirst = string.substring(0, 1);
        if (strFirst.matches(pattern)){
            /*if (iLen>=33)
                iFontSize=8;
            else if (iLen>=30)
                iFontSize=9;
            else if (iLen>=26)
                iFontSize=10;
            else if (iLen>=22)
                iFontSize=12;
            else if (iLen>=18)
                iFontSize=13;
            else if (iLen>=14)
                iFontSize=14;
            else if (iLen>=10)
                iFontSize=18;
            else
                iFontSize=20;*/
            if (iTextWidth >= 150){
                iFontSize = 9;
                iScreenRatio=iScreenRatio/35;
            }
            else if (iTextWidth >= 140) {
                iFontSize = 11;
                iScreenRatio=iScreenRatio/28;
            }
            else if (iTextWidth >= 120){
                iFontSize = 12;
                iScreenRatio=iScreenRatio/30;
            }
            else if (iTextWidth >= 100){
                iFontSize = 12;
                iScreenRatio=iScreenRatio/32;
            }
            else if (iTextWidth <= 40){
                iFontSize = 17;
                iScreenRatio=iScreenRatio/30;
            }
            else{
                iFontSize = 14;
                iScreenRatio=iScreenRatio/30;
            }
        }
        else {
            /*if (iLen>=20)
                iFontSize=5;
            else if(iLen>=14)
                iFontSize=6;
            else if(iLen>=10)
                iFontSize=7;
            else
                iFontSize=8;*/
            if (iTextWidth >= 120){
                iFontSize = 13;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 110) {
                iFontSize = 14;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 100) {
                iFontSize = 15;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 80) {
                iFontSize = 15;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 60) {
                iFontSize = 15;
                iScreenRatio=iScreenRatio/25;
            }
            else{
                iFontSize = 16;
                iScreenRatio=iScreenRatio/20;
            }
        }

        if ((ydpi-ydpi_ideal)==0){
            iFontSize=iFontSize;
        }else{
            ydpi=ydpi-ydpi_ideal;
            ydpi=ydpi/30;
            //iFontSize=iFontSize+iScreenRatio+((int) ydpi);
            //iFontSize=iFontSize+((int) ydpi);
            //iFontSize=iFontSize+iScreenRatio+((int) ydpi);
        }
        iFontSize=iFontSize+5;
        return iFontSize;
    }
    public void cmdPlus_click(View v)
    {
        //get the row the clicked button is in
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        datanum=(Map<String, String>) data.get(position);
        int iQty=Integer.parseInt(datanum.get("Qty"));
        iQty++;
        datanum.put("Qty",Integer.toString(iQty));
        data.set(position,datanum);
        myCursorAdapter.notifyDataSetChanged();
    }
    public void cmdMinus_click(View v)
    {
        //get the row the clicked button is in

        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        datanum=(Map<String, String>) data.get(position);
        int iEntryID=Integer.parseInt(datanum.get("EntryID"));
        int iQty=Integer.parseInt(datanum.get("Qty"));
        if (iEntryID!=0){
            //if it's already loaded sale, don't allow to remove
            int iPrevQty=Integer.parseInt(datanum.get("PreviousQty"));
            if (iQty<=iPrevQty)
                return;
        }

        iQty--;
        if (iQty==0){
            datanum.clear();
            data.remove(position);
        }
        else{
            datanum.put("Qty",Integer.toString(iQty));
            data.set(position,datanum);
        }

        myCursorAdapter.notifyDataSetChanged();
    }
    private void addItemToLV(int iFMID,String strName)
    {
        boolean bNotAvailable=false;
        bNotAvailable = utility_functions.getSingleBooleanValue("Not_Available","FinalMaterials"," WHERE EntryID="+Integer.toString(iFMID),context);
        if (bNotAvailable)
        {
            Message.message(context,"Not Available");
            return;
        }
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        //String strSize=utility_functions.getSingleStringValue("FinalMaterials","FinalMaterials"," WHERE FinalMaterialName='"+strName+"'",context);
        String query;
        //if (strSize=="")
        query = "SELECT * FROM FinalMaterials WHERE EntryID="+Integer.toString(iFMID);
        //else
        //    return;

        String strFMName="";
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            if (rs.next())
            {
                int i;
                int iQty=0;
                for (i=0;i<data.size();i++)
                {
                    //datanum=data[i];
                    Map<String, String> datanum=(Map<String, String>) data.get(i);
                    int iEntryID=Integer.parseInt(datanum.get("FM_EntryID"));

                    if (iEntryID==iFMID)
                    {
                        iQty=Integer.parseInt(datanum.get("Qty"));
                        iQty=iQty+1;
                        //datanum.entrySet("Qty",Integer.toString(iQty));
                        datanum.put("Qty",Integer.toString(iQty));
                        data.set(i,datanum);
                        myCursorAdapter.notifyDataSetChanged();
                        return;
                    }
                    //Check if Item already exists, just increment the qty.
                }
                int dRate=0;
                strFMName=strName;//rs.getString("UrduName");
                dRate=rs.getInt("Price");
                datanum=new HashMap<String, String>();
                datanum.put("ItemName",strFMName);
                datanum.put("Qty","1");
                datanum.put("Rate",Integer.toString(dRate));
                datanum.put("FM_EntryID",Integer.toString(iFMID));
                datanum.put("EntryID","0");
                datanum.put("PreviousQty","0");
                datanum.put("TabQty","0");
                datanum.put("Column6Tag","");
                datanum.put("QtyPrinted","0");
                //datanum.put("Size","");
                data.add(datanum);

                myCursorAdapter.notifyDataSetChanged();
                myLV.setSelection(myCursorAdapter.getCount() - 1);

            }
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }
    }
}
