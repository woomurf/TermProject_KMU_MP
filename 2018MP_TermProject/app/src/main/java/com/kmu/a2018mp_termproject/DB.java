package com.kmu.a2018mp_termproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DB extends SQLiteOpenHelper {


    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }


    // DB를 만든다.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ACCOUNTBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, date Date);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    //DB에 데이터를 삽입 할 때 사용되는 함수. db를 쓰기모드로 열고, 쓴 다음 닫아준다.
    public void insert(String date, String item, int price){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO ACCOUNTBOOK VALUES(null,'" + item + "', " + price + ", '" + date + "');");
        db.close();
    }

    //DB에 데이터를 수정 할 때 사용되는 함수. db를 쓰기모드로 열고, 쓴 다음 닫아준다.
    public void update(String item, int price, String date){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE ACCOUNTBOOK SET price = "+ price +" WHERE item = "+ item +" ");
        db.close();
    }

    //DB에서 데이터를 삭제한다.
    public void delete(String date,int price ,String item){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM ACCOUNTBOOK WHERE item = "+item+"  AND date = "+date+" AND price ="+price+";");
        db.close();
    }

    //날짜에 맞는 데이터를 표시한다.
    public String[] getResult(String searchDate){
        SQLiteDatabase db = getReadableDatabase();
        String[] result = {""};
        Cursor cursor;

        if (searchDate == ""){
            cursor = db.rawQuery("SELECT * FROM ACCOUNTBOOK",null);
        }
        else{
            cursor = db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE date = '"+searchDate+"'", null);
        }

        int i = 0;

        while(cursor.moveToNext()){
            result[i] += cursor.getString(0)
                    + ":"
                    + cursor.getString(1)
                    + " | "
                    + cursor.getInt(2)
                    + "원"
                    + cursor.getString(3);
            i++;
        }

        return result;
    }
}
