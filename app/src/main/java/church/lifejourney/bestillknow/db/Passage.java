package church.lifejourney.bestillknow.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by bdavis on 2/3/16.
 */
public class Passage extends RealmObject {
	private String devotionalId;
	private String translation;
	private String content;

	public String getDevotionalId() {
		return devotionalId;
	}

	public void setDevotionalId(String devotionalId) {
		this.devotionalId = devotionalId;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
