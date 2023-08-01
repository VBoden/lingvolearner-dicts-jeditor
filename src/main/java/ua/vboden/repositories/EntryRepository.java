package ua.vboden.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Word;

public interface EntryRepository extends CrudRepository<DictionaryEntry, Integer> {

	List<DictionaryEntry> findByWordCategoryId(Integer catogoryId);

	List<DictionaryEntry> findByWordCategoryIdIn(List<Integer> catogoryIds);

	List<DictionaryEntry> findByDictionaryId(Integer dictionaryId);

	List<DictionaryEntry> findByDictionaryIdIn(List<Integer> dictionaryIds);

	List<DictionaryEntry> findByWordWordContaining(String word);

	List<DictionaryEntry> findByTranslationWordContaining(String word);

	List<DictionaryEntry> findByWord(Word word);

	List<DictionaryEntry> findByTranslation(Word word);
}
