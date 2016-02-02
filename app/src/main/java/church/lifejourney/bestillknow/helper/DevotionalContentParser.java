package church.lifejourney.bestillknow.helper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bdavis on 2/1/16.
 */
public class DevotionalContentParser {

	public Sections splitContent(String content) {
		Sections output = new Sections();
		Matcher scriptureSection = findScriptureSection(content);
		if (scriptureSection != null) {
			try {
				parseScriptureSection(content.substring(scriptureSection.start(),
						scriptureSection.end()), output);
				output.intro = content.substring(0, scriptureSection.start());
				output.content = content.substring(scriptureSection.end());
				return output;
			} catch (IOException e) {
				//this is OK, just fall through
			}
		}

		//default if we couldn't parse properly
		output.content = content;
		return output;
	}

	private Matcher findScriptureSection(String content) {
		Matcher matcher = Pattern.compile("<p[^<]+<strong>Today[^<]+</strong>[^\\(]+(\\([^\\)]+\\)[ ]*)+[^<]*</p>").matcher(content);
		if (matcher.find()) {
			return matcher;
		} else {
			Logger.error(this, "Couldn't find scripture section in [" + content + "]", null);
			return null;
		}
	}

	private void parseScriptureSection(String scriptureSection, Sections
			sections) throws IOException {
		Matcher matcher = Pattern.compile("</strong>([^\\(]+)(\\([^\\)]+\\)[ ]*)+[^<]*</p>").matcher(scriptureSection);
		if (matcher.find()) {
			sections.passages = matcher.group(1);
		} else {
			Logger.error(this, "Couldn't find passages in [" + scriptureSection + "]", null);
			throw new IOException("Couldn't find passages");
		}

		matcher = Pattern.compile("<a href=\"(http[s]*://www.biblegateway.com/passage/\\?search=[^&]+&amp;version=)").matcher(scriptureSection);
		if (matcher.find()) {
			sections.linkStub = matcher.group(1).replaceAll("&amp;", "&");
		} else {
			Logger.error(this, "Couldn't find bg link in [" + scriptureSection + "]", null);
			throw new IOException("Couldn't find BibleGateway link");
		}
	}

	public static class Sections {
		public String intro = null;
		public String passages = null;
		public String linkStub = null;
		public String content = null;
	}
}
