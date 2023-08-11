package ua.vboden;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.vboden.services.CategoryService;
import ua.vboden.services.DictionaryService;
import ua.vboden.services.EntryService;
import ua.vboden.services.LanguageService;
import ua.vboden.services.PreferencesService;

@Component
public class AppContextInitializer {

	@Autowired
	private EntryService entryService;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private LanguageService languageService;

	@Autowired
	private PreferencesService preferencesService;

	public void initApp() {
		categoryService.loadCategories();
		dictionaryService.loadData();
		languageService.loadLanguages();
		preferencesService.loadPreferences();
		entryService.loadTranslations();
	}

}
