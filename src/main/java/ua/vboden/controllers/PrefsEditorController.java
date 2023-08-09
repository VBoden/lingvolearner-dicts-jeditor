package ua.vboden.controllers;

import static ua.vboden.PreferencesConstants.DEFAULT_LANGUAGE_FROM;
import static ua.vboden.PreferencesConstants.DEFAULT_LANGUAGE_TO;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import ua.vboden.dto.CodeString;
import ua.vboden.services.LanguageService;
import ua.vboden.services.SessionService;

@Component
public class PrefsEditorController extends AbstractController {

	@FXML
	private CheckBox displayDefaultsOnlyCheck;

	@FXML
	private ComboBox<CodeString> languageFrom;

	@FXML
	private ComboBox<CodeString> languageTo;

	@FXML
	private CheckBox useDefaultCheck;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private LanguageService languageService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		languageService.loadLanguages();
		getSessionService().loadPreferences();
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languageFrom.setItems(languages);
		languageFrom.getSelectionModel().select(getSessionService().getDefaultLanguageFrom());
		languageTo.setItems(languages);
		languageTo.getSelectionModel().select(getSessionService().getDefaultLanguageTo());
		sessionService.getPreferences();

	}

	@Override
	String getFXML() {
		return "/fxml/prefsEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.file.preferences");
	}

	@FXML
	void onCancel(ActionEvent event) {
		getStage().close();
	}

	@FXML
	void onSave(ActionEvent event) {
		CodeString selectedItemFrom = languageFrom.getSelectionModel().getSelectedItem();
		if (selectedItemFrom != null) {
			sessionService.getPreferences().put(DEFAULT_LANGUAGE_FROM, selectedItemFrom.getCode());
			sessionService.setDefaultLanguageFrom(selectedItemFrom);
		}
		CodeString selectedItemTo = languageTo.getSelectionModel().getSelectedItem();
		if (selectedItemTo != null) {
			sessionService.getPreferences().put(DEFAULT_LANGUAGE_TO, selectedItemTo.getCode());
			sessionService.setDefaultLanguageTo(selectedItemTo);
		}
		getStage().close();
	}

}
