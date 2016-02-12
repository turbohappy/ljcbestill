package church.lifejourney.bestillknow.download;

import android.content.Context;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/27/16.
 */
public class LoadDevotionalTask extends AsyncTask<Integer, Void, List<RSSItem>> {

	int page;
	private LoadDevotionalListener listener;

	public LoadDevotionalTask(LoadDevotionalListener listener) {
		this.listener = listener;
	}

	private Exception exception;

	@Override
	protected List<RSSItem> doInBackground(Integer... pages) {
		try {
			page = pages[0];
			return loadPage(page);
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	private List<RSSItem> loadPage(int page) throws Exception {
		String url = "http://lifejourneychurch.cc/bestill/feed?paged=" +
				page;
		Logger.debug("LoadDevotionalTask", "Reading " + url);
		RSS rss = read(url);
		return rss.getChannel().getItems();
	}

	@Override
	protected void onPostExecute(List<RSSItem> items) {
		if (this.exception != null) {
			Logger.error(this, "Problem loading devotionals", this.exception);
		} else {
			listener.devotionalsLoaded(page, items);
		}
	}

	private static RSS read(String url) throws Exception {
		Serializer serializer = new Persister(new AnnotationStrategy());

		return serializer.read(RSS.class, new URL(url).openStream(), false);
	}

	public interface LoadDevotionalListener {
		void devotionalsLoaded(int page, List<RSSItem> items);
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