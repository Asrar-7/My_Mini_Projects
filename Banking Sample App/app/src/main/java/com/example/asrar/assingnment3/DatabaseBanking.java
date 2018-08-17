package com.example.asrar.assingnment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseBanking extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "Customer1.db";
    public final static String TABLE_NAME = "customer_table1";
    public static final String COL_1 = "ACCOUNT_NO";
    public static final String COL_2 = "AMOUNT";

    public DatabaseBanking(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public boolean create(String accno,int amt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, accno);
        cv.put(COL_2, amt);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) return false;
        else return true;
    }

    public boolean updateData(String accno,int amt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, accno);
        contentValues.put(COL_2, amt);
        db.update(TABLE_NAME, contentValues, "ACCOUNT_NO=?", new String[]{accno});
        return true;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ACCOUNT_NO INTEGER PRIMARY KEY,AMOUNT INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public Cursor getdetails(String accno){
        SQLiteDatabase db = this.getWritableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE ACCOUNT_NO='"+accno+"'";
        Cursor  cursor = db.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return res;
    }

}