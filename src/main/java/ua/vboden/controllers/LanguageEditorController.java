package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.DictionaryData;
import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.Dictionary;
import ua.vboden.entities.Language;
import ua.vboden.repositories.DictionaryRepository;
import ua.vboden.repositories.LanguageRepository;
import ua.vboden.services.LanguageService;
import ua.vboden.services.SessionService;

@Component
public class LanguageEditorController extends AbstractEditorController {

	@FXML
	private TableView<CodeString> languagesTable;

	@FXML
	private TableColumn<CodeString, String> codeColumn;

	@FXML
	private TableColumn<CodeString, String> titleColumn;

	@FXML
	private TextField languageCode;

	@FXML
	private TextField languageTitle;

	@FXML
	private Button closeButton;

	@FXML
	private Button removeDictionary;

	@FXML
	private Button saveAsNewDictionary;

	@FXML
	private Button saveDictionary;

	@FXML
	private Label statusMessage;

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private LanguageService languageService;

	private CodeString current;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		generalInit(resources);
		languagesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		codeColumn.setCellValueFactory(new PropertyValueFactory<CodeString, String>("code"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<CodeString, String>("value"));
		initView();
	}

	private void initView() {
		languageService.loadLanguages();
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languagesTable.setItems(languages);
		statusMessage.setText(MessageFormat.format(getResources().getString("language.status"), languages.size()));
	}

	@Override
	String getFXML() {
		return "/fxml/languageEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.languages");
	}

	@FXML
	void startEditing(MouseEvent event) {
		if (event.getClickCount() == 2) {
			current = languagesTable.getSelectionModel().getSelectedItem();
			languageCode.setText(current.getCode());
			languageCode.setEditable(false);
			languageTitle.setText(current.getValue());
		}
	}

	@FXML
	void removeSelected(ActionEvent event) {
		ObservableList<CodeString> selected = languagesTable.getSelectionModel().getSelectedItems();
		languageService.deleteSelected(selected);
		updateView();
	}


	protected void save() {
		String newTitle = languageTitle.getText();
		if (StringUtils.isBlank(newTitle)) {
			return;
		}
		Language entity = null;
		if (current != null) {
			entity = languageRepository.findByCode(current.getCode());
		}
		if (entity == null) {
			entity = new Language();
		}
		save(entity);
	}

	@FXML
	void saveEnter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			save();
		}
	}

	@FXML
	void saveNew(ActionEvent event) {
		save(new Language());
	}

	protected void save(Language entity) {
		populateEntity(entity);
		languageRepository.save(entity);
		resetEditing();
		updateView();
	}

	protected void populateEntity(Language entity) {
		String newCode = languageCode.getText();
		if (StringUtils.isBlank(newCode)) {
			return;
		}
		String newTitle = languageTitle.getText();
		if (StringUtils.isBlank(newTitle)) {
			return;
		}
		entity.setCode(newCode);
		entity.setName(newTitle);
	}

	private void updateView() {
		initView();
	}

	private void resetEditing() {
		languageCode.setText("");
		languageTitle.setText("");
		languageCode.setEditable(true);
		current = null;
	}

	@FXML
	void close(ActionEvent event) {
		getStage().close();
	}
}
