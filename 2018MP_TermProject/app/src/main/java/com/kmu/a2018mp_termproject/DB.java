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
        db.execSQL("CREATE TABLE ACCOUNTBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, date Date,category TEXT,tag TEXT);");
        db.execSQL("CREATE TABLE CATEGORYDB (_id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT UNIQUE );");
        db.execSQL("INSERT INTO CATEGORYDB VALUES(category = dinner);");
        db.execSQL("INSERT INTO CATEGORYDB VALUES(category = lunch);");
        db.execSQL("INSERT INTO CATEGORYDB VALUES(category = movie);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    //DB에 데이터를 삽입 할 때 사용되는 함수. db를 쓰기모드로 열고, 쓴 다음 닫아준다.
    public void insert(String date, String item, int price,String category, String tag){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO ACCOUNTBOOK VALUES(null,'" + item + "', " + price + ", '" + date + "','"+category+"','"+tag+"');");
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
        db.execSQL("DELETE FROM ACCOUNTBOOK WHERE item = "+item+"  AND date = "+date+" AND price = price;");
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
            result[i] += cursor.getString(4)
                    + ":"
                    + cursor.getString(1)
                    + " | "
                    + cursor.getInt(2)
                    + "원"
                    + cursor.getString(3)
                    + "#"
                    + cursor.getString(5);
            i++;
        }

        return result;
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
    public String[] getCategory(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORYDB", null);

        String[] list = {};
        int i = 0;

        while(cursor.moveToNext()){
            list[i] = cursor.getString(1);
            i++;
        }

        return list;
    }


}
