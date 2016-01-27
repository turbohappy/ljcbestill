package church.lifejourney.bestillknow.rss;

import android.os.AsyncTask;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.net.URL;

/**
 * Created by bdavis on 1/27/16.
 */
public class RSSTask  extends AsyncTask<String, Void, RSS> {

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
        // TODO: do something with the feed
        int x=0;
        x++;
    }

    private RSS read(String url) throws Exception {
        Serializer serializer = new Persister();

        RSS rss = serializer.read(RSS.class, new URL(url).openStream(), false);
        return rss;
    }
}