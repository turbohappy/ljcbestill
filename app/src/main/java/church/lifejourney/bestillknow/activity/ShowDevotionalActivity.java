package church.lifejourney.bestillknow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.db.Devotional;
import church.lifejourney.bestillknow.db.DevotionalDb;
import church.lifejourney.bestillknow.db.Passage;
import church.lifejourney.bestillknow.db.PassageDb;
import church.lifejourney.bestillknow.download.LoadPassageTask;
import church.lifejourney.bestillknow.download.Translation;
import church.lifejourney.bestillknow.download.URLImageGetter;
import church.lifejourney.bestillknow.helper.DevotionalLinkMethod;
import church.lifejourney.bestillknow.helper.DevotionalTagHandler;
import church.lifejourney.bestillknow.helper.Logger;

public class ShowDevotionalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
		LoadPassageTask.LoadPassageListener {
	private DevotionalDb devotionalDb;
	private PassageDb passageDb;
	private List<Passage> passages;
	private Devotional devotional;
	private TextSwitcher passageContent;

	public ShowDevotionalActivity() {
		devotionalDb = new DevotionalDb();
		passageDb = new PassageDb();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_devotional);

		String guid = getIntent().getStringExtra("guid");
		devotional = devotionalDb.readDevotional(guid);

		setupToolbar(devotional);

		if (devotional.getIntro() != null) {
			setupIntroSection(devotional.getIntro(), findViewById(R.id.dev_intro_view));
		} else {
			findViewById(R.id.dev_intro_view).setVisibility(View.GONE);
		}
		if (devotional.getLinkStub() != null) {
			setupPassageSection(devotional, findViewById(R.id.dev_passage_view));
		} else {
			findViewById(R.id.dev_passage_view).setVisibility(View.GONE);
		}
		setupContentSection(devotional.getContent(), findViewById(R.id.dev_content_view));


		devotionalDb.markDevotionalRead(devotional);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		devotionalDb.close();
		passageDb.close();
	}

	private void setupToolbar(Devotional dev) {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(dev.getTitle());
		setSupportActionBar(toolbar);
		assert getSupportActionBar() != null;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setupIntroSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setupPassageSection(Devotional dev, View view) {
		TextView verses = (TextView) view.findViewById(R.id.dev_passage_verses);
		verses.setText(dev.getPassages());

		this.passages = passageDb.readPassages(dev.getGuid());

		passageContent = (TextSwitcher) view.findViewById(R.id.dev_passage_content);
		passageContent.setFactory(new ViewSwitcher.ViewFactory() {
			public View makeView() {
				TextView myText = new TextView(ShowDevotionalActivity.this);
				return myText;
			}
		});
		Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		passageContent.setInAnimation(in);

		Spinner transSelector = (Spinner) view.findViewById(R.id.dev_passage_translation_selector);
		ArrayAdapter<Translation> transSelAdapter = new ArrayAdapter<>(this, android.R.layout
				.simple_spinner_item, Translation.values());
		transSelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		transSelector.setAdapter(transSelAdapter);
		transSelector.setOnItemSelectedListener(this);
	}

	private void setupContentSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setContent(String html, TextView target) {
		target.setText(Html.fromHtml(html, new URLImageGetter(target, this), new DevotionalTagHandler()));
		target.setMovementMethod(new DevotionalLinkMethod(this));
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Translation translation = Translation.values()[position];
		setTranslation(translation);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		passageContent.setText("");
	}

	private Translation currTranslation;

	public void setTranslation(Translation translation) {
		currTranslation = translation;
		String currTransCode = translation.getCode();
		if (currTransCode != null) {
			for (Passage passage : passages) {
				if (currTransCode.equals(passage.getTranslation())) {
					passageContent.setText(Html.fromHtml(passage.getContent()));
					return;
				}
			}
			//guess we didn't have it already, get it
			new LoadPassageTask(this).execute(devotional.getLinkStub() + currTranslation);
		} else {
			Logger.error(this, "No translation specified", null);
			passageContent.setText("");
		}
	}

	@Override
	public void loaded(String passageText) {
		passageContent.setText(Html.fromHtml(passageText));

		// store in db
		String devGuid =  devotional.getGuid();
		String translation = currTranslation.getCode();
		Passage passage = new Passage();
		passage.setDevotionalId(devGuid);
		passage.setTranslation(translation);
		passage.setContent(passageText);
		PassageDb passageDb = new PassageDb();
		passageDb.savePassage(passage);
		passageDb.close();
	}
}
