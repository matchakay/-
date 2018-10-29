package matcha.kay.jp.simplehouseholdaccountbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateFragment extends Fragment {

    private View view = null;
    private MyAdapter adapter;
    private ArrayList<HistoryBean> array = new ArrayList<>();
    private int tapPosition = 0;
    private boolean isFirst = false;

    public UpdateFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.update_fragment, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        getAll();

        //カレンダーボタンで遷移
        ImageButton calendarView = view.findViewById(R.id.search_calendar_btn);
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.show(getFragmentManager(), "calendar");
            }
        });
        //検索ボタンクリック
        Button search_btn = view.findViewById(R.id.search_data_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData();
            }
        });


        //リストを長押しで動作
        ListView listView = view.findViewById(R.id.updateListView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tapPosition = position;
                alertCheck();
                return false;
            }
        });
    }

    //CalendarActivityからの返しの結果を受け取る
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 5) {
            this.getAll();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // 表示時にさせたい処理を書く
            TextView tv_year = this.view.findViewById(R.id.textYear);
            TextView tv_month = this.view.findViewById(R.id.textMonth);
            tv_year.setText("");
            tv_month.setText("");
            getAll();
        }
    }
    private void alertCheck() {
        String[] alert_menu = {"編集", "削除", "キャンセル"};

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setItems(alert_menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //項目選択時の処理
                //削除・キャンセル
                if(which == 0) {
                    updateCheck();
                }
                else if(which == 1) {
                    deleteCheck();
                }
                else {
                    Log.d("debug", "cancel");
                }
            }
        });
        alert.show();
    }

    private void updateCheck() {
        Intent intent = new Intent(getActivity(), UpdateActivity.class);
        intent.putExtra("id", this.array.get(this.tapPosition).getId());
        intent.putExtra("bop", this.array.get(this.tapPosition).getBop_name());
        intent.putExtra("money", this.array.get(this.tapPosition).getMoney());
        intent.putExtra("year", this.array.get(this.tapPosition).getYear());
        intent.putExtra("month", this.array.get(this.tapPosition).getMonth());
        intent.putExtra("day", this.array.get(this.tapPosition).getDayOfMonth());
        startActivityForResult(intent,5);
    }

    private void deleteCheck() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        //AlertDialogのタイトル
        alertDialogBuilder.setTitle("削除");

        //AlertDialogのメッセージ設定
        alertDialogBuilder.setMessage("本当に削除しますか？");

        //AlertDialogのYesボタンのコールバックリスナーを登録
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem();
            }
        });

        //AlertDialogのNoボタンのコールバックリスナーを登録
        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void deleteItem() {
        int position = this.tapPosition;
        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from t_income_and_spending where _id =" + array.get(position).getId());
        db.close();

        this.array.remove(this.array.get(position));
        TextView tv_year = this.view.findViewById(R.id.textYear);
        if(tv_year.getText().toString().isEmpty()) {
            getAll();
        }
        else {
            searchData();
        }
        Toast.makeText(getActivity(), "削除しました", Toast.LENGTH_LONG).show();
    }

    protected void getAll() {
        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<HistoryBean> CopyArray = new ArrayList<>();
        this.array = CopyArray;
        String[] columns = {"money", "bop_name", "year", "month", "dayOfMonth", "_id"};
        Cursor cursor =
                    db.query("t_income_and_spending", columns, null, null, null, null, "year asc, month asc, dayOfMonth asc");
        boolean flg = cursor.moveToFirst();
        if(flg) {
            while (flg) {
                HistoryBean hb = new HistoryBean();
                hb.setMoney(cursor.getInt(0));
                hb.setBop_name(String.valueOf(cursor.getString(1)));
                hb.setYear(cursor.getInt(2));
                hb.setMonth(cursor.getInt(3));
                hb.setDayOfMonth(cursor.getInt(4));
                hb.setId(cursor.getInt(5));
                array.add(hb);
                flg = cursor.moveToNext();
            }
        }
        else {
            if(isFirst) {
                Toast.makeText(getActivity(), "データはありません", Toast.LENGTH_LONG).show();
            }
        }
        ListView listView = this.view.findViewById(R.id.updateListView);
        this.adapter = new MyAdapter(getActivity());
        this.adapter.setHistoryList(array);
        listView.setAdapter(adapter);
        this.isFirst = true;
    }
    private void searchData() {
        TextView tv_year = this.view.findViewById(R.id.textYear);
        TextView tv_month = this.view.findViewById(R.id.textMonth);
        ArrayList<HistoryBean> CopyArray = new ArrayList<>();
        this.array = CopyArray;
        if (!tv_year.getText().toString().isEmpty() && !tv_month.getText().toString().isEmpty()) {
            MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();
            String[] columns = {"money", "bop_name", "year", "month", "dayOfMonth", "_id"};
            String[] whereArgs = {tv_year.getText().toString(), tv_month.getText().toString()};
            Cursor cursor =
                    db.query("t_income_and_spending", columns, "year = ? and month = ?", whereArgs, null, null, "year asc, month asc, dayOfMonth asc");
            boolean flg = cursor.moveToFirst();
            while (flg) {
                HistoryBean hb = new HistoryBean();
                hb.setMoney(cursor.getInt(0));
                hb.setBop_name(String.valueOf(cursor.getString(1)));
                hb.setYear(cursor.getInt(2));
                hb.setMonth(cursor.getInt(3));
                hb.setDayOfMonth(cursor.getInt(4));
                hb.setId(cursor.getInt(5));
                array.add(hb);
                flg = cursor.moveToNext();
            }
            ListView listView = this.view.findViewById(R.id.updateListView);
            this.adapter = new MyAdapter(getActivity());
            this.adapter.setHistoryList(this.array);
            listView.setAdapter(this.adapter);
        }
        else {
            Toast.makeText(getActivity(), "期間を選択してください", Toast.LENGTH_LONG).show();
        }

    }
}