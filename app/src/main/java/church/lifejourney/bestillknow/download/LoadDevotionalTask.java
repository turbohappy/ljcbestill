package church.lifejourney.bestillknow.download;

import android.os.AsyncTask;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.net.URL;
import java.util.List;

import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/27/16.
 */
public class LoadDevotionalTask extends AsyncTask<Integer, Void, List<RSSItem>> {

	private RSSItemsListener listener;

	public LoadDevotionalTask(RSSItemsListener listener) {
		this.listener = listener;
	}

	private Exception exception;

	protected List<RSSItem> doInBackground(Integer... pages) {
		try {
			int page = pages[0];
			String url = "http://lifejourneychurch.cc/bestill/feed?paged=" +
					page;
			Logger.debug(this, "Reading " + url);
			RSS rss = read(url);
			return rss.getChannel().getItems();
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	protected void onPostExecute(List<RSSItem> items) {
		if (this.exception != null) {
			Logger.error(this, "Problem loading devotionals", this.exception);
		} else {
			listener.itemsReturned(items);
		}
	}

	private RSS read(String url) throws Exception {
		Serializer serializer = new Persister(new AnnotationStrategy());

		return serializer.read(RSS.class, new URL(url).openStream(), false);
	}

	public interface RSSItemsListener {
		void itemsReturned(List<RSSItem> items);
	}

	@Root
	private static class RSS {
		@Element
		private Channel channel;

		public Channel getChannel() {
			return channel;
		}
	}

	private static class Channel {
		@ElementList(inline = true)
		private List<RSSItem> items;

		public List<RSSItem> getItems() {
			return items;
		}
	}
}