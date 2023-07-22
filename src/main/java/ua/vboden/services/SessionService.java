package ua.vboden.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.vboden.converters.TranslationConverter;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
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

	private ObservableList<IdString> categories;

	private ObservableList<IdString> dictionaries;

	public void loadData() {
		loadTranslations();
		loadCategories();
		loadDictionaries();
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

	public void loadDictionaries() {
		dictionaries = FXCollections.observableArrayList();
		dictionaryRepository.findAll().forEach(entry -> dictionaries.add(new IdString(entry.getId(), entry.getName()
				+ " (" + entry.getLanguageFrom().getName() + "-" + entry.getLanguageTo().getName() + ")")));
	}

	public ObservableList<TranslationRow> getTranslations() {
		return translations;
	}

	public ObservableList<IdString> getCategories() {
		return categories;
	}

	public ObservableList<IdString> getDictionaries() {
		return dictionaries;
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
//		entryService.getAllEntries().forEach(entry -> translations.add(translationConverter.convert(entry)));
	}

}
