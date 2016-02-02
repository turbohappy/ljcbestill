package church.lifejourney.bestillknow.helper;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by bdavis on 2/2/16.
 */
public class URLDrawable extends BitmapDrawable {
	// the drawable that you need to set, you could set the initial drawing
	// with the loading image if you need to
	public Drawable drawable;

	@Override
	public void draw(Canvas canvas) {
		// override the draw to facilitate refresh function later
		if(drawable != null) {
			drawable.draw(canvas);
		}
	}
}