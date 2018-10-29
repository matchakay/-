package matcha.kay.jp.simplehouseholdaccountbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OriginalFragmentPagerAdapter extends FragmentPagerAdapter {


    public OriginalFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WriteFragment();
            case 1:
                return new ListFragment();
            case 2:
                return new UpdateFragment();
            case 3:
                return new ViewFragment();
            case 4:
                return new ChartFragment();
             default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
