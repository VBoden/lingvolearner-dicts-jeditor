package ua.vboden.services;

import java.util.List;
import java.util.ResourceBundle;

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

	protected void setCategories(ObservableList<IdString> categories) {
		if (this.categories == null) {
			this.categories = categories;
		} else {
			this.categories.clear();
			this.categories.addAll(categories);
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

	public void setDictionaries(ObservableList<IdString> dictionaries) {
		if (this.dictionaries == null) {
			this.dictionaries = dictionaries;
		} else {
			this.dictionaries.clear();
			this.dictionaries.addAll(dictionaries);
		}
		this.dictionaries.add(0, new IdString(null, resources.getString("dictionary.noDictionary")));
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
