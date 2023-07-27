package ua.vboden.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.entities.Word;

public interface WordRepository extends CrudRepository<Word, Integer> {

	List<Word> findByWordContaining(String word);

}
