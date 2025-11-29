
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
import android.os.Parcelable;
import android.provider.Settings;
import androidx.annotation.NonNull;

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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class pos extends Activity {
    private final static int REQUEST_CODE_1 = 1;
    DealsData DD;
    EditText txtTableNo,txtServer,txtOrderFrom,txtOrderDuration;
    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    SimpleAdapter myCursorAdapter;
    Button cmdBack,cmdFamilyHall,cmdLaibaHall,cmdGentsHall,cmdCafe,cmdTakeAway;
    Context context;
    ArrayList data;
    Map<String, String> datanum;
    Button cmdClear,cmdCRM;
    ListView myLV;
    ScrollView svSubMenus,svSizes;
    int iAmount=0,lPendingSaleEntryID=0;
    LinearLayout ll_Family_Hall,ll_Laiba_HHall,ll_Gents_Hall,ll_Cafe,ll_TakeAway;
    TextView txtSubMenu;
    //Button[] myTables=new Button[100];;
    class tableData{
        Button btnTable;
        int iTableIndex;
        int iEntryID;
        int iStatus;//0=No Sales Data,1=Yellow Color Sales Data,2=Blue Color,3=Invoice
    }

    //ArrayList<ArrayList<DealsData>> DealsData_List_Outer=new ArrayList<ArrayList<DealsData>>();
    Map<String,DealsData> DealsData_List_Outer=new HashMap<String,DealsData>();

    ArrayList<tableData> TableData_List=new ArrayList<tableData>();
    //String strDeviceName="Emulator";
    //String strDeviceName = BluetoothAdapter.getDefaultAdapter().getName();
    String strDeviceName = "";
    int iSalesTax,iSC_Amount;
    int iMilliSeconds;
    int iDealGroupID=0;
    boolean bDealGroup=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.pos);
        context = this.getBaseContext();

        strDeviceName= Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_NAME);

        txtTableNo=(EditText) findViewById(R.id.txtTableno);
        txtServer=(EditText)  findViewById(R.id.txtServer);
        txtOrderFrom=(EditText) findViewById(R.id.txtOrderfrom);
        txtOrderDuration=(EditText) findViewById(R.id.txtOrderduration);
        txtSubMenu=(TextView) findViewById(R.id.tvSubmenu);
        /*cmdTable1=(Button)  findViewById(R.id.tbl1);
        cmdTable1.setBackgroundColor(Color.GREEN);*/
        //cmdCRM=(Button) findViewById(R.id.cmdCRM);

        ll_Family_Hall=(LinearLayout) findViewById(R.id.ll_FamilyHall);
        ll_Laiba_HHall=(LinearLayout) findViewById(R.id.ll_LaibaHall);
        ll_Gents_Hall=(LinearLayout) findViewById(R.id.ll_GentsHall);
        ll_Cafe=(LinearLayout) findViewById(R.id.ll_Cafe);
        ll_TakeAway=(LinearLayout) findViewById(R.id.ll_TakeAway);

        cmdFamilyHall=(Button) findViewById(R.id.btnfamilyhall);
        cmdLaibaHall=(Button) findViewById(R.id.btnlaibahall);
        cmdGentsHall=(Button) findViewById(R.id.btngentshall);
        cmdCafe=(Button) findViewById(R.id.btncafe);
        cmdTakeAway=(Button) findViewById(R.id.btntakeaway);
        svSubMenus=(ScrollView) findViewById(R.id.sv_SubMenus);
        svSizes=(ScrollView) findViewById(R.id.sv_Sizes);
        /*cmdCRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), crm.class);
                startActivity(i);
            }
        });*/

        cmdFamilyHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_TakeAway.setVisibility(view.GONE);
                ll_Cafe.setVisibility(view.GONE);
                ll_Gents_Hall.setVisibility(view.GONE);
                ll_Laiba_HHall.setVisibility(View.GONE);
                ll_Family_Hall.setVisibility(View.VISIBLE);
            }
        });
        cmdLaibaHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_TakeAway.setVisibility(view.GONE);
                ll_Cafe.setVisibility(view.GONE);
                ll_Gents_Hall.setVisibility(view.GONE);
                ll_Family_Hall.setVisibility(View.GONE);
                ll_Laiba_HHall.setVisibility(View.VISIBLE);
            }
        });
        cmdGentsHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_TakeAway.setVisibility(view.GONE);
                ll_Cafe.setVisibility(view.GONE);
                ll_Gents_Hall.setVisibility(view.VISIBLE);
                ll_Family_Hall.setVisibility(View.GONE);
                ll_Laiba_HHall.setVisibility(View.GONE);
            }
        });
        cmdCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_TakeAway.setVisibility(view.GONE);
                ll_Cafe.setVisibility(view.VISIBLE);
                ll_Gents_Hall.setVisibility(view.GONE);
                ll_Family_Hall.setVisibility(View.GONE);
                ll_Laiba_HHall.setVisibility(View.GONE);
            }
        });
        cmdTakeAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_TakeAway.setVisibility(view.VISIBLE);
                ll_Cafe.setVisibility(view.GONE);
                ll_Gents_Hall.setVisibility(view.GONE);
                ll_Family_Hall.setVisibility(View.GONE);
                ll_Laiba_HHall.setVisibility(View.GONE);
            }
        });

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
        myLV.setAdapter(myCursorAdapter);

        LinearLayout ll;
        ll=(LinearLayout) findViewById(R.id.ll_FH_1);
        assignTables(ll,0);
        ll=(LinearLayout) findViewById(R.id.ll_FH_2);
        assignTables(ll,8);

        ll=(LinearLayout) findViewById(R.id.ll_LH_1);
        assignTables(ll,15);
        ll=(LinearLayout) findViewById(R.id.ll_LH_2);
        assignTables(ll,23);

        ll=(LinearLayout) findViewById(R.id.ll_GH_1);
        assignTables(ll,30);
        ll=(LinearLayout) findViewById(R.id.ll_GH_2);
        assignTables(ll,40);

        ll=(LinearLayout) findViewById(R.id.ll_Cafe_1);
        assignTables(ll,50);
        ll=(LinearLayout) findViewById(R.id.ll_Cafe_2);
        assignTables(ll,58);

        /*ll=(LinearLayout) findViewById(R.id.ll_TakeAway_1);
        assignTables(ll,99);
        ll=(LinearLayout) findViewById(R.id.ll_TakeAway_2);
        assignTables(ll,105);
*/
        iDealGroupID=Integer.parseInt(utility_functions.getSingleStringValue("DataValue","GeneralData"," WHERE DataName='DealGroupNumber'",context));
        iMilliSeconds = Integer.parseInt(utility_functions.getSingleStringValue("DataValue","GeneralData"," WHERE DataName='Tables_Refresh_Seconds'",context));
        iMilliSeconds = iMilliSeconds *1000;

        loadMainMenus();
        refreshTables();
        reload();
    }


    private void loadMainMenus()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT * FROM MenuGroups ORDER BY SortNo";
        String strMenu_Urdu="";
        int iID;
        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                GridLayout menuLayout = (GridLayout) findViewById(R.id.ll_Menus);
                Button b1 = new Button(this);
                //strMenu_Urdu=rs.getString("UrduDescription");
                strMenu_Urdu=rs.getString("Description");
                iID=rs.getInt("ID");
                //b1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                b1.setText(strMenu_Urdu);
                b1.setId(iID);
                GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                params.rightMargin = 2;
                params.bottomMargin=2;
                params.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.width=0;
                params.rowSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.height=70;
                //b1.setHeight(70);
                //b1.setWidth(130);

                int iFontSize=8;
                /*if (strMenu_Urdu.length()>=9)
                    iFontSize=5;
                else if (strMenu_Urdu.length()>8)men
                    iFontSize=7;
                else
                    iFontSize=8;*/
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
                    iFontSize=getFontSize(strMenu_Urdu,strMenu_Urdu.length(),iWidht);
                    //Reduce to 60%
                    iFontSize=iFontSize-(iFontSize*40/100);
                }
                else
                    iFontSize=getFontSize(strMenu_Urdu,strMenu_Urdu.length(),iWidht);

                b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,iFontSize);
                //b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                b1.setTypeface(null,Typeface.BOLD);
                //b1.setTextColor(Color.WHITE);
                int iBKColor;

                iBKColor=rs.getInt("GrpColor");
                if (iBKColor==0)
                    iBKColor=986895;
                String strColor=utility_functions.covertToAndroidColor(String.valueOf(iBKColor));
                try{
                    iBKColor=Color.parseColor(strColor);
                } catch(Exception e){
                    //Do Nothing...
                    iBKColor=Color.GRAY;
                }
                b1.setBackgroundColor(iBKColor);
                b1.setTag(iBKColor);


                menuLayout.addView(b1,params);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int iID=v.getId();

                        Button btnSelected = (Button)v;
                        txtSubMenu.setText(btnSelected.getText().toString().toUpperCase());

                        //Firstly, Remove all sub menus...
                        GridLayout menuLayout = (GridLayout) findViewById(R.id.ll_SubMenus);
                        menuLayout.removeAllViews();

                        if (iID==iDealGroupID)
                            bDealGroup=true;
                        else
                            bDealGroup=false;

                        loadSubMenus(iID,Integer.parseInt(btnSelected.getTag().toString()));

                    }
                });
            }

        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }
    private void loadSubMenus(int iGroupID, int iBGColor)
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT  * FROM VProductsForPOS WHERE GroupID="+Integer.toString(iGroupID)+" AND InActive=0 ";
        if (bDealGroup)
            query=query+" AND Deal=1";
        else
            query=query+" AND Deal=0";

        query=query+" ORDER BY SortNo";
        String strMenu_Urdu="";
        int iID;
        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                GridLayout menuLayout = (GridLayout) findViewById(R.id.ll_SubMenus);
                Button b1 = new Button(this);
                strMenu_Urdu=rs.getString("DealName");
                iID=rs.getInt("EntryID");
                //b1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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

                b1.setBackgroundColor(iBGColor);
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

                        if (isSaleAvailable()==false)
                            return;

                        String strName="";
                        Button myBtn=(Button) v;
                        strName=myBtn.getText().toString();
                        String strSize=utility_functions.getSingleStringValue("MaterialSize","FinalMaterials"," WHERE FinalMaterialName='"+strName+"'",context);
                        //Message.message(context,Integer.toString(v.getTop()));
                        if (bDealGroup){
                            loadDealDetails(iID,strName);
                        }
                        else if (strSize.equals(""))
                            addItemToLV(iID,strName,false);
                        else
                            loadSizes(strName);
                    }
                });
            }
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }
        svSizes.setVisibility(View.GONE);
        svSubMenus.setVisibility(View.VISIBLE);

    }
    private int addItemToLV(int iFMID,String strName,boolean isDeal)
    {
        int iReturnIndex=0;
        boolean bNotAvailable=false;
        bNotAvailable = utility_functions.getSingleBooleanValue("Not_Available","FinalMaterials"," WHERE EntryID="+Integer.toString(iFMID),context);
        if (bNotAvailable)
        {
            Message.message(context,"Not Available");
            return iReturnIndex;
        }
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        //String strSize=utility_functions.getSingleStringValue("FinalMaterials","FinalMaterials"," WHERE FinalMaterialName='"+strName+"'",context);
        String query;
        //if (strSize=="")
        if (isDeal)
            query = "SELECT * FROM Deals WHERE EntryID="+Integer.toString(iFMID);
        else
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
                    boolean iIsDeal=Boolean.parseBoolean(datanum.get("IsDeal"));
                    //String strName_Added=(datanum.get("ItemName"));
                    //String strSize_Added=(datanum.get("Size"));
                    if (iEntryID==iFMID)  //Deal Is Zero
                    {
                        iQty=Integer.parseInt(datanum.get("Qty"));
                        iQty=iQty+1;
                        //datanum.entrySet("Qty",Integer.toString(iQty));
                        datanum.put("Qty",Integer.toString(iQty));
                        data.set(i,datanum);
                        myCursorAdapter.notifyDataSetChanged();
                        return (i+1);
                    }
                    //Check if Item already exists, just increment the qty.
                }
                int dRate=0;
                if (isDeal)
                    strFMName=rs.getString("DealName");
                else
                    strFMName=rs.getString("FinalMaterialName");

                String strSize=rs.getString("MaterialSize");
                if (strSize.equals("")==false)
                    strFMName=strFMName+" ("+strSize+")";

                //strFMName=strName;//rs.getString("UrduName");
                if (isDeal)
                    dRate=rs.getInt("DealPrice");
                else
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
                datanum.put("IsDeal",String.valueOf(isDeal));
                //datanum.put("Size","");
                data.add(datanum);

                myCursorAdapter.notifyDataSetChanged();
                myLV.setSelection(myCursorAdapter.getCount() - 1);
                iReturnIndex=data.size();

            }
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
            return iReturnIndex;
        }
        return iReturnIndex;
    }

    public void tbl_btn_click(View view){

        int iSaleType=0;
        Button cmdSelected=(Button) findViewById(view.getId());
        //String strTableNo=cmdSelected.getText().toString();
        String strTableNo=cmdSelected.getTag().toString();
        int iTableNo=Integer.parseInt(strTableNo);
        tableData tableData_Obj=(tableData) TableData_List.get(iTableNo-1);
        //If iStatus=1, it means it holds some data, we need to load pending sale.
        if (tableData_Obj.iStatus==1){
            loadPendingSale(iTableNo,tableData_Obj);
            return;
        }
        else if (tableData_Obj.iStatus==3){ //If iStatis=3, it means i's Invoice, No need to do anything in tablet.
            return;
        }
        if (!txtTableNo.getText().toString().equals(""))
        {
            if (!strTableNo.equals(txtTableNo.getText().toString()))
            {
                Message.message(context,"This sale is pending from "+txtTableNo.getText().toString());
                return;
            }
        }
        if (data.size()==0)
        {
            Message.message(context,"No Sale Data !!!");
            return;
        }
        if (txtServer.getText().toString().equals("")){    //New Pending Sale
            String strTableLocked_ComputerName = utility_functions.getSingleStringValue("MachineName","Tables_Locked"," WHERE TableNo='"+strTableNo+"' AND MachineName<>'"+strDeviceName+"'",context);
            if (strTableLocked_ComputerName!="")
            {
                Message.message(context,"Table is locked from "+strTableLocked_ComputerName+", Please user other table.");
                return;
            }
            String strServer=utility_functions.getSingleStringValue("WaiterName","POS_Settings"," WHERE MachineName='"+strDeviceName+"'",context);
            txtServer.setText(strServer);
            txtTableNo.setText(strTableNo);
        }
        else
        {
            /*
            strServer = lblServer.Caption
                strTableNo = lblTableNo.Caption
                iSaleType = lblServer.Tag
            */

        }
        final java.util.Calendar CurrentTime=java.util.Calendar.getInstance();
        java.util.Date DT=null;
        DT=utility_functions.getCurrentDate(context);
        java.sql.Date sqlDate=new java.sql.Date(DT.getTime());
        String strDT="";
        java.sql.Date DTInvoice=null;
        if (CurrentTime.get(Calendar.HOUR_OF_DAY)<5)
        {
            CurrentTime.add(Calendar.DATE,-1);
            DTInvoice=new java.sql.Date(CurrentTime.getTime().getTime());
            strDT=utility_functions.convertDateToString(CurrentTime.getTime(),"MMM-dd-yyyy");
            //DTInvoice=utility_functions.convertStringToSQLDate(strDT,context);
        }
        else
        {
            DTInvoice=new java.sql.Date(CurrentTime.getTime().getTime());
            strDT=utility_functions.convertDateToString(DT,"yyyy-MM-dd HH:mm:ss z");
            //DTInvoice=utility_functions.convertStringToSQLDate(strDT,context);
        }
        //strDT=utility_functions.convertDateToString(DT,"yyyy-MM-dd HH:mm:ss z");
        //strDT=strDT.substring(0,10);
        int lInvNoFromPending=0,lInvNoFromSales=0,lInvoiceNo=0;
        if (txtServer.getTag().toString().equals(""))
        {
            lInvNoFromPending = utility_functions.getSingleIntValue("MAX(InvoiceNo)","PendingSales","WHERE CAST(CAST(DTEntry_For_InvoiceNo AS DATE) AS DATETIME)='"+DTInvoice.toString()+"'",context);
            lInvNoFromSales = utility_functions.getSingleIntValue("MAX(InvoiceNo)", "ItemSales", " WHERE DT='" + DTInvoice.toString() + "'",context);
            if (lInvNoFromPending > lInvNoFromSales)
                lInvoiceNo = lInvNoFromPending;
            else
                lInvoiceNo = lInvNoFromSales;

            lInvoiceNo = lInvoiceNo + 1;
        }
        else{
            lInvoiceNo=Integer.parseInt(txtServer.getTag().toString());
        }
        String strOrderFrom_ComputerName="";
        if (txtOrderFrom.getText().toString().equals("")){
            strOrderFrom_ComputerName = strDeviceName;
        }
        else{
            strOrderFrom_ComputerName = txtOrderFrom.getText().toString();
        }
        txtOrderFrom.setText("");
        txtOrderDuration.setText("");
        int iTotalAmount=0;
        int i;
        for (i=0;i<data.size();i++){
            Map<String, String> datanum=(Map<String, String>) data.get(i);
            int iRate=Integer.parseInt(datanum.get("Rate"));
            int iQty=Integer.parseInt(datanum.get("Qty"));
            iTotalAmount=iTotalAmount+(iQty*iRate);
        }
        boolean bSTax_Enabled,bSC_Enabled;
        bSTax_Enabled = utility_functions.getSingleBooleanValue("Sales_Tax_Applicable", "Hall_List", " WHERE " + Integer.toString(iTableNo) + " BETWEEN TableFrom AND TableTo",context);
        bSC_Enabled = utility_functions.getSingleBooleanValue("Service_Charges_Applicable", "Hall_List", " WHERE " + Integer.toString(iTableNo) + " BETWEEN TableFrom AND TableTo",context);
        iSalesTax=0;
        iSC_Amount=0;
        if (bSTax_Enabled){
            double dSalesTaxPer;
            dSalesTaxPer = Double.parseDouble(utility_functions.getSingleStringValue("DataValue","GeneralData"," WHERE DataName='SalesTax'",context)) ;
            iSalesTax=(int) Math.round(iTotalAmount*(dSalesTaxPer /100));
        }
        if (bSC_Enabled){
            double dSCPer;
            dSCPer = utility_functions.getSingleDoubleValue("Service_Charges_Rate","Hall_List", " WHERE " + Integer.toString(iTableNo) + " BETWEEN TableFrom AND TableTo",context);
            iSC_Amount=(int) Math.round(iTotalAmount*(dSCPer /100));
        }
        boolean bOrderChanged_New=false;
        String strUserName=utility_functions.getSingleStringValue("UserName","POS_Settings"," WHERE MachineName='"+strDeviceName+"'",context);
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String strQuery="";
        int iPS_EntryID=0,iPSD_EntryID=0;
        PreparedStatement stmt;
        try {
            MyCon.setAutoCommit(false);
            if (lPendingSaleEntryID==0) {
                strQuery = "INSERT INTO PendingSales(ButtonNumber,Server,TableNo,Payable,Received,Status,UserName,MachineName,SaleType,DrinksUpsize,FriesUpsize,DrinksUpsizeRate,FriesUpsizeRate,InvoiceNo,ManualSTax,STaxAmt,DTEntry_Tab,DT_For_InvoiceNo,DTEntry_For_InvoiceNo,ServiceCharges) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, Integer.parseInt((txtTableNo.getText().toString())));
                stmt.setString(2, txtServer.getText().toString());
                stmt.setString(3, txtTableNo.getText().toString());
                stmt.setInt(4, iTotalAmount);
                stmt.setInt(5, 0);
                stmt.setInt(6, 0);
                stmt.setString(7, strUserName);
                stmt.setString(8, strDeviceName);
                stmt.setInt(9, iSaleType);
                stmt.setInt(10, 0);
                stmt.setInt(11, 0);
                stmt.setInt(12, 0);
                stmt.setInt(13, 0);
                stmt.setInt(14, lInvoiceNo);
                stmt.setInt(15, 0);
                stmt.setInt(16, iSalesTax);
                stmt.setDate(17,sqlDate);
                stmt.setString(18,strDT);
                stmt.setDate(19,DTInvoice);
                stmt.setInt(20, iSC_Amount);
                stmt.addBatch();
                stmt.executeBatch();

                iPS_EntryID = utility_functions.getSingleIntValue(MyCon, "MAX(EntryID)", "PendingSales", " WHERE MachineName='" + strDeviceName + "'", context);
            }
            else{
                strQuery = "UPDATE PendingSales SET Payable=?,STaxAmt=?,ServiceCharges=? WHERE EntryID=?";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, iTotalAmount);
                stmt.setInt(2, iSalesTax);
                stmt.setInt(3, iSC_Amount);
                stmt.setInt(4, lPendingSaleEntryID);

                stmt.addBatch();
                stmt.executeBatch();
                iPS_EntryID=lPendingSaleEntryID;
            }
            for (i=0;i<data.size();i++){
                Map<String, String> datanum=(Map<String, String>) data.get(i);

                int iEntryID=Integer.parseInt(datanum.get("EntryID"));
                String strItemName=datanum.get("ItemName");
                int iFM_EntryID=Integer.parseInt(datanum.get("FM_EntryID"));
                int iRate=Integer.parseInt(datanum.get("Rate"));
                int iQty=Integer.parseInt(datanum.get("Qty"));
                int iPreviouseQty=Integer.parseInt(datanum.get("PreviousQty"));
                int iTabQty_Saved=Integer.parseInt(datanum.get("TabQty"));
                String strColumn6Tag=datanum.get("Column6Tag");     //will have "saved" once loaded.
                int iQtyPrinted=Integer.parseInt(datanum.get("QtyPrinted"));
                boolean isDeal=Boolean.parseBoolean(datanum.get("IsDeal")) ;
                int iTabQty=iTabQty_Saved+iQty-iPreviouseQty;
                if (iEntryID==0){
                    strQuery = "INSERT INTO PendingSalesDetail(RefID,Column1,Column1Key,Column1Tag,Column2,Column2Tag,Column3,Column3Tag,Column4,Column4Tag,Column5,Column5Tag,Column6,Column6Tag,QtyPrinted,MachineName_Last,QtyTab,MachineName_Last_Tab) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = MyCon.prepareStatement(strQuery);
                    stmt.setInt(1, iPS_EntryID);
                    stmt.setString(2,strItemName);
                    if (isDeal)
                        stmt.setString(3, Integer.toString(iFM_EntryID)+"'Deal");
                    else
                        stmt.setString(3, Integer.toString(iFM_EntryID)+"'FM");
                    stmt.setString(4,"");
                    stmt.setString(5,Integer.toString(iQty));
                    stmt.setString(6,"FALSE");
                    stmt.setString(7,Integer.toString(iRate));
                    stmt.setString(8,"");
                    stmt.setString(9,"0");
                    stmt.setString(10,"");
                    stmt.setString(11,Integer.toString(iQty*iRate));
                    stmt.setString(12,"");
                    stmt.setString(13,"");
                    stmt.setString(14,strColumn6Tag);
                    stmt.setString(15,Integer.toString(iQtyPrinted));
                    stmt.setString(16,strDeviceName);
                    stmt.setString(17,Integer.toString(iQty));
                    stmt.setString(18,strDeviceName);
                    stmt.addBatch();
                    stmt.executeBatch();
                    iPSD_EntryID = utility_functions.getSingleIntValue(MyCon, "MAX(EntryID)", "PendingSalesDetail", " WHERE RefID="+Integer.toString(iPS_EntryID), context);
                    bOrderChanged_New=true;
                }
                else{
                    strQuery = "UPDATE PendingSalesDetail SET Column1=?,Column1Key=?,Column1Tag=?,Column2=?,Column2Tag=?,Column3=?,Column3Tag=?,Column4=?,Column4Tag=?,Column5=?,Column5Tag=?,Column6=?,Column6Tag=?,MachineName_Last=?,QtyTab=?,MachineName_Last_Tab=? WHERE EntryID=?";
                    stmt = MyCon.prepareStatement(strQuery);
                    stmt.setString(1,strItemName);
                    if (isDeal)
                        stmt.setString(2, Integer.toString(iFM_EntryID)+"'Deal");
                    else
                        stmt.setString(2, Integer.toString(iFM_EntryID)+"'FM");

                    stmt.setString(3,"");
                    stmt.setString(4,Integer.toString(iQty));
                    stmt.setString(5,"FALSE");
                    stmt.setString(6,Integer.toString(iRate));
                    stmt.setString(7,"");
                    stmt.setString(8,"0");
                    stmt.setString(9,"");
                    stmt.setString(10,Integer.toString(iQty*iRate));
                    stmt.setString(11,"");
                    stmt.setString(12,"");
                    stmt.setString(13,strColumn6Tag);
                    stmt.setString(14,strDeviceName);
                    stmt.setString(15,Integer.toString(iQty));
                    stmt.setString(16,strDeviceName);
                    stmt.setInt(17,iEntryID);
                    stmt.addBatch();
                    stmt.executeBatch();
                    iPSD_EntryID=iEntryID;
                    if (iQty>iPreviouseQty){
                        bOrderChanged_New=true;
                    }
                    strQuery = "DELETE FROM PendingSalesDealObject WHERE RefID=?";
                    stmt = MyCon.prepareStatement(strQuery);
                    stmt.setInt(1,iEntryID);
                    stmt.addBatch();
                    stmt.executeBatch();
                }
                if (isDeal){
                    int j;
                    Iterator itr = DealsData_List_Outer.entrySet().iterator();
                    while(itr.hasNext()){
                        Map.Entry pair = (Map.Entry) itr.next();
                        DealsData DD_Temp;
                        DD_Temp=(DealsData) pair.getValue();
                        if (DD_Temp.iDealID==iFM_EntryID){

                            int[] iPizzasArr=DD_Temp.iPizzas;
                            int[] iPizzasQty=DD_Temp.iPizzas_Qty;
                            strQuery = "INSERT INTO PendingSalesDealObject(RefID,DealNo,PizzaCount,SaladCount,Printed) VALUES(?,?,?,?,?)";
                            stmt = MyCon.prepareStatement(strQuery);
                            stmt.setInt(1, iPSD_EntryID);
                            stmt.setInt(2, 1);
                            stmt.setInt(3,iPizzasArr.length);
                            stmt.setInt(4, 0);
                            stmt.setInt(5,0);
                            stmt.addBatch();
                            stmt.executeBatch();
                            int lObjRefID = utility_functions.getSingleIntValue(MyCon, "MAX(EntryID)", "PendingSalesDealObject", " WHERE RefID="+Integer.toString(iPSD_EntryID), context);

                            for (j=0;j<iPizzasArr.length;j++)
                            {
                                strQuery = "INSERT INTO PendingSalesDealObjectDetail(RefID,Pizza,FM_RefID,Qty,FromBack) VALUES(?,?,?,?,?)";
                                stmt = MyCon.prepareStatement(strQuery);
                                stmt.setInt(1, lObjRefID);
                                stmt.setInt(2, 1);
                                stmt.setInt(3,iPizzasArr[j]);
                                stmt.setInt(4, iPizzasQty[j]);
                                stmt.setInt(5,0);
                                stmt.addBatch();
                                stmt.executeBatch();
                            }
                            //Same should be done for Salads when make available.
                            strQuery = "INSERT INTO PendingSalesDealObjectDetail(RefID,Pizza,FM_RefID,Qty,FromBack) SELECT ?,0,FinalMaterials.EntryID,Qty,1 FROM DealsMaterials INNER JOIN FinalMaterials ON DealsMaterials.FinalMaterialID=FinalMaterials.FinalMaterialID WHERE RefID=?";
                            stmt = MyCon.prepareStatement(strQuery);
                            stmt.setInt(1, lObjRefID);
                            stmt.setInt(2, DD_Temp.iDealID);
                            stmt.addBatch();
                            stmt.executeBatch();
                        }
                        //iterator.remove();
                    }
                }
            }
            strQuery = "DELETE FROM Tables_Locked WHERE MachineName=?";
            stmt = MyCon.prepareStatement(strQuery);
            stmt.setString(1, strDeviceName);
            stmt.addBatch();
            stmt.executeBatch();
            if (bOrderChanged_New){
                strQuery = "UPDATE PendingSales SET DTEntry_Last=getDate() WHERE EntryID=?";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, lPendingSaleEntryID);
                stmt.addBatch();
                stmt.executeBatch();
            }
            lPendingSaleEntryID = 0;

            MyCon.commit();
            data.clear();
            myCursorAdapter.notifyDataSetChanged();
            txtTableNo.setText("");
            txtServer.setText("");
            txtServer.setTag("");
            tableData_Obj.iStatus=1;
            tableData_Obj.iEntryID=iPS_EntryID;
            //tableData_Obj.btnTable.setBackgroundColor(Color.YELLOW);
            tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_yellow);
            DealsData_List_Outer.clear();

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
        }

    }
    private void refreshTables(){
        int i;
        tableData tableData_Obj;
        for (i=0;i<TableData_List.size();i++){
            tableData_Obj=(tableData) TableData_List.get(i);
            if (tableData_Obj.iStatus!=2){
                tableData_Obj.iStatus=0;
                tableData_Obj.iEntryID=0;
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_grey);
            }
        }
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT EntryID,ButtonNumber FROM PendingSales WHERE PostedInvoiceNo IS NULL";
        int iIndex;

        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                iIndex=rs.getInt("ButtonNumber")-1;
                tableData_Obj=(tableData) TableData_List.get(iIndex);
                if (tableData_Obj.iStatus!=2){
                    tableData_Obj.iStatus=1;
                    tableData_Obj.iEntryID=rs.getInt("EntryID");
                    //tableData_Obj.btnTable.setBackgroundColor(Color.YELLOW);
                    tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            rs.close();
            query="SELECT EntryID,TableNo FROM VItemSales WHERE CashRcvd=0 AND Payment_Type=0 AND ISNULL(TableNo,'')<>''";
            stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();
            while (rs.next()){
                iIndex=rs.getInt("TableNo")-1;
                tableData_Obj=(tableData) TableData_List.get(iIndex);
                tableData_Obj.iStatus=3;
                tableData_Obj.iEntryID=rs.getInt("EntryID");
                //tableData_Obj.btnTable.setBackgroundColor(Color.MAGENTA);
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_magenta);
            }
            rs.close();
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }
    }
    private void assignTables(@NonNull LinearLayout ll, int iStart){
        int i,iCount,iIndex;
        tableData tableData_Obj;
        iCount=ll.getChildCount()+iStart;
        for(i=iStart;i<iCount;i++){
            iIndex=i-iStart;
            View  child=ll.getChildAt(iIndex);
            if (child instanceof Button){
                Button button=(Button) child;
                tableData_Obj=new tableData();
                tableData_Obj.btnTable=button;
                tableData_Obj.iEntryID=0;
                tableData_Obj.iStatus=0;
                tableData_Obj.iTableIndex=i;
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_grey);
                TableData_List.add(i,tableData_Obj);
                //Message.message(context,"Please Help me, God !!!");
            }
        }

    }
    private void loadPendingSale(int iTableIndex,tableData tableData_Obj){
        if (data.size()>0){
            Message.message(context,"Please Settle Existing Sale.");
            return;
        }
        String strTableLocked_ComputerName = utility_functions.getSingleStringValue("MachineName","Tables_Locked", " WHERE TableNo='" + Integer.toString(iTableIndex) + "' AND MachineName<>'" + strDeviceName + "'",context);
        if (!strTableLocked_ComputerName.equals("")){
            Message.message(context,"Table is locked from "+strTableLocked_ComputerName);
            return;
        }
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        int iEntryID=tableData_Obj.iEntryID;
        String query = "SELECT DATEDIFF(MINUTE,DTEntry,getDATE()) AS TotalMinutes,* FROM PendingSales WHERE EntryID="+Integer.toString(iEntryID);
        int iIndex;
        int iObjRefID=0;
        boolean isDeal=false;
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            ResultSet rsDO,rsDOD;
            if (!rs.next())
            {
                Message.message(context,"Table data has been changed, Please Refresh Tables");
            }
            else
            {
                lPendingSaleEntryID = iEntryID;

                txtServer.setText(rs.getString("Server"));
                txtTableNo.setTag(rs.getInt("SaleType"));
                txtTableNo.setText(rs.getString("TableNo"));
                txtServer.setTag(rs.getInt("InvoiceNo"));
                txtOrderFrom.setText(rs.getString("MachineName"));
                txtOrderDuration.setText(rs.getString("TotalMinutes")+" Mins.");
                iSalesTax= rs.getInt("STaxAmt");
                iSC_Amount=rs.getInt("ServiceCharges");
                rs.close();
                //Lock the Table for others to See...
                String strQuery = "INSERT INTO Tables_Locked(TableNo,MachineName) VALUES(?,?)";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setString(1,Integer.toString(iTableIndex));
                stmt.setString(2,strDeviceName);
                stmt.addBatch();
                stmt.executeBatch();
                //MyCon.commit();
                //Lock the Table for others to See...

                query="SELECT * FROM PendingSalesDetail WHERE RefID="+Integer.toString(iEntryID)+" ORDER BY EntryID";
                stmt=MyCon.prepareStatement(query);
                rs=stmt.executeQuery();
                while (rs.next()){
                    datanum=new HashMap<String, String>();
                    datanum.put("ItemName",rs.getString("Column1"));
                    datanum.put("Qty",rs.getString("Column2"));
                    datanum.put("Rate",rs.getString("Column3"));

                    String strFMID=rs.getString("Column1Key");
                    if (strFMID.substring(strFMID.length()-2).equals("FM"))
                        isDeal=false;
                    else
                        isDeal=true;

                    strFMID=strFMID.substring(0,strFMID.indexOf("'"));

                    datanum.put("FM_EntryID",strFMID);
                    datanum.put("EntryID",Integer.toString(rs.getInt("EntryID")));
                    datanum.put("PreviousQty",rs.getString("Column2"));
                    datanum.put("TabQty",Integer.toString(rs.getInt("QtyTab")));
                    datanum.put("Column6Tag","Saved");
                    datanum.put("QtyPrinted",Integer.toString(rs.getInt("QtyPrinted")));

                    datanum.put("IsDeal",Boolean.toString(isDeal));
                    data.add(datanum);
                    query="SELECT * FROM PendingSalesDealObject WHERE RefID="+Integer.toString(rs.getInt("EntryID"));
                    stmt=MyCon.prepareStatement(query);
                    rsDO=stmt.executeQuery();
                    DealsData_List_Outer.clear();
                    while (rsDO.next()){
                        DD=new DealsData();
                        DD.iDealNo=rsDO.getInt("DealNo");
                        DD.iDealID=Integer.parseInt(strFMID);
                        DD.iPizzaCount=rsDO.getInt("PizzaCount");
                        DD.iSaladCount=rsDO.getInt("SaladCount");
                        iObjRefID=rsDO.getInt("EntryID");
                        query="SELECT * FROM PendingSalesDealObjectDetail WHERE RefID="+Integer.toString(iObjRefID)+" AND Pizza=1 AND FromBack=0";
                        stmt=MyCon.prepareStatement(query);
                        rsDOD=stmt.executeQuery();
                        int iCount=utility_functions.getSingleIntValue("COUNT(*)","PendingSalesDealObjectDetail"," WHERE RefID="+Integer.toString(iObjRefID)+" AND Pizza=1 AND FromBack=0",context);

                        int[] iPizzas=new int[iCount];
                        int[] iPizzasQty=new int[iCount];
                        iCount=0;
                        while(rsDOD.next()){
                            iPizzas[iCount]=rsDOD.getInt("FM_RefID");
                            iPizzasQty[iCount]=rsDOD.getInt("Qty");
                            iCount=iCount+1;
                        }
                        DD.iPizzas= Arrays.copyOf(iPizzas,iPizzas.length);
                        DD.iPizzas_Qty= Arrays.copyOf(iPizzasQty,iPizzasQty.length);
                        int iLastIndex=DealsData_List_Outer.size()+1;
                        DealsData_List_Outer.put(Integer.toString(iLastIndex),DD);
                        rsDOD.close();
                    }
                    rsDO.close();
                }
                myCursorAdapter.notifyDataSetChanged();
                rs.close();




                tableData_Obj.iStatus=2;
                tableData_Obj.iEntryID=iEntryID;
                //tableData_Obj.btnTable.setBackgroundColor(Color.BLUE);
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_blue);
                //res.getColor(R.color.your_special_color));
            }

        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }

    public void reload(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshTables();
                handler.postDelayed(this, iMilliSeconds);
            }
        }, iMilliSeconds);
    }
    public void cmdPlus_click(View v)
    {
        //get the row the clicked button is in
        if (isSaleAvailable()==false)
            return;
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        datanum=(Map<String, String>) data.get(position);
        if (Boolean.parseBoolean(datanum.get("IsDeal")))
            return;

        int iQty=Integer.parseInt(datanum.get("Qty"));
        iQty++;
        datanum.put("Qty",Integer.toString(iQty));
        data.set(position,datanum);
        myCursorAdapter.notifyDataSetChanged();
    }
    public void cmdMinus_click(View v)
    {
        //get the row the clicked button is in
        if (isSaleAvailable()==false)
            return;

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
        if (Boolean.parseBoolean(datanum.get("IsDeal")))
        {
            datanum.clear();
            data.remove(position);
            DealsData_List_Outer.clear();
        }
        else {
            iQty--;
            if (iQty == 0) {
                datanum.clear();
                data.remove(position);
            } else {
                datanum.put("Qty", Integer.toString(iQty));
                data.set(position, datanum);
            }
        }
        myCursorAdapter.notifyDataSetChanged();
    }

    private boolean isSaleAvailable()
    {
        int iPostedInvNo=utility_functions.getSingleIntValue("PostedInvoiceNo","PendingSales"," WHERE EntryID="+String.valueOf(lPendingSaleEntryID),context);
        if (iPostedInvNo>0)
        {
            Message.message(context,"Selected Sale is not available.");
            lPendingSaleEntryID = 0;
            data.clear();
            myCursorAdapter.notifyDataSetChanged();
            txtTableNo.setText("");
            txtServer.setText("");
            txtServer.setTag("");
            DealsData_List_Outer.clear();
            return false;
        }
        else
            return true;
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
    private int getFontSize_MM(String string,int iLen){
        int iFontSize=9;
        String pattern = "^[A-Za-z0-9. ]+$";
        float fSD=getResources().getDisplayMetrics().density;
        if (string.matches(pattern))
            if (iLen>=20)
                iFontSize=6;
            else if (iLen>=14)
                iFontSize=8;
            else if (iLen>=10)
                iFontSize=10;
            else
                iFontSize=12;
        else
        if (iLen>=20)
            iFontSize=5;
        else if(iLen>=14)
            iFontSize=6;
        else if(iLen>=10)
            iFontSize=7;
        else
            iFontSize=8;
        return iFontSize;
    }
    private void loadSizes(String strName)
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT  * FROM FinalMaterials WHERE FinalMaterialName='"+strName+"' AND InActive=0 AND MaterialSize<>'' ORDER BY SortNo";
        String strMenu_Urdu="";
        int iID;
        GridLayout menuLayout_sizes = (GridLayout) findViewById(R.id.ll_SubMenu_Sizes);
        menuLayout_sizes.removeAllViews();
        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                Button b1 = new Button(this);
                strMenu_Urdu=rs.getString("MaterialSize");
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
                menuLayout_sizes.addView(b1,params);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int iID=v.getId();
                        String strName="";
                        Button myBtn=(Button) v;
                        strName=utility_functions.getSingleStringValue("FinalMaterialName","FinalMaterials"," WHERE EntryID="+Integer.toString(iID),context);
                        strName=strName+" ("+myBtn.getText().toString()+")";

                        //Message.message(context,Integer.toString(v.getTop()));
                        addItemToLV(iID,strName,false);
                    }
                });
            }
            txtSubMenu.setText(strName);
            svSubMenus.setVisibility(View.GONE);
            svSizes.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }
    private void loadDealDetails(int iDealID ,String strDealName){
        ArrayList<DealsData> DealsData_List_Inner=new ArrayList<DealsData>();
        Intent i = new Intent(context, deal_detail.class);
        //i.putParcelableArrayListExtra("DealData_List",DealsData_List_Inner);
        Bundle args = new Bundle();
        i.putExtra("DealID",iDealID);
        DD=new DealsData();
        DD.iDealID=iDealID;
        DD.iDealNo=1;

        DealsData_List_Inner.add(DD);

        //args.putSerializable("DealData_List",(Serializable)DealsData_List_Inner);
        //args.putParcelableArrayList("DealData_List",DealsData_List_Inner);//This line Passes Array to Activity.
        args.putParcelable("DealData",DD);
        i.putExtra("BUNDLE",args);
        //startActivity(i);
        startActivityForResult(i,REQUEST_CODE_1);
        //System should wait here...
        //Now First use addItemsToLV & get the Index then use that index as the Key....
        //DealsData_List_Outer.put()
        //datanum=new HashMap<String, String>();
    }
    // This method is invoked when target activity return result data back.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        // The returned result data is identified by requestCode.
        // The request code is specified in startActivityForResult(intent, REQUEST_CODE_1); method.
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case REQUEST_CODE_1:
                if(resultCode == RESULT_OK)
                {
                    Bundle args = dataIntent.getBundleExtra("BUNDLE");
                    DD=args.getParcelable("DealData");
                    int iIndex;
                    //Now that I have got what is returned from Activity, we should add it to List View
                    iIndex=addItemToLV(DD.iDealID,"",true);
                    int iLastIndex=DealsData_List_Outer.size()+1;
                    DealsData_List_Outer.put(Integer.toString(iLastIndex),DD);
                    //Message.message(context,"OKay,"+Integer.toString(DD.iDealID));

                }
                else if(resultCode==RESULT_CANCELED)
            {
                Message.message(context,"Cancelled");
            }
        }
    }
}
