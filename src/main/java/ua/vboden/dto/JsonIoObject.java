package ua.vboden.dto;

import java.util.List;
import java.util.Set;

import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Language;
import ua.vboden.entities.Word;

public class JsonIoObject {

	private Set<Language> languages;
	private List<Word> words;
	private List<DictionaryEntry> entries;

	public Set<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public List<DictionaryEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DictionaryEntry> entries) {
		this.entries = entries;
	}

}
