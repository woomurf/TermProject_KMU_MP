package com.kmu.a2018mp_termproject;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DB db;

    private TextView date;
    private EditText editDate;
    private TextView price;
    private EditText editPrice;
    private TextView category;
    private EditText editCategory;
    private TextView content;
    private EditText editContent;
    private TextView tag;
    private EditText editTag;

    private Button insert;
    private Button delete;
    private Button modify;
    private Button search;

    private ListView contentList;
    private ArrayAdapter<String> mAdapter;

    private ArrayList<String> items;

    private DatePicker dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(MainActivity.this,"AccountBook.db",null,1);


        date = (TextView)findViewById(R.id.date);
        price = (TextView)findViewById(R.id.price);
        category = (TextView)findViewById(R.id.category);
        content = (TextView)findViewById(R.id.content);
        tag = (TextView)findViewById(R.id.tag);

        editPrice = (EditText)findViewById(R.id.editPrice);
        editCategory = (EditText)findViewById(R.id.editCategory);
        editContent = (EditText)findViewById(R.id.editContent);
        editTag = (EditText)findViewById(R.id.editTag);

        insert = (Button)findViewById(R.id.insert);
        delete = (Button)findViewById(R.id.delete);
        modify = (Button)findViewById(R.id.modify);
        search = (Button)findViewById(R.id.search);

        dp = (DatePicker)findViewById(R.id.dp);


        items = new ArrayList<String>();

        contentList = (ListView)findViewById(R.id.contentList);
        mAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,items);
        contentList.setAdapter(mAdapter);





        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    searchData();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void insertData(){
        String tmpDate = "";
        tmpDate += dp.getYear() + (dp.getMonth()+1) + dp.getDayOfMonth();
        String tmpItem = editContent.getText().toString();
        int tmpPrice = Integer.parseInt(editPrice.getText().toString());
        String tmpCategory = editCategory.getText().toString();
        String tmpTag = editTag.getText().toString();


        db.insert(tmpDate,tmpItem,tmpPrice,tmpCategory,tmpTag);
    }

    private void searchData(){

        mAdapter.clear();

        String tmpDate = "";
        tmpDate += dp.getYear() + (dp.getMonth()+1) + dp.getDayOfMonth();

        String[] result = db.getResult(tmpDate);

        for (int j = 0 ; j < result.length; j++ ){
            items.add(result[j]);
        }

        mAdapter.notifyDataSetChanged();     // ListView refresh
    }

    private void deleteData(){
        String tmpDate = editDate.getText().toString();
        String tmpItem = editContent.getText().toString();
        int tmpPrice = Integer.parseInt(editPrice.getText().toString());

        db.delete(tmpDate,tmpPrice,tmpItem);

    }

    /*
    private void modifyData(){
        String tmpDate = editDate.getText().toString();
        String tmpItem = editContent.getText().toString();
        int tmpPrice = Integer.parseInt(editPrice.getText().toString());
    }
    */
}
