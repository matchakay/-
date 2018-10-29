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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class ListFragment extends Fragment {

    private View view = null;
    private ArrayAdapter<String> adapter;

    public ListFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        this.adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        this.adapter.add("項目一覧");

        getList();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            EditText et_new_list = view.findViewById(R.id.newListName);
            String selectItem = "";
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectItem = String.valueOf(parent.getSelectedItem());
                    et_new_list.setText(selectItem);
                    spinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //
        RadioButton rb_delete = view.findViewById(R.id.radioDelete);
        rb_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
            }
        });

        RadioButton rb_income = view.findViewById(R.id.radioIncome);
        rb_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.INVISIBLE);
            }
        });

        RadioButton rb_spending = view.findViewById(R.id.radioSpending);
        rb_spending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.INVISIBLE);
            }
        });

        //実行
        Button do_btn = view.findViewById(R.id.compAddListBtn);
        do_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addList();
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            this.getList();
        }
    }
    private void addList() {
        // ラジオグループのオブジェクトを取得
        RadioGroup rg = view.findViewById(R.id.listRadioGroup);
        // チェックされているラジオボタンの ID を取得
        int id = rg.getCheckedRadioButtonId();
        int i_or_s;
        if(id == R.id.radioDelete) {
            this.deleteList();
        }
        else if(id  == R.id.radioIncome) {
            i_or_s = 1;
            this.insertList(i_or_s);
        }
        else {
            i_or_s = 0;
            this.insertList(i_or_s);
        }
    }
    //削除
    private void deleteList() {
        EditText et_delete_name = view.findViewById(R.id.newListName);
        String whereName = et_delete_name.getText().toString();
        if(!whereName.isEmpty()) {
            MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("delete from t_bop where bop_name ='" + whereName + "'");
            db.close();
            et_delete_name.setText("");
            getList();
            Toast.makeText(getActivity(),"削除完了",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(), "削除項目を選択してください", Toast.LENGTH_LONG).show();
        }
    }
    private void getList() {

        MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {"bop_name"};
        Cursor cursor =
                db.query("t_bop", columns, null, null, null, null, null);
        //Adapter
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner
        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        adapter.add("削除項目一覧");
        boolean flg = cursor.moveToFirst();
        while(flg) {
            adapter.add(cursor.getString(0));
            flg = cursor.moveToNext();
        }
        db.close();
    }
    //追加
    private void insertList(int i_or_s) {
        EditText et_list_name = view.findViewById(R.id.newListName);
        String list_name = et_list_name.getText().toString();
        if(!list_name.isEmpty()) {
            if(adapter.getPosition(list_name) == -1) {
                ContentValues cv = new ContentValues();
                cv.put("bop_name", list_name);
                cv.put("i_or_s", i_or_s);
                MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getActivity());
                SQLiteDatabase db = helper.getWritableDatabase();
                db.insert("t_bop", null, cv);
                cv.clear();
                et_list_name.setText("");
                getList();
                Toast.makeText(getActivity(), "追加完了", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(),"この項目名は追加済みです",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getActivity(),"追加項目名を入力してください",Toast.LENGTH_LONG).show();
        }
    }
}
