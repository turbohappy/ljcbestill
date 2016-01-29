package church.lifejourney.bestillknow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import church.lifejourney.bestillknow.download.HtmlTask;

public class ShowDevotionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_devotional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView webView = (WebView) findViewById(R.id.dev_web_view);
        webView.setWebViewClient(new WebViewClient());
        String url = getIntent().getStringExtra("url");
        new HtmlTask(webView).execute(url);
    }
}
