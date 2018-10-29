package matcha.kay.jp.simplehouseholdaccountbook;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WriteFragment extends Fragment {

    private View view = null;
    private boolean first = false;

    public WriteFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.write_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        this.first = true;

        //Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinner
        final Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        adapter.add("項目一覧");

        //カレンダー遷移
        final ImageButton calendarImgView = view.findViewById(R.id.calendarImageView);
        calendarImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
                calendarDialogFragment.show(getFragmentManager(), "calendar");
            }
        });

        //ラジオボタン
        RadioButton radioInButton = view.findViewById(R.id.radioIncome);
        radioInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeList(v);
            }
        });

        //
        RadioButton radioSpButton = view.findViewById(R.id.radioSpending);
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
                    TextView tv_select_spent = view.findViewById(R.id.selectSpent);
                    tv_select_spent.setText(String.valueOf(parent.getSelectedItem()));
                    spinner.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //追加
        Button add_data_btn = view.findViewById(R.id.addData);
        add_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

    }
    //再訪問時
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && first) {
            // 表示時にさせたい処理を書く
            RadioGroup rg = view.findViewById(R.id.radioGroup);
            rg.clearCheck();
        }
    }

    private void changeList(View v){
        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("項目一覧");
        //spinner
        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        boolean flg = cursor.moveToFirst();
        while (flg) {
            adapter.add(cursor.getString(0));
            flg = cursor.moveToNext();
        }
        db.close();
    }

    private void addData() {
        // ラジオグループのオブジェクトを取得
        RadioGroup rg = view.findViewById(R.id.radioGroup);
        // チェックされているラジオボタンの ID を取得
        int id = rg.getCheckedRadioButtonId();
        TextView tv_date = view.findViewById(R.id.dateText);
        String date = tv_date.getText().toString();
        EditText et_money = view.findViewById(R.id.input_money);
        TextView tv_select_spent = view.findViewById(R.id.selectSpent);
        if(!et_money.getText().toString().isEmpty() && id != 0 && !date.isEmpty()&&
                !tv_select_spent.getText().toString().isEmpty()) {
            String[] dateList = date.split("[一-龠]");
            int[] list = new int[3];
            for (int i = 0; i < 3; i++) {
                list[i] = Integer.parseInt(dateList[i]);
            }
            int money = Integer.parseInt(et_money.getText().toString());
            if(id == R.id.radioIncome) {
                MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("money", money);
                cv.put("bop_name", tv_select_spent.getText().toString());
                cv.put("year", list[0]);
                cv.put("month", list[1]);
                cv.put("dayOfMonth", list[2]);
                db.insert("t_income_and_spending", null, cv);

                cv.clear();
                db.close();
                tv_date.setText("");
                tv_select_spent.setText(" ");
                et_money.setText("");
                Toast.makeText(getActivity(), "追加完了",Toast.LENGTH_LONG).show();
            }
            else {
                MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                money = -money;
                cv.put("money", money);
                cv.put("bop_name", tv_select_spent.getText().toString());
                cv.put("year", list[0]);
                cv.put("month", list[1]);
                cv.put("dayOfMonth", list[2]);
                db.insert("t_income_and_spending", null, cv);

                cv.clear();
                db.close();
                tv_date.setText("");
                tv_select_spent.setText(" ");
                et_money.setText("");
                Toast.makeText(getActivity(), "追加完了",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getActivity(), "データを入力してください",Toast.LENGTH_LONG).show();
        }
    }
}
