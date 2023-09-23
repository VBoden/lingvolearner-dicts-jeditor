package ua.vboden.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		long start = System.nanoTime();
		List<DictionaryEntry> allEntries = filterByLanguages(getAllEntries());
		System.out.println("\n\n=================time=\n" + ((System.nanoTime() - start) / 1000));
		System.out.println("\n\n=================time=\n");
		sessionService.setTranslations(translationConverter.convertAll(allEntries));
		sessionService.setTranslationIds(allEntries.stream().map(entry -> entry.getId()).collect(Collectors.toList()));
		sessionService.setWordUsages(new HashMap<>());
		for (DictionaryEntry entry : allEntries) {
			sessionService.increaseUsages(entry.getWord().getId());
			sessionService.increaseUsages(entry.getTranslation().getId());
		}
	}

	private List<DictionaryEntry> getAllEntries() {
		return (List<DictionaryEntry>) entryRepository.findAll();
	}

	public void loadTranslationsByCategories(List<Integer> selectedIds, boolean conditionAnd) {
		List<DictionaryEntry> allEntries = filterByLanguages(getAllByCategoryIds(selectedIds, conditionAnd));
		sessionService.setTranslations(translationConverter.convertAll(allEntries));
		sessionService.setTranslationIds(allEntries.stream().map(entry -> entry.getId()).collect(Collectors.toList()));
	}

	private List<DictionaryEntry> getAllByCategoryIds(List<Integer> selectedIds, boolean conditionAnd) {
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

	public List<DictionaryEntry> getAllByCategoryId(Integer categoryId) {
		if (categoryId == null) {
			return entryRepository.findByWordCategoryIsEmpty();
		} else {
			return entryRepository.findByWordCategoryId(categoryId);
		}
	}

	public void loadTranslationsByDictionaries(List<Integer> selectedIds, boolean conditionAnd) {
		List<DictionaryEntry> allEntries = filterByLanguages(getAllByDictionaryIds(selectedIds, conditionAnd));
		sessionService.setTranslations(translationConverter.convertAll(allEntries));
		sessionService.setTranslationIds(allEntries.stream().map(entry -> entry.getId()).collect(Collectors.toList()));
	}

	private List<DictionaryEntry> getAllByDictionaryIds(List<Integer> selectedIds, boolean conditionAnd) {
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

	public List<DictionaryEntry> getAllByDictionaryId(Integer dictionaryId) {
		if (dictionaryId == null) {
			return entryRepository.findByDictionaryIsEmpty();
		} else {
			return entryRepository.findByDictionaryId(dictionaryId);
		}
	}

	public List<TranslationRow> getAllByWord(String word) {
		return translationConverter.convertAll(filterByLanguages(entryRepository.findByWordWordContaining(word)));
	}

	private List<DictionaryEntry> filterByLanguages(List<DictionaryEntry> result) {
		if (sessionService.isDisplayDefaultLanguagesOnly()) {
			return result.stream()
					.filter(row -> row.getWord().getLanguage().getCode()
							.equals(sessionService.getDefaultLanguageFrom().getCode())
							&& row.getTranslation().getLanguage().getCode()
									.equals(sessionService.getDefaultLanguageTo().getCode()))
					.collect(Collectors.toList());
		}
		return result;
	}

	public void loadTranslations(List<Integer> ids) {
		sessionService.setTranslations(translationConverter.convertAll(getAllById(ids)));
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
		return translationConverter
				.convertAll(filterByLanguages(entryRepository.findByTranslationWordContaining(word)));
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

	@Override
	public void saveAll(List<DictionaryEntry> entities) {
		entryRepository.saveAll(entities);
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
