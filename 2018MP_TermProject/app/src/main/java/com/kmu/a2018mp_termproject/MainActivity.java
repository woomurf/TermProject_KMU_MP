package com.kmu.a2018mp_termproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
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
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    DB account_db;
    categoryDB category_db;

    private TextView date;
    private EditText editDate;
    private TextView price;
    private EditText editPrice;
    private TextView category;
    private TextView editCategory;
    private TextView content;
    private EditText editContent;
    private TextView tag;
    private EditText editTag;

    private Button insert;
    private Button delete;
    private Button modify;
    private Button search;
    private Button statistic;

    private ListView contentList;
    private ArrayAdapter<String> mAdapter;

    private ArrayList<String> items;

    private DatePicker dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account_db = new DB(MainActivity.this,"AccountBook.db",null,1);
        category_db = new categoryDB(MainActivity.this,"categoryDB.db",null,1);



        date = (TextView)findViewById(R.id.date);
        price = (TextView)findViewById(R.id.price);
        category = (TextView)findViewById(R.id.category);
        content = (TextView)findViewById(R.id.content);
        tag = (TextView)findViewById(R.id.tag);

        editPrice = (EditText)findViewById(R.id.editPrice);
        editCategory = (TextView)findViewById(R.id.editCategory);
        editContent = (EditText)findViewById(R.id.editContent);
        editTag = (EditText)findViewById(R.id.editTag);

        insert = (Button)findViewById(R.id.insert);
        delete = (Button)findViewById(R.id.delete);
        modify = (Button)findViewById(R.id.modify);
        search = (Button)findViewById(R.id.search);

        statistic = (Button)findViewById(R.id.btn_statistics);
        dp = (DatePicker)findViewById(R.id.dp);


        // search 했을 때 내용을 보여줄 아이템을 담을 list
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

        // category를 누르면 팝업이 떠서 category를 고를 수 있다.
        // 팝업 뜨는걸 dialog라고 한다.
        editCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        MainActivity.this);
                alertBuilder.setTitle("select category");

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.select_dialog_singlechoice);

                Vector<String> list = category_db.getCategory();

                for(int i = 0; i < list.size(); i++){
                    adapter.add(list.elementAt(i));
                }


                alertBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                alertBuilder.setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {


                        String strName = adapter.getItem(id);
                        editCategory.setText(strName);
                    }
                });
                alertBuilder.show();

            }
        });

        statistic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,statisticsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertData(){
        String tmpDate = "";
        tmpDate += dp.getYear();
        tmpDate += dp.getMonth()+1;
        tmpDate += dp.getDayOfMonth();

        String tmpItem = editContent.getText().toString();
        int tmpPrice = Integer.parseInt(editPrice.getText().toString());
        String tmpCategory = editCategory.getText().toString();
        String tmpTag = editTag.getText().toString();


        account_db.insert(tmpDate,tmpItem,tmpPrice,tmpCategory,tmpTag);

        editContent.setText("");
        editPrice.setText("");
        editCategory.setText(" select category ");
        editTag.setText("");

    }

    private void searchData(){

        mAdapter.clear();

        String tmpDate = "";
        tmpDate += dp.getYear();
        tmpDate += dp.getMonth()+1;
        tmpDate += dp.getDayOfMonth();


        Vector<String> result = account_db.getResult("");

        for (int j = 0 ; j < result.size(); j++ ){
            items.add(result.elementAt(j));
        }

        mAdapter.notifyDataSetChanged();     // ListView refresh
    }

    private void deleteData(){
        String tmpDate = editDate.getText().toString();
        String tmpItem = editContent.getText().toString();
        int tmpPrice = Integer.parseInt(editPrice.getText().toString());

        account_db.delete(tmpDate,tmpPrice,tmpItem);

    }

    /*
    private void modifyData(){
        String tmpDate = editDate.getText().toString();
        String tmpItem = editContent.getText().toString();
        int tmpPrice = Integer.parseInt(editPrice.getText().toString());
    }
    */
}
