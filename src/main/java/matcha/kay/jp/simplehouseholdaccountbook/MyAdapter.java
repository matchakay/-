package matcha.kay.jp.simplehouseholdaccountbook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<HistoryBean> historyList;

    public MyAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setHistoryList(ArrayList<HistoryBean> historyList) {
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.my_list_view, parent, false);


        ((TextView)convertView.findViewById(R.id.date)).setText(String.valueOf(historyList.get(position).getYear()+"/"+historyList.get(position).getMonth()+"/"+historyList.get(position).getDayOfMonth()));
        ((TextView) convertView.findViewById(R.id.use)).setText(String.valueOf(historyList.get(position).getBop_name()));

        if (historyList.get(position).getMoney() < 0) {
            ((TextView)convertView.findViewById(R.id.money)).setTextColor(Color.parseColor("#EE0000"));
        }
        else {
            ((TextView)convertView.findViewById(R.id.money)).setTextColor(Color.parseColor("#17C618"));
        }
        ((TextView)convertView.findViewById(R.id.money)).setText(String.valueOf(String.format("%,d",historyList.get(position).getMoney())));
        return convertView;
    }
}
