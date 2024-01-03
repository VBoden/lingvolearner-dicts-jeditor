package ua.vboden.controllers;

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
import ua.vboden.services.PreferencesService;
import ua.vboden.services.SessionService;

@Component
public class PrefsEditorController extends AbstractController {

	@FXML
	private CheckBox autoLoadLastSelectedDb;

	@FXML
	private CheckBox displayDefaultsOnlyCheck;

	@FXML
	private ComboBox<CodeString> languageFrom;

	@FXML
	private ComboBox<CodeString> languageTo;

	@FXML
	private CheckBox useDefaultCheck;

	@FXML
	private CheckBox showTranscription;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private PreferencesService preferencesService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		preferencesService.loadPreferences();
		initView();
	}

	private void initView() {
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languageFrom.setItems(languages);
		languageFrom.getSelectionModel().select(getSessionService().getDefaultLanguageFrom());
		languageTo.setItems(languages);
		languageTo.getSelectionModel().select(getSessionService().getDefaultLanguageTo());
		displayDefaultsOnlyCheck.setSelected(sessionService.isDisplayDefaultLanguagesOnly());
		autoLoadLastSelectedDb.setSelected(sessionService.isAutoLoadLastSelectedDb());
		useDefaultCheck.setSelected(sessionService.isFillDefaultLanguages());
		showTranscription.setSelected(sessionService.isShowTranscription());
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
		preferencesService.saveLangugeFrom(languageFrom.getSelectionModel().getSelectedItem());
		preferencesService.saveLanguageTo(languageTo.getSelectionModel().getSelectedItem());
		preferencesService.saveFillDefaultLanguage(useDefaultCheck.isSelected());
		preferencesService.saveShowDefaultLanguagesOnly(displayDefaultsOnlyCheck.isSelected());
		preferencesService.saveAutoLoadLastSelectedDb(autoLoadLastSelectedDb.isSelected());
		preferencesService.saveShowTranscription(showTranscription.isSelected());
		getStage().close();
	}

}
