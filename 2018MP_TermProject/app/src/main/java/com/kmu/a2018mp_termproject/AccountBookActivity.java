package com.kmu.a2018mp_termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class AccountBookActivity extends AppCompatActivity {

    SQLiteDatabase accountDB;
    DB account_db;
    categoryDB categoryDB;

    Calendar date;
    ArrayList<myGroup> billList;
    ExpandableListView billListView;
    BaseExpandableAdapter billAdapter;

    ImageButton btn_sts;
    TextView title;
    Button add;
    ImageButton prev;
    ImageButton next;

    TextView tvIncome;
    TextView tvExpenditure;
    TextView tvBalance;

    String title_month;

    int width;
    int curMonth;
    int curYear;

    Vector<String> incomeCategory;
    Vector<String> expenditureCategory;

    int incomeMoney;
    int outMoney;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_accountbook);

        account_db = new DB(AccountBookActivity.this,"AccountBook.db",null,1);
        accountDB = account_db.getReadableDatabase();


        categoryDB = new categoryDB(AccountBookActivity.this,"categoryDB.db",null,1);

        Display newDisplay = getWindowManager().getDefaultDisplay();
        width = newDisplay.getWidth();

        accountDB = AccountBookActivity.this.openOrCreateDatabase("AccountBook.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);

        date = Calendar.getInstance();

        billList = new ArrayList<myGroup>();
        billListView = (ExpandableListView) findViewById(R.id.weekBill);

        billAdapter = new BaseExpandableAdapter(this,R.layout.group_row,R.layout.child_row,billList);
        billListView.setIndicatorBounds(width-50,width);
        billListView.setAdapter(billAdapter);

        add = (Button)findViewById(R.id.add);
        btn_sts = (ImageButton)findViewById(R.id.btn_statistics);
        prev = (ImageButton)findViewById(R.id.prevMonth);
        next = (ImageButton)findViewById(R.id.nextMonth);

        tvIncome = (TextView)findViewById(R.id.textIncome);
        tvExpenditure = (TextView)findViewById(R.id.textExpenditure);
        tvBalance = (TextView)findViewById(R.id.balance);



        // 해당 월을 표시할 title
        title = (TextView)findViewById(R.id.title);

        curMonth = date.get(Calendar.MONTH) + 1;
        curYear = date.get(Calendar.YEAR);

        if(curMonth < 10){
            title_month = "0" + curMonth;
        }else{
            title_month = curMonth +"";
        }

        title.setText(title_month + "월");

        incomeCategory = categoryDB.getIncomeCategory();
        expenditureCategory = categoryDB.getExpenditureCategory();

        incomeMoney = 0;
        outMoney = 0;



        setBillList();

        tvIncome.setText(incomeMoney + "원 ");
        tvExpenditure.setText(outMoney+ "원 ");
        tvBalance.setText((incomeMoney-outMoney)+ "원 " );

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountBookActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_sts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountBookActivity.this,statisticsActivity.class);
                startActivity(intent);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeMoney = 0;
                outMoney = 0;

                if(curMonth == 1){
                    curMonth = 12;
                    curYear -= 1;
                }
                else{
                    curMonth -=1;
                }

                if(curMonth < 10){
                    title_month = "0" + curMonth;
                }
                else{
                    title_month = curMonth +"";
                }


                title.setText(title_month + "월");
                setBillList();

                tvIncome.setText(incomeMoney + "원 ");
                tvExpenditure.setText(outMoney+ "원 ");
                tvBalance.setText((incomeMoney-outMoney)+ "원 " );

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeMoney = 0;
                outMoney = 0;

                if(curMonth == 12){
                    curMonth = 1;
                    curYear ++;
                }
                else{
                    curMonth += 1;
                }

                if(curMonth < 10){
                    title_month = "0" + curMonth;
                }
                else{
                    title_month = curMonth +"";
                }

                title.setText(title_month+"월");
                setBillList();

                tvIncome.setText(incomeMoney + "원 ");
                tvExpenditure.setText(outMoney+ "원 ");
                tvBalance.setText((incomeMoney-outMoney)+ "원 " );
            }
        });



    }

    private void setBillList(){
        String listTitle = title_month + "월";
        String listTitles[] = {"","","","",""};

        listTitles[0] += listTitle+ "1일 ~ " + listTitle + "7일";
        listTitles[1] += listTitle + "8일 ~ " + listTitle + "14일";
        listTitles[2] += listTitle + "15일 ~ " +listTitle + "21일";
        listTitles[3] += listTitle + "22일 ~ " + listTitle + "28일";
        listTitles[4] += listTitle + "29일 ~ ";

        billList.clear();

        for (int i = 0; i < 5; i ++){
            myGroup week = new myGroup(listTitles[i]);

            int min = 7*i +1;
            int max = 7*(i+1);
            String minDate ="";
            String maxDate ="";

            if(min < 10){
                minDate += curYear + title_month + "0" +min;
            }
            else{
                minDate += curYear + title_month + min;
            }

            if(max < 10){
                maxDate += curYear + title_month +"0"+ max;
            }
            else{
                maxDate += curYear + title_month + max;
            }
            Log.d("account",minDate);
            Log.d("account",maxDate);
            Cursor cursor;
            cursor = accountDB.rawQuery("SELECT * FROM ACCOUNTBOOK WHERE date >='"+ minDate+"' AND date <= '"+maxDate+"'",null);

            while(cursor.moveToNext()){
                String how = cursor.getString(4);
                if(incomeCategory.indexOf(how) != -1){
                    String row = "++ " +cursor.getString(3) + " "
                            + cursor.getString(1) + " "
                            + cursor.getInt(2) + "원";

                    week.child.add(row);

                    incomeMoney += cursor.getInt(2);

                }
                else{
                    String row = "-- " + cursor.getString(3) + " "
                            + cursor.getString(1) + " "
                            + cursor.getInt(2) + "원";

                    week.child.add(row);

                    outMoney += cursor.getInt(2);
                }
            }

            billList.add(week);




        }
        billAdapter.notifyDataSetChanged();
    }

    private void setTextColor(){

    }


}
