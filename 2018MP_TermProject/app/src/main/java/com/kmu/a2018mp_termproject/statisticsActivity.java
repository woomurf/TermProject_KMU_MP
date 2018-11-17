package com.kmu.a2018mp_termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class statisticsActivity extends AppCompatActivity {

    SQLiteDatabase category_db;
    SQLiteDatabase account_db;


    int TotalPrice;
    int lenght;

    Vector<String> categoryList;
    ArrayList<String> xVals;
    ArrayList<Entry> yvalues;

    ListView billList;
    ArrayAdapter<String> billAdapter;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_statistics);

        category_db = statisticsActivity.this.openOrCreateDatabase("categoryDB.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
        account_db = statisticsActivity.this.openOrCreateDatabase("AccountBook.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);

        billList = (ListView) findViewById(R.id.billList);
        billAdapter = new ArrayAdapter<String>(statisticsActivity.this,android.R.layout.simple_list_item_1);
        billList.setAdapter(billAdapter);


        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        categoryList = new Vector<String>();


        yvalues = new ArrayList<Entry>();
        xVals = new ArrayList<String>();

        getStatistic();
        getList("","");

        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");


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

        categoryList = getCategory();
        for(int i = 0; i < categoryList.size(); i++){
            xVals.add(categoryList.elementAt(i));
        }

        Vector<Integer> categoryPrice = new Vector<Integer>();
        int totalPrice = getTotalPrice();
        Vector<Float> priceRating = new Vector<Float>();

        for(int i = 0; i < categoryList.size(); i++){
            categoryPrice.add(getCategoryPrice(categoryList.elementAt(i)));
            priceRating.add((float)categoryPrice.elementAt(i)/totalPrice * 100);

            yvalues.add(new Entry(priceRating.elementAt(i),i));
        }
    }



    /*
    category vector를 받아오는 함수이다.
     */

    public Vector<String> getCategory(){
        Vector<String> list = new Vector<>();

        Cursor cursor;
        cursor = category_db.rawQuery("SELECT * FROM CATEGORYDB", null);

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

        cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK",null);

        while(cursor.moveToNext()){
            price += cursor.getInt(2);

        }

        return price;
    }

    /*
    category별 price를 구하는 함수이다.
     */
    public int getCategoryPrice(String category){
        int price = 0;


        Cursor cursor;

        cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE category = '"+ category+"' ",null);

        while(cursor.moveToNext()){
            price += cursor.getInt(2);
        }

        return price;
    }

    public void getList(String category, String tag) {

        billAdapter.clear();

        Cursor cursor;

        if (category != "" && tag != "") {
            cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE category = '" + category + "', tag = '" + tag + "'", null);
        } else if (category == "" && tag == "") {
            cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK", null);
        } else if (category == "") {
            cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE category = '" + category + "'", null);
        } else {
            cursor = account_db.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE tag = '" + tag + "'", null);
        }


        while (cursor.moveToNext()) {
            String query = "";

            query += cursor.getString(4) + "/ "
                    + cursor.getString(1) + "/ "
                    + cursor.getInt(2) + "/ "
                    + cursor.getString(5);

            billAdapter.add(query);
        }

        billAdapter.notifyDataSetChanged();


    }

}
