package ua.vboden.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.entities.Word;

public interface WordRepository extends CrudRepository<Word, Long> {

}
