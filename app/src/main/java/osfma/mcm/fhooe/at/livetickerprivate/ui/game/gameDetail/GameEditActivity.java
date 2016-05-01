package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.ui.BaseActivity;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

public class GameEditActivity extends BaseActivity {
    private static final String LOG_TAG = GameEditActivity.class.getSimpleName();
    private Spinner mSportType;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private EditText mTeam1, mTeam2, mDate, mTime;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private ToggleButton mTogglePrivacy;
    private String mGameId;
    private Constants.GameType mGameType;
    private Firebase mActiveGameRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_edit);

         /* Get the push ID from the extra passed by GameDetailActivity */
        Intent intent = this.getIntent();
        mGameId = intent.getStringExtra(Constants.GAME_ID);

        mGameType = (Constants.GameType) intent.getSerializableExtra(Constants.KEY_GAME_TYPE);
        String gameTypeRef = Helper.checkGameType(mGameType);
        mActiveGameRef = new Firebase(gameTypeRef).child(mGameId);

        ValueEventListener mActiveGameRefListener =  mActiveGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                if(game != null) {

                    int spinnerPosition = mSpinnerAdapter.getPosition(game.getSportType());
                    mSportType.setSelection(spinnerPosition);
                    mTeam1.setText(game.getTeam1());
                    mTeam2.setText(game.getTeam2());
                    mDate.setText(Helper.DATE_FORMATTER.format(game.getDateAndTime()));
                    mTime.setText(Helper.TIME_FORMATTER.format(game.getDateAndTime()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        setDateTimeFields();
    }



    private void setDateTimeFields() {
        mDate.setInputType(InputType.TYPE_NULL);
        mTime.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDate.setText(Helper.DATE_FORMATTER.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hour, int min) {
                Calendar newDate = Calendar.getInstance();
                //newDate.set(newDate));
                newDate.set(Calendar.HOUR_OF_DAY,hour);
                newDate.set(Calendar.MINUTE, min);
                mTime.setText(Helper.TIME_FORMATTER.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Spinner with values
        mSportType = (Spinner) findViewById(R.id.spinner_sportType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sport_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSportType.setAdapter(mSpinnerAdapter);

        mTeam1 = (EditText) findViewById(R.id.editText_team1);
        mTeam2 = (EditText) findViewById(R.id.editText_team2);
        mDate = (EditText) findViewById(R.id.editText_date);

        mTime = (EditText) findViewById(R.id.editText_time);
        Button updateGame = (Button) findViewById(R.id.button_update_game);

        mTogglePrivacy = (ToggleButton) findViewById(R.id.toggleButton_privacy);
        if(mGameType == Constants.GameType.PRIVATE){
            mTogglePrivacy.setChecked(false);
        }


        updateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGame();
                //finish();
                Intent intent = new Intent(getApplicationContext(), GameDetailActivity.class);
                intent.putExtra(Constants.KEY_LIST_ID, mGameId);
                intent.putExtra(Constants.KEY_GAME_TYPE, mGameType);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.show();
            }
        });


    }

    private void updateGame() {
        String sportType = mSportType.getSelectedItem().toString();
        String team1 = mTeam1.getText().toString();
        String team2 = mTeam2.getText().toString();
        final Boolean isPublic = mTogglePrivacy.isChecked();
        long dateAndTime = 0;

        try {
            Date fullDate = (Date) Helper.TIMESTAMP_FORMATTER.parse(mDate.getText().toString()+" "+mTime.getText().toString());
            dateAndTime = fullDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // updated Values
        final HashMap<String, Object> updatedGame = new HashMap<String, Object>();
        updatedGame.put(Constants.FIREBASE_PROPERTY_GAMES_SPORTTYPE, sportType);
        updatedGame.put(Constants.FIREBASE_PROPERTY_GAMES_TEAM1, team1);
        updatedGame.put(Constants.FIREBASE_PROPERTY_GAMES_TEAM2, team2);
        updatedGame.put(Constants.FIREBASE_PROPERTY_GAMES_DATEANDTIME, dateAndTime);

        // Check whether the privacy is changed since the game needs to be moved
        if(mGameType.toString().equalsIgnoreCase(mTogglePrivacy.getText().toString())){
            mActiveGameRef.updateChildren(updatedGame);
        } else{
            // Get the reference to the root node in Firebase
            final Firebase ref = new Firebase(Constants.FIREBASE_URL);

            // Read and copy old data to new location
            mActiveGameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Game game = dataSnapshot.getValue(Game.class);
                    if(game != null) {
                        String gameType = Constants.FIREBASE_LOCATION_PRIVATE_GAMES;
                        if(isPublic){
                            gameType = Constants.FIREBASE_LOCATION_PUBLIC_GAMES;
                        }
                        // Create new game with the same GameId
                        ref.child(gameType).child(mGameId).setValue(game);

                        // update with new values
                        ref.child(gameType).child(mGameId).updateChildren(updatedGame);

                        // Delete old game
                        mActiveGameRef.removeValue();
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            // Set gameType to right value
            mGameType = Constants.GameType.PRIVATE;
            if(isPublic){
                mGameType = Constants.GameType.PUBLIC;
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_discard) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.game_discard)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Create Intent to turn back
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();

            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_edit, menu);
        return true;
    }
}
