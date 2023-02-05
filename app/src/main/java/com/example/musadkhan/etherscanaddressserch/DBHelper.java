package com.example.musadkhan.etherscanaddressserch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "userData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        DB.execSQL("create Table userApi_tb(chainName TEXT primary key,apiToken TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {

        DB.execSQL("drop Table if exists userApi_tb");
    }

    public Boolean insertApiData(String chainName,String apiToken){

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("chainName",chainName);
        contentValues.put("apiToken",apiToken);
        long result = DB.insert("userApi_tb",null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }


    public Cursor searchApiData(String chainName){

        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select apiToken from userApi_tb where chainName= ?", new String[]{chainName});
        return  cursor;
    }
}
