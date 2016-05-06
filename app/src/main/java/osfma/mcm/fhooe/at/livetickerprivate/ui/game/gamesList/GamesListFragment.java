package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gamesList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.ui.MainActivity;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter.GameListItemAdapter;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail.GameDetailActivity;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

/**
 * Created by Tob0t on 18.03.2016.
 */
public class GamesListFragment extends Fragment {
    private static final String LOG_TAG = GamesListFragment.class.getSimpleName();
    private ListView mGamesView;
    private GameListItemAdapter mGameListItemAdapter;
    private Constants.GameType mGameType;
    private String mGameState;
    private String mUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_games_list, container, false);
        mGamesView = (ListView) rootView.findViewById(R.id.listView_game_items);

        Bundle bundle = this.getArguments();
        mGameState = bundle.getString(Constants.GAME_STATE);
        mGameType = ((MainActivity) getActivity()).getGameType();
        //mUserId = Helper.getUserId();
        mUserId = ((MainActivity) getActivity()).getUserId();

        Query gamesListRefQuery = createQuery();
        setupGameListItemAdapter(gamesListRefQuery, mUserId);

        mGamesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game selectedGame = mGameListItemAdapter.getItem(position);
                if (selectedGame != null) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), GameDetailActivity.class);
                    /* Get the list ID using the adapter's get ref method to get the Firebase
                     * ref and then grab the key.
                     */
                    String listId = mGameListItemAdapter.getRef(position).getKey();
                    intent.putExtra(Constants.KEY_LIST_ID, listId);
                    intent.putExtra(Constants.KEY_GAME_TYPE, mGameType);
                    /* Starts an activity showing the details for the selected list */
                    startActivity(intent);
                }
            }
        });


        return rootView;
    }

    // query to decide to search for public or private games
    private Query createQuery() {
        String gameTypeUrl ="";
        if (mGameType == Constants.GameType.PUBLIC) {
            gameTypeUrl = Constants.FIREBASE_URL_PUBLIC_GAMES;
        } else if(mGameType == Constants.GameType.PRIVATE) {
            gameTypeUrl = Constants.FIREBASE_URL_PRIVATE_GAMES;
        }
        return new Firebase(gameTypeUrl).orderByChild("dateAndTime");
    }

    // setup adapter and add some filters
    private void setupGameListItemAdapter(Query gamesListRefQuery, String userId) {
        Map<Method, Object> filterMap = new HashMap<Method, Object>();

        switch (mGameState) {
            case Constants.GAMES_RUNNING:
                filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_STARTED, true);
                filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_FINISHED, false);
                break;
            case Constants.GAMES_FUTURE:
                filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_STARTED, false);
                break;
            case Constants.GAMES_FINISHED:
                filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_FINISHED, true);
                break;
        }

        // if the mode is private add a filter to only show private games
        if(mGameType == Constants.GameType.PRIVATE){
            filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_OWNER, userId);
        }

        mGameListItemAdapter = new GameListItemAdapter(getActivity(), Game.class,
                R.layout.single_game_list_item, gamesListRefQuery, filterMap);

        /* Create ActiveListItemAdapter and set to listView */
        mGamesView.setAdapter(mGameListItemAdapter);

    }

    public void updateGameType(Constants.GameType gameType, String userId) {
        mGameType = gameType;
        Query gamesListRefQuery = createQuery();
        setupGameListItemAdapter(gamesListRefQuery, userId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGameListItemAdapter.cleanup();
    }
}
