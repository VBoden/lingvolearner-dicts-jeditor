package ua.vboden.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.ObservableList;
import ua.vboden.converters.WordConverter;
import ua.vboden.dto.WordData;
import ua.vboden.entities.Word;
import ua.vboden.repositories.WordRepository;

@Service
public class WordService implements EntityService<WordData, Word> {

	@Autowired
	private WordRepository wordRepository;

	@Autowired
	private WordConverter wordConverter;

	@Autowired
	private SessionService sessionService;

	public void loadData() {
		List<WordData> models = new ArrayList<>();
		wordRepository.findAll().forEach(entry -> models.add(wordConverter.convert(entry)));
		Collections.sort(models, WordData.lastFirstComparator());
		sessionService.setWords(models);
	}

	@Override
	public void deleteSelected(ObservableList<? extends WordData> selected) {
		wordRepository.deleteAllById(selected.stream().map(WordData::getId).collect(Collectors.toList()));
	}

	@Override
	public Word findEntity(WordData current) {
		return wordRepository.findById(current.getId()).get();
	}

	public List<Word> getAllByIds(List<Integer> ids) {
		List<Word> result = new ArrayList<>();
		wordRepository.findAllById(ids).forEach(result::add);
		return result;
	}

	@Override
	public void save(Word entity) {
		wordRepository.save(entity);
	}

	@Override
	public void saveAll(List<Word> entities) {
		wordRepository.saveAll(entities);
	}

	public List<WordData> getAllByWord(String word) {
		return wordConverter.convertAll(wordRepository.findByWordContaining(word));
	}
	public WordData getDataById(int id) {
		return wordConverter.convert(wordRepository.findById(id).get());
	}

	public void delete(Word entity) {
		wordRepository.delete(entity);
	}

}
