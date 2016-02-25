package osfma.mcm.fhooe.at.livetickerprivate.ui.gameList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameEvent;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;

public class GameDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = GameDetailActivity.class.getSimpleName();
    private Firebase mActiveGameRef;
    private Firebase mGamesEventsRef;
    private Firebase mLastChildAdded;
    //private GameEvent mGameEvent;
    private String mGameId;
    private GameDetailListAdapter mGameDetailListAdapter;
    private ListView mGameDetailListView;
    private TextView mTeam1Name, mTeam1Points, mTeam2Name, mTeam2Points;
    private TextView mTeam1NameTable, mTeam1Points1Set, mTeam1Points2Set, mTeam1Points3Set;
    private TextView mTeam2NameTable, mTeam2Points1Set, mTeam2Points2Set, mTeam2Points3Set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        /* Get the push ID from the extra passed by ShoppingListFragment */
        Intent intent = this.getIntent();
        mGameId = intent.getStringExtra(Constants.KEY_LIST_ID);
        if (mGameId == null) {
            /* No point in continuing without a valid ID. */
            finish();
            return;
        }

        /**
         * Create Firebase references
         */
        mActiveGameRef = new Firebase(Constants.FIREBASE_URL_GAMES).child(mGameId);
        mGamesEventsRef = new Firebase(Constants.FIREBASE_URL_GAMES_EVENTS).child(mGameId);


        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        /**
         * Setup the adapter
         */
        mGameDetailListAdapter = new GameDetailListAdapter(this, GameEvent.class,
                R.layout.single_game_event_list_item, mGamesEventsRef);
        /* Create ActiveListItemAdapter and set to listView */
        mGameDetailListView.setAdapter(mGameDetailListAdapter);

        mActiveGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                mTeam1Name.setText(game.getTeam1());
                mTeam2Name.setText(game.getTeam2());

                mTeam1NameTable.setText(game.getTeam1());
                mTeam2NameTable.setText(game.getTeam2());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mGamesEventsRef.addChildEventListener(new ChildEventListener() {
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

        mGamesEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //GameEvent gameEvent = dataSnapshot.getValue(GameEvent.class);
                /*if(dataSnapshot.getValue() instanceof GameEvent) {
                    mLastChildAdded = dataSnapshot.getRef();
                }*/

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGameDetailListView = (ListView) findViewById(R.id.listView_game_events);

        mTeam1Name = (TextView) findViewById(R.id.textView_game_detail_team1);
        mTeam1Points = (TextView) findViewById(R.id.textView_game_detail_team1_points);
        mTeam2Name = (TextView) findViewById(R.id.textView_game_detail_team2);
        mTeam2Points = (TextView) findViewById(R.id.textView_game_detail_team2_points);

        mTeam1NameTable = (TextView) findViewById(R.id.textView_game_detail_table_team1);
        mTeam1Points1Set = (TextView) findViewById(R.id.textView_game_detail_table_team1_set1);
        mTeam1Points2Set = (TextView) findViewById(R.id.textView_game_detail_table_team1_set2);
        mTeam1Points3Set = (TextView) findViewById(R.id.textView_game_detail_table_team1_set3);

        mTeam2NameTable = (TextView) findViewById(R.id.textView_game_detail_table_team2);
        mTeam2Points1Set = (TextView) findViewById(R.id.textView_game_detail_table_team2_set1);
        mTeam2Points2Set = (TextView) findViewById(R.id.textView_game_detail_table_team2_set2);
        mTeam2Points3Set = (TextView) findViewById(R.id.textView_game_detail_table_team2_set3);

        final EditText message = (EditText) findViewById(R.id.editText_chat);

        ImageButton ButtonSendMessage = (ImageButton) findViewById(R.id.imageButton_send);
        ButtonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(message.getText().toString());
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
                message.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        //mGamesEventsRef.push().setValue(new GameEvent(message,"Anonymous"));
        mLastChildAdded.setValue(new GameEvent(message, "eichel"));
    }



}