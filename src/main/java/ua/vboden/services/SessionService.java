package ua.vboden.services;

import java.util.List;

import org.springframework.stereotype.Service;

import javafx.collections.ObservableList;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.DictionaryData;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.dto.WordData;

@Service
public class SessionService {

	private ObservableList<TranslationRow> translations;

	private List<Integer> translationIds;

	private ObservableList<CodeString> languages;

	private ObservableList<WordData> words;

	private ObservableList<IdString> categories;

	private ObservableList<IdString> dictionaries;

	private ObservableList<DictionaryData> dictionaryData;

	public ObservableList<TranslationRow> getTranslations() {
		return translations;
	}

	public void setTranslations(ObservableList<TranslationRow> translations) {
		this.translations = translations;
	}

	public List<Integer> getTranslationIds() {
		return translationIds;
	}

	public void setTranslationIds(List<Integer> translationIds) {
		this.translationIds = translationIds;
	}

	public ObservableList<IdString> getCategories() {
		return categories;
	}

	protected void setCategories(ObservableList<IdString> categories) {
		this.categories = categories;
	}

	public ObservableList<IdString> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(ObservableList<IdString> dictionaries) {
		this.dictionaries = dictionaries;
	}

	public ObservableList<DictionaryData> getDictionaryData() {
		return dictionaryData;
	}

	public void setDictionaryData(ObservableList<DictionaryData> dictionaryData) {
		this.dictionaryData = dictionaryData;
	}

	public ObservableList<CodeString> getLanguages() {
		return languages;
	}

	public void setLanguages(ObservableList<CodeString> languages) {
		this.languages = languages;
	}

	public ObservableList<WordData> getWords() {
		return words;
	}

	public void setWords(ObservableList<WordData> words) {
		this.words = words;
	}

}
