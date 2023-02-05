package com.example.musadkhan.etherscanaddressserch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    CardView tronBtn,etherBtn,bscBtn,polygonBtn;
    String etherUserInput,tronUserInput,bscUserInput,polygonUserInput;
    public final static String URL = "com.example.musadkhan.etherscanaddressserch.URL";
    public final static String SCANNER = "com.example.musadkhan.etherscanaddressserch.SCANNER";
    public final static String INPUT = "com.example.musadkhan.etherscanaddressserch.INPUT";

    String etherBaseUrl="https://api.etherscan.io/api?module=account&action=tokentx&address=";
    String tronBaseUrl = "https://apilist.tronscan.org/api/transaction?sort=-timestamp&count=true&limit=5&start=0&address=";
    String bscBaseUrl = "https://api.bscscan.com/api?module=account&action=tokentx&page=1&offset=5&startblock=0&endblock=999999999&sort=desc&apikey=EGRS6BHDNFDVW76N67AI6KCKUGNS7VGVU8&address=";
    String polygonBaseUrl = "https://api.polygonscan.com/api?module=account&action=tokentx&startblock=0&endblock=99999999&page=1&offset=5&sort=desc&apikey=8H5PRURRFD6KPT5DQ5ICWVA8Z8RE3JVMJD&address=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etherBtn = findViewById(R.id.etherCard);
        etherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etherTransBuilder();
            }
        });

        tronBtn = findViewById(R.id.tronCard);
        tronBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tronTransBuilder();
            }
        });


        bscBtn = findViewById(R.id.bscCard);
        bscBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bscTransBuilder();
            }
        });

        polygonBtn = findViewById(R.id.polygonChainCard);
        polygonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                polyTransBuilder();
            }
        });



    }


    private void etherTransBuilder() {

        Intent etherIntent = new Intent(HomeActivity.this,userInputActivity.class);
        etherIntent.putExtra(SCANNER,"ether");
        startActivity(etherIntent);


//
//        AlertDialog.Builder etherDialog = new AlertDialog.Builder(HomeActivity.this);
//        etherDialog.setTitle("Please Enter Your Ether Wallet Address ");
//
//        final EditText etherInput = new EditText(HomeActivity.this);
//
//        etherDialog.setView(etherInput);
//
//        etherDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                etherUserInput = etherInput.getText().toString();
//                String etherUrl = etherBaseUrl+etherUserInput+"&page=1&offset=3&startblock=0&endblock=27025780&sort=desc&apikey=6Q5ZBAYSJVSR31Z34FYXI9ZG24EPQEBA61";
//                Intent etherIntent = new Intent(HomeActivity.this,MainActivity.class);
//                etherIntent.putExtra(URL,etherUrl);
//                etherIntent.putExtra(SCANNER,"ether");
//                etherIntent.putExtra(INPUT,etherUserInput);
//                startActivity(etherIntent);
//            }
//        });
//
//        etherDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.cancel();
//            }
//        });
//        etherDialog.show();
    }

    private void tronTransBuilder() {


        Intent etherIntent = new Intent(HomeActivity.this,userInputActivity.class);
        etherIntent.putExtra(SCANNER,"tron");
        startActivity(etherIntent);


//        Toast.makeText(this, "Your Selected Module is Unavailable Right now ... Please Try Again Later", Toast.LENGTH_SHORT).show();

//        AlertDialog.Builder tronDialog = new AlertDialog.Builder(HomeActivity.this);
//        tronDialog.setTitle("Please Enter Your Tron Wallet Address ");
//
//        final EditText tronInput = new EditText(HomeActivity.this);
//
//        tronDialog.setView(tronInput);
//
//        tronDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                tronUserInput = tronInput.getText().toString();
//                //Toast.makeText(HomeActivity.this, ""+tronUserInput, Toast.LENGTH_SHORT).show();
//                String tronUrl = tronBaseUrl+""+tronUserInput+"&start_timestamp=";
//                Intent tronIntent = new Intent(HomeActivity.this,MainActivity.class);
//                tronIntent.putExtra(URL,tronUrl);
//                tronIntent.putExtra(SCANNER,"tron");
//                tronIntent.putExtra(INPUT,tronUserInput);
//                startActivity(tronIntent);
//            }
//        });
//
//        tronDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.cancel();
//            }
//        });
//
//        tronDialog.show();

    }

    private void bscTransBuilder() {

        //Toast.makeText(this, "Your Selected Module is Unavailable Right now ... Please Try Again Later", Toast.LENGTH_SHORT).show();

        Intent etherIntent = new Intent(HomeActivity.this,userInputActivity.class);
        etherIntent.putExtra(SCANNER,"bsc");
        startActivity(etherIntent);

//        AlertDialog.Builder bscDialog = new AlertDialog.Builder(HomeActivity.this);
//        bscDialog.setTitle("Please Enter Your BSC Wallet Address ");
//
//        final EditText bscInput = new EditText(HomeActivity.this);
//
//        bscDialog.setView(bscInput);
//
//        bscDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                bscUserInput = bscInput.getText().toString();
//                //Toast.makeText(HomeActivity.this, ""+etherUserInput, Toast.LENGTH_SHORT).show();
//                String bscUrl = bscBaseUrl+""+bscUserInput;
//                Intent bscIntent = new Intent(HomeActivity.this,MainActivity.class);
//                bscIntent.putExtra(URL,bscUrl);
//                bscIntent.putExtra(SCANNER,"bsc");
//                bscIntent.putExtra(INPUT,bscUserInput);
//                startActivity(bscIntent);
//            }
//        });
//
//        bscDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.cancel();
//            }
//        });
//        bscDialog.show();

    }

    private void polyTransBuilder() {


        Intent etherIntent = new Intent(HomeActivity.this,userInputActivity.class);
        etherIntent.putExtra(SCANNER,"polygon");
        startActivity(etherIntent);


//        AlertDialog.Builder polyDialog = new AlertDialog.Builder(HomeActivity.this);
//        polyDialog.setTitle("Please Enter Your Polygon Chain Wallet Address ");
//
//        final EditText polyInput = new EditText(HomeActivity.this);
//
//        polyDialog.setView(polyInput);
//
//        polyDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                polygonUserInput = polyInput.getText().toString();
//                //Toast.makeText(HomeActivity.this, ""+etherUserInput, Toast.LENGTH_SHORT).show();
//                String polyUrl = polygonBaseUrl+""+polygonUserInput;
//                Intent polyIntent = new Intent(HomeActivity.this,MainActivity.class);
//                polyIntent.putExtra(URL,polyUrl);
//                polyIntent.putExtra(SCANNER,"poly");
//                polyIntent.putExtra(INPUT,polygonUserInput);
//                startActivity(polyIntent);
//            }
//        });
//
//        polyDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.cancel();
//            }
//        });
//        polyDialog.show();



    }



}