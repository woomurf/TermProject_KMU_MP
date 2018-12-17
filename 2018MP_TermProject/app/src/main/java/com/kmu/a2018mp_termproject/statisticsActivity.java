package com.kmu.a2018mp_termproject;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.math.*;

public class statisticsActivity extends AppCompatActivity {

    SQLiteDatabase category_db;
    SQLiteDatabase account_db;
    TagDB tag_db;



    int TotalPrice;
    int lenght;

    Vector<String> categoryList;
    ArrayList<String> xVals;
    ArrayList<Entry> yvalues;
    Vector<Float> priceRating;
    Vector<Integer> categoryPrice;

    ArrayList<myGroup> billList;
    ExpandableListView billListView;
    BaseExpandableAdapter billAdapter;

    RadioButton Rb_income;
    RadioButton Rb_expenditure;
    RadioGroup radioGroup;

    PieChart pieChart;

    TextView tvTagList;
    String S_tagList;
    Button addTag;

    Vector<String> tagList;

    ImageButton prev;
    ImageButton next;
    TextView title_month;
    int month;
    int year;

    Calendar date;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_statistics);
        Display newDisplay = getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();

        date = Calendar.getInstance();

        title_month = (TextView)findViewById(R.id.title_month);
        month = date.get(Calendar.MONTH)+1;
        year = date.get(Calendar.YEAR);

        title_month.setText(month);

        ImageButton prev = (ImageButton)findViewById(R.id.btn_prev);
        ImageButton next = (ImageButton)findViewById(R.id.btn_next);


        category_db = statisticsActivity.this.openOrCreateDatabase("categoryDB.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
        account_db = statisticsActivity.this.openOrCreateDatabase("AccountBook.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
        tag_db = new TagDB(statisticsActivity.this,"tagDB.db",null,1);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        Rb_expenditure = (RadioButton)findViewById(R.id.Rb_expenditure);
        Rb_income = (RadioButton)findViewById(R.id.Rb_income);

        addTag = (Button)findViewById(R.id.addTag);
        tvTagList = (TextView)findViewById(R.id.tagList);

        radioGroup.check(Rb_expenditure.getId());

        billList = new ArrayList<myGroup>();
        billListView = (ExpandableListView) findViewById(R.id.BillList);

        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        categoryList = new Vector<String>();


        yvalues = new ArrayList<Entry>();
        xVals = new ArrayList<String>();

        tagList = new Vector<String>();

        billAdapter = new BaseExpandableAdapter(this,R.layout.group_row,R.layout.child_row,billList);
        billListView.setIndicatorBounds(width-50,width);
        billListView.setAdapter(billAdapter);


        getStatistic();
        getListDefault();


        drawPieChart();

        S_tagList = "";

        addTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        statisticsActivity.this);
                alertBuilder.setTitle("select Tag");

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        statisticsActivity.this,
                        android.R.layout.select_dialog_singlechoice);

                Vector<String> list = tag_db.getTag();
                list.add("clear");

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
                               // editTag.setText(strName);
                                if(strName == "clear"){
                                    S_tagList = "";
                                    tagList.clear();
                                }
                                else if(S_tagList == ""){
                                    S_tagList += strName;
                                    tagList.add(strName);
                                }
                                else{
                                    S_tagList += ", " + strName;
                                    tagList.add(strName);
                                }
                                tvTagList.setText(S_tagList);
                                getListDefault();
                            }
                        });
                alertBuilder.show();

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.Rb_expenditure:
                        getStatistic();
                        getListDefault();
                        billAdapter.notifyDataSetChanged();
                        drawPieChart();

                        break;
                    case R.id.Rb_income:
                        getStatistic();
                        getListDefault();
                        billAdapter.notifyDataSetChanged();
                        drawPieChart();
                        break;

                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month == 1){
                    month = 12;
                    title_month.setText(month);
                    year--;
                }
                else{
                    month--;
                    title_month.setText(month);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month == 12){
                    month = 1;
                    year++;
                    title_month.setText(month);
                }
                else{
                    month++;
                    title_month.setText(month);
                }
            }
        });




    }

    public void drawPieChart(){
        pieChart.setUsePercentValues(true);
        PieDataSet dataSet = new PieDataSet(yvalues, null);


        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new PercentFormatter());

        pieChart.setData(data);


        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setDescription("");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.animateXY(1400, 1400);
    }


    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }


    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


    /*
     xval, yval을 추가한다.
     totalPrice와 category별 price를 구해서 비율을 구한다.
     */
    private void getStatistic(){
        xVals.clear();
        yvalues.clear();

        categoryList = getCategory();

        categoryPrice = new Vector<Integer>();
        int totalPrice = getTotalPrice();
        priceRating = new Vector<Float>();

        for(int i = 0; i < categoryList.size(); i++){
            categoryPrice.add(getCategoryPrice(categoryList.elementAt(i)));
            priceRating.add((float)categoryPrice.elementAt(i)/totalPrice * 100);

            if(priceRating.elementAt(i) > 0){
                xVals.add(categoryList.elementAt(i));
                yvalues.add(new Entry(priceRating.elementAt(i),i));
            }
        }
    }



    /*
    category vector를 받아오는 함수이다.
     */

    public Vector<String> getCategory(){
        Vector<String> list = new Vector<>();

        Cursor cursor;

        if(Rb_expenditure.isChecked()){
            cursor = category_db.rawQuery("SELECT * FROM EXPENDITURE_CATEGORYDB", null);
        }
        else{
            cursor = category_db.rawQuery("SELECT * FROM INCOME_CATEGORYDB", null);
        }

        int i = 0;

        while(cursor.moveToNext()){
            String ele = cursor.getString(1);
            i++;
            list.add(ele);
        }

        return list;
    }



    /*
    총 price를 구해주는 함수이다.
     */
    public int getTotalPrice(){
        int price = 0;

        Cursor cursor;
        for (int i = 0; i < categoryList.size(); i++){
            cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE category = '"+categoryList.elementAt(i)+"'",null);

            while(cursor.moveToNext()){
                price += cursor.getInt(2);

            }
        }

        return price;
    }

    /*
    category별 price를 구하는 함수이다.
     */
    public int getCategoryPrice(String category){
        int price = 0;


        Cursor cursor;

        if(tagList.size() == 0){
            cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE category = '"+ category+"' ",null);
        }
        else{
            String ttt ="";
            String sql = "SELECT * FROM ACCOUNTBOOK WHERE category = '"+ category+"' ";
            for(int i = 0; i < tagList.size(); i++){
                ttt += "AND tag IN('"+tagList.elementAt(i) +"')";
            }
            sql += ttt;

            cursor = account_db.rawQuery(sql,null);
        }

        while(cursor.moveToNext()){
            price += cursor.getInt(2);
        }

        return price;
    }

    /*
    1. list view 에 category 이름(비율%)     총 가격 을 띄워준다.
    2. 각 list view를 클릭하면 해당 category의 내역을 보여준다.
    3. category와 tag를 선택하면 해당하는 내역을 다 보여준다.
    4. list view 위에 총 price를 띄워주는 것이 좋을 것 같다.
     */

    public void getListDefault() {

        billList.clear();
        getStatistic();

        for(int i = 0; i < categoryList.size(); i ++){



            String cRow = "";

            if(categoryPrice.elementAt(i) > 0){

                cRow += categoryList.elementAt(i)
                        +"("
                        +Math.round(priceRating.elementAt(i))
                        +"%) : "
                        +categoryPrice.elementAt(i)
                        +"원";

                myGroup temp = new myGroup(cRow);

                Cursor cursor;
                if(tagList.size() == 0){
                    cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE category = '"+categoryList.elementAt(i)+"'",null );

                    while(cursor.moveToNext()){
                        String row = "";


                        row += cursor.getString(1)
                                + " "
                                + cursor.getInt(2)
                                + "원";


                        temp.child.add(row);

                    }
                    billList.add(temp);

                }
                else{
                    for (int j = 0; j < tagList.size(); j++){
                        cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE category = '"+categoryList.elementAt(i)+"' AND tag = '"+tagList.elementAt(j)+"'",null );

                        while(cursor.moveToNext()){
                            String row = "";


                            row += cursor.getString(1)
                                    + " "
                                    + cursor.getInt(2)
                                    + "원";


                            temp.child.add(row);

                        }
                        if(temp.getSize() > 0){
                            billList.add(temp);
                        }
                    }
                }



            }



        }

        billAdapter.notifyDataSetChanged();




    }

}
