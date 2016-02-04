package church.lifejourney.bestillknow.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by bdavis on 2/3/16.
 */
public class Devotional extends RealmObject {
	@PrimaryKey
	private String guid;
	private String intro;
	@Required
	private String content;
	private String linkStub;
	private String passages;
	private String title;
	private String creator;
	private Date pubDate;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLinkStub() {
		return linkStub;
	}

	public void setLinkStub(String linkStub) {
		this.linkStub = linkStub;
	}

	public String getPassages() {
		return passages;
	}

	public void setPassages(String passages) {
		this.passages = passages;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
}
