package church.lifejourney.bestillknow.helper;

import android.graphics.drawable.Drawable;
import android.text.Html;

/**
 * Created by bdavis on 2/1/16.
 */
public class DevotionalImageGetter implements Html.ImageGetter {

    @Override
    public Drawable getDrawable(String source) {
        Logger.debug(this, "need to handle image [" + source + "]");
        return null;
    }
}
