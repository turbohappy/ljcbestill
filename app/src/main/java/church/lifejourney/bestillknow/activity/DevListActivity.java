package church.lifejourney.bestillknow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.helper.VerticalBoundsScrollListener;
import church.lifejourney.bestillknow.download.RSSList;

public class DevListActivity extends AppCompatActivity implements RSSList.RSSListUpdatedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RSSList rssList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        this.rssList = new RSSList(this);

        // specify an adapter (see also next example)
        mAdapter = new DevListAdapter(this, rssList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new VerticalBoundsScrollListener() {
            @Override
            public void onScrolledToTop() {
                // don't care
            }

            @Override
            public void onScrolledToBottom() {
                if(!rssList.loading()) {
                    rssList.loadAnotherPage();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_dev_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void listUpdated() {
        mAdapter.notifyItemInserted(rssList.size() - 1);
    }
}
