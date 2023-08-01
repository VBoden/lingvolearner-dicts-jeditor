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

@Service
public class CategoryService implements EntityService<IdString, Category> {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SessionService sessionService;

	@Override
	public void deleteSelected(ObservableList<? extends IdString> selected) {
		categoryRepository.deleteAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()));
	}

	@Override
	public Category findEntity(IdString current) {
		return categoryRepository.findById(current.getId()).get();
	}

	@Override
	public void save(Category entity) {
		categoryRepository.save(entity);
	}

	public void loadCategories() {
		List<IdString> categoryModels = new ArrayList<>();
		categoryRepository.findAll().forEach(entry -> categoryModels.add(new IdString(entry.getId(), entry.getName())));
		Collections.sort(categoryModels);
		sessionService.setCategories(FXCollections.observableArrayList(categoryModels));
	}

	public Category findEntity(int id) {
		return categoryRepository.findById(id).get();
	}

	public List<Category> findEntities(List<IdString> selected) {
		List<Category> result = new ArrayList<>();
		categoryRepository.findAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()))
				.forEach(result::add);
		return result;
	}

}
