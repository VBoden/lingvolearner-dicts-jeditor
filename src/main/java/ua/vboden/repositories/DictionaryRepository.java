package ua.vboden.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.entities.Dictionary;

public interface DictionaryRepository extends CrudRepository<Dictionary, Integer> {

	Dictionary findByName(String name);

}
