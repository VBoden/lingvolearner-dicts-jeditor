package ua.vboden.dto;

import java.util.Comparator;

public class TranslationRow {

	private int recordId;
	private int number;
	private String word;
	private String wordLangCode;
	private int wordId;
	private String translation;
	private String translationLangCode;
	private int translationId;
	private String transcription;
	private String categories;
	private String transCategories;
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

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getTransCategories() {
		return transCategories;
	}

	public void setTransCategories(String transCategories) {
		this.transCategories = transCategories;
	}

	public String getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(String dictionaries) {
		this.dictionaries = dictionaries;
	}

	public static Comparator<? super TranslationRow> lastFirstComparator() {
		return (a, b) -> b.getNumber() - a.getNumber();
	}

	public static Comparator<? super TranslationRow> lastAtEndComparator() {
		return (a, b) -> a.getNumber() - b.getNumber();
	}

	public String getWordLangCode() {
		return wordLangCode;
	}

	public void setWordLangCode(String wordLangCode) {
		this.wordLangCode = wordLangCode;
	}

	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public String getTranslationLangCode() {
		return translationLangCode;
	}

	public void setTranslationLangCode(String translationLangCode) {
		this.translationLangCode = translationLangCode;
	}

	public int getTranslationId() {
		return translationId;
	}

	public void setTranslationId(int translationId) {
		this.translationId = translationId;
	}

	@Override
	public String toString() {
		return word.split("\n")[0] + " - " + translation
				+ (categories != null ? " (" + categories.replaceAll("\n", "; ") + ")" : "");
	}

}
