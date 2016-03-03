package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameCreate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.ui.BaseActivity;
import osfma.mcm.fhooe.at.livetickerprivate.ui.MainActivity;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

public class GameCreateActivity extends BaseActivity {
    private Spinner mSportType;
    private EditText mTeam1, mTeam2, mDate, mTime;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private ToggleButton mTogglePrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_create);

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
        mDate.setText(Helper.DATE_FORMATTER.format(newCalendar.getTime()));
        mTime.setText(Helper.TIME_FORMATTER.format(newCalendar.getTime()));
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sport_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSportType.setAdapter(adapter);

        mTeam1 = (EditText) findViewById(R.id.editText_team1);
        mTeam2 = (EditText) findViewById(R.id.editText_team2);
        mDate = (EditText) findViewById(R.id.editText_date);

        mTime = (EditText) findViewById(R.id.editText_time);
        Button createGame = (Button) findViewById(R.id.button_create_game);

        mTogglePrivacy = (ToggleButton) findViewById(R.id.toggleButton_privacy);


        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGame();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

       mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.show();
            }
        });


    }



    private void createGame() {

        String sportType = mSportType.getSelectedItem().toString();
        String team1 = mTeam1.getText().toString();
        String team2 = mTeam2.getText().toString();
        Boolean isPublic = mTogglePrivacy.isChecked();
        long dateAndTime = 0;

        try {
            Date fullDate = (Date) Helper.TIMESTAMP_FORMATTER.parse(mDate.getText().toString()+" "+mTime.getText().toString());
            dateAndTime = fullDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Game newGame = new Game(sportType,team1,team2,dateAndTime,3,mEncodedEmail);

        // Get the reference to the root node in Firebase
        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        String gameType = Constants.FIREBASE_LOCATION_PRIVATE_GAMES;
        if(isPublic){
            gameType = Constants.FIREBASE_LOCATION_PUBLIC_GAMES;
        }

        ref.child(gameType).push().setValue(newGame);

    }

}
