package ua.vboden.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.vboden.converters.TranslationConverter;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.DictionaryData;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.dto.WordData;
import ua.vboden.repositories.CategoryRepository;
import ua.vboden.repositories.DictionaryRepository;

@Service
public class SessionService {

	@Autowired
	private EntryService entryService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private DictionaryRepository dictionaryRepository;

	@Autowired
	private TranslationConverter translationConverter;

	private ObservableList<TranslationRow> translations;

	private ObservableList<CodeString> languages;

	private ObservableList<WordData> words;

	private ObservableList<IdString> categories;

	private ObservableList<IdString> dictionaries;

	private ObservableList<DictionaryData> dictionaryData;

	public void loadData() {
		loadTranslations();
		loadCategories();
	}

	public void loadTranslations() {
		translations = FXCollections.observableArrayList();
		translations.addAll(translationConverter.convertAll(entryService.getAllEntries()));
	}

	public void loadCategories() {
		List<IdString> categoryModels = new ArrayList<>();
		categoryRepository.findAll().forEach(entry -> categoryModels.add(new IdString(entry.getId(), entry.getName())));
		Collections.sort(categoryModels);
		categories = FXCollections.observableArrayList(categoryModels);
	}

	public ObservableList<TranslationRow> getTranslations() {
		return translations;
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

	public void loadTranslationsByCategories(List<Integer> selectedIds, boolean conditionAnd) {
		translations = FXCollections.observableArrayList();
		translations
				.addAll(translationConverter.convertAll(entryService.getAllByCategoryIds(selectedIds, conditionAnd)));
	}

	public void loadTranslationsByDictionaries(List<Integer> selectedIds, boolean conditionAnd) {
		translations = FXCollections.observableArrayList();
		translations
				.addAll(translationConverter.convertAll(entryService.getAllByDictionaryIds(selectedIds, conditionAnd)));
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
