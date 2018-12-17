package com.kmu.a2018mp_termproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class TagDB extends SQLiteOpenHelper {

    public TagDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS TAGDB (_id INTEGER PRIMARY KEY AUTOINCREMENT,tag TEXT UNIQUE);");
        Cursor cursor = db.rawQuery("SELECT * FROM TAGDB",null);
    }

    public void insertTag(String tag){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TAGDB VALUES('"+tag+"');");
    }

    public void deleteTag(String tag){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TAGDB WHERE tag = '"+tag+"';");
    }

    public void updateTag(String oldtag,String newtag){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TAGDB SET tag = '"+newtag+"' WHERE tag = oldtag;");
    }

    public Vector<String> getTag(){
        SQLiteDatabase db = getReadableDatabase();
        Vector<String> list = new Vector<>();

        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM TAGDB", null);

        int i = 0;

        while(cursor.moveToNext()){
            String ele = cursor.getString(1);
            i++;
            list.add(ele);
        }

        return list;
    }

    public void basicData(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM TAGDB",null);

        if(cursor != null && cursor.getCount() ==0){
            db.execSQL("INSERT INTO TAGDB VALUES(null,'혼자');");
            db.execSQL("INSERT INTO TAGDB VALUES(null,'대학친구');");
            db.execSQL("INSERT INTO TAGDB VALUES(null,'동네친구');");
            db.execSQL("INSERT INTO TAGDB VALUES(null,'가족');");
            db.execSQL("INSERT INTO TAGDB VALUES(null,'연인');");
            db.execSQL("INSERT INTO TAGDB VALUES(null,'등교길');");
            db.execSQL("INSERT INTO TAGDB VALUES(null,'출근길');");
            db.execSQL("INSERT INTO TAGDB VALUES(null,'하교길');");
        }
    }

}
