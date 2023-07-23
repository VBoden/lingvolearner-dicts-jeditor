package ua.vboden.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import ua.vboden.dto.CodeString;
import ua.vboden.entities.Language;
import ua.vboden.repositories.LanguageRepository;

@Service
public class LanguageService {

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private SessionService sessionService;

	public void loadLanguages() {
		List<CodeString> languageModels = new ArrayList<>();
		languageRepository.findAll()
				.forEach(entry -> languageModels.add(new CodeString(entry.getCode(), entry.getName())));
		Collections.sort(languageModels);
		sessionService.setLanguages(FXCollections.observableArrayList(languageModels));
	}

	public Language getByCode(String code) {
		return languageRepository.findByCode(code);
	}

}
