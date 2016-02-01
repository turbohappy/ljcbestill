package church.lifejourney.bestillknow;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

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
        contentView.setMovementMethod(DevotionalLinkHandler.getInstance());
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

    private static class DevotionalLinkHandler extends LinkMovementMethod {
        private void handleLinkClick(ClickableSpan link, TextView widget) {
            if (link instanceof URLSpan) {
                String url = ((URLSpan) link).getURL();
                if (url != null && (url.contains("oremus.org") || url.contains("biblegateway.com"))) {
                    Toast.makeText(widget.getContext(), "clicked on a passage link - " + url, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            link.onClick(widget);
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0 && action == MotionEvent.ACTION_UP) {
                    handleLinkClick(link[0], widget);
                    return true;
                }
            }

            return super.onTouchEvent(widget, buffer, event);
        }

        public static MovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new DevotionalLinkHandler();

            return sInstance;
        }

        private static DevotionalLinkHandler sInstance;
    }
}
