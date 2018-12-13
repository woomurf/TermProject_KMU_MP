package com.kmu.a2018mp_termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AccountBookActivity extends AppCompatActivity {

    SQLiteDatabase accountDB;

    Calendar date;
    ListView bill_view;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> bill_list;

    ImageButton btn_sts;
    TextView title;

    String title_month;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);

        accountDB = AccountBookActivity.this.openOrCreateDatabase("AccountBook.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);

        date = Calendar.getInstance();

        // ArrayList와 Adapter, ListView를 연결해준다. ArrayList에 내용을 추가하면 됨!
        bill_view = (ListView)findViewById(R.id.bills);
        bill_list = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(AccountBookActivity.this,android.R.layout.simple_list_item_1,bill_list);
        bill_view.setAdapter(mAdapter);

        // 해당 월을 표시할 title
        title = (TextView)findViewById(R.id.title);

        int curMonth = date.get(Calendar.MONTH) + 1;

        title_month  = curMonth + "월";

        title.setText(title_month);

    }

    private void getBills(){
        // 해당 월에 발생한 영수증을 모두 list에 넣는다!
        String minDate = "";
        String maxDate = "";

        minDate += date.get(Calendar.YEAR) + (date.get(Calendar.MONTH)+1) + "1";
        maxDate += date.get(Calendar.YEAR) + (date.get(Calendar.MONTH)+2) + "1";


        Cursor cursor;
        cursor = accountDB.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE date >= minDate AND date < maxDate",null);

        List<String> list;

        while(cursor.moveToNext()){
            String row = "";

            row += cursor.getColumnName(1) + " "
                    + cursor
        }

    }


}
