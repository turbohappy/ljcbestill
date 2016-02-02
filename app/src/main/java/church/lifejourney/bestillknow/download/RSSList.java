package church.lifejourney.bestillknow.download;

import java.util.ArrayList;
import java.util.List;

import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/28/16.
 */
public class RSSList {
    private List<Item> items;
    private RSSListUpdatedListener listener;
    private int lastPageLoaded;
    private boolean loading;

    public RSSList(RSSListUpdatedListener listener) {
        this.listener = listener;
        this.items = new ArrayList<>();
        this.lastPageLoaded = 0;
        this.loading = false;
        loadAnotherPage(); // load first page
    }

    public boolean loading() {
        return this.loading;
    }

    public void loadAnotherPage() {
        this.loading = true;
        final RSSListUpdatedListener pageListener = new RSSListUpdatedListener() {
            @Override
            public void listUpdated() {
                listener.listUpdated();
                lastPageLoaded++;
                Logger.debug(this, "List updated, lastPageLoaded=" + lastPageLoaded);
                loading = false;
            }
        };
        new RSSTask(items, pageListener).execute("http://lifejourneychurch.cc/bestill/feed?paged=" + (lastPageLoaded + 1)); // next page
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
