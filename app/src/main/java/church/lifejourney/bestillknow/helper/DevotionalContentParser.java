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
			output.before = "";
			output.scripture = "";
			output.after = content;
		} else {
			output.before = content.substring(0, scriptureSection.start());
			output.scripture = content.substring(scriptureSection.start(), scriptureSection.end());
			output.after = content.substring(scriptureSection.end());
		}
		return output;
	}

	private Matcher findScriptureSection(String content) {
		Matcher matcher = Pattern.compile("<p><strong>Today[^:]+:</strong>[^\\(]+(\\([^\\)]+\\)[ ]*)+[^<]+</p>").matcher(content);
		if (matcher.find()) {
			return matcher;
		} else {
			return null;
		}
	}

	public String findBGLink(String content) {
		Matcher matcher = Pattern.compile("<a href=\"(http://www.biblegateway.com/passage/\\?search=[^&]+&amp;version=)").matcher(content);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			throw new RuntimeException("Couldn't find BibleGateway link");
		}
	}

	public ScriptureSections parseScriptureSection(String content) {
		return null;
	}

	public static class Sections {
		public String before;
		public String scripture;
		public String after;
	}

	public static class ScriptureSections {
		public String passages;
		public String linkStub;
	}
}
