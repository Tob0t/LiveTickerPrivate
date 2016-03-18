package osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import osfma.mcm.fhooe.at.livetickerprivate.ui.game.gamesList.GamesFragment;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;

/**
 * Created by Tob0t on 17.03.2016.
 */
public class GameTabsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public GameTabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        switch (position) {
            case 0:
                b.putString(Constants.GAME_STATE, Constants.GAMES_RUNNING);
                GamesFragment tab1 = new GamesFragment();
                tab1.setArguments(b);
                return tab1;
            case 1:
                b.putString(Constants.GAME_STATE, Constants.GAMES_FUTURE);
                GamesFragment tab2 = new GamesFragment();
                tab2.setArguments(b);
                return tab2;
            case 2:
                b.putString(Constants.GAME_STATE, Constants.GAMES_PAST);
                GamesFragment tab3 = new GamesFragment();
                tab3.setArguments(b);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
