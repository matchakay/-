package matcha.kay.jp.simplehouseholdaccountbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        TextView tv_set_date = findViewById(R.id.dateText);
        TextView tv_use = findViewById(R.id.selectSpent);
        EditText et_money = findViewById(R.id.input_money);

        Intent intent = getIntent();
        this.id = intent.getIntExtra("id", 0);
        int money = intent.getIntExtra("money",0);
        if(money < 0) {
            money = -money;
        }
        et_money.setText(String.valueOf(money));
        tv_use.setText(intent.getStringExtra("bop"));
        tv_set_date.setText(String.valueOf(intent.getIntExtra("year",0) + "年" + intent.getIntExtra("month",0) + "月" + intent.getIntExtra("day",0)+ "日"));

        //Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinner
        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        adapter.add("項目一覧");

        //カレンダー遷移
        final ImageButton calendarImgView = findViewById(R.id.calendarImageView);
        calendarImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDialogFragment updaterDialogFragment = new UpdateDialogFragment();
                updaterDialogFragment.show(getSupportFragmentManager(),"ok");
            }
        });

        //ラジオボタン
        RadioButton radioInButton = findViewById(R.id.radioIncome);
        radioInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeList(v);
            }
        });

        //
        RadioButton radioSpButton = findViewById(R.id.radioSpending);
        radioSpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeList(v);
            }
        });

        //スピナー選択
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if(position != 0) {
                    TextView tv_select_spent = findViewById(R.id.selectSpent);
                    tv_select_spent.setText(String.valueOf(parent.getSelectedItem()));
                    spinner.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //更新
        Button update_data_btn = findViewById(R.id.updateData);
        update_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        //キャンセル
        Button cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void changeList(View v){
        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {"bop_name"};
        int id = v.getId();
        String[] whereArgs = {"0"};
        if (id == R.id.radioIncome) {
            whereArgs[0] = "1";
        }
        else {
            whereArgs[0] = "0";
        }
        Cursor cursor =
                db.query("t_bop", columns, "i_or_s = ?", whereArgs, null, null, null);
        //Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("項目一覧");
        //spinner
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        boolean flg = cursor.moveToFirst();
        while (flg) {
            adapter.add(cursor.getString(0));
            flg = cursor.moveToNext();
        }
        db.close();
    }

    private  void updateData() {
        RadioGroup rg = findViewById(R.id.radioGroup);
        int radioId = rg.getCheckedRadioButtonId();

        TextView tv_date = findViewById(R.id.dateText);
        TextView tv_use = findViewById(R.id.selectSpent);
        EditText et_money = findViewById(R.id.input_money);

        int money = 0;

        if(!tv_date.getText().toString().isEmpty() && !tv_use.getText().toString().isEmpty() &&
                !et_money.getText().toString().isEmpty()) {
            money = Integer.parseInt(et_money.getText().toString());
            if(radioId == R.id.radioSpending) {
                money = -money;
            }
            String date = tv_date.getText().toString();
            String[] dateList = date.split("[一-龠]");
            int[] list = new int[3];
            for (int i = 0; i < list.length; i++) {
                list[i] = Integer.parseInt(dateList[i]);
            }

            MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL("UPDATE t_income_and_spending SET bop_name = '" + tv_use.getText().toString() + "', money = " +
                    money + ", year = " + list[0] + ", month = " + list[1] +
                    ", dayOfMonth = " + list[2] + " where _id = " + id);
            db.close();
            Toast.makeText(this, "変更完了", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Toast.makeText(this, "項目を埋めてください", Toast.LENGTH_LONG).show();
        }
    }
    // ダイアログで入力した値をtextViewに入れる - ダイアログから呼び出される
    void updateDate(String date){
        TextView tv_date = findViewById(R.id.dateText);
        tv_date.setText(date);
    }

}
