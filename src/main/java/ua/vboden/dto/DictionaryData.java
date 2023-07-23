package ua.vboden.dto;

public class DictionaryData {
	private int id;
	private String title;
	private String langFrom;
	private String langTo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLangFrom() {
		return langFrom;
	}

	public void setLangFrom(String langFrom) {
		this.langFrom = langFrom;
	}

	public String getLangTo() {
		return langTo;
	}

	public void setLangTo(String langTo) {
		this.langTo = langTo;
	}

}
