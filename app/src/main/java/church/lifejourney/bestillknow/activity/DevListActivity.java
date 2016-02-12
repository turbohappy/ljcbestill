package church.lifejourney.bestillknow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.background.DevotionalUpdateAlarmReceiver;
import church.lifejourney.bestillknow.db.Devotional;
import church.lifejourney.bestillknow.db.DevotionalDb;
import church.lifejourney.bestillknow.download.LoadDevotionalTask;
import church.lifejourney.bestillknow.download.RSSItem;
import church.lifejourney.bestillknow.helper.Logger;
import io.realm.RealmChangeListener;

public class DevListActivity extends AppCompatActivity implements LoadDevotionalTask
		.LoadDevotionalListener {
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private LinearLayoutManager mLayoutManager;
	private DevotionalDb devotionalDb;
	List<Devotional> devotionals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DevotionalUpdateAlarmReceiver.schedule(this);

		setContentView(R.layout.activity_dev_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mRecyclerView = (RecyclerView) findViewById(R.id.dev_recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		devotionalDb = new DevotionalDb();
		devotionals = devotionalDb.readDevotionals();

		mAdapter = new DevListAdapter(this, devotionals);
		devotionalDb.addChangeListener(new RealmChangeListener() {
			@Override
			public void onChange() {
				Logger.debug(this, "Realm dataset changed");
				mAdapter.notifyDataSetChanged();
				mRecyclerView.invalidate();
			}
		});

		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (notEnoughDevotionalsLoaded()) {
					loadMoreDevotionals();
				}
			}
		});

		// make sure we have enough to fill the starting screen
		if (notEnoughDevotionalsLoaded()) {
			loadMoreDevotionals();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		devotionalDb.close();
	}

	private static final int visibleThreshold = 3;
	private boolean loading = false;

	private boolean notEnoughDevotionalsLoaded() {
		int totalItemCount = mLayoutManager.getItemCount();
		int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
		return (totalItemCount <= (lastVisibleItem + visibleThreshold));
	}

	public synchronized void loadMoreDevotionals() {
		if (!loading) {
			loading = true;
			new LoadDevotionalTask(this).execute(numDevotionalPagesAlreadyLoaded() + 1);
		}
	}

	private static final int PAGE_SIZE = 6;

	private int numDevotionalPagesAlreadyLoaded() {
		return (int) Math.floor(devotionals.size() * 1.0 / PAGE_SIZE);
	}

	@Override
	public void devotionalsLoaded(int page, List<RSSItem> items) {
		Logger.debug(this, "Loaded " + items.size() + " items");
		devotionalDb.saveIfNew(items, false);
		loading = false;

		if (notEnoughDevotionalsLoaded()) {
			loadMoreDevotionals();
		}
	}
}
