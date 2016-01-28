package church.lifejourney.bestillknow.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import church.lifejourney.bestillknow.R;

/**
 * Created by bdavis on 1/28/16.
 */
public class DevListAdapter extends RecyclerView.Adapter<DevListAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleView;
        public TextView authorView;
        public ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.card_title);
            authorView = (TextView) v.findViewById(R.id.card_author);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DevListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DevListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_dev, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //TODO:
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleView.setText(mDataset[position]);
        holder.authorView.setText("your momma");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}