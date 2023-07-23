package ua.vboden.dto;

public class TranslationRow {

	private int recordId;
	private int number;
	private String word;
	private String translation;
	private String categories;
	private String dictionaries;

	public TranslationRow() {
		super();
	}

	public TranslationRow(int recordId, String word, String translation) {
		super();
		this.recordId = recordId;
		this.word = word;
		this.translation = translation;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(String dictionaries) {
		this.dictionaries = dictionaries;
	}

}
