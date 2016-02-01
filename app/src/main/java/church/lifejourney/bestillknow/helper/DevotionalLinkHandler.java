package church.lifejourney.bestillknow.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import church.lifejourney.bestillknow.activity.PassageActivity;

/**
 * Created by bdavis on 2/1/16.
 */
public class DevotionalLinkHandler extends LinkMovementMethod {
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
