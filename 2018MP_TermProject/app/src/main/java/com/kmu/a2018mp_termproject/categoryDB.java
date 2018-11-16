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
        db.execSQL("CREATE TABLE IF NOT EXISTS CATEGORYDB (_id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT UNIQUE);");

    }

    public void resetDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS CATEGORYDB");
        onCreate(db);
    }

    public void dumyData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO CATEGORYDB VALUES(null,'education');");

    }

    // category 추가
    public void insertCategory(String c){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO CATEGORYDB VALUES('"+ c +"');");
        db.close();
    }


    // category 삭제
    public void deleteCategory(String c){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CATEGORYDB WHERE category = '"+ c +"';");
        db.close();

    }

    //category를 담고있는 리스트를 반환.
    public Vector<String> getCategory(){
        SQLiteDatabase db = getReadableDatabase();
        Vector<String> list = new Vector<>();

        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM CATEGORYDB", null);

        int i = 0;

        while(cursor.moveToNext()){
            String ele = cursor.getString(1);
            i++;
            list.add(ele);
        }

        return list;
    }
}
