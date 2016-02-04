package church.lifejourney.bestillknow.helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.db.Passage;
import church.lifejourney.bestillknow.db.PassageDb;
import church.lifejourney.bestillknow.download.LoadPassageTask;

/**
 * Created by bdavis on 2/1/16.
 */
public class PassageFragment extends Fragment implements LoadPassageTask.LoadPassageListener {
	private static final String ARG_CONTENT = "the_content";
	private static final String ARG_GUID = "the_guid";
	private static final String ARG_LINKSTUB = "the_linkstub";
	private static final String ARG_TRANSLATION = "the_translation";

	public PassageFragment() {
	}

	public static PassageFragment newInstance(String content) {
		PassageFragment fragment = new PassageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_CONTENT, content);
		fragment.setArguments(args);
		return fragment;
	}

	public static PassageFragment newInstance(String devGuid, String devLinkstub, String translation) {
		PassageFragment fragment = new PassageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_GUID, devGuid);
		args.putString(ARG_LINKSTUB, devLinkstub);
		args.putString(ARG_TRANSLATION, translation);
		fragment.setArguments(args);
		return fragment;
	}

	private TextView passageTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_page_layout, container, false);

		passageTextView = (TextView) rootView.findViewById(R.id.dev_passage_text);

		String content = getArguments().getString(ARG_CONTENT);
		if(content!=null){
			passageTextView.setText(Html.fromHtml(content));
		}else{
			String devLinkstub = getArguments().getString(ARG_LINKSTUB);
			String translation = getArguments().getString(ARG_TRANSLATION);
			new LoadPassageTask(this).execute(devLinkstub + translation);
		}

		return rootView;
	}

	private String devGuid;

	@Override
	public void loaded(String passageText) {
		passageTextView.setText(Html.fromHtml(passageText));

		// store in db
		String devGuid = getArguments().getString(ARG_GUID);
		String translation = getArguments().getString(ARG_TRANSLATION);
		Passage passage = new Passage();
		passage.setDevotionalId(devGuid);
		passage.setTranslation(translation);
		passage.setContent(passageText);
		new PassageDb().savePassage(passage);
	}
}