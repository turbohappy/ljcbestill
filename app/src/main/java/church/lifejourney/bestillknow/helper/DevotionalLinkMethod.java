package church.lifejourney.bestillknow.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import church.lifejourney.bestillknow.activity.PassageActivity;

/**
 * Created by bdavis on 2/1/16.
 */
public class DevotionalLinkMethod implements MovementMethod {
    private Activity activity;

    public DevotionalLinkMethod(Activity activity){
        this.activity = activity;
    }

    @Override
    public void initialize(TextView widget, Spannable text) {
        Selection.removeSelection(text);
    }

    @Override
    public boolean onKeyDown(TextView widget, Spannable text, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(TextView widget, Spannable text, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyOther(TextView view, Spannable text, KeyEvent event) {
        return false;
    }

    @Override
    public void onTakeFocus(TextView widget, Spannable text, int direction) {

    }

    @Override
    public boolean onTrackballEvent(TextView widget, Spannable text, MotionEvent event) {
        return false;
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

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    handleLinkClick(link[0], widget);
                } else {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return false;
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
    public boolean onGenericMotionEvent(TextView widget, Spannable text, MotionEvent event) {
        return false;
    }

    @Override
    public boolean canSelectArbitrarily() {
        return false;
    }
}
