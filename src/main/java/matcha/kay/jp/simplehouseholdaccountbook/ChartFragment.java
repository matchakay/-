package matcha.kay.jp.simplehouseholdaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChartFragment extends Fragment {

    private View view = null;
    private BarChart barChart = null;
    private ArrayAdapter<String> adapter = null;

    public ChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        this.adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = view.findViewById(R.id.spinnerYear);
        spinner.setAdapter(adapter);
        this.adapter.add("登録年一覧");

        getYear();

        setBarChart();

        Button btn = view.findViewById(R.id.chart_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBarChart();
            }
        });

    }

    private void setBarChart() {

        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        int[] monthList = new int[12];
        int[] totalMoney = new int[12];
        String[] columns = {"month","total(money)"};
        Spinner spinner = view.findViewById(R.id.spinnerYear);
        // 選択されているアイテムを取得
        String[] whereArgs = {String.valueOf(spinner.getSelectedItem())};
        if(whereArgs[0].equals("登録年一覧")) {
            Calendar calendar = Calendar.getInstance();
            whereArgs[0] = String.valueOf(calendar.get(Calendar.YEAR));
        }
        Cursor cursor = db.query("t_income_and_spending", columns, "year = ?", whereArgs, "month", null, "month asc", null);
        boolean flg = cursor.moveToFirst();
        if(flg) {
            int i = 0;
            while (flg) {
                monthList[i] = cursor.getInt(0);
                totalMoney[i] = cursor.getInt(1);
                flg = cursor.moveToNext();
                i++;
            }
        }

        barChart = view.findViewById(R.id.bar_chart);

        //表示データ取得
        BarData data = new BarData(getBarData(monthList, totalMoney));
        barChart.setData(data);

        //Y軸(左)
        YAxis left = barChart.getAxisLeft();
        left.setAxisMinimum(-300000);
        left.setAxisMaximum(300000);
        left.setLabelCount(30);
        left.setDrawTopYLabelEntry(true);
        left.setDrawGridLines(true);
        left.setDrawZeroLine(true);

        //Y軸(右)
        YAxis right = barChart.getAxisRight();
        right.setDrawLabels(false);
        right.setDrawZeroLine(false);
        right.setDrawGridLines(false);
        right.setDrawTopYLabelEntry(false);

        //X軸
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(12);
        //X軸に表示するLabelのリスト(最初の""は原点の位置)
        final String[] labels =
                {"", "1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        XAxis bottomAxis = barChart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(true);
        bottomAxis.setDrawAxisLine(false);

        //グラフ上の表示
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setClickable(false);

        //凡例
        barChart.getLegend().setEnabled(false);

        barChart.setScaleEnabled(false);
        //アニメーション
        barChart.animateY(2000, Easing.EasingOption.EaseInBack);
    }

    //棒グラフのデータを取得
    private ArrayList<IBarDataSet> getBarData(int[] monthList, int[] totalMoney){

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 1;i <= totalMoney.length;i++) {
            map.put(i, 0);
        }

        for (int i = 0;i < totalMoney.length;i++) {
            map.put(monthList[i], totalMoney[i]);
        }

        //表示させるデータ
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, map.get(1)));
        entries.add(new BarEntry(2, map.get(2)));
        entries.add(new BarEntry(3, map.get(3)));
        entries.add(new BarEntry(4, map.get(4)));
        entries.add(new BarEntry(5, map.get(5)));
        entries.add(new BarEntry(6, map.get(6)));
        entries.add(new BarEntry(7, map.get(7)));
        entries.add(new BarEntry(8, map.get(8)));
        entries.add(new BarEntry(9, map.get(9)));
        entries.add(new BarEntry(10, map.get(10)));
        entries.add(new BarEntry(11, map.get(11)));
        entries.add(new BarEntry(12, map.get(12)));

        ArrayList<IBarDataSet> bars = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "bar");
        //ハイライトさせない
        dataSet.setHighlightEnabled(false);

        //Barの色をセット
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorButton, R.color.colorAccent}, getActivity());
        bars.add(dataSet);

        return bars;
    }

    private void getYear() {
        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {"year"};
        Cursor cursor = db.query(true,"t_income_and_spending", columns, null, null, "year", null, "year asc",null);
        boolean flg =  cursor.moveToFirst();
        if(flg) {
            while(flg) {
                adapter.add(cursor.getString(0));
                flg = cursor.moveToNext();
            }
        }
        Spinner spinner = view.findViewById(R.id.spinnerYear);
        spinner.setAdapter(adapter);
    }
}
