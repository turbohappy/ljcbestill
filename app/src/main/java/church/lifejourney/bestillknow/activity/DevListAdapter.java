package church.lifejourney.bestillknow.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.download.Item;
import church.lifejourney.bestillknow.download.RSSList;

/**
 * Created by bdavis on 1/28/16.
 */
public class DevListAdapter extends RecyclerView.Adapter<DevListAdapter.ViewHolder> {
    private Activity parent;
    private RSSList rssList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView authorView;
        public TextView dayView;
        public TextView monthyearView;

        public ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.card_title);
            authorView = (TextView) v.findViewById(R.id.card_author);
            dayView = (TextView) v.findViewById(R.id.card_date_day);
            monthyearView = (TextView) v.findViewById(R.id.card_date_monthyear);
        }
    }

    public DevListAdapter(Activity parent, RSSList rssList) {
        this.parent = parent;
        this.rssList = rssList;
    }

    @Override
    public DevListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_dev, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = rssList.getItem(position);
        holder.titleView.setText(item.getTitle());
        holder.authorView.setText(String.format("by %s", item.getCreator()));
        holder.dayView.setText(new SimpleDateFormat("d", Locale.US).format(item.getPubDate()));
        holder.monthyearView.setText(new SimpleDateFormat("MMM yyyy", Locale.US).format(item.getPubDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getApplicationContext(), ShowDevotionalActivity.class);
                intent.putExtra("url", item.getLink());
                intent.putExtra("content", item.getContent());
                parent.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssList.size();
    }
}