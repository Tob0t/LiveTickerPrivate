package osfma.mcm.fhooe.at.livetickerprivate.ui.game.gameDetail;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameEvent;
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
        TextView author = (TextView) view.findViewById(R.id.text_view_author);
        TextView message = (TextView) view.findViewById(R.id.text_view_message);
        TextView timestamp = (TextView) view.findViewById(R.id.text_view_timestamp);

        author.setText(gameEvent.getAuthor());
        message.setText(gameEvent.getMessage());
        timestamp.setText(Helper.TIME_FORMATTER.format(gameEvent.getTimestampSentLong()));
        switch (gameEvent.getType()) {
            case SCORE:
                showScoreEvent(view, gameEvent);
                break;
            case CHAT:
                showChatEvent(view, gameEvent);
                break;
            case INFO:
                showInfoEvent(view, gameEvent);
        }
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
