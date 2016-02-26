package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameManage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameEvent;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameSet;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;

public class GameManageActivity extends AppCompatActivity {
    private static final String LOG_TAG = GameManageActivity.class.getSimpleName();
    private Firebase mActiveGameRef;
    private Firebase mGamesEventsRef;
    private Firebase mActiveGameSetsRef;
    private Firebase mActiveGameActiveSetRef;
    private Firebase mLastChildAdded;
    private ValueEventListener mActiveGameRefListener;
    private ChildEventListener mActiveGameSetsRefListener, mGamesEventsRefListener;
    private String mGameId;
    private TextView mTeam1NameTable, mTeam1Points1Set, mTeam1Points2Set, mTeam1Points3Set;
    private TextView mTeam2NameTable, mTeam2Points1Set, mTeam2Points2Set, mTeam2Points3Set;
    private TextView mTeam1Name, mTeam1Points, mTeam2Name, mTeam2Points;
    private TextView mCustomEvent;
    private Button mNextSet, mPrevSet;
    private TableRow mHeadline,mSet1,mSet2,mSet3;
    private int mTeam1PointsCurrent, mTeam2PointsCurrent;
    private String mActiveGameSet;
    private int mNumberGameSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manage);

        // Temp setting on set one
        mActiveGameSet = Constants.GAMESET_ONE;
        mNumberGameSets = 1;


         /* Get the push ID from the extra passed by ShoppingListFragment */
        Intent intent = this.getIntent();
        mGameId = intent.getStringExtra(Constants.KEY_LIST_ID);
        if (mGameId == null) {
            /* No point in continuing without a valid ID. */
            finish();
            return;
        }

        mActiveGameRef = new Firebase(Constants.FIREBASE_URL_GAMES).child(mGameId);
        mGamesEventsRef = new Firebase(Constants.FIREBASE_URL_GAMES_EVENTS).child(mGameId);
        mActiveGameSetsRef = new Firebase(Constants.FIREBASE_URL_GAMES).child(mGameId).child(Constants.FIREBASE_LOCATION_GAMES_GAMESETS);

        mActiveGameRefListener = mActiveGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                mTeam1Name.setText(game.getTeam1());
                mTeam2Name.setText(game.getTeam2());

                mTeam1NameTable.setText(game.getTeam1());
                mTeam2NameTable.setText(game.getTeam2());

                mNumberGameSets = game.getGameSets().size();

                // Make Map Sorted by Key
                Map<String, GameSet> treeMap = new TreeMap<String, GameSet>(game.getGameSets());
                for (Map.Entry<String, GameSet> entry : treeMap.entrySet()) {
                    if (entry.getKey().equals(Constants.GAMESET_ONE)) {
                        if (entry.getValue().isRunning()) {
                            setRunningParameters(entry);
                            mSet1.setBackgroundColor(Color.YELLOW);
                        } else{
                            mSet1.setBackgroundColor(Color.TRANSPARENT);
                        }
                        mTeam1Points1Set.setText(String.valueOf(entry.getValue().getScoreTeam1()));
                        mTeam2Points1Set.setText(String.valueOf(entry.getValue().getScoreTeam2()));
                    } else if (entry.getKey().equals(Constants.GAMESET_TWO)) {
                        if (entry.getValue().isRunning()) {
                            setRunningParameters(entry);
                            mSet2.setBackgroundColor(Color.YELLOW);
                        } else{
                            mSet2.setBackgroundColor(Color.TRANSPARENT);
                        }
                        mTeam1Points2Set.setText(String.valueOf(entry.getValue().getScoreTeam1()));
                        mTeam2Points2Set.setText(String.valueOf(entry.getValue().getScoreTeam2()));
                    } else if (entry.getKey().equals(Constants.GAMESET_THREE)) {
                        if (entry.getValue().isRunning()) {
                            setRunningParameters(entry);
                            mSet3.setBackgroundColor(Color.YELLOW);
                        } else{
                            mSet3.setBackgroundColor(Color.TRANSPARENT);
                        }
                        mTeam1Points3Set.setText(String.valueOf(entry.getValue().getScoreTeam1()));
                        mTeam2Points3Set.setText(String.valueOf(entry.getValue().getScoreTeam2()));
                    }

                }
                mTeam1PointsCurrent = Integer.valueOf(mTeam1Points.getText().toString());
                mTeam2PointsCurrent = Integer.valueOf(mTeam2Points.getText().toString());
                updateButtonVisibility();
            }

            private void setRunningParameters(Map.Entry<String, GameSet> entry) {
                mActiveGameSet = entry.getKey();
                // write the score in the middle of the active gameSet
                mTeam1Points.setText(String.valueOf(entry.getValue().getScoreTeam1()));
                mTeam2Points.setText(String.valueOf(entry.getValue().getScoreTeam2()));


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mActiveGameSetsRefListener = mActiveGameSetsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                GameSet gameSet = dataSnapshot.getValue(GameSet.class);
                /*if (dataSnapshot.getKey().equals(Constants.GAMESET_ONE)) {
                    mTeam1Points1Set.setText(String.valueOf(gameSet.getScoreTeam1()));
                    mTeam2Points1Set.setText(String.valueOf(gameSet.getScoreTeam2()));
                } else if (dataSnapshot.getKey().equals(Constants.GAMESET_TWO)) {
                    mTeam1Points2Set.setText(String.valueOf(gameSet.getScoreTeam1()));
                    mTeam2Points2Set.setText(String.valueOf(gameSet.getScoreTeam2()));
                } else if (dataSnapshot.getKey().equals(Constants.GAMESET_THREE)) {
                    mTeam1Points3Set.setText(String.valueOf(gameSet.getScoreTeam1()));
                    mTeam2Points3Set.setText(String.valueOf(gameSet.getScoreTeam2()));
                }
                if (gameSet.isRunning()) {
                    mTeam1Points.setText(String.valueOf(gameSet.getScoreTeam1()));
                    mTeam2Points.setText(String.valueOf(gameSet.getScoreTeam2()));
                }*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mGamesEventsRefListener = mGamesEventsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mLastChildAdded = dataSnapshot.getRef();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        initializeScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActiveGameRef.removeEventListener(mActiveGameRefListener);
        mActiveGameSetsRef.removeEventListener(mActiveGameSetsRefListener);
        mGamesEventsRef.removeEventListener(mGamesEventsRefListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.button_increment_team1: {
                    mTeam1PointsCurrent++;
                    updateScoresTeam1();
                    break;
                }
                case R.id.button_increment_team2: {
                    mTeam2PointsCurrent++;
                    updateScoresTeam2();
                    break;
                }
                case R.id.button_decrement_team1: {
                    mTeam1PointsCurrent--;
                    updateScoresTeam1();
                    break;
                }
                case R.id.button_decrement_team2: {
                    mTeam2PointsCurrent--;
                    updateScoresTeam2();
                    break;
                }
                case R.id.button_nextSet: {
                    changeGameSet(Constants.Navigate.NEXT);
                    updateButtonVisibility();
                    break;
                }
                case R.id.button_prevSet: {
                    changeGameSet(Constants.Navigate.PREVIOUS);
                    updateButtonVisibility();
                    break;
                }
                case R.id.button_event_ace: {
                    mCustomEvent.setText(getResources().getString(R.string.event_ace));
                    break;
                }
                case R.id.button_event_block: {
                    mCustomEvent.setText(getResources().getString(R.string.event_block));
                    break;
                }
                case R.id.button_event_lineshot: {
                    mCustomEvent.setText(getResources().getString(R.string.event_lineshot));
                    break;
                }
                case R.id.button_event_cut: {
                    mCustomEvent.setText(getResources().getString(R.string.event_cut));
                    break;
                }
                case R.id.button_event_rainbow: {
                    mCustomEvent.setText(getResources().getString(R.string.event_rainbow));
                    break;
                }
                case R.id.button_send_event: {
                    String message = mCustomEvent.getText().toString();
                    if (mLastChildAdded != null) {
                        mLastChildAdded.child("info").setValue(message);
                        // Hide keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mCustomEvent.getWindowToken(), 0);
                        mCustomEvent.setText("");
                    } else {
                        Snackbar.make(v, "Game not started yet!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    break;
                }
            }
        }

        private void changeGameSet(Constants.Navigate direction) {
            HashMap<String, Object> updatedGameSets = new HashMap<String, Object>();
            int activeGameSetInt = Constants.GAMESETS_LIST.indexOf(mActiveGameSet);

            updatedGameSets.put(mActiveGameSet + "/" + Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_RUNNING, false);

            if(direction == Constants.Navigate.NEXT) {
                if(activeGameSetInt+1 < mNumberGameSets){
                    mActiveGameSet = Constants.GAMESETS_LIST.get(activeGameSetInt+1);
                }
            } else if(direction == Constants.Navigate.PREVIOUS){
                if(activeGameSetInt > 0){
                    mActiveGameSet = Constants.GAMESETS_LIST.get(activeGameSetInt-1);
                }
            }
            updatedGameSets.put(mActiveGameSet + "/" + Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_RUNNING, true);
            mActiveGameSetsRef.updateChildren(updatedGameSets);
       }
    };

    private void updateButtonVisibility() {
        // Visibility of Buttons
        int visibilityNext = View.INVISIBLE;
        int visibilityPrev = View.INVISIBLE;
        if(Constants.GAMESETS_LIST.indexOf(mActiveGameSet) > 0){
            visibilityPrev = View.VISIBLE;
        }
        if (Constants.GAMESETS_LIST.indexOf(mActiveGameSet)+1 < mNumberGameSets){
            visibilityNext = View.VISIBLE;
        }
        mPrevSet.setVisibility(visibilityPrev);
        mNextSet.setVisibility(visibilityNext);
    }

    private void updateScoresTeam2() {
        mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM2).setValue(mTeam2PointsCurrent);
        createGameEvent();
    }

    private void updateScoresTeam1() {
        mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM1).setValue(mTeam1PointsCurrent);
        createGameEvent();
    }

    private void createGameEvent() {
        StringBuffer score = new StringBuffer();
        score.append(mTeam1PointsCurrent);
        score.append(":");
        score.append(mTeam2PointsCurrent);
        mGamesEventsRef.push().setValue(new GameEvent(score.toString(),mCustomEvent.getText().toString()));
        mCustomEvent.setText("");
    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTeam1PointsCurrent = mTeam2PointsCurrent = 0;

        mTeam1Name = (TextView) findViewById(R.id.textView_game_manage_team1);
        mTeam1Points = (TextView) findViewById(R.id.textView_game_manage_team1_points);
        mTeam2Name = (TextView) findViewById(R.id.textView_game_manage_team2);
        mTeam2Points = (TextView) findViewById(R.id.textView_game_manage_team2_points);

        mTeam1NameTable = (TextView) findViewById(R.id.textView_game_manage_table_team1);
        mTeam1Points1Set = (TextView) findViewById(R.id.textView_game_manage_table_team1_set1);
        mTeam1Points2Set = (TextView) findViewById(R.id.textView_game_manage_table_team1_set2);
        mTeam1Points3Set = (TextView) findViewById(R.id.textView_game_manage_table_team1_set3);

        mTeam2NameTable = (TextView) findViewById(R.id.textView_game_manage_table_team2);
        mTeam2Points1Set = (TextView) findViewById(R.id.textView_game_manage_table_team2_set1);
        mTeam2Points2Set = (TextView) findViewById(R.id.textView_game_manage_table_team2_set2);
        mTeam2Points3Set = (TextView) findViewById(R.id.textView_game_manage_table_team2_set3);

        Button incrementTeam1 = (Button) findViewById(R.id.button_increment_team1);
        Button incrementTeam2 = (Button) findViewById(R.id.button_increment_team2);
        Button decrementTeam1 = (Button) findViewById(R.id.button_decrement_team1);
        Button decrementTeam2 = (Button) findViewById(R.id.button_decrement_team2);

        Button eventAce = (Button) findViewById(R.id.button_event_ace);
        Button eventBlock = (Button) findViewById(R.id.button_event_block);
        Button eventLine = (Button) findViewById(R.id.button_event_lineshot);
        Button eventCut = (Button) findViewById(R.id.button_event_cut);
        Button eventRainbow = (Button) findViewById(R.id.button_event_rainbow);

        ImageButton eventSend = (ImageButton) findViewById(R.id.button_send_event);
        mCustomEvent = (TextView) findViewById(R.id.editText_manage_game_event);

        mNextSet = (Button) findViewById(R.id.button_nextSet);
        mPrevSet = (Button) findViewById(R.id.button_prevSet);

        mSet1 = (TableRow) findViewById(R.id.tableRow_manage_game_set1);
        mSet2 = (TableRow) findViewById(R.id.tableRow_manage_game_set2);
        mSet3 = (TableRow) findViewById(R.id.tableRow_manage_game_set3);

        incrementTeam1.setOnClickListener(onClickListener);
        incrementTeam2.setOnClickListener(onClickListener);
        decrementTeam1.setOnClickListener(onClickListener);
        decrementTeam2.setOnClickListener(onClickListener);
        eventAce.setOnClickListener(onClickListener);
        eventBlock.setOnClickListener(onClickListener);
        eventLine.setOnClickListener(onClickListener);
        eventCut.setOnClickListener(onClickListener);
        eventRainbow.setOnClickListener(onClickListener);
        eventSend.setOnClickListener(onClickListener);

        mNextSet.setOnClickListener(onClickListener);
        mPrevSet.setOnClickListener(onClickListener);


        if(mActiveGameSet.equals(Constants.GAMESET_ONE)){
            mNextSet.setVisibility(View.VISIBLE);
            mPrevSet.setVisibility(View.INVISIBLE);
        } else if(mActiveGameSet.equals(Constants.GAMESET_TWO)){
            mPrevSet.setVisibility(View.VISIBLE);
            mNextSet.setVisibility(View.INVISIBLE);
        }

    }


}
