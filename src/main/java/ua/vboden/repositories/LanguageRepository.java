package ua.vboden.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.entities.Language;

public interface LanguageRepository extends CrudRepository<Language, Integer> {

	Language findByCode(String code);

}
