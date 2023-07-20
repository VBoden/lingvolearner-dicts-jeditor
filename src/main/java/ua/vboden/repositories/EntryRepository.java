package ua.vboden.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.entities.DictionaryEntry;

public interface EntryRepository extends CrudRepository<DictionaryEntry, Integer> {

}
