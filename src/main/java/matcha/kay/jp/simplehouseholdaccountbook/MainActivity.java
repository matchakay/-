package matcha.kay.jp.simplehouseholdaccountbook;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OriginalFragmentPagerAdapter adapter = new OriginalFragmentPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        View tab1View = inflater.inflate(R.layout.tab1_layout, null);
        tab1.setCustomView(tab1View);

        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        View tab2View = inflater.inflate(R.layout.tab2_layout, null);
        tab2.setCustomView(tab2View);

        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        View tab3View = inflater.inflate(R.layout.tab3_layout, null);
        tab3.setCustomView(tab3View);

        TabLayout.Tab tab4 = tabLayout.getTabAt(3);
        View tab4View = inflater.inflate(R.layout.tab4_layout, null);
        tab4.setCustomView(tab4View);

        TabLayout.Tab tab5 = tabLayout.getTabAt(4);
        View tab5View = inflater.inflate(R.layout.tab5_layout, null);
        tab5.setCustomView(tab5View);

    }
    // ダイアログで入力した値をtextViewに入れる - ダイアログから呼び出される
    void setDate(String date){
        TextView tv_date = findViewById(R.id.dateText);
        tv_date.setText(date);
    }

    void searchDate(int year, int month) {
        TextView tv_year = findViewById(R.id.textYear);
        TextView tv_month = findViewById(R.id.textMonth);
        tv_year.setText(String.valueOf(year));
        tv_month.setText(String.valueOf(month));
    }

    void addDate(int year, int month) {
        TextView tv_year = findViewById(R.id.viewYear);
        TextView tv_month = findViewById(R.id.viewMonth);
        tv_year.setText(String.valueOf(year));
        tv_month.setText(String.valueOf(month));
    }
}
