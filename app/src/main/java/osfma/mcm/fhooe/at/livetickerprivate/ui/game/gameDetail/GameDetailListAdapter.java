package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameEvent;
import osfma.mcm.fhooe.at.livetickerprivate.model.User;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;
import osfma.mcm.fhooe.at.livetickerprivate.utils.FireBaseMultipleItems.FirebaseListAdapterMultipleItems;
import osfma.mcm.fhooe.at.livetickerprivate.utils.Helper;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class GameDetailListAdapter extends FirebaseListAdapterMultipleItems<GameEvent> {
    private static final String LOG_TAG = GameDetailListAdapter.class.getSimpleName();

    public GameDetailListAdapter(Activity activity, Class<GameEvent> modelClass, int[] modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    @Override
    protected void populateView(View view, GameEvent gameEvent, int i) {
        TextView author = (TextView) view.findViewById(R.id.text_view_owner);
        TextView message = (TextView) view.findViewById(R.id.text_view_message);
        TextView timestamp = (TextView) view.findViewById(R.id.text_view_timestamp);

        message.setText(gameEvent.getMessage());
        timestamp.setText(Helper.TIME_FORMATTER.format(gameEvent.getTimestampSentLong()));


        switch (gameEvent.getType()) {
            case SCORE:
                author.setText(gameEvent.getAuthor());
                showScoreEvent(view, gameEvent);
                break;
            case CHAT:
                setAuthorName(author, gameEvent.getAuthor());
                showChatEvent(view, gameEvent);
                break;
            case INFO:
                showInfoEvent(view, gameEvent);
        }
    }

    private void setAuthorName(final TextView textViewAuthor, String authorEmail) {
        Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(authorEmail);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    textViewAuthor.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, mActivity.getString(R.string.log_error_the_read_failed)
                        + firebaseError.getMessage());
            }
        });
    }

    private void showScoreEvent(View view, GameEvent gameEvent) {
        TextView info = (TextView) view.findViewById(R.id.text_view_info);
        info.setText(gameEvent.getInfo());
    }

    private void showChatEvent(View view, GameEvent gameEvent) {

    }

    private void showInfoEvent(View view, GameEvent gameEvent) {
    }

    @Override
    public int getItemViewType(int position) {
        GameEvent model = this.getItem(position);
        return model.getType().ordinal();
    }
}
