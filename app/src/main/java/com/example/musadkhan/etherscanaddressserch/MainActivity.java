package com.example.musadkhan.etherscanaddressserch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    String userInput,GlobalUrl,normalGlobalUrl,nftGlobalUrl,timeInString;
    int GlobalStamp;
    int timeStampRps,pauseFlag=2,newTransCount=0,newCount=0;
    long timeS,awe,awe1,updatedTime,ercUpdatedTime,nftUpdatedTime;
    BigDecimal timeInMili;
    RecyclerView recyclerView;
    ArrayList<model> arrayList = new ArrayList<>();
    TextView add,fromTimeTxt,newTransactionCountTxt;
    ImageView logo,newTranImg;
    ImageButton editBtn;
    Button clearBtn,pauseBtn;
    adapterClass aClass;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = findViewById(R.id.recyclerView);
        clearBtn = findViewById(R.id.clearBtn);
        pauseBtn = findViewById(R.id.pauseBtn);
        newTranImg = findViewById(R.id.newTranImg);
        newTransactionCountTxt = findViewById(R.id.newTransactionCount);
        editBtn = findViewById(R.id.editBtn);

        aClass = new adapterClass(arrayList,MainActivity.this);
        //recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,1,GridLayoutManager.VERTICAL,true));
        layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(aClass);







        add = findViewById(R.id.addressTxt);
        logo = findViewById(R.id.logoImg);

        Intent intent = getIntent();
        String tempGlobalUrl = intent.getStringExtra(walletAddressInput.erc20URL);
        String tempNormalGlobalUrl = intent.getStringExtra(walletAddressInput.NormalURL);
        String tempNftGlobalUrl = intent.getStringExtra(walletAddressInput.nftURL);
        String etherValue  = intent.getStringExtra(walletAddressInput.SCANNER);
        userInput = intent.getStringExtra(walletAddressInput.INPUT);


        if (etherValue.equals("tron"))
        {
            GlobalUrl = tempGlobalUrl+userInput;
        }
        else {
            GlobalUrl = tempGlobalUrl + userInput;
            normalGlobalUrl = tempNormalGlobalUrl + userInput;
            nftGlobalUrl = tempNftGlobalUrl + userInput;
        }
        //Toast.makeText(this, ""+etherValue, Toast.LENGTH_SHORT).show();

        // GET CURRENT TIME STAMP VALUE
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timeS = timestamp.getTime();
        //Toast.makeText(this, ""+timeS, Toast.LENGTH_SHORT).show();
        timeInMili = BigDecimal.valueOf(timeS);
        timeInString = Long.toString(timeS);
        timeS = timeS / 10;
        timeS = timeS /10;
        timeS = timeS /10;

        updatedTime = timeS;
        ercUpdatedTime = timeS;
        nftUpdatedTime = timeS;


        fromTimeTxt = findViewById(R.id.fromTiming);
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timeS*1000L);
        fromTimeTxt.setText("Transaction's From : "+DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString());

        add.setText("Wallet Address on Monitoring : "+userInput);
        if (etherValue.equals("ether"))
        {
            logo.setImageDrawable(getResources().getDrawable(R.drawable.ethereum));
            startGettingData();
        }
        else if (etherValue.equals("tron")){
            logo.setImageDrawable(getResources().getDrawable(R.drawable.tron_logo));

            Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
            awe1 = timestamp1.getTime();
            timeInString = Long.toString(awe1);

            tronDataReciever();
        }
        else if(etherValue.equals("bsc")){
            logo.setImageDrawable(getResources().getDrawable(R.drawable.binance));
            bscDataRetrive();
        }
        else
        {
            logo.setImageDrawable(getResources().getDrawable(R.drawable.polylogo));
            polyDataRetrive();
        }



        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                aClass.notifyDataSetChanged();
            }
        });


        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pauseFlag % 2 == 0)
                {
                    pauseFlag++;
                    pauseBtn.setText("Start");
                }
                else{
                    pauseFlag++;
                    pauseBtn.setText("Pause");


                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    timeS = timestamp.getTime();

                    timeS = timeS / 10;
                    timeS = timeS /10;
                    timeS = timeS /10;

                    updatedTime = timeS;
                    ercUpdatedTime = timeS;
                    nftUpdatedTime = timeS;



                    Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
                    awe1 = timestamp1.getTime();
                    //timeInString = Long.toString(awe1);



                    if(etherValue.equals("ether")) {
                        startGettingData();
                    }
                    else if (etherValue.equals("tron")){
                        tronDataReciever();
                    }
                    else if (etherValue.equals("bsc")){
                        bscScannerCalling();
                    }
                    else if (etherValue.equals("polygon")){
                        polyScannerCalling();
                    }
                }

            }
        });

        newTranImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerView.smoothScrollToPosition(arrayList.size()-1);
                checkForTop();

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder editDialog = new AlertDialog.Builder(MainActivity.this);
                editDialog.setTitle("Please Enter Your New Wallet Address ");
                final EditText addInput = new EditText(MainActivity.this);
                editDialog.setView(addInput);


                editDialog.setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String newAdd = addInput.getText().toString();

                        if (newAdd.equals(""))
                        {
                            dialogInterface.cancel();
                        }
                        else {

                            GlobalUrl = tempGlobalUrl+newAdd;
                            normalGlobalUrl = tempNormalGlobalUrl+newAdd;
                            nftGlobalUrl = tempNftGlobalUrl+newAdd;

                            add.setText("Wallet Address on Monitoring : "+newAdd);

                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            timeS = timestamp.getTime();

                            timeS = timeS / 10;
                            timeS = timeS /10;
                            timeS = timeS /10;

                            updatedTime = timeS;
                            ercUpdatedTime = timeS;
                            nftUpdatedTime = timeS;

                            Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
                            awe1 = timestamp1.getTime();

                            arrayList.clear();
                            aClass.notifyDataSetChanged();
                        }
                    }
                });


                editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });

                editDialog.show();

            }
        });

    }




    private boolean sactionAddressCheck(String add){

        String url = "https://public.chainalysis.com/api/v1/address/";
        String url1 = url +add;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //resultTxt.setText(response);
                    //Toast.makeText(MainActivity.this, "mm", Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("identifications");




                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    //resultTxt.setText(""+e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                //resultTxt.setText(""+error);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap header = new HashMap();
                header.put("X-API-Key","c3268bbecfb8e5de7215d6d1005522fe927b48dae16f9c34d0e25b6cca813c27");
                header.put("Accept","application/json");
                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

        return true;
    }






    private void startGettingData() {

        checkForTop();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // mTextField.setText("done!");
                if(pauseFlag % 2 == 0)
                {
                    retriveData();
                }
            }
        }.start();
    }


    private void retriveData() {

        // Toast.makeText(this, "inside retrive data", Toast.LENGTH_SHORT).show();

        checkForTop();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=4;j>=0;j--)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = jsonObject1.getString("tokenName");
                            String tokenSymbolRps = jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = jsonObject1.getInt("blockNumber");

                            int tokenDecimalRps  = jsonObject1.getInt("tokenDecimal");
                            String value = jsonObject1.getString("value");

                            int sCount = value.length()-tokenDecimalRps;
                            String a = (value.substring(0,sCount));
                            String b = (value.substring(sCount,(sCount+4)));
                            String c= (a+"."+b);
                            BigDecimal tokenAmountRps = new BigDecimal(c);



                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();


                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "ether";

                            if (timeStampRps >= ercUpdatedTime) {
                                //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps,date ,scannerName,blockNumberRps, tokenAmountRps));
                                timeS = timeStampRps;

                                newTransCount++;
                                newTransactionCountTxt.setVisibility(View.VISIBLE);
                                newTransactionCountTxt.setText(newTransCount+" New Transaction");
                                recyclerView.scrollToPosition(recyclerView.getTop());

                                ercUpdatedTime = timeStampRps;


                                newTranImg.setVisibility(View.VISIBLE);



                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        ercUpdatedTime++;
        checkForTop();
        retriveNormalTransaction();
    }


    private void retriveNormalTransaction() {



        StringRequest stringRequest = new StringRequest(Request.Method.GET, normalGlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=9;j>=0;j--)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = "Ethereum";//jsonObject1.getString("tokenName");
                            String tokenSymbolRps = "ETH";//jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = jsonObject1.getInt("blockNumber");

                            //int tokenDecimalRps  = jsonObject1.getInt("tokenDecimal");
                            String value = jsonObject1.getString("value");


                            Double power = Math.pow(10,18);
                            String cvb = (String.format("%.2f", power));

                            BigDecimal poweDec = new BigDecimal(cvb);
                            BigDecimal valueDec = new BigDecimal(value);

                            BigDecimal tokenAmountRps = valueDec.divide(poweDec);



                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();


                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "ether";

                            if (timeStampRps >= updatedTime) {

                                if(value.equals("0")) {

                                    //Toast.makeText(MainActivity.this, "vro", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                    arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps, date, scannerName, blockNumberRps, tokenAmountRps));

                                    recyclerView.scrollToPosition(recyclerView.getTop());
                                    newTransCount++;
                                    newTransactionCountTxt.setVisibility(View.VISIBLE);
                                    newTransactionCountTxt.setText(newTransCount+" New Transaction");

                                    updatedTime = timeStampRps;
                                    newTranImg.setVisibility(View.VISIBLE);



                                }
                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        updatedTime++;

        checkForTop();
        retriveNftTransaction();

    }


    private void retriveNftTransaction(){

        checkForTop();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, nftGlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=4;j>=0;j--)
                        {



                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = jsonObject1.getString("tokenName");
                            String tokenSymbolRps = jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = 0;//jsonObject1.getInt("blockNumber");
                            String value = jsonObject1.getString("tokenID");
                            BigDecimal tokenAmountRps = new BigDecimal(value);

                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();

                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "ether";

                            if (timeStampRps >= nftUpdatedTime) {
                                //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps,date ,scannerName,blockNumberRps, tokenAmountRps));
                                timeS = timeStampRps;

                                newTransCount++;
                                newTransactionCountTxt.setVisibility(View.VISIBLE);
                                newTransactionCountTxt.setText(newTransCount+" New Transaction");

                                //arrayList.set(arrayList.size()-2,new model());

                                recyclerView.scrollToPosition(recyclerView.getTop());
                                nftUpdatedTime = timeStampRps;


                                newTranImg.setVisibility(View.VISIBLE);


                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        checkForTop();

        nftUpdatedTime++;

        startGettingData();

    }

    private void checkForTop(){
        if (layoutManager.findFirstCompletelyVisibleItemPosition()==arrayList.size()-1)
        {
            newTranImg.setVisibility(View.INVISIBLE);
            newTransCount = 0;
            newTransactionCountTxt.setVisibility(View.INVISIBLE);
        }
    }



























    private void tronDataReciever() {
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

            }
            public void onFinish() {
                // mTextField.setText("done!");
                // GET CURRENT TIME STAMP VALUE
                if(pauseFlag % 2 == 0)
                {
                    tronDataRetrive();
                }
            }
        }.start();
    }

    private void tronDataRetrive(){
        //recyclerView.scrollToPosition(recyclerView.getTop());



        //String fixedUrl = GlobalUrl+timeInString;
        //Toast.makeText(this, ""+awe, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //Toast.makeText(MainActivity.this, "in Try", Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");


                    int tranCount =0;
                    if(jsonArrayData.length()>0) {

                        for (int j=jsonArrayData.length()-1;j>=0; j--) {


                            tranCount++;
                            //Toast.makeText(MainActivity.this, "in for loop", Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject1 = jsonArrayData.getJSONObject(j);

                            int blockNumberRs = jsonObject1.getInt("block");
                            String hashNumberRs = jsonObject1.getString("hash");

                            String timeStampRsq = jsonObject1.getString("timestamp");
                            long sab =  Long.parseLong(timeStampRsq);


                            String fromRs = jsonObject1.getString("ownerAddress");
                            String toRs = jsonObject1.getString("toAddress");

                            String valueRs = jsonObject1.getString("amount");



                            String statusRs="";
                            Boolean cnf = fromRs.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRs = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRs = "INCOMING TRANSACTION";
                            }

                            String tokenInfo = jsonObject1.getString("tokenInfo");
                            JSONObject jsonObject2 = new JSONObject(tokenInfo);

                            String tokenNameRs = jsonObject2.getString("tokenName");
                            String tokenSymbolRs = jsonObject2.getString("tokenType");

                            int tokenDecimalRs = jsonObject2.getInt("tokenDecimal");


                            Double power = Math.pow(10,tokenDecimalRs);
                            String cvb = (String.format("%.2f", power));

                            BigDecimal poweDec = new BigDecimal(cvb);
                            BigDecimal valueDec = new BigDecimal(valueRs);

                            BigDecimal tokenAmountRs = valueDec.divide(poweDec);





                            int abc=109;
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis((sab));
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();
                            String finalTimeStamp = ""+date;

                            String scannerName = "tron";



                            if (sab>=awe1) {
                                arrayList.add(new model(fromRs, toRs, tokenNameRs, tokenSymbolRs, hashNumberRs, statusRs, finalTimeStamp, scannerName, blockNumberRs, tokenAmountRs));

                                newTransCount++;
                                newTransactionCountTxt.setVisibility(View.VISIBLE);
                                newTransactionCountTxt.setText(newTransCount + " New Transaction");
                                recyclerView.scrollToPosition(recyclerView.getTop());

                                newTranImg.setVisibility(View.VISIBLE);

                                awe1 =sab;
                            }





                        }
                        //Toast.makeText(MainActivity.this, ""+tranCount+" New Transaction Added ...", Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



        awe1++;
        checkForTop();
        tronDataReciever();

    }



























    private void bscScannerCalling() {

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // mTextField.setText("done!");
                if(pauseFlag % 2 == 0)
                {
                    bscDataRetrive();
                }

            }
        }.start();
    }


    private void bscDataRetrive() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, GlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=4;j>=0;j--)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = jsonObject1.getString("tokenName");
                            String tokenSymbolRps = jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = jsonObject1.getInt("blockNumber");

                            int tokenDecimalRps  = jsonObject1.getInt("tokenDecimal");
                            String valueRps = jsonObject1.getString("value");

                            int sCount = valueRps.length()-tokenDecimalRps;
                            String a = (valueRps.substring(0,sCount));
                            String b = (valueRps.substring(sCount,(sCount+4)));
                            String c= (a+"."+b);
                            BigDecimal tokenAmountRps = new BigDecimal(c);
                            System.out.println(tokenAmountRps);


                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();


                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "bsc";

                            if (timeStampRps >= ercUpdatedTime) {
                                arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps,date, scannerName,blockNumberRps, tokenAmountRps));
                                ercUpdatedTime = timeStampRps;

                                //Array Adapter Work Here...........

                                recyclerView.scrollToPosition(recyclerView.getTop());
                                newTransCount++;
                                newTransactionCountTxt.setVisibility(View.VISIBLE);
                                newTransactionCountTxt.setText(newTransCount+" New Transaction");


                                newTranImg.setVisibility(View.VISIBLE);


                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        ercUpdatedTime++;

        checkForTop();
        bscNormalDataRetrive();


    }


    private void bscNormalDataRetrive(){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, normalGlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=9;j>=0;j--)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = "Binance";//jsonObject1.getString("tokenName");
                            String tokenSymbolRps = "BNB";//jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = jsonObject1.getInt("blockNumber");

                            //int tokenDecimalRps  = jsonObject1.getInt("tokenDecimal");
                            String value = jsonObject1.getString("value");

                            Double power = Math.pow(10,18);
                            String cvb = (String.format("%.2f", power));

                            BigDecimal poweDec = new BigDecimal(cvb);
                            BigDecimal valueDec = new BigDecimal(value);

                            BigDecimal tokenAmountRps = valueDec.divide(poweDec);



                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();


                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "bsc";

                            if (timeStampRps >= updatedTime) {

                                if(value.equals("0")) {

                                    //Toast.makeText(MainActivity.this, "vro", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                    arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps, date, scannerName, blockNumberRps, tokenAmountRps));

                                    recyclerView.scrollToPosition(recyclerView.getTop());
                                    newTransCount++;
                                    newTransactionCountTxt.setVisibility(View.VISIBLE);
                                    newTransactionCountTxt.setText(newTransCount+" New Transaction");


                                    newTranImg.setVisibility(View.VISIBLE);
                                    updatedTime = timeStampRps;


                                }
                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        updatedTime++;

        checkForTop();



        bscNftDataRetrive();

    }

    private void bscNftDataRetrive(){

        checkForTop();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, nftGlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=4;j>=0;j--)
                        {



                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = jsonObject1.getString("tokenName");
                            String tokenSymbolRps = jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = 0;//jsonObject1.getInt("blockNumber");
                            String value = jsonObject1.getString("tokenID");
                            BigDecimal tokenAmountRps = new BigDecimal(value);

                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();

                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "bsc";

                            if (timeStampRps >= nftUpdatedTime) {
                                //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps,date ,scannerName,blockNumberRps, tokenAmountRps));
                                timeS = timeStampRps;

                                newTransCount++;
                                newTransactionCountTxt.setVisibility(View.VISIBLE);
                                newTransactionCountTxt.setText(newTransCount+" New Transaction");

                                //arrayList.set(arrayList.size()-2,new model());

                                recyclerView.scrollToPosition(recyclerView.getTop());
                                nftUpdatedTime = timeStampRps;


                                newTranImg.setVisibility(View.VISIBLE);


                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        checkForTop();

        nftUpdatedTime++;

        bscScannerCalling();

    }


























    private void polyScannerCalling() {

        checkForTop();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // mTextField.setText("done!");
                if(pauseFlag % 2 == 0)
                {
                    polyDataRetrive();
                }
            }
        }.start();
    }

    private void polyDataRetrive() {


        checkForTop();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=4;j>=0;j--)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = jsonObject1.getString("tokenName");
                            String tokenSymbolRps = jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = jsonObject1.getInt("blockNumber");

                            int tokenDecimalRps  = jsonObject1.getInt("tokenDecimal");
                            String value = jsonObject1.getString("value");

                            int sCount = value.length()-tokenDecimalRps;
                            String a = (value.substring(0,sCount));
                            String b = (value.substring(sCount,(sCount+4)));
                            String c= (a+"."+b);
                            BigDecimal tokenAmountRps = new BigDecimal(c);



                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();


                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "polygon";

                            if (timeStampRps >= ercUpdatedTime) {
                                //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps,date ,scannerName,blockNumberRps, tokenAmountRps));
                                timeS = timeStampRps;

                                newTransCount++;
                                newTransactionCountTxt.setVisibility(View.VISIBLE);
                                newTransactionCountTxt.setText(newTransCount+" New Transaction");
                                recyclerView.scrollToPosition(recyclerView.getTop());

                                ercUpdatedTime = timeStampRps;


                                newTranImg.setVisibility(View.VISIBLE);



                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        ercUpdatedTime++;
        checkForTop();


        polyNormalDataRetrive();

    }


    private void polyNormalDataRetrive(){



        StringRequest stringRequest = new StringRequest(Request.Method.GET, normalGlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=9;j>=0;j--)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = "PolyGon Chain";//jsonObject1.getString("tokenName");
                            String tokenSymbolRps = "MATIC";//jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = jsonObject1.getInt("blockNumber");

                            //int tokenDecimalRps  = jsonObject1.getInt("tokenDecimal");
                            String value = jsonObject1.getString("value");

                            Double power = Math.pow(10,18);
                            String cvb = (String.format("%.2f", power));

                            BigDecimal poweDec = new BigDecimal(cvb);
                            BigDecimal valueDec = new BigDecimal(value);

                            BigDecimal tokenAmountRps = valueDec.divide(poweDec);



                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();


                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "polygon";

                            if (timeStampRps >= updatedTime) {

                                if(value.equals("0")) {

                                    //Toast.makeText(MainActivity.this, "vro", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                    arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps, date, scannerName, blockNumberRps, tokenAmountRps));

                                    recyclerView.scrollToPosition(recyclerView.getTop());
                                    newTransCount++;
                                    newTransactionCountTxt.setVisibility(View.VISIBLE);
                                    newTransactionCountTxt.setText(newTransCount+" New Transaction");


                                    newTranImg.setVisibility(View.VISIBLE);
                                    updatedTime = timeStampRps;


                                }
                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        updatedTime++;
        checkForTop();
        polyNftDataRetrive();

    }


    private void polyNftDataRetrive(){


        checkForTop();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, nftGlobalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(jsonArray.length()>0)
                    {
                        for (int j=4;j>=0;j--)
                        {



                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String fromRps = jsonObject1.getString("from");
                            String toRps = jsonObject1.getString("to");
                            String tokenNameRps = jsonObject1.getString("tokenName");
                            String tokenSymbolRps = jsonObject1.getString("tokenSymbol");
                            String hashNumberRps = jsonObject1.getString("hash");
                            int blockNumberRps = 0;//jsonObject1.getInt("blockNumber");
                            String value = jsonObject1.getString("tokenID");
                            BigDecimal tokenAmountRps = new BigDecimal(value);

                            timeStampRps = jsonObject1.getInt("timeStamp");
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(timeStampRps*1000L);
                            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa",calendar).toString();

                            String statusRps="";
                            Boolean cnf = fromRps.equalsIgnoreCase(userInput);
                            if (cnf){
                                statusRps = "OUTGOING TRANSACTION";
                            }
                            else{
                                statusRps = "INCOMING TRANSACTION";
                            }

                            String scannerName = "bsc";

                            if (timeStampRps >= nftUpdatedTime) {
                                //Toast.makeText(MainActivity.this, "New Transaction Found !!!", Toast.LENGTH_SHORT).show();
                                arrayList.add(new model(fromRps, toRps, tokenNameRps, tokenSymbolRps, hashNumberRps, statusRps,date ,scannerName,blockNumberRps, tokenAmountRps));
                                timeS = timeStampRps;

                                newTransCount++;
                                newTransactionCountTxt.setVisibility(View.VISIBLE);
                                newTransactionCountTxt.setText(newTransCount+" New Transaction");

                                //arrayList.set(arrayList.size()-2,new model());

                                recyclerView.scrollToPosition(recyclerView.getTop());
                                nftUpdatedTime = timeStampRps;


                                newTranImg.setVisibility(View.VISIBLE);


                            }
                        }



                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Went Wrong... Please Try again !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        checkForTop();

        nftUpdatedTime++;
        polyScannerCalling();


    }

}