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
public class GamesFragment extends Fragment {
    private static final String LOG_TAG = GamesFragment.class.getSimpleName();
    private ListView mGamesView;
    private GameListItemAdapter mGameListItemAdapter;
    private Firebase mGamesListRef;
    private Constants.GameType mGameType;
    private String mGameState;
    private String mEncodedEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGamesView = (ListView) rootView.findViewById(R.id.listView_game_items);

        Bundle bundle = this.getArguments();
        mGameState = bundle.getString(Constants.GAME_STATE);
        mGameType = ((MainActivity) getActivity()).getGameType();
        mEncodedEmail = ((MainActivity)getActivity()).getmEncodedEmail();

        String gameTypeUrl ="";
        Query gamesListRefQuery = null;

        if (mGameType == Constants.GameType.PUBLIC) {
            gameTypeUrl = Constants.FIREBASE_URL_PUBLIC_GAMES;
            gamesListRefQuery = new Firebase(gameTypeUrl).orderByChild("dateAndTime");
        } else if(mGameType == Constants.GameType.PRIVATE) {
            gameTypeUrl = Constants.FIREBASE_URL_PRIVATE_GAMES;
            gamesListRefQuery = new Firebase(gameTypeUrl).orderByChild("owner").equalTo(mEncodedEmail);
        }

        setupGameListItemAdapter(gamesListRefQuery);

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
                    // TODO GameType
                    intent.putExtra(Constants.KEY_GAME_TYPE, mGameType);
                    /* Starts an activity showing the details for the selected list */
                    startActivity(intent);
                }
            }
        });


        return rootView;
    }

    private void setupGameListItemAdapter(Query gamesListRefQuery) {
        Map<Method, Boolean> filterMap = new HashMap<Method, Boolean>();

        if(mGameState.equals(Constants.GAMES_RUNNING)){
            filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_STARTED, true);
            filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_FINISHED, false);
        } else if(mGameState.equals(Constants.GAMES_FUTURE)){
            filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_STARTED, false);
        } else if(mGameState.equals(Constants.GAMES_PAST)){
            filterMap = Helper.addFilter(filterMap, Game.class, Constants.METHOD_GAME_FINISHED, true);
        }

        mGameListItemAdapter = new GameListItemAdapter(getActivity(), Game.class,
                R.layout.single_game_list_item, gamesListRefQuery, filterMap);

        /* Create ActiveListItemAdapter and set to listView */
        mGamesView.setAdapter(mGameListItemAdapter);

    }

    public void updateGameType(Constants.GameType gameType, String encodedEmail) {
        String gameTypeUrl ="";
        mGameType = gameType;
        Query gamesListRefQuery = null;
        if (mGameType == Constants.GameType.PUBLIC) {
            gameTypeUrl = Constants.FIREBASE_URL_PUBLIC_GAMES;
            gamesListRefQuery = new Firebase(gameTypeUrl).orderByChild("dateAndTime");
        } else if(mGameType == Constants.GameType.PRIVATE) {
            gameTypeUrl = Constants.FIREBASE_URL_PRIVATE_GAMES;
            gamesListRefQuery = new Firebase(gameTypeUrl).orderByChild("owner").equalTo(encodedEmail);
        }
        //Query gamesListRefQuery = new Firebase(gameTypeUrl).orderByChild("dateAndTime");

        mGameListItemAdapter.cleanup();
        setupGameListItemAdapter(gamesListRefQuery);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGameListItemAdapter.cleanup();
    }
}
