package osfma.mcm.fhooe.at.livetickerprivate.ui.gameList;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.GameEvent;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class GameDetailListAdapter extends FirebaseListAdapter<GameEvent> {

    public GameDetailListAdapter(Activity activity, Class<GameEvent> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    @Override
    protected void populateView(View view, GameEvent gameEvent, int i) {

        TextView textViewPointsEvent = (TextView) view.findViewById(R.id.text_view_points_event);
        TextView textViewInfoEvent = (TextView) view.findViewById(R.id.text_view_info_event);
        TextView textViewChatEvent = (TextView) view.findViewById(R.id.text_view_chat_event);

        textViewPointsEvent.setText(gameEvent.getCurrentScore());
        textViewInfoEvent.setText(gameEvent.getInfo());
        textViewChatEvent.setText(gameEvent.getMessage());

    }
}
