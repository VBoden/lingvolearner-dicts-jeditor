package ua.vboden.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import ua.vboden.converters.TranslationConverter;
import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.repositories.EntryRepository;

@Service
public class EntryService {

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private TranslationConverter translationConverter;

	public List<DictionaryEntry> getAllEntries() {
		List<DictionaryEntry> result = new ArrayList<>();
		entryRepository.findAll().forEach(result::add);
		return result;
	}

	public List<DictionaryEntry> getAllByCategoryIds(List<Integer> selectedIds, boolean conditionAnd) {
		if (conditionAnd) {
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

	public List<DictionaryEntry> getAllByDictionaryIds(List<Integer> selectedIds, boolean conditionAnd) {
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

	public List<TranslationRow> getAllByTranslation(String word) {
		return translationConverter.convertAll(entryRepository.findByTranslationWordContaining(word));
	}

}
