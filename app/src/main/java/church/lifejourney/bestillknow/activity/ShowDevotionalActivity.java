package church.lifejourney.bestillknow.activity;

import android.app.Activity;
import android.content.Intent;
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

import org.jsoup.Jsoup;
import org.xml.sax.XMLReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.download.HtmlTask;
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
        content = fixContent(content);
        contentView.setText(Html.fromHtml(content, new DevotionalImageGetter(), new DevotionalTagHandler()));
        contentView.setMovementMethod(DevotionalLinkHandler.getInstance(this));
    }

    private String fixContent(String content) {

        Matcher matcher = Pattern.compile("(<a href=\"http://www.biblegateway.com/passage/\\?search=[^&]+&amp;version=)").matcher(content);
        if (matcher.find()) {
            String bgLink = matcher.group();
            String nrsvLink = bgLink + "NRSV";
            Matcher matcher2 = Pattern.compile("<a href=\"http://bible.oremus.org/\\?ql=[0-9]+").matcher(content);
            if (matcher2.find()) {
                content = matcher2.replaceAll(nrsvLink);
            }
        }
        return content;
    }

    private static class DevotionalTagHandler implements Html.TagHandler {

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if ((!"html".equals(tag)) && (!"body".equals(tag))) {
                Logger.debug(this, "need to handle tag [" + tag + "] [opening=" + opening + "]");
            }
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
        private Activity activity;

        public DevotionalLinkHandler(Activity activity) {

            this.activity = activity;
        }

        private void handleLinkClick(ClickableSpan link, TextView widget) {
            if (link instanceof URLSpan) {
                String url = ((URLSpan) link).getURL();
                if (url != null && (url.contains("biblegateway.com"))) {
                    Intent intent = new Intent(widget.getContext(), PassageActivity.class);
                    intent.putExtra("passageUrl", url);
                    activity.startActivity(intent);
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

        public static MovementMethod getInstance(Activity activity) {
            if (sInstance == null)
                sInstance = new DevotionalLinkHandler(activity);

            return sInstance;
        }

        private static DevotionalLinkHandler sInstance;
    }
}
