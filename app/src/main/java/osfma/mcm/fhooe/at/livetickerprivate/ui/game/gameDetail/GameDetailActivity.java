package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.ui.BaseActivity;
import osfma.mcm.fhooe.at.livetickerprivate.ui.MainActivity;
import osfma.mcm.fhooe.at.livetickerprivate.ui.game.adapter.GameDetailTabsPagerAdapter;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

public class GameDetailActivity extends BaseActivity {
    private static final String LOG_TAG = GameDetailActivity.class.getSimpleName();
    private String mGameId;
    private Constants.GameType mGameType;
    private String mGameOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        /* Get the push ID from the extra passed by ShoppingListFragment */
        Intent intent = this.getIntent();
        mGameId = intent.getStringExtra(Constants.KEY_LIST_ID);
        mGameType = (Constants.GameType) intent.getSerializableExtra(Constants.KEY_GAME_TYPE);
        if (mGameId == null) {
            /* No point in continuing without a valid ID. */
            finish();
            return;
        }
        /**
         * Create Firebase references
         */
        String gameType = Helper.checkGameType(mGameType);

        Firebase activeGameRef = new Firebase(gameType).child(mGameId);

        if(activeGameRef != null) {
            activeGameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Game game = dataSnapshot.getValue(Game.class);
                    mGameOwner = game.getOwner();
                    setupLayout();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else{
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setupLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Watch"));
        tabLayout.setVisibility(View.GONE);
        if(mEncodedEmail.equals(mGameOwner)) {
            tabLayout.addTab(tabLayout.newTab().setText("Manage"));
            tabLayout.setVisibility(View.VISIBLE);
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final GameDetailTabsPagerAdapter adapter = new GameDetailTabsPagerAdapter
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_detail, menu);
        return true;
    }

    public String getmEncodedEmail() {
        return mEncodedEmail;
    }
}
