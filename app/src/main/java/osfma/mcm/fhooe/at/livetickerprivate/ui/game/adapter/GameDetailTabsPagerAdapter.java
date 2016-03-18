package osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail.GameManageFragment;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail.GameWatchFragment;

/**
 * Created by Tob0t on 17.03.2016.
 */
public class GameDetailTabsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public GameDetailTabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                GameWatchFragment tab1 = new GameWatchFragment();
                return tab1;
            case 1:
                GameManageFragment tab2 = new GameManageFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
