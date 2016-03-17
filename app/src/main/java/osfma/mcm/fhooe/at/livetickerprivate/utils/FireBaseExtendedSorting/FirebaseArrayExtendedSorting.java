package osfma.mcm.fhooe.at.livetickerprivate.utils.FireBaseExtendedSorting;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

import osfma.mcm.fhooe.at.livetickerprivate.model.Game;

/**
 * Created by Tob0t on 03.03.2016.
 */
public class FirebaseArrayExtendedSorting implements ChildEventListener {
    public interface OnChangedListener {
        enum EventType { Added, Changed, Removed, Moved }
        void onChanged(EventType type, int index, int oldIndex);
    }

    private Query mQuery;
    private OnChangedListener mListener;
    private ArrayList<DataSnapshot> mSnapshots;
    private String[] mParams;
    private String mPreviousChildKey;

    public FirebaseArrayExtendedSorting(Query ref, String[] params) {
        mQuery = ref;
        mSnapshots = new ArrayList<DataSnapshot>();
        mQuery.addChildEventListener(this);
        mParams = params;
    }

    public void cleanup() {
        mQuery.removeEventListener(this);
    }

    public int getCount() {
        return mSnapshots.size();

    }
    public DataSnapshot getItem(int index) {
        return mSnapshots.get(index);
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (DataSnapshot snapshot : mSnapshots) {
            if (snapshot.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    // Start of ChildEventListener methods
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        if(checkParamsEqual(snapshot)) {
            int index = 0;
            if (mPreviousChildKey != null) {
                if(mSnapshots.size() >0) {
                    index = getIndexForKey(mPreviousChildKey) + 1;
                }
            }
            mPreviousChildKey = snapshot.getKey();
            mSnapshots.add(index, snapshot);
            notifyChangedListeners(OnChangedListener.EventType.Added, index);
        }
    }

    private boolean checkParamsEqual(DataSnapshot snapshot) {
        Game game = snapshot.getValue(Game.class);
        if(game != null) {
            if(game.isStarted()){
                return true;
            }
        }
        return false;
        //return (snapshot.child("started").equals(true));
        //return (mParams == null || (snapshot.child("started").equals(true)));
    }

    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
        if(checkParamsEqual(snapshot)) {
            int index = getIndexForKey(snapshot.getKey());
            mSnapshots.set(index, snapshot);
            notifyChangedListeners(OnChangedListener.EventType.Changed, index);
        }
    }

    public void onChildRemoved(DataSnapshot snapshot) {
        if(checkParamsEqual(snapshot)) {
            int index = getIndexForKey(snapshot.getKey());
            mSnapshots.remove(index);
            notifyChangedListeners(OnChangedListener.EventType.Removed, index);
        }
    }

    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        if(checkParamsEqual(snapshot)) {
            int oldIndex = getIndexForKey(snapshot.getKey());
            mSnapshots.remove(oldIndex);
            int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
            mSnapshots.add(newIndex, snapshot);
            notifyChangedListeners(OnChangedListener.EventType.Moved, newIndex, oldIndex);
        }
    }

    public void onCancelled(FirebaseError firebaseError) {
        // TODO: what do we do with this?
    }
    // End of ChildEventListener methods

    public void setOnChangedListener(OnChangedListener listener) {
        mListener = listener;
    }
    protected void notifyChangedListeners(OnChangedListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }
    protected void notifyChangedListeners(OnChangedListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChanged(type, index, oldIndex);
        }
    }
}
