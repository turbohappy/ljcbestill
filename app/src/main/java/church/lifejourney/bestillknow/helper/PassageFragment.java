package church.lifejourney.bestillknow.helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.download.LoadPassageTask;

/**
 * Created by bdavis on 2/1/16.
 */
public class PassageFragment extends Fragment {
	private static final String ARG_URL = "the_url";

	public PassageFragment() {
	}

	public static PassageFragment newInstance(String url) {
		PassageFragment fragment = new PassageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_URL, url);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_page_layout, container, false);

		TextView txt = (TextView) rootView.findViewById(R.id.dev_passage_text);
		String url = getArguments().getString(ARG_URL);
		new LoadPassageTask(txt).execute(url);

		return rootView;
	}
}