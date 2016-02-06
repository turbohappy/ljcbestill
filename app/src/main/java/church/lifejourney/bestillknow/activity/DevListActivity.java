package church.lifejourney.bestillknow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.List;

import church.lifejourney.bestillknow.BuildConfig;
import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.db.Devotional;
import church.lifejourney.bestillknow.db.DevotionalDb;
import church.lifejourney.bestillknow.download.LoadDevotionalTask;
import church.lifejourney.bestillknow.download.RSSItem;
import church.lifejourney.bestillknow.helper.Logger;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DevListActivity extends AppCompatActivity implements LoadDevotionalTask
		.RSSItemsListener {
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private LinearLayoutManager mLayoutManager;
	private DevotionalDb devotionalDb;
	List<Devotional> devotionals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeCrashlytics();
		initializeRealm();
		devotionalDb = new DevotionalDb();

		setContentView(R.layout.activity_dev_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mRecyclerView = (RecyclerView) findViewById(R.id.dev_recycler_view);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
		devotionals = devotionalDb.readDevotionals();
		mAdapter = new DevListAdapter(this, devotionals);
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

	private void initializeCrashlytics() {
		Crashlytics crashlyticsKit = new Crashlytics.Builder()
				.core(new CrashlyticsCore.Builder().disabled(BuildConfig
						.DEBUG).build())
				.build();
		Fabric.with(this, crashlyticsKit);
	}

	private void initializeRealm() {
		RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).name(RealmConfiguration
				.DEFAULT_REALM_NAME).build();
		//TODO:		Realm.deleteRealm(realmConfig);
		Realm.setDefaultConfiguration(realmConfig);
		Realm realm = Realm.getDefaultInstance();
		Logger.debug(this, "Realm DB is at " + realm.getPath());
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
		return (int) Math.floor(mLayoutManager.getItemCount() * 1.0 / PAGE_SIZE);
	}

	@Override
	public void itemsReturned(List<RSSItem> items) {
		Logger.debug(this, "Loaded " + items.size() + " items");
		for (RSSItem item : items) {
			devotionalDb.saveOrGetStored(item);
		}
		mAdapter.notifyItemInserted(devotionals.size() - 1);
		loading = false;
	}
}
