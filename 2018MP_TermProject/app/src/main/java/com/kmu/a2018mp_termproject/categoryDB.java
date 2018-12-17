package com.kmu.a2018mp_termproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Vector;

public class categoryDB extends SQLiteOpenHelper {

    public categoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS INCOME_CATEGORYDB (_id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT UNIQUE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS EXPENDITURE_CATEGORYDB (_id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT UNIQUE);");
    }

    public void resetDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS CATEGORYDB");
        onCreate(db);
    }

    public void basicData(){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM EXPENDITURE_CATEGORYDB",null);

        if(cursor != null && cursor.getCount() ==0){
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'식사');");
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'문화생활');");
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'교통비');");
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'교육');");
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'의료');");
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'의류');");
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'회비');");
            db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES(null,'기타');");
        }

        cursor = db.rawQuery("SELECT * FROM INCOME_CATEGORYDB",null);

        if(cursor != null && cursor.getCount() == 0){
            db.execSQL("INSERT INTO INCOME_CATEGORYDB VALUES(null,'월급');");
            db.execSQL("INSERT INTO INCOME_CATEGORYDB VALUES(null,'이자');");
            db.execSQL("INSERT INTO INCOME_CATEGORYDB VALUES(null,'용돈');");
            db.execSQL("INSERT INTO INCOME_CATEGORYDB VALUES(null,'기타');");
        }

    }

    // category 추가
    public void insertExpenditureCategory(String c){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO EXPENDITURE_CATEGORYDB VALUES('"+ c +"');");
        db.close();
    }

    public void insertIncomeCategory(String c){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO INCOME_CATEGORYDB VALUES('"+ c +"');");
        db.close();
    }


    // category 삭제
    public void deleteExpenditureCategory(String c){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM EXPENDITURE_CATEGORYDB WHERE category = '"+ c +"';");
        db.close();

    }

    public void deleteIncomeCategory(String c){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM INCOME_CATEGORYDB WHERE category = '"+ c +"';");
        db.close();

    }

    //category를 담고있는 리스트를 반환.
    public Vector<String> getIncomeCategory(){
        SQLiteDatabase db = getReadableDatabase();
        Vector<String> list = new Vector<>();

        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM INCOME_CATEGORYDB", null);


        int i = 0;

        while(cursor.moveToNext()){
            String ele = cursor.getString(1);
            i++;
            list.add(ele);
        }

        return list;
    }

    public Vector<String> getExpenditureCategory(){
        SQLiteDatabase db = getReadableDatabase();
        Vector<String> list = new Vector<>();

        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM EXPENDITURE_CATEGORYDB", null);


        int i = 0;

        while(cursor.moveToNext()){
            String ele = cursor.getString(1);
            i++;
            list.add(ele);
        }

        return list;
    }
}


