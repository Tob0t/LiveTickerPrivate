package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameEvent;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameSet;
import osfma.mcm.fhooe.at.livetickerprivate.model.User;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

/**
 * Created by Tob0t on 17.03.2016.
 */
public class GameManageFragment extends Fragment {

    private static final String LOG_TAG = GameManageFragment.class.getSimpleName();
    private Firebase mActiveGameRef;
    private Firebase mGamesEventsRef;
    private Firebase mActiveGameSetsRef;
    private Firebase mActiveGameActiveSetRef;
    private Firebase mUserRef;
    private Firebase mLastChildAdded;
    private ValueEventListener mActiveGameRefListener, mUserRefListener;
    private ChildEventListener mActiveGameSetsRefListener, mGamesEventsRefListener;
    private String mGameId;
    private TextView mTeam1NameTable;
    private ArrayList<TextView> mTeam1PointsSets, mTeam2PointsSets;
    private TextView mTeam2NameTable;
    private TextView mTeam1Name, mTeam1Points, mTeam2Name, mTeam2Points;
    private EditText mCustomEvent;
    private Button mNextSet, mPrevSet;
    private ArrayList<TableRow> mSetTableRows;
    private TableRow mHeadline;
    private int mTeam1PointsCurrent, mTeam2PointsCurrent;
    private String mActiveGameSet;
    private int mNumberGameSets;
    private boolean mGameStarted;
    private User mUser;
    private Constants.GameType mGameType;
    private String mEncodedEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_manage, container, false);

        // some inital variables
        mActiveGameSet = Constants.GAMESET_ONE;
        mGameStarted = false;

         /* Get the push ID from the extra passed by ShoppingListFragment */
        Intent intent = getActivity().getIntent();
        mGameId = intent.getStringExtra(Constants.KEY_LIST_ID);
        mGameType = (Constants.GameType) intent.getSerializableExtra(Constants.KEY_GAME_TYPE);

        // Get email from Activity
        mEncodedEmail = ((GameDetailActivity)getActivity()).getmEncodedEmail();

        String gameType = Helper.checkGameType(mGameType);
        mActiveGameRef = new Firebase(gameType).child(mGameId);
        mGamesEventsRef = new Firebase(Constants.FIREBASE_URL_GAMES_EVENTS).child(mGameId);
        mActiveGameSetsRef = new Firebase(gameType).child(mGameId).child(Constants.FIREBASE_LOCATION_GAMES_GAMESETS);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);

        mActiveGameRefListener = mActiveGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                mTeam1Name.setText(game.getTeam1());
                mTeam2Name.setText(game.getTeam2());

                mTeam1NameTable.setText(game.getTeam1());
                mTeam2NameTable.setText(game.getTeam2());

                mNumberGameSets = game.getGameSets().size();

                mGameStarted = game.isStarted();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mActiveGameSetsRefListener = mActiveGameSetsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // having the right Scores on startup
                updateView(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateView(dataSnapshot);
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

        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, getString(R.string.log_error_the_read_failed)
                        + firebaseError.getMessage());
            }
        });

        initializeScreen(rootView);

        return rootView;
    }

    private void updateView(DataSnapshot dataSnapshot) {
        GameSet gameSet = dataSnapshot.getValue(GameSet.class);
        updateTableScores(dataSnapshot);
        updateTableRows(dataSnapshot);
        if(gameSet.isActive()){
            mActiveGameSet = dataSnapshot.getKey();
            updateMainScore(gameSet);
        }
        updateButtonVisibility();
    }

    private void updateTableRows(DataSnapshot dataSnapshot) {
        GameSet gameSet = dataSnapshot.getValue(GameSet.class);
        int currentGameSet = Constants.GAMESETS_LIST.indexOf(dataSnapshot.getKey());
        if(gameSet.isActive()) {
            mSetTableRows.get(currentGameSet).setBackgroundColor(Color.YELLOW);
        } else{
            mSetTableRows.get(currentGameSet).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void updateMainScore(GameSet gameSet) {
        if(gameSet.isActive()) {
            // write the score in the middle of the active gameSet
            mTeam1Points.setText(String.valueOf(gameSet.getScoreTeam1()));
            mTeam2Points.setText(String.valueOf(gameSet.getScoreTeam2()));
        }
    }

    private void updateTableScores(DataSnapshot dataSnapshot) {
        GameSet gameSet = dataSnapshot.getValue(GameSet.class);
        int currentGameSet = Constants.GAMESETS_LIST.indexOf(dataSnapshot.getKey());
        mTeam1PointsSets.get(currentGameSet).setText(String.valueOf(gameSet.getScoreTeam1()));
        mTeam2PointsSets.get(currentGameSet).setText(String.valueOf(gameSet.getScoreTeam2()));
    }



    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.button_increment_team1: {
                    initiateScoreUpdate(Constants.Team.TEAM1, 1);
                    break;
                }
                case R.id.button_increment_team2: {
                    initiateScoreUpdate(Constants.Team.TEAM2, 1);
                    break;
                }
                case R.id.button_decrement_team1: {
                    initiateScoreUpdate(Constants.Team.TEAM1, -1);
                    break;
                }
                case R.id.button_decrement_team2: {
                    initiateScoreUpdate(Constants.Team.TEAM2, -1);
                    break;
                }
                case R.id.button_nextSet: {
                    changeGameSet(Constants.Navigate.NEXT);
                    break;
                }
                case R.id.button_prevSet: {
                    changeGameSet(Constants.Navigate.PREVIOUS);
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
                    sendEvent(v);
                    break;
                }
                case R.id.button_finish_game:{
                    sendFinishGame();
                    break;
                }
            }
        }

        private void sendEvent(View v) {
            String message = mCustomEvent.getText().toString();
            if (mGameStarted) {
                mLastChildAdded.child("info").setValue(message);
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mCustomEvent.getWindowToken(), 0);
                mCustomEvent.setText("");
            } else {
                Snackbar.make(v, "Game not started yet!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        private void sendFinishGame() {
            mGamesEventsRef.push().setValue(new GameEvent("Game is finished! Thank you for using the liveticker!","Admin", Constants.ItemType.INFO));
            mActiveGameRef.child(Constants.FIREBASE_PROPERTY_GAMES_FINISHED).setValue(true);
        }

        private void changeGameSet(Constants.Navigate direction) {
            HashMap<String, Object> updatedGameSets = new HashMap<String, Object>();
            int activeGameSetInt = Constants.GAMESETS_LIST.indexOf(mActiveGameSet);
            updatedGameSets.put(mActiveGameSet + "/" + Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_ACTIVE, false);

            if(direction == Constants.Navigate.NEXT) {
                if(activeGameSetInt+1 < mNumberGameSets){
                    mActiveGameSet = Constants.GAMESETS_LIST.get(activeGameSetInt+1);
                    mGamesEventsRef.push().setValue(new GameEvent("Changed to "+mActiveGameSet,"Admin", Constants.ItemType.INFO));
                }
            } else if(direction == Constants.Navigate.PREVIOUS){
                if(activeGameSetInt > 0){
                    mActiveGameSet = Constants.GAMESETS_LIST.get(activeGameSetInt-1);
                    mGamesEventsRef.push().setValue(new GameEvent("Changed to "+mActiveGameSet,"Admin", Constants.ItemType.INFO));
                }
            }
            updatedGameSets.put(mActiveGameSet + "/" + Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_ACTIVE, true);
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

    private void initiateScoreUpdate(Constants.Team team, int unaryValue) {
        // Set Game state to started if it isn't set yet
        if(!mGameStarted){
            mGameStarted = true;
            mGamesEventsRef.push().setValue(new GameEvent("Game is started!","Admin", Constants.ItemType.INFO));
            mActiveGameRef.child(Constants.FIREBASE_PROPERTY_GAMES_STARTED).setValue(true);
            mActiveGameSetsRef.child(mActiveGameSet).child(Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_ACTIVE).setValue(true);
        }
        updateScore(team, unaryValue);
        createGameEvent();
    }

    private void updateScore(Constants.Team team, int unaryValue) {
        HashMap<String, Object> updatedScores = new HashMap<String, Object>();
        mTeam1PointsCurrent = Integer.valueOf(mTeam1Points.getText().toString());
        mTeam2PointsCurrent = Integer.valueOf(mTeam2Points.getText().toString());
        if(team == Constants.Team.TEAM1){
            mTeam1PointsCurrent += unaryValue;
            updatedScores.put(mActiveGameSet + "/" + Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM1, mTeam1PointsCurrent);
        } else if(team == Constants.Team.TEAM2){
            mTeam2PointsCurrent += unaryValue;
            updatedScores.put(mActiveGameSet + "/" + Constants.FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM2, mTeam2PointsCurrent);
        }
        mActiveGameSetsRef.updateChildren(updatedScores);
    }

    private void createGameEvent() {
        StringBuffer score = new StringBuffer();
        score.append(mTeam1PointsCurrent);
        score.append(":");
        score.append(mTeam2PointsCurrent);
        mGamesEventsRef.push().setValue(new GameEvent(score.toString(), mCustomEvent.getText().toString(), mUser.getName(), Constants.ItemType.SCORE));
        mCustomEvent.setText("");
    }

    private void initializeScreen(View rootView) {

        mTeam1PointsCurrent = mTeam2PointsCurrent = 0;

        mTeam1Name = (TextView) rootView.findViewById(R.id.textView_game_manage_team1);
        mTeam1Points = (TextView) rootView.findViewById(R.id.textView_game_manage_team1_points);
        mTeam2Name = (TextView) rootView.findViewById(R.id.textView_game_manage_team2);
        mTeam2Points = (TextView) rootView.findViewById(R.id.textView_game_manage_team2_points);

        mTeam1NameTable = (TextView) rootView.findViewById(R.id.textView_game_manage_table_team1);
        mTeam1PointsSets = new ArrayList<TextView>();
        mTeam1PointsSets.add((TextView) rootView.findViewById(R.id.textView_game_manage_table_team1_set1));
        mTeam1PointsSets.add((TextView) rootView.findViewById(R.id.textView_game_manage_table_team1_set2));
        mTeam1PointsSets.add((TextView) rootView.findViewById(R.id.textView_game_manage_table_team1_set3));

        mTeam2NameTable = (TextView) rootView.findViewById(R.id.textView_game_manage_table_team2);
        mTeam2PointsSets = new ArrayList<TextView>();
        mTeam2PointsSets.add((TextView) rootView.findViewById(R.id.textView_game_manage_table_team2_set1));
        mTeam2PointsSets.add((TextView) rootView.findViewById(R.id.textView_game_manage_table_team2_set2));
        mTeam2PointsSets.add((TextView) rootView.findViewById(R.id.textView_game_manage_table_team2_set3));

        mSetTableRows = new ArrayList<TableRow>();
        mSetTableRows.add((TableRow) rootView.findViewById(R.id.tableRow_manage_game_set1));
        mSetTableRows.add((TableRow) rootView.findViewById(R.id.tableRow_manage_game_set2));
        mSetTableRows.add((TableRow) rootView.findViewById(R.id.tableRow_manage_game_set3));

        Button incrementTeam1 = (Button) rootView.findViewById(R.id.button_increment_team1);
        Button incrementTeam2 = (Button) rootView.findViewById(R.id.button_increment_team2);
        Button decrementTeam1 = (Button) rootView.findViewById(R.id.button_decrement_team1);
        Button decrementTeam2 = (Button) rootView.findViewById(R.id.button_decrement_team2);

        Button eventAce = (Button) rootView.findViewById(R.id.button_event_ace);
        Button eventBlock = (Button) rootView.findViewById(R.id.button_event_block);
        Button eventLine = (Button) rootView.findViewById(R.id.button_event_lineshot);
        Button eventCut = (Button) rootView.findViewById(R.id.button_event_cut);
        Button eventRainbow = (Button) rootView.findViewById(R.id.button_event_rainbow);

        final ImageButton eventSend = (ImageButton) rootView.findViewById(R.id.button_send_event);
        mCustomEvent = (EditText) rootView.findViewById(R.id.editText_manage_game_event);

        mNextSet = (Button) rootView.findViewById(R.id.button_nextSet);
        mPrevSet = (Button) rootView.findViewById(R.id.button_prevSet);

        Button finishGame = (Button) rootView.findViewById(R.id.button_finish_game);

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
        finishGame.setOnClickListener(onClickListener);

        mNextSet.setOnClickListener(onClickListener);
        mPrevSet.setOnClickListener(onClickListener);

        // Hide send button if there box is empty
        mCustomEvent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    eventSend.setVisibility(View.INVISIBLE);
                } else {
                    eventSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActiveGameRef.removeEventListener(mActiveGameRefListener);
        mActiveGameSetsRef.removeEventListener(mActiveGameSetsRefListener);
        mGamesEventsRef.removeEventListener(mGamesEventsRefListener);
        mUserRef.removeEventListener(mUserRefListener);

    }

}
