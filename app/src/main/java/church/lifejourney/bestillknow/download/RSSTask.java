package church.lifejourney.bestillknow.download;

import android.os.AsyncTask;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.net.URL;
import java.util.List;

import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/27/16.
 */
public class RSSTask extends AsyncTask<Integer, Void, RSS> {

	private RSSItemsListener listener;

	public RSSTask(RSSItemsListener listener) {
		this.listener = listener;
	}

	private Exception exception;

	protected RSS doInBackground(Integer... pages) {
		try {
			int page = pages[0];
			String url = "http://lifejourneychurch.cc/bestill/feed?paged=" +
					page;
			Logger.debug(this, "Reading " + url);
			return read(url);
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	protected void onPostExecute(RSS rss) {
		// TODO: check this.exception

		List<Item> items = rss.getChannel().getItems();
		listener.itemsReturned(items);
	}

	private RSS read(String url) throws Exception {
		Serializer serializer = new Persister(new AnnotationStrategy());

		return serializer.read(RSS.class, new URL(url).openStream(), false);
	}

	public interface RSSItemsListener {
		void itemsReturned(List<Item> items);
	}
}