package com.example.musadkhan.etherscanaddressserch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class userInputActivity extends AppCompatActivity {


    TextView noteLink,apiDesc;
    EditText apiTokenTxt;
    Button verifyApiBtn,backBtn;
    ImageButton submitBtn;
    String apiInput,localUrl,apiFlag="false",currentVerifiStatus,chainScanner,universalHelpUrl;
    ImageView checkImg;
    DBHelper DB;


    public final static String NormalURL = "com.example.musadkhan.etherscanaddressserch.NORMALURL";
    public final static String erc20URL = "com.example.musadkhan.etherscanaddressserch.ERCURL";
    public final static String nftURL = "com.example.musadkhan.etherscanaddressserch.NFTURL";
    public final static String SCANNER = "com.example.musadkhan.etherscanaddressserch.SCANNER";
    public final static String API_INPUT = "com.example.musadkhan.etherscanaddressserch.INPUT";


    public static final String SHARED_PREF = "sharedPrefs";
    public static final String etherApiTokenText = "TEXT";
    public static final String bscApiTokenText = "TEXT";
    public static final String apiVerificationStatus = "VerifiStatus";

    private String loadedApiToken = "";
    private String bscLoadedApiToken = "";
    private String loadedStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        noteLink = findViewById(R.id.howToTxt);
        verifyApiBtn = findViewById(R.id.apiVerifyBtn);
        apiTokenTxt = findViewById(R.id.apiTokenTxt);
        checkImg = findViewById(R.id.checkImg);
        submitBtn = findViewById(R.id.startBtn);
        //backBtn = findViewById(R.id.backBtn);
        apiDesc = findViewById(R.id.apiDesciption);
        DB = new DBHelper(this);


        Intent intent = getIntent();
        chainScanner = intent.getStringExtra(HomeActivity.SCANNER);


        if(chainScanner.equals("tron"))
        {
            Intent redirectIntent = new Intent(userInputActivity.this, walletAddressInput.class);
            redirectIntent.putExtra(SCANNER, chainScanner);
            redirectIntent.putExtra(API_INPUT, "");
            startActivity(redirectIntent);
        }


        String searchResult="";
        Cursor crs = DB.searchApiData(chainScanner);
        if (crs.moveToFirst()) {
           searchResult = crs.getString(0);
        }
        if (searchResult.equals(""))
        {

        }
        else
        {
            Intent redirectIntent = new Intent(userInputActivity.this, walletAddressInput.class);
            redirectIntent.putExtra(SCANNER, chainScanner);
            redirectIntent.putExtra(API_INPUT, searchResult);
            startActivity(redirectIntent);
        }



        if (chainScanner.equals("ether"))
        {
            apiDesc.setText("Enter your API token from Ether Chain .");
            universalHelpUrl = "https://docs.etherscan.io/getting-started/viewing-api-usage-statistics";

        }

        else if (chainScanner.equals("bsc"))
        {
            apiDesc.setText("Enter your API token from Bsc Chain .");
            universalHelpUrl="https://docs.bscscan.com/getting-started/viewing-api-usage-statistics";
        }

        else if (chainScanner.equals("polygon"))
        {
            apiDesc.setText("Enter your API token from Polygon Chain .");
            universalHelpUrl="https://docs.polygonscan.com/getting-started/viewing-api-usage-statistics";
        }




        String sampleInput = apiTokenTxt.getText().toString();





        noteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noteUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(universalHelpUrl));
                startActivity(noteUrl);
            }
        });



        verifyApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                verifyApiBtn.setText("Please Wait !!");
                String ss = apiTokenTxt.getText().toString();

                verifyApi();
                Boolean check = DB.insertApiData(chainScanner,ss);
                if(check==true) {
                    //Toast.makeText(userInputActivity.this, "insertes Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(userInputActivity.this, "Please Verify Again ...", Toast.LENGTH_SHORT).show();
                }
//                if(chainScanner.equals("ether")) {
//                    saveData();
//                }
//                else{
//                    saveBscData();
//                }
            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(apiFlag=="false")
                {
                    Toast.makeText(userInputActivity.this, "Please Verify Your API Token", Toast.LENGTH_SHORT).show();
                }
                else{
                    passActivity();
                }


            }
        });



//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent backIntent = new Intent(userInputActivity.this,HomeActivity.class);
//                startActivity(backIntent);
//            }
//        });








    }



    private void passActivity() {

        Intent etherIntent = new Intent(userInputActivity.this,walletAddressInput.class);
        etherIntent.putExtra(SCANNER,chainScanner);
        etherIntent.putExtra(API_INPUT,apiInput);
        startActivity(etherIntent);



    }




    private void verifyApi() {


        checkImg.setVisibility(View.INVISIBLE);
        apiFlag = "false";

        apiInput = apiTokenTxt.getText().toString();

        if (chainScanner.equals("ether")) {
            localUrl = "https://api.etherscan.io/api?module=account&action=balance&address=0xBE0eB53F46cd790Cd13851d5EFf43D12404d33E8&tag=latest&apikey=" + apiInput;
        }

        else if (chainScanner.equals("bsc"))
        {
            localUrl = "https://api.bscscan.com/api?module=account&action=balance&address=0x21d45650db732cE5dF77685d6021d7D5d1da807f&apikey="+apiInput;
        }

        else if(chainScanner.equals("polygon"))
        {
            localUrl = "https://api.polygonscan.com/api?module=account&action=balance&address=0x06959153B974D0D5fDfd87D561db6d8d4FA0bb0B&apikey="+apiInput;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, localUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if (status==1)
                    {
                        //Toast.makeText(userInputActivity.this, "cong", Toast.LENGTH_SHORT).show();
                        if(apiInput.equals(""))
                        {
                            verifyApiBtn.setText("VERIFY");
                            Toast.makeText(userInputActivity.this, "Please Enter Your API Token", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            checkImg.setVisibility(View.VISIBLE);
                            apiFlag = "true";
                            apiInput = apiTokenTxt.getText().toString();
                            currentVerifiStatus = "ok";
                            verifyApiBtn.setText("Successfully Verified");
                            //submitBtn.setText("Continue");
                        }
                    }
                    else
                    {
                        verifyApiBtn.setText("VERIFY");
                        Toast.makeText(userInputActivity.this, "Invalid Api Token ... Please Try Another Input", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                verifyApiBtn.setText("VERIFY");
                Toast.makeText(userInputActivity.this, "Something went wrong ... Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




    }
}