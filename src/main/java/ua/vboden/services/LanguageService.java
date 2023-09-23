package ua.vboden.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.ObservableList;
import ua.vboden.dto.CodeString;
import ua.vboden.entities.Language;
import ua.vboden.repositories.LanguageRepository;

@Service
public class LanguageService implements EntityService<CodeString, Language> {

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private SessionService sessionService;

	public void loadLanguages() {
		List<CodeString> languageModels = new ArrayList<>();
		languageRepository.findAll()
				.forEach(entry -> languageModels.add(new CodeString(entry.getCode(), entry.getName())));
		Collections.sort(languageModels);
		sessionService.setLanguages(languageModels);
	}

	public Language getByCode(String code) {
		return languageRepository.findByCode(code);
	}

	@Override
	public void deleteSelected(ObservableList<? extends CodeString> selected) {
		selected.stream().map(CodeString::getCode).map(this::getByCode).forEach(languageRepository::delete);
	}

	@Override
	public Language findEntity(CodeString current) {
		return getByCode(current.getCode());
	}

	@Override
	public void save(Language entity) {
		languageRepository.save(entity);
	}

	@Override
	public void saveAll(List<Language> entities) {
		languageRepository.saveAll(entities);
	}

}
