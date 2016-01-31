package church.lifejourney.bestillknow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import church.lifejourney.bestillknow.download.HtmlTask;

public class ShowDevotionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_devotional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView contentView = (TextView) findViewById(R.id.dev_content_view);

        String url = getIntent().getStringExtra("url");
        new HtmlTask(contentView).execute(url);

//        String content = getIntent().getStringExtra("content");
//        contentView.setText(Html.fromHtml(content));
//        contentView.setMovementMethod(new ScrollingMovementMethod());
    }
}
