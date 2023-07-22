package ua.vboden.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TranslationRow {

	private long recordId;
	private BooleanProperty selected;
	private String word;
	private String translation;
	
	public TranslationRow() {
		super();
	}

	public TranslationRow(long recordId, String word, String translation) {
		super();
		this.recordId = recordId;
		this.word = word;
		this.translation = translation;
		this.selected = new SimpleBooleanProperty(false);
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public BooleanProperty getSelected() {
		return selected;
	}

	public void setSelected(BooleanProperty selected) {
		this.selected = selected;
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

}
