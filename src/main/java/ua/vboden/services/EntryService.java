package ua.vboden.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.vboden.converters.TranslationConverter;
import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Word;
import ua.vboden.repositories.EntryRepository;

@Service
public class EntryService implements EntityService<TranslationRow, DictionaryEntry> {

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private TranslationConverter translationConverter;

	@Autowired
	private SessionService sessionService;

	public void loadTranslations() {
		List<DictionaryEntry> allEntries = getAllEntries();
		sessionService.setTranslations(FXCollections.observableArrayList(translationConverter.convertAll(allEntries)));
		sessionService.setTranslationIds(allEntries.stream().map(entry -> entry.getId()).collect(Collectors.toList()));
	}

	public List<DictionaryEntry> getAllEntries() {
		List<DictionaryEntry> result = new ArrayList<>();
		entryRepository.findAll().forEach(result::add);
		return result;
	}

	public void loadTranslationsByCategories(List<Integer> selectedIds, boolean conditionAnd) {
		List<DictionaryEntry> allEntries = getAllByCategoryIds(selectedIds, conditionAnd);
		sessionService.setTranslations(FXCollections.observableArrayList(translationConverter.convertAll(allEntries)));
		sessionService.setTranslationIds(allEntries.stream().map(entry -> entry.getId()).collect(Collectors.toList()));
	}

	public List<DictionaryEntry> getAllByCategoryIds(List<Integer> selectedIds, boolean conditionAnd) {
		if (selectedIds == null || selectedIds.size() == 1 && selectedIds.get(0) == null) {
			return entryRepository.findByWordCategoryIsEmpty();
		} else if (conditionAnd) {
			boolean firstId = true;
			List<DictionaryEntry> result = new ArrayList<>();
			for (Integer id : selectedIds) {
				List<DictionaryEntry> resultOne = entryRepository.findByWordCategoryId(id);
				if (firstId) {
					result = resultOne;
					firstId = false;
				} else {
					result.retainAll(resultOne);
				}
			}
			return result;
		} else {
			return entryRepository.findByWordCategoryIdIn(selectedIds);
		}
	}

	public void loadTranslationsByDictionaries(List<Integer> selectedIds, boolean conditionAnd) {
		List<DictionaryEntry> allEntries = getAllByDictionaryIds(selectedIds, conditionAnd);
		sessionService.setTranslations(FXCollections.observableArrayList(translationConverter.convertAll(allEntries)));
		sessionService.setTranslationIds(allEntries.stream().map(entry -> entry.getId()).collect(Collectors.toList()));
	}

	public List<DictionaryEntry> getAllByDictionaryIds(List<Integer> selectedIds, boolean conditionAnd) {
		if (selectedIds == null || selectedIds.size() == 1 && selectedIds.get(0) == null) {
			return entryRepository.findByDictionaryIsEmpty();
		}
		if (conditionAnd) {
			boolean firstId = true;
			List<DictionaryEntry> result = new ArrayList<>();
			for (Integer id : selectedIds) {
				List<DictionaryEntry> resultOne = entryRepository.findByDictionaryId(id);
				if (firstId) {
					result = resultOne;
					firstId = false;
				} else {
					result.retainAll(resultOne);
				}
			}
			return result;
		} else {
			return entryRepository.findByDictionaryIdIn(selectedIds);
		}
	}

	public List<TranslationRow> getAllByWord(String word) {
		return translationConverter.convertAll(entryRepository.findByWordWordContaining(word));
	}

	public void loadTranslations(List<Integer> ids) {
		sessionService
				.setTranslations(FXCollections.observableArrayList(translationConverter.convertAll(getAllById(ids))));
		sessionService.setTranslationIds(ids);
	}

	public List<DictionaryEntry> getAllById(List<Integer> ids) {
		List<DictionaryEntry> result = new ArrayList<>();
		entryRepository.findAllById(ids).forEach(result::add);
		return result;
	}

	public TranslationRow getRowById(Integer id) {
		return translationConverter.convert(getById(id));
	}

	private DictionaryEntry getById(Integer id) {
		return entryRepository.findById(id).get();
	}

	public List<TranslationRow> getAllByTranslation(String word) {
		return translationConverter.convertAll(entryRepository.findByTranslationWordContaining(word));
	}

	@Override
	public void deleteSelected(ObservableList<? extends TranslationRow> selected) {
		entryRepository.deleteAllById(selected.stream().map(TranslationRow::getRecordId).collect(Collectors.toList()));
	}

	public List<DictionaryEntry> getAllBySelected(ObservableList<? extends TranslationRow> selected) {
		List<DictionaryEntry> result = new ArrayList<>();
		entryRepository.findAllById(selected.stream().map(TranslationRow::getRecordId).collect(Collectors.toList()))
				.forEach(result::add);
		return result;
	}

	@Override
	public DictionaryEntry findEntity(TranslationRow current) {
		return entryRepository.findById(current.getRecordId()).get();
	}

	@Override
	public void save(DictionaryEntry entity) {
		entryRepository.save(entity);
	}

	public int getWordUsages(Word source) {
		int inWord = getAllByWord(source).size();
		int inTranslations = getAllByTranslation(source).size();
		return inWord + inTranslations;
	}

	private List<DictionaryEntry> getAllByWord(Word source) {
		return entryRepository.findByWord(source);
	}

	private List<DictionaryEntry> getAllByTranslation(Word source) {
		return entryRepository.findByTranslation(source);
	}
}
