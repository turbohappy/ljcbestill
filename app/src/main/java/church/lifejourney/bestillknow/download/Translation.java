package church.lifejourney.bestillknow.download;

/**
 * Created by bdavis on 2/1/16.
 */
public enum Translation {
	NRSV("NRSV", "NRSV"), MSG("MSG", "The Message"), KJV("KJV", "King James");

	private String code;
	private String name;

	Translation(String code, String name) {
		this.code = code;

		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}