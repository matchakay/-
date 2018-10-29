package matcha.kay.jp.simplehouseholdaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewFragment extends Fragment {

    private View view = null;
    private boolean isFirst = false;

    public ViewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_fragment, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        this.viewData();

        ImageButton calendar_btn = view.findViewById(R.id.calendar_btn);
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OriginalDialogFragment originalDialogFragment = new OriginalDialogFragment();
                originalDialogFragment.show(getFragmentManager(), "ok");
            }
        });

        Button agg_btn = view.findViewById(R.id.aggregate_btn);
        agg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggregateData();
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // 表示時にさせたい処理を書く
            TextView tv_year = this.view.findViewById(R.id.viewYear);
            TextView tv_month = this.view.findViewById(R.id.viewMonth);
            TextView tv_all = this.view.findViewById(R.id.allResult);
            tv_year.setText("");
            tv_month.setText("");
            tv_all.setText("");
            this.viewData();
        }
    }


    private void viewData() {
        ArrayList<HistoryBean> array = new ArrayList<>();
        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {"money", "bop_name", "year", "month", "dayOfMonth"};
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
                array.add(hb);
                flg = cursor.moveToNext();
            }
            ListView listView = this.view.findViewById(R.id.listView);
            MyAdapter adapter = new MyAdapter(getActivity());
            adapter.setHistoryList(array);
            listView.setAdapter(adapter);
            String[] columns2 = {"sum(money)"};
            Cursor cursor2 =
                    db.query("t_income_and_spending", columns2, null, null, "year", null, null);
            if (cursor2.moveToFirst()) {
                TextView tv_all = this.view.findViewById(R.id.allResult);
                int allMoney = cursor2.getInt(0);
                if (allMoney < 0) {
                    tv_all.setTextColor(Color.parseColor("#EE0000"));
                } else {
                    tv_all.setTextColor(Color.parseColor("#17C618"));
                }
                tv_all.setText(String.valueOf(String.format("%,d", cursor2.getInt(0))));
            }
        }
        else {
            if(isFirst) {
                Toast.makeText(getActivity(), "データはありません", Toast.LENGTH_LONG).show();
            }
        }
        this.isFirst = true;
    }

    private void aggregateData() {
        ArrayList<HistoryBean> array = new ArrayList<>();
        TextView tv_year = this.view.findViewById(R.id.viewYear);
        TextView tv_month = this.view.findViewById(R.id.viewMonth);
        if(!tv_year.getText().toString().isEmpty() && !tv_month.getText().toString().isEmpty()) {
            MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();
            String[] columns = {"money", "bop_name", "year", "month", "dayOfMonth"};
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
                array.add(hb);
                flg = cursor.moveToNext();
            }
            TextView tv_all = this.view.findViewById(R.id.allResult);
            String[] columns2 = {"sum(money)"};
            String[] whereArgs2 = {tv_year.getText().toString(), tv_month.getText().toString()};
            Cursor cursor2 = db.query("t_income_and_spending", columns2, "year = ? and month = ?", whereArgs2, "year, month", null, null);
            if(cursor2.moveToFirst()) {
                int allMoney = cursor2.getInt(0);
                if (allMoney < 0) {
                    tv_all.setTextColor(Color.parseColor("#EE0000"));
                }
                else {
                    tv_all.setTextColor(Color.parseColor("#17C618"));
                }
                tv_all.setText(String.valueOf(String.format("%,d", cursor2.getInt(0))));
            }
            else {
                tv_all.setText("");
            }
            ListView listView = this.view.findViewById(R.id.listView);
            MyAdapter adapter = new MyAdapter(getActivity());
            adapter.setHistoryList(array);
            listView.setAdapter(adapter);
        }
        else {
            Toast.makeText(getActivity(), "期間を選択してください", Toast.LENGTH_LONG).show();
        }
    }
}
