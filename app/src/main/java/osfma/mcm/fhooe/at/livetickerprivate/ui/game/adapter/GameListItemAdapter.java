package osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameSet;
import osfma.mcm.fhooe.at.livetickerprivate.model.User;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class GameListItemAdapter extends FirebaseListAdapter<Game> {
    private static final String LOG_TAG = GameListItemAdapter.class.getSimpleName();
    private Map<Method, Object> mFilter;
    public GameListItemAdapter(Activity activity, Class<Game> modelClass, int modelLayout, Query ref, Map<Method, Object> filter) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mLayout = modelLayout;
        this.mFilter = filter;
    }

    @Override
    protected void populateView(View view, Game game, int i) {
        ImageView bgImage = (ImageView) view.findViewById(R.id.bgImage);
        TextView textViewGame = (TextView) view.findViewById(R.id.textView_game);
        TextView textViewStateConcrete = (TextView) view.findViewById(R.id.textView_state_concrete);
        TextView textViewDate = (TextView) view.findViewById(R.id.textView_date);
        TextView textViewTime = (TextView) view.findViewById(R.id.textView_time);
        TextView textViewSets = (TextView) view.findViewById(R.id.textView_sets);
        TextView textViewSportType = (TextView) view.findViewById(R.id.textView_sportType);
        TextView textViewOwner = (TextView) view.findViewById(R.id.textView_owner);

        textViewDate.setText(Helper.DATE_FORMATTER.format(game.getDateAndTime()));
        textViewTime.setText(Helper.TIME_FORMATTER.format(game.getDateAndTime()));
        setOwnerName(textViewOwner, game.getUserId());
        textViewSportType.setText(game.getSportType());

        // Set Background image depending on sportType
        switch (game.getSportType()){
            case "Beachvolleyball":
                bgImage.setImageResource(R.drawable.beachvolleyball);
                break;
            case "Volleyball":
                bgImage.setImageResource(R.drawable.volleyball);
                break;
            case "Badminton":
                bgImage.setImageResource(R.drawable.badminton);
                break;
            default:
                bgImage.setImageResource(R.drawable.default_sportype);
        }

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

    // Set displayName
    private void setOwnerName(final TextView textViewOwner, String userId) {
        Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    textViewOwner.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, mActivity.getString(R.string.log_error_the_read_failed)
                        + firebaseError.getMessage());
            }
        });
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Game model = getItem(position);
        boolean allConditionsTrue = Helper.checkAllCondtionsTrue(model, mFilter);
        if(allConditionsTrue) {
            view = mActivity.getLayoutInflater().inflate(mLayout, viewGroup, false);
            populateView(view, model, position);
        } else{
            // Hack to hide view if its filtered blank item
            view = new View(mActivity);
        }

        return view;
    }
}
