package church.lifejourney.bestillknow.download;

import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

import church.lifejourney.bestillknow.db.Passage;
import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/27/16.
 */
public class LoadPassageTask extends AsyncTask<String, Void, String> {

	private LoadPassageListener listener;

	public LoadPassageTask(LoadPassageListener listener) {
		this.listener = listener;
	}

	private Exception exception;

	protected String doInBackground(String... urls) {
		try {
			String url = urls[0];
			Logger.debug(this, "Loading passage from " + url);
			Document doc = Jsoup.connect(url).get();
			Element el = doc.select("div.result-text-style-normal.text-html").first();
			el.select("h1.passage-display").remove();
			el.select("div.footnotes").remove();
			return el.outerHtml();
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	protected void onPostExecute(String html) {
		if (this.exception != null) {
			Logger.error(this, "Problem loading passage", this.exception);
		} else {
			listener.loaded(html);
		}
	}

	public interface LoadPassageListener {
		void loaded(String passageText);
	}
}