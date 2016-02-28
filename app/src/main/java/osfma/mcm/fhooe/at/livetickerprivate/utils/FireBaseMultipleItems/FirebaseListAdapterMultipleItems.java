package osfma.mcm.fhooe.at.livetickerprivate.utils.FireBaseMultipleItems;

/**
 * Created by Tob0t on 26.02.2016.
 */

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

public abstract class FirebaseListAdapterMultipleItems<T> extends BaseAdapter {
    private final Class<T> mModelClass;
    protected int[] mLayout;
    protected Activity mActivity;
    FirebaseArrayMultipleItems mSnapshots;


    /**
     * @param activity    The activity containing the ListView
     * @param modelClass  Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout This is the layout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of modelClass.
     * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     */
    public FirebaseListAdapterMultipleItems(Activity activity, Class<T> modelClass, int[] modelLayout, Query ref) {
        mModelClass = modelClass;
        mLayout = modelLayout;
        mActivity = activity;
        mSnapshots = new FirebaseArrayMultipleItems(ref);
        mSnapshots.setOnChangedListener(new FirebaseArrayMultipleItems.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                notifyDataSetChanged();
            }
        });
    }
    /**
     * @param activity    The activity containing the ListView
     * @param modelClass  Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout This is the layout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of modelClass.
     * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     */
    public FirebaseListAdapterMultipleItems(Activity activity, Class<T> modelClass, int[] modelLayout, Firebase ref) {
        this(activity, modelClass, modelLayout, (Query) ref);
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mSnapshots.cleanup();
    }

    @Override
    public int getCount() {
        return mSnapshots.getCount();
    }

    @Override
    public T getItem(int position) {
        return parseSnapshot(mSnapshots.getItem(position));
    }

    /**
     * This method parses the DataSnapshot into the requested type. You can override it in subclasses
     * to do custom parsing.
     *
     * @param snapshot the DataSnapshot to extract the model from
     * @return the model extracted from the DataSnapshot
     */
    protected T parseSnapshot(DataSnapshot snapshot) {
        return snapshot.getValue(mModelClass);
    }

    public Firebase getRef(int position) { return mSnapshots.getItem(position).getRef(); }

    @Override
    public long getItemId(int i) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mSnapshots.getItem(i).getKey().hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mActivity.getLayoutInflater().inflate(mLayout[getItemViewType(position)], viewGroup, false);
        }

        T model = getItem(position);

        // Call out to subclass to marshall this model into the provided view
        populateView(view, model, position);
        return view;
    }

    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The first two arguments correspond to the mLayout and mModelClass given to the constructor of
     * this class. The third argument is the item's position in the list.
     * <p>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param v         The view to populate
     * @param model     The object containing the data used to populate the view
     * @param position  The position in the list of the view being populated
     */
    abstract protected void populateView(View v, T model, int position);

    @Override
    public int getViewTypeCount() {
        return mLayout.length;
    }
}
