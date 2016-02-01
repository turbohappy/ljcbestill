package church.lifejourney.bestillknow;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.xml.sax.XMLReader;

import church.lifejourney.bestillknow.helper.Logger;

public class ShowDevotionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_devotional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView contentView = (TextView) findViewById(R.id.dev_content_view);

        String content = getIntent().getStringExtra("content");
        contentView.setText(Html.fromHtml(content, new DevotionalImageGetter(), new DevotionalTagHandler()));
        contentView.setMovementMethod(new ScrollingMovementMethod());
    }

    private static class DevotionalTagHandler implements Html.TagHandler {

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            Logger.debug(this, "need to handle tag [" + tag + "] [opening=" + opening + "]");
        }
    }

    private static class DevotionalImageGetter implements Html.ImageGetter {

        @Override
        public Drawable getDrawable(String source) {
            Logger.debug(this, "need to handle image [" + source + "]");
            return null;
        }
    }
}
