package osfma.mcm.fhooe.at.livetickerprivate.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.User;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter.GameDetailTabsPagerAdapter;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter.GameTabsPagerAdapter;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameCreate.GameCreateActivity;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter.GameListItemAdapter;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail.GameDetailActivity;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.gamesList.GamesFragment;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Firebase mUserRef;
    private ValueEventListener mUserRefListener;
    private User mUser;
    private DrawerLayout mDrawerLayout;
    private Constants.GameType mGameType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial Value
        mGameType = Constants.GameType.PUBLIC;

        /**
         * Create Firebase references
         */
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);

        initializeScreen();

        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, getString(R.string.log_error_the_read_failed)
                        +firebaseError.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Create new Filters
        Map<Method, Boolean> filterMap = new HashMap<Method, Boolean>();

        if (id == R.id.nav_public_games) {
            mGameType = Constants.GameType.PUBLIC;
            setTitle(this.getString(R.string.title_public_games));
            updateGameType();
        } else if (id == R.id.nav_private_games) {
            mGameType = Constants.GameType.PRIVATE;
            setTitle(this.getString(R.string.title_private_games));
            updateGameType();
        } else if (id == R.id.action_manage_account) {
            createDialog();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateGameType() {
        GamesFragment gamesFragment = (GamesFragment) getSupportFragmentManager().findFragmentById(R.id.pager);
        gamesFragment.updateGameType(mGameType, mEncodedEmail);
    }
    public Constants.GameType getGameType(){
        return mGameType;
    }
    public String getmEncodedEmail() {
        return mEncodedEmail;
    }

    // TODO Improve
    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_edit_name, null);
        TextView name = (TextView) view.findViewById(R.id.edit_name_dialog);
        name.setText(mUser.getName(),null);
        builder.setView(view);
        builder.create();
        builder.show();

    }

    /**
     * Cleanup when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //mGameListItemAdapter.cleanup();
        mUserRef.removeEventListener(mUserRefListener);
    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameCreateActivity.class);
                startActivity(intent);
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {



            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView accountName = (TextView) findViewById(R.id.textView_account_name);
                TextView accountEmail = (TextView) findViewById(R.id.textView_account_email);
                RoundedImageView profilePic = (RoundedImageView) findViewById(R.id.imageView_profilePic);
                if(mUser != null) {
                    accountName.setText(mUser.getName());
                    accountEmail.setText(Helper.decodeEmail(mUser.getEmail()));
                    /* Load Image URL with Ion */
                    Ion.with(profilePic)
                            .load(mUser.getProfileImageUrl());

                    profilePic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    profilePic.setCornerRadius((float) 10);
                    profilePic.setBorderWidth((float) 2);
                    profilePic.setBorderColor(Color.DKGRAY);
                    profilePic.mutateBackground(true);
                    profilePic.setOval(false);
                    profilePic.setTileModeX(Shader.TileMode.REPEAT);
                    profilePic.setTileModeY(Shader.TileMode.REPEAT);
                }
            }

        };
        mDrawerLayout.setDrawerListener(toggle);
        /** Called when a drawer has settled in a completely open state. */

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(Constants.GAMES_RUNNING));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.GAMES_FUTURE));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.GAMES_PAST));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final GameTabsPagerAdapter adapter = new GameTabsPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


}
