package ua.vboden.services;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.DictionaryData;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.dto.WordData;

@Service
public class SessionService {

	private ResourceBundle resources = ResourceBundle.getBundle("bundles/localization");

	private ObservableList<TranslationRow> translations;

	private List<Integer> translationIds;

	private ObservableList<CodeString> languages;

	private ObservableList<WordData> words;

	private ObservableList<IdString> categories;

	private ObservableList<IdString> dictionaries;

	private ObservableList<DictionaryData> dictionaryData;

	private Map<Integer, Integer> wordUsages;

	private CodeString defaultLanguageFrom;

	private CodeString defaultLanguageTo;

	private boolean displayDefaultLanguagesOnly;

	private boolean fillDefaultLanguages;

	private boolean showTranscription;

	private IdString lastSelectedDictionary;

	public void increaseUsages(int id) {
		if (wordUsages.containsKey(id)) {
			wordUsages.put(id, wordUsages.get(id) + 1);
		} else {
			wordUsages.put(id, 1);
		}
	}

	public void decreaseUsages(int id) {
		if (wordUsages.containsKey(id)) {
			wordUsages.put(id, wordUsages.get(id) - 1);
		} else {
			wordUsages.put(id, 0);
		}
	}

	public ObservableList<TranslationRow> getTranslations() {
		return translations;
	}

	public void setTranslations(List<TranslationRow> translations) {
		translations.sort(TranslationRow.lastFirstComparator());
		if (this.translations == null) {
			this.translations = FXCollections.observableArrayList(translations);
		} else {
			this.translations.clear();
			this.translations.addAll(translations);
		}
	}

	public List<Integer> getTranslationIds() {
		return translationIds;
	}

	public void setTranslationIds(List<Integer> translationIds) {
		this.translationIds = translationIds;
	}

	public ObservableList<IdString> getCategoriesWithEmpty() {
		return categories;
	}

	public ObservableList<IdString> getCategories() {
		if (categories.get(0).getId() == null) {
			ObservableList<IdString> newCategories = FXCollections.observableArrayList(categories);
			newCategories.remove(0);
			return newCategories;
		}
		return categories;
	}

	protected void setCategories(List<IdString> categoryModels) {
		if (this.categories == null) {
			this.categories = FXCollections.observableArrayList(categoryModels);
		} else {
			this.categories.clear();
			this.categories.addAll(categoryModels);
		}
		this.categories.add(0, new IdString(null, resources.getString("category.noCategory")));
	}

	public ObservableList<IdString> getDictionariesWithEmpty() {
		return dictionaries;
	}

	public ObservableList<IdString> getDictionaries() {
		if (dictionaries.get(0).getId() == null) {
			ObservableList<IdString> newDictionaries = FXCollections.observableArrayList(dictionaries);
			newDictionaries.remove(0);
			return newDictionaries;
		}
		return dictionaries;
	}

	public void setDictionaries(List<IdString> dictionaries) {
		if (this.dictionaries == null) {
			this.dictionaries = FXCollections.observableArrayList(dictionaries);
		} else {
			this.dictionaries.clear();
			this.dictionaries.addAll(dictionaries);
		}
		this.dictionaries.add(0, new IdString(null, resources.getString("dictionary.noDictionary")));
	}

	public ObservableList<DictionaryData> getDictionaryData() {
		return dictionaryData;
	}

	public void setDictionaryData(List<DictionaryData> dictionaryData) {
		this.dictionaryData = FXCollections.observableArrayList(dictionaryData);
	}

	public ObservableList<CodeString> getLanguages() {
		return languages;
	}

	public void setLanguages(List<CodeString> languages) {
		this.languages = FXCollections.observableArrayList(languages);
	}

	public ObservableList<WordData> getWords() {
		return words;
	}

	public void setWords(List<WordData> models) {
		this.words = FXCollections.observableArrayList(models);
	}

	public Map<Integer, Integer> getWordUsages() {
		return wordUsages;
	}

	public void setWordUsages(Map<Integer, Integer> wordUsages) {
		this.wordUsages = wordUsages;
	}

	public CodeString getDefaultLanguageFrom() {
		return defaultLanguageFrom;
	}

	public void setDefaultLanguageFrom(CodeString defaultLanguageFrom) {
		this.defaultLanguageFrom = defaultLanguageFrom;
	}

	public CodeString getDefaultLanguageTo() {
		return defaultLanguageTo;
	}

	public void setDefaultLanguageTo(CodeString defaultLanguageTo) {
		this.defaultLanguageTo = defaultLanguageTo;
	}

	public boolean isDisplayDefaultLanguagesOnly() {
		return displayDefaultLanguagesOnly;
	}

	public void setDisplayDefaultLanguagesOnly(boolean displayDefaultLanguagesOnly) {
		this.displayDefaultLanguagesOnly = displayDefaultLanguagesOnly;
	}

	public boolean isFillDefaultLanguages() {
		return fillDefaultLanguages;
	}

	public void setFillDefaultLanguages(boolean fillDefaultLanguages) {
		this.fillDefaultLanguages = fillDefaultLanguages;
	}

	public IdString getLastSelectedDictionary() {
		return lastSelectedDictionary;
	}

	public void setLastSelectedDictionary(IdString lastSelectedDictionary) {
		this.lastSelectedDictionary = lastSelectedDictionary;
	}

	public boolean isShowTranscription() {
		return showTranscription;
	}

	public void setShowTranscription(boolean showTranscription) {
		this.showTranscription = showTranscription;
	}

}
