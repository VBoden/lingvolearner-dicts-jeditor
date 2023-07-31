package ua.vboden.dto;

import org.apache.commons.lang3.StringUtils;

public class WordData implements Comparable<WordData> {
	private int id;
	private String word;
	private String notes;
	private String language;
	private String categories;
	private int usages;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public int getUsages() {
		return usages;
	}

	public void setUsages(int usages) {
		this.usages = usages;
	}

	@Override
	public int compareTo(WordData another) {
		return word.compareTo(another.word);
	}

	@Override
	public String toString() {
		String notesPart = StringUtils.isNotBlank(notes) ? " (" + notes + ")" : "";
		return word + notesPart + " - " + language + " - " + categories.replaceAll("\n", ";");
	}

}
