package church.lifejourney.bestillknow.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bdavis on 2/1/16.
 */
public class DevotionalContentParser {

	public Sections splitContent(String content) {
		Sections output = new Sections();
		Matcher scriptureSection = findScriptureSection(content);
		if (scriptureSection == null) {
			output.content = content;
		} else {
			output.intro = content.substring(0, scriptureSection.start());
			parseScriptureSection(content.substring(scriptureSection.start(),
					scriptureSection.end()), output);
			output.content = content.substring(scriptureSection.end());
		}
		return output;
	}

	private Matcher findScriptureSection(String content) {
		Matcher matcher = Pattern.compile("<p><strong>Today[^:]+:</strong>[^\\(]+(\\([^\\)]+\\)[ ]*)+[^<]*</p>").matcher(content);
		if (matcher.find()) {
			return matcher;
		} else {
			Logger.error(this, "Couldn't find scripture section in [" + content + "]", null);
			return null;
		}
	}

	private void parseScriptureSection(String scriptureSection, Sections
			sections) {
		Matcher matcher = Pattern.compile("</strong>([^\\(]+)(\\([^\\)]+\\)[ ]*)+[^<]*</p>").matcher(scriptureSection);
		if (matcher.find()) {
			sections.passages = matcher.group(1);
		} else {
			Logger.error(this, "Couldn't find passages in [" + scriptureSection + "]", null);
			throw new RuntimeException("Couldn't find passages");
		}

		matcher = Pattern.compile("<a href=\"(http[s]*://www.biblegateway.com/passage/\\?search=[^&]+&amp;version=)").matcher(scriptureSection);
		if (matcher.find()) {
			sections.linkStub = matcher.group(1).replaceAll("&amp;", "&");
		} else {
			Logger.error(this, "Couldn't find bg link in [" + scriptureSection + "]", null);
			throw new RuntimeException("Couldn't find BibleGateway link");
		}
	}

	public static class Sections {
		public String intro = "";
		public String passages = "";
		public String linkStub = "";
		public String content = "";
	}
}
