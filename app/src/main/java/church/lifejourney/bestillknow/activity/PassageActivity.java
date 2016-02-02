package church.lifejourney.bestillknow.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.download.LoadPassageTask;

public class PassageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage);

        String passageUrl = getIntent().getStringExtra("passageUrl");

        TextView contentView = (TextView) findViewById(R.id.passage_text);
        new LoadPassageTask(contentView).execute(passageUrl);
    }
}
