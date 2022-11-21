package com.example.musadkhan.etherscanaddressserch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    LinearLayout statmentLayout;
    String userUrl="",baseUrl="https://api.etherscan.io/api?module=account&action=tokentx&address=";
    ProgressDialog progressDialog;
    String userInput;
    TextView transactionStatusTxt,fromTxt,toTxt,blockNoTxt,tokenNameTxt,tokenSymbolTxt,tokenValueTxt,timeStamptxt,hashNumberTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statmentLayout = findViewById(R.id.statmentLayout);
        transactionStatusTxt = findViewById(R.id.transactionStatus);
        fromTxt = findViewById(R.id.from);
        toTxt = findViewById(R.id.to);
        blockNoTxt = findViewById(R.id.blockNumber);
        tokenNameTxt = findViewById(R.id.tokenName);
        tokenSymbolTxt = findViewById(R.id.tokenSymbol);
        tokenValueTxt = findViewById(R.id.tokenAmount);
        timeStamptxt = findViewById(R.id.timeStamp);
        hashNumberTxt = findViewById(R.id.hash);





        searchView = findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);



                statmentLayout.setVisibility(View.VISIBLE);
                userUrl = baseUrl+""+query+"&page=1&offset=1&startblock=0&endblock=27025780&sort=desc&apikey=6Q5ZBAYSJVSR31Z34FYXI9ZG24EPQEBA61";
                userInput=query;

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Fetching Your Transaction Data...");
                progressDialog.show();

                getdata();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void getdata() {

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.GET, userUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        fromTxt.append(" "+jsonObject.getString("from"));

                        String from = jsonObject.getString("from");
                        if (from != userInput){

                            transactionStatusTxt.setText("INCOMING TRANSACTION");
                            //transactionStatusTxt.setText(userInput);
                            transactionStatusTxt.setTextColor(Color.GREEN);
                        }
                        else{

                            transactionStatusTxt.setText("OUTGOING TRANSACTION");
                            transactionStatusTxt.setTextColor(Color.RED);
                        }





                        toTxt.append(" "+jsonObject.getString("to"));
                        blockNoTxt.append(" "+jsonObject.getInt("blockNumber"));
                        tokenNameTxt.append(" "+jsonObject.getString("tokenName"));
                        tokenValueTxt.append(" "+jsonObject.getInt("value"));
                        tokenSymbolTxt.append(" "+jsonObject.getString("tokenSymbol"));
                        hashNumberTxt.append(" "+jsonObject.getString("hash"));


                        int TimeStamp = jsonObject.getInt("timeStamp");
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(TimeStamp*1000L);
                        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss",calendar).toString();
                        timeStamptxt.append(" "+date);




                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "musad", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Sorry Something Went wrong..Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);


    }

    private void checkStatus(String from) {

    }
}