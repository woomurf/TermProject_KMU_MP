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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    DB account_db;
    categoryDB category_db;
    TagDB tag_db;

    private TextView date;
    private EditText editDate;
    private TextView price;
    private EditText editPrice;
    private TextView category;
    private TextView editCategory;
    private TextView content;
    private EditText editContent;
    private TextView tag;
    private TextView editTag;

    private Button insert;

    private RadioButton Rb_income;
    private RadioButton Rb_expenditure;
    private RadioGroup radioGroup;

    private ListView contentList;
    private ArrayAdapter<String> mAdapter;

    private ArrayList<String> items;

    private DatePicker dp;

    String S_tag;
    int tagCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account_db = new DB(MainActivity.this,"AccountBook.db",null,1);
        category_db = new categoryDB(MainActivity.this,"categoryDB.db",null,1);
        tag_db = new TagDB(MainActivity.this,"tagDB.db",null,1);
        category_db.basicData();
        tag_db.basicData();



        date = (TextView)findViewById(R.id.date);
        price = (TextView)findViewById(R.id.price);
        category = (TextView)findViewById(R.id.category);
        content = (TextView)findViewById(R.id.content);
        tag = (TextView)findViewById(R.id.tag);

        editPrice = (EditText)findViewById(R.id.editPrice);
        editCategory = (TextView)findViewById(R.id.editCategory);
        editContent = (EditText)findViewById(R.id.editContent);
        editTag = (TextView) findViewById(R.id.editTag);

        insert = (Button)findViewById(R.id.insert);


        Rb_income = (RadioButton)findViewById(R.id.income);
        Rb_expenditure = (RadioButton)findViewById(R.id.expenditure);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        // 수입이 기본적으로 체크
        radioGroup.check(Rb_income.getId());


        dp = (DatePicker)findViewById(R.id.dp);

        S_tag = "";
        tagCount = 0;




        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
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

                Vector<String> list;
                if(Rb_expenditure.isChecked()){
                    list = category_db.getExpenditureCategory();
                }
                else{
                    list = category_db.getIncomeCategory();
                }


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

        editTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        MainActivity.this);
                alertBuilder.setTitle("select Tag");

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.select_dialog_singlechoice);

                Vector<String> list = tag_db.getTag();

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

                                editTag.setText(strName);
                            }
                        });
                alertBuilder.show();

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
        editCategory.setText(" Select Category ");
        editTag.setText("  Select Tag");

    }
/*
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
*/
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
