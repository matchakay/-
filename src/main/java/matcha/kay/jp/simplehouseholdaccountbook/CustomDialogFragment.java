package matcha.kay.jp.simplehouseholdaccountbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class CustomDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // MainActivityのインスタンスを取得
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.searchDate(year, (month + 1));
            }
        },
        calendar.get(Calendar.YEAR), // 初期選択年
        calendar.get(Calendar.MONTH), // 初期選択月
        calendar.get(Calendar.DAY_OF_MONTH) // 初期選択日
        );
        return datePickerDialog;
    }
}
