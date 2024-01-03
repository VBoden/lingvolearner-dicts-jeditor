package ua.vboden.services;

import java.util.prefs.Preferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.transformation.SortedList;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.IdString;

@Service
public class PreferencesService {

	public static final String DEFAULT_LANGUAGE_TO = "default.language.to";

	public static final String DEFAULT_LANGUAGE_FROM = "default.language.from";

	public static final String FILL_DEFAULT_LANGUAGES = "fill.default.languages";

	public static final String SHOW_DEFAULT_LANGUAGES = "show.default.languages.only";

	public static final String AUTO_LOAD_LAST_SELECTED_DB = "auto.load.last.selected.db";

	private static final String DICTIONARY_LAST_SELECTED_ID = "dictionary.last.selected.id";

	public static final String MAIN_TABLE_SHOW_TRANSCRIPTION = "main.table.show.transcription";

	public static final String LAST_DB = "db.last.opened";

	@Autowired
	private SessionService sessionService;

	private Preferences preferences;

	public static Preferences getPreferences() {
		return Preferences.userRoot().node("lingvist.fxeditor");
	}

	public static void setLastDB(String dbFile) {
		getPreferences().put(LAST_DB, dbFile);
	}

	public static String getLastDB() {
		return getPreferences().get(LAST_DB, null);
	}

	public static boolean isSelectDbOnStart() {
		return !getPreferences().getBoolean(AUTO_LOAD_LAST_SELECTED_DB, false);
	}

	public void loadPreferences() {
		preferences = getPreferences();
		String langFromCode = preferences.get(DEFAULT_LANGUAGE_FROM, null);
		if (langFromCode != null) {
			sessionService.setDefaultLanguageFrom(sessionService.getLanguages().stream()
					.filter(lang -> langFromCode.equals(lang.getCode())).findAny().orElse(null));
		}
		String langToCode = preferences.get(DEFAULT_LANGUAGE_TO, null);
		if (langToCode != null) {
			sessionService.setDefaultLanguageTo(sessionService.getLanguages().stream()
					.filter(lang -> langToCode.equals(lang.getCode())).findAny().orElse(null));
		}
		sessionService.setFillDefaultLanguages(preferences.getBoolean(FILL_DEFAULT_LANGUAGES, false));
		sessionService.setDisplayDefaultLanguagesOnly(preferences.getBoolean(SHOW_DEFAULT_LANGUAGES, false));
		sessionService.setAutoLoadLastSelectedDb(preferences.getBoolean(AUTO_LOAD_LAST_SELECTED_DB, false));
		sessionService.setShowTranscription(preferences.getBoolean(MAIN_TABLE_SHOW_TRANSCRIPTION, true));
		loadLastDictionary();
	}

	private void loadLastDictionary() {
		SortedList<IdString> allDictionaries = sessionService.getDictionaries().sorted((a, b) -> {
			return b.getId() - a.getId();
		});
		if (allDictionaries != null && !allDictionaries.isEmpty()) {
			int dictId = preferences.getInt(DICTIONARY_LAST_SELECTED_ID, allDictionaries.get(0).getId());

			IdString dictionary = allDictionaries.stream().filter(dic -> dic.getId() == dictId).findAny()
					.orElse(allDictionaries.get(0));
			sessionService.setLastSelectedDictionary(dictionary);
		}
	}

	public void saveShowDefaultLanguagesOnly(boolean showDefaultsOnly) {
		preferences.putBoolean(SHOW_DEFAULT_LANGUAGES, showDefaultsOnly);
		sessionService.setDisplayDefaultLanguagesOnly(showDefaultsOnly);
	}

	public void saveAutoLoadLastSelectedDb(boolean autoLoadLastDb) {
		preferences.putBoolean(AUTO_LOAD_LAST_SELECTED_DB, autoLoadLastDb);
		sessionService.setAutoLoadLastSelectedDb(autoLoadLastDb);
	}

	public void saveFillDefaultLanguage(boolean fillDefault) {
		preferences.putBoolean(FILL_DEFAULT_LANGUAGES, fillDefault);
		sessionService.setFillDefaultLanguages(fillDefault);
	}

	public void saveLanguageTo(CodeString selectedItemTo) {
		if (selectedItemTo != null) {
			preferences.put(DEFAULT_LANGUAGE_TO, selectedItemTo.getCode());
			sessionService.setDefaultLanguageTo(selectedItemTo);
		}
	}

	public void saveLangugeFrom(CodeString selectedItemFrom) {
		if (selectedItemFrom != null) {
			preferences.put(DEFAULT_LANGUAGE_FROM, selectedItemFrom.getCode());
			sessionService.setDefaultLanguageFrom(selectedItemFrom);
		}
	}

	public void saveLastSelectedDictionary(IdString dictionary) {
		preferences.putInt(DICTIONARY_LAST_SELECTED_ID, dictionary.getId());
		sessionService.setLastSelectedDictionary(dictionary);
	}

	public void saveShowTranscription(boolean showTranscription) {
		preferences.putBoolean(MAIN_TABLE_SHOW_TRANSCRIPTION, showTranscription);
		sessionService.setShowTranscription(showTranscription);
	}

}
