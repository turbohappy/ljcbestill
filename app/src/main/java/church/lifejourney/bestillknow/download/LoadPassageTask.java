package church.lifejourney.bestillknow.download;

import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/27/16.
 */
public class LoadPassageTask extends AsyncTask<String, Void, String> {

	private TextView contentView;

	public LoadPassageTask(TextView contentView) {
		this.contentView = contentView;
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
		// TODO: check this.exception
		contentView.setText(Html.fromHtml(html));
//		contentView.setText(Html.fromHtml(html, new DevotionalImageGetter(), new DevotionalTagHandler()));
	}
}