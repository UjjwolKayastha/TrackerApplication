package kayastha.ujjwol.atrackerapp.utilities;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kayastha.ujjwol.atrackerapp.friends.Friends_tab;
import kayastha.ujjwol.atrackerapp.friends.Request_tab;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                Friends_tab friends_tab = new Friends_tab();
                return friends_tab;
            case 1:
                Request_tab request_tab = new Request_tab();
                return request_tab;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return  "FRIENDS";
            case 1:
                return  "REQUESTS";
            default:
                return null;
        }
    }
}
