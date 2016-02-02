package church.lifejourney.bestillknow.download;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import church.lifejourney.bestillknow.helper.URLDrawable;

/**
 * Created by bdavis on 2/2/16.
 */
public class URLImageGetter implements Html.ImageGetter {
	Context c;
	View container;

	/***
	 * Construct the URLImageGetter which will execute AsyncTask and refresh the container
	 * @param t
	 * @param c
	 */
	public URLImageGetter(View t, Context c) {
		this.c = c;
		this.container = t;
	}

	public Drawable getDrawable(String source) {
		URLDrawable urlDrawable = new URLDrawable();

		// get the actual source
		ImageGetterAsyncTask asyncTask =
				new ImageGetterAsyncTask( urlDrawable);

		asyncTask.execute(source);

		// return reference to URLDrawable where I will change with actual image from
		// the src tag
		return urlDrawable;
	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
		URLDrawable urlDrawable;

		public ImageGetterAsyncTask(URLDrawable d) {
			this.urlDrawable = d;
		}

		@Override
		protected Drawable doInBackground(String... params) {
			String source = params[0];
			return fetchDrawable(source);
		}

		@Override
		protected void onPostExecute(Drawable result) {
			// set the correct bound according to the result from HTTP call
			if(result!=null) {
				urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0
						+ result.getIntrinsicHeight());
			}

			// change the reference of the current drawable to the result
			// from the HTTP call
			urlDrawable.drawable = result;

			// redraw the image by invalidating the container
			URLImageGetter.this.container.invalidate();
		}

		/***
		 * Get the Drawable from URL
		 * @param urlString
		 * @return
		 */
		public Drawable fetchDrawable(String urlString) {
			try {
				InputStream is = fetch(urlString);
				Drawable drawable = Drawable.createFromStream(is, "src");
				drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0
						+ drawable.getIntrinsicHeight());
				return drawable;
			} catch (Exception e) {
				return null;
			}
		}

		private InputStream fetch(String urlString) throws IOException {
			HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
			connection.connect();
			return connection.getInputStream();
		}
	}
}