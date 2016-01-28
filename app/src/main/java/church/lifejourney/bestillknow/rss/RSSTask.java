package church.lifejourney.bestillknow.rss;

import android.os.AsyncTask;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.net.URL;
import java.util.List;

/**
 * Created by bdavis on 1/27/16.
 */
public class RSSTask  extends AsyncTask<String, Void, RSS> {
    private List<Item> existingItems;
    private RSSList.RSSListUpdatedListener listener;

    public RSSTask(List<Item> existingItems, RSSList.RSSListUpdatedListener listener) {
        this.existingItems = existingItems;
        this.listener = listener;
    }

    private Exception exception;

    protected RSS doInBackground(String... urls) {
        try {
            RSS rss = read(urls[0]);
            return rss;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(RSS rss) {
        // TODO: check this.exception

        existingItems.addAll(rss.getChannel().getItems());
        listener.listUpdated();
    }

    private RSS read(String url) throws Exception {
        Serializer serializer = new Persister(new AnnotationStrategy());

        return serializer.read(RSS.class, new URL(url).openStream(), false);
    }
}