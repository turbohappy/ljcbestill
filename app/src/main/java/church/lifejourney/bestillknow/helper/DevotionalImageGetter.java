package church.lifejourney.bestillknow.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bdavis on 2/1/16.
 */
public class DevotionalImageGetter implements Html.ImageGetter {

	private Activity activity;

	public DevotionalImageGetter(Activity activity) {

		this.activity = activity;
	}

	@Override
	public Drawable getDrawable(String source) {
		try {
			return drawableFromUrl(source);
		} catch (IOException e) {
			Logger.error(this, "couldn't load image [" + source + "]", e);
			return null;
		}
	}

	public Drawable drawableFromUrl(String url) throws IOException {
		Bitmap img;

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();

		img = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(activity.getApplicationContext().getResources(), img);
	}
}
