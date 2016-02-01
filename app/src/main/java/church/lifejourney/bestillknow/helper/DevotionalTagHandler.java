package church.lifejourney.bestillknow.helper;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.XMLReader;

/**
 * Created by bdavis on 2/1/16.
 */
public class DevotionalTagHandler implements Html.TagHandler {

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if ((!"html".equals(tag)) && (!"body".equals(tag))) {
            Logger.debug(this, "need to handle tag [" + tag + "] [opening=" + opening + "]");
        }
    }
}
