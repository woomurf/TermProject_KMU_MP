package com.kmu.a2018mp_termproject;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

        editDate = (EditText)findViewById(R.id.editDate);
        editPrice = (EditText)findViewById(R.id.editPrice);
        editCategory = (EditText)findViewById(R.id.editCategory);
        editContent = (EditText)findViewById(R.id.editContent);
        editTag = (EditText)findViewById(R.id.editTag);

        insert = (Button)findViewById(R.id.insert);
        delete = (Button)findViewById(R.id.delete);
        modify = (Button)findViewById(R.id.modify);
        search = (Button)findViewById(R.id.search);

        contentList = (ListView)findViewById(R.id.contentList);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(MainActivity.this,0);
        contentList.setAdapter(mAdapter);
    }

    private void insertData(){
        String tmpDate = editDate.getText().toString();
        String tmpItem = editContent.getText().toString();
        int tmpPrice = Integer.parseInt(editPrice.getText().toString());


        db.insert(tmpDate,tmpItem,tmpPrice);
    }
}
