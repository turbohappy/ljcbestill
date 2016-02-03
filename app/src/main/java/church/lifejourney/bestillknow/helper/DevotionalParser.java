package church.lifejourney.bestillknow.helper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import church.lifejourney.bestillknow.db.Devotional;
import church.lifejourney.bestillknow.download.Item;

/**
 * Created by bdavis on 2/3/16.
 */
public class DevotionalParser {
	public Devotional parse(Item item) {
		Devotional dev = new Devotional();
		dev.setGuid(item.getGuid());
		dev.setTitle(item.getTitle());
		dev.setCreator(item.getCreator());
		dev.setPubDate(item.getPubDate());
		splitContentAndSetSections(dev, item.getContent());

		return dev;
	}

	private void splitContentAndSetSections(Devotional dev, String content) {
		try {
			Matcher scriptureSection = findScriptureSection(content);
			if (scriptureSection != null) {
				parseScriptureSection(dev, content.substring(scriptureSection.start(), scriptureSection.end()));
				dev.setIntro(content.substring(0, scriptureSection.start()));
				dev.setContent(content.substring(scriptureSection.end()));
				return;
			}
		} catch (Exception e) {
			//this is OK, just fall through
		}

		//default if we couldn't parse properly
		dev.setContent(content);
	}

	private Matcher findScriptureSection(String content) {
		Matcher matcher = Pattern.compile("<p[^<]+<strong>Today[^<]+</strong>[^\\(]+(\\([^\\)]+\\)[ ]*)+[^<]*</p>")
				.matcher(content);
		if (matcher.find()) {
			return matcher;
		} else {
			Logger.error(this, "Couldn't find scripture section in [" +
					content + "]", null);
			return null;
		}
	}

	private void parseScriptureSection(Devotional dev, String
			scriptureSection) throws IOException {
		Matcher matcher = Pattern.compile("</strong>([^\\(]+)(\\([^\\)]+\\)[ ]*)+[^<]*</p>").matcher(scriptureSection);
		if (matcher.find()) {
			dev.setPassages(matcher.group(1));
		} else {
			Logger.error(this, "Couldn't find passages in [" +
					scriptureSection + "]", null);
			throw new IOException("Couldn't find passages");
		}

		matcher = Pattern.compile("(www.biblegateway.com/passage/\\?search=[^&]+&amp;version=)")
				.matcher
						(scriptureSection);
		if (matcher.find()) {
			dev.setLinkStub("http://" + matcher.group(1).replaceAll("&amp;", "&"));
		} else {
			Logger.error(this, "Couldn't find bg link in [" + scriptureSection
					+ "]", null);
			throw new IOException("Couldn't find BibleGateway link");
		}
	}
}
