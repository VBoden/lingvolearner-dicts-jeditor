package ua.vboden.controllers;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ua.vboden.dto.CodeString;
import ua.vboden.services.EntryService;
import ua.vboden.services.ImportService;

@Component
public class ImportDialogController extends AbstractController {

	@FXML
	private CheckBox addToNewDictionaryCheck;

	@FXML
	private TextField filePath;

	@FXML
	private ComboBox<CodeString> languageFrom;

	@FXML
	private ComboBox<CodeString> languageTo;

	@FXML
	private TextField newDictionaryName;

	@Autowired
	private EntryService entryService;
	@Autowired
	private ImportService importService;

	private File fileToImport;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initView();
	}

	private void initView() {
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languageFrom.setItems(languages);
		languageFrom.getSelectionModel().select(getSessionService().getDefaultLanguageFrom());
		languageTo.setItems(languages);
		languageTo.getSelectionModel().select(getSessionService().getDefaultLanguageTo());
		newDictionaryName.setDisable(true);
	}

	@Override
	String getFXML() {
		return "/fxml/importDialog.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.file.import");
	}

	@FXML
	void addToNewDictionaryChecked(ActionEvent event) {
		newDictionaryName.setDisable(!addToNewDictionaryCheck.isSelected());
	}

	@FXML
	void doClose(ActionEvent event) {
		getStage().close();
	}

	@FXML
	void doImport(ActionEvent event) {
		String dictionaryName = null;
		if (addToNewDictionaryCheck.isSelected()) {
			dictionaryName = newDictionaryName.getText();
		}
		int count = importService.importFromFile(fileToImport, languageFrom.getSelectionModel().getSelectedItem(),
				languageTo.getSelectionModel().getSelectedItem(), dictionaryName);
		showInformationAlert(MessageFormat.format(getResources().getString("import.results"), count));
		getStage().close();
		entryService.loadTranslations();
	}

	@FXML
	void selectFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(getResources().getString("import.dictionary.title"));
		fileChooser.getExtensionFilters()
				.addAll(new ExtensionFilter(getResources().getString("import.dictionary.filter"), "*.vcb"));
		fileChooser.setInitialDirectory(new File(System.getProperty(".")));
		File file = fileChooser.showOpenDialog(getStage());
		if (file != null) {
			System.out.println(file.getAbsolutePath());
			filePath.setText(file.getAbsolutePath());
			newDictionaryName.setText(file.getName().replace(".vcb", ""));
			addToNewDictionaryCheck.setSelected(true);
			newDictionaryName.setDisable(false);
			fileToImport = file;
		}
	}

}
