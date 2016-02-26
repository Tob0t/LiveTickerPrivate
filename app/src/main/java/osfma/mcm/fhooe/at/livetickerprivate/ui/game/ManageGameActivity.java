package osfma.mcm.fhooe.at.livetickerprivate.ui.game;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Chat;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameEvent;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameSet;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;

public class ManageGameActivity extends AppCompatActivity {
    private static final String LOG_TAG = ManageGameActivity.class.getSimpleName();
    private Firebase mActiveGameRef;
    private Firebase mGamesEventsRef;
    private Firebase mActiveGameSetsRef;
    private Firebase mActiveGameActiveSetRef;
    private String mGameId;
    private TextView mTeam1NameTable, mTeam1Points1Set, mTeam1Points2Set, mTeam1Points3Set;
    private TextView mTeam2NameTable, mTeam2Points1Set, mTeam2Points2Set, mTeam2Points3Set;
    private TextView mTeam1Name, mTeam1Points, mTeam2Name, mTeam2Points;
    private TextView mCustomEvent;
    private Button mNextSet, mPrevSet;
    private TableRow mHeadline,mSet1,mSet2,mSet3;
    private int mTeam1PointsCurrent, mTeam2PointsCurrent;
    private String mActiveGameSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_game);

        // Temp setting on set one
        mActiveGameSet = Constants.GAMESET_ONE;


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

        mActiveGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                mTeam1Name.setText(game.getTeam1());
                mTeam2Name.setText(game.getTeam2());

                mTeam1NameTable.setText(game.getTeam1());
                mTeam2NameTable.setText(game.getTeam2());

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
                            mSet2.setBackgroundColor(Color.TRANSPARENT);
                        }
                        mTeam1Points3Set.setText(String.valueOf(entry.getValue().getScoreTeam1()));
                        mTeam2Points3Set.setText(String.valueOf(entry.getValue().getScoreTeam2()));
                    }

                }
                mTeam1PointsCurrent = Integer.valueOf(mTeam1Points.getText().toString());
                mTeam2PointsCurrent = Integer.valueOf(mTeam2Points.getText().toString());
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

        mActiveGameSetsRef.addChildEventListener(new ChildEventListener() {
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


        initializeScreen();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.button_increment_team1:
                    mTeam1Points.setText(String.valueOf(++mTeam1PointsCurrent));
                    updateScoresTeam1();
                    break;
                case R.id.button_increment_team2:
                    mTeam2Points.setText(String.valueOf(++mTeam2PointsCurrent));
                    updateScoresTeam2();
                    break;
                case R.id.button_decrement_team1:
                    mTeam1Points.setText(String.valueOf(--mTeam1PointsCurrent));
                    updateScoresTeam1();
                    break;
                case R.id.button_decrement_team2:
                    mTeam2Points.setText(String.valueOf(--mTeam2PointsCurrent));
                    updateScoresTeam2();
                    break;
                case R.id.button_nextSet:
                    mNextSet.setVisibility(View.INVISIBLE);
                    mPrevSet.setVisibility(View.VISIBLE);
                    mTeam1PointsCurrent = Integer.parseInt(mTeam1Points2Set.getText().toString());
                    mTeam2PointsCurrent = Integer.parseInt(mTeam2Points2Set.getText().toString());
                    mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_RUNNING).setValue(false);
                    mActiveGameSet = Constants.GAMESET_TWO;
                    mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_RUNNING).setValue(true);
                    break;
                case R.id.button_prevSet:
                    mPrevSet.setVisibility(View.INVISIBLE);
                    mNextSet.setVisibility(View.VISIBLE);
                    mTeam1PointsCurrent = Integer.parseInt(mTeam1Points1Set.getText().toString());
                    mTeam2PointsCurrent = Integer.parseInt(mTeam2Points1Set.getText().toString());
                    mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_RUNNING).setValue(false);
                    mActiveGameSet = Constants.GAMESET_ONE;
                    mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_RUNNING).setValue(true);
                    break;
                case R.id.button_event_ace:
                    mCustomEvent.setText(getResources().getString(R.string.event_ace));
                    break;
                case R.id.button_event_block:
                    mCustomEvent.setText(getResources().getString(R.string.event_block));
                    break;
                case R.id.button_event_lineshot:
                    mCustomEvent.setText(getResources().getString(R.string.event_lineshot));
                    break;
                case R.id.button_event_cut:
                    mCustomEvent.setText(getResources().getString(R.string.event_cut));
                    break;
                case R.id.button_event_rainbow:
                    mCustomEvent.setText(getResources().getString(R.string.event_rainbow));
                    break;
                case R.id.button_send_event:
                    String message = mCustomEvent.getText().toString();
                    /*if(mLastChildAdded != null) {
                        mLastChildAdded.child("info").setValue(message);
                    } else{
                        Toast.makeText(ManageGameActivity.this, "Event not started yet!", Toast.LENGTH_SHORT).show();
                    }*/
                    break;
            }
        }
    };

    private void updateScoresTeam2() {
        mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM2).setValue(mTeam2PointsCurrent);
        updateGameEvent();
    }

    private void updateScoresTeam1() {
        mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM1).setValue(mTeam1PointsCurrent);
        updateGameEvent();
    }

    private void updateGameEvent() {
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
            mPrevSet.setVisibility(View.INVISIBLE);
        }

    }


}
