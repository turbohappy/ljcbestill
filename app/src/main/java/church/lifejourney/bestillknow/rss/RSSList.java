package church.lifejourney.bestillknow.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdavis on 1/28/16.
 */
public class RSSList {
    private List<Item> items;
    private RSSListUpdatedListener listener;

    public RSSList(RSSListUpdatedListener listener) {
        this.listener = listener;
        this.items = new ArrayList<>();

        new RSSTask(items, listener).execute("http://lifejourneychurch.cc/bestill/feed"); // first page
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    public int size() {
        return items.size();
    }

    public static interface RSSListUpdatedListener {
        public void listUpdated();
    }
}
