package osfma.mcm.fhooe.at.livetickerprivate.ui.game;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameSet;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class GameListItemAdapter extends FirebaseListAdapter<Game> {
    public GameListItemAdapter(Activity activity, Class<Game> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    @Override
    protected void populateView(View view, Game game, int i) {
        TextView textViewGame = (TextView) view.findViewById(R.id.text_view_game);
        TextView textViewStateConcrete = (TextView) view.findViewById(R.id.text_view_state_concrete);
        TextView textViewDate = (TextView) view.findViewById(R.id.text_view_date);
        TextView textViewTime = (TextView) view.findViewById(R.id.text_view_time);
        TextView textViewSets = (TextView) view.findViewById(R.id.text_view_sets);

        textViewDate.setText(Helper.DATE_FORMATTER.format(game.getDateAndTime()));
        textViewTime.setText(Helper.TIME_FORMATTER.format(game.getDateAndTime()));

        // Format SetView
        StringBuffer s = new StringBuffer();
        if (game.getGameSets() != null) {
            Map<String, GameSet> treeMap = new TreeMap<String, GameSet>(game.getGameSets());
            for (Map.Entry<String, GameSet> entry : treeMap.entrySet()) {
                s.append(entry.getValue().getScoreTeam1());
                s.append(":");
                s.append(entry.getValue().getScoreTeam2());
                s.append(" | ");
            }
            if (s != null) {
                // remove the last |
                textViewSets.setText(s.substring(0, s.length() - 3));
            }
        }

        // Format GameState
        String[] gameStates = view.getResources().getStringArray(R.array.game_states);
        String concreteState = gameStates[0];
        if (game.isFinished()) {
            concreteState = gameStates[2];
        } else if (game.isStarted()) {
            concreteState = gameStates[1];
        }
        textViewStateConcrete.setText(concreteState);

        textViewGame.setText(game.getTeam1() + " vs. " + game.getTeam2());
    }
}
