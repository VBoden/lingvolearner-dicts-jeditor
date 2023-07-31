package ua.vboden.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.vboden.dto.IdString;
import ua.vboden.entities.Category;
import ua.vboden.entities.Dictionary;
import ua.vboden.repositories.CategoryRepository;
import ua.vboden.repositories.DictionaryRepository;

@Service
public class DictionaryService implements EntityService<IdString, Dictionary> {

	@Autowired
	private DictionaryRepository dictionaryRepository;
	@Autowired
	private SessionService sessionService;

	@Override
	public void deleteSelected(ObservableList<? extends IdString> selected) {
		dictionaryRepository.deleteAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()));
	}

	@Override
	public Dictionary findEntity(IdString current) {
		return dictionaryRepository.findById(current.getId()).get();
	}

	@Override
	public void save(Dictionary entity) {
		dictionaryRepository.save(entity);
	}

	public void loadData() {
		List<IdString> result = new ArrayList<>();
		dictionaryRepository.findAll().forEach(entry -> result.add(new IdString(entry.getId(), entry.getName())));
		Collections.sort(result);
		sessionService.setDictionaries(FXCollections.observableArrayList(result));
	}

	public List<Dictionary> findEntities(List<IdString> selected) {
		List<Dictionary> result = new ArrayList<>();
		dictionaryRepository.findAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()))
				.forEach(result::add);
		return result;
	}

}
