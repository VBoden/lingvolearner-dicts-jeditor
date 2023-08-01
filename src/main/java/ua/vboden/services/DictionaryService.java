package ua.vboden.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.vboden.converters.DictionaryConverter;
import ua.vboden.dto.DictionaryData;
import ua.vboden.dto.IdString;
import ua.vboden.entities.Dictionary;
import ua.vboden.repositories.DictionaryRepository;

@Service
public class DictionaryService implements EntityService<DictionaryData, Dictionary> {

	@Autowired
	private DictionaryRepository dictionaryRepository;
	@Autowired
	private SessionService sessionService;

	@Autowired
	private DictionaryConverter dictionaryConverter;

	@Override
	public void deleteSelected(ObservableList<? extends DictionaryData> selected) {
		dictionaryRepository.deleteAllById(selected.stream().map(DictionaryData::getId).collect(Collectors.toList()));
	}

	@Override
	public Dictionary findEntity(DictionaryData current) {
		return dictionaryRepository.findById(current.getId()).get();
	}

	@Override
	public void save(Dictionary entity) {
		dictionaryRepository.save(entity);
	}

	public void loadData() {
		List<IdString> dictionaries = new ArrayList<>();
		List<DictionaryData> dictionaryData = new ArrayList<>();
		dictionaryRepository.findAll().forEach(entry -> {
			dictionaries.add(new IdString(entry.getId(), entry.getName() + " (" + entry.getLanguageFrom().getName()
					+ "-" + entry.getLanguageTo().getName() + ")"));
			dictionaryData.add(dictionaryConverter.convert(entry));
		});
		sessionService.setDictionaries(FXCollections.observableArrayList(dictionaries));
		sessionService.setDictionaryData(FXCollections.observableArrayList(dictionaryData));
	}

	public Dictionary findEntity(int id) {
		return dictionaryRepository.findById(id).get();
	}

	public List<Dictionary> findEntities(List<IdString> selected) {
		List<Dictionary> result = new ArrayList<>();
		dictionaryRepository.findAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()))
				.forEach(result::add);
		return result;
	}

}
