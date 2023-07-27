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
import ua.vboden.repositories.DictionaryRepository;
import ua.vboden.services.LanguageService;
import ua.vboden.services.SessionService;

@Component
public class DictionaryEditorController extends AbstractController {

	@FXML
	private TableView<DictionaryData> dictionariesTable;

	@FXML
	private TableColumn<DictionaryData, String> titleColumn;

	@FXML
	private TableColumn<DictionaryData, String> languageFromColumn;

	@FXML
	private TableColumn<DictionaryData, String> languageToColumn;

	@FXML
	private TextField dictionaryName;

	@FXML
	private ComboBox<CodeString> languageFrom;

	@FXML
	private ComboBox<CodeString> languageTo;

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
	private DictionaryRepository dictionaryRepository;

	@Autowired
	private LanguageService languageService;

	private DictionaryData current;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		generalInit(resources);
		languageService.loadLanguages();
		dictionariesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		titleColumn.setCellValueFactory(new PropertyValueFactory<DictionaryData, String>("title"));
		languageFromColumn.setCellValueFactory(new PropertyValueFactory<DictionaryData, String>("langFrom"));
		languageToColumn.setCellValueFactory(new PropertyValueFactory<DictionaryData, String>("langTo"));
		initDictionariesView();
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languageFrom.setItems(languages);
		languageTo.setItems(languages);
	}

	private void initDictionariesView() {
		ObservableList<DictionaryData> dictionaries = getSessionService().getDictionaryData();
		dictionariesTable.setItems(dictionaries);
		statusMessage.setText(MessageFormat.format(getResources().getString("dictionary.status"), dictionaries.size()));
	}

	@Override
	String getFXML() {
		return "/fxml/dictionaryEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.dictionaries");
	}

	@FXML
	void startEditing(MouseEvent event) {
		if (event.getClickCount() == 2) {
			current = dictionariesTable.getSelectionModel().getSelectedItem();
			dictionaryName.setText(current.getTitle());
			languageFrom.getSelectionModel().select(find(current.getLangFrom(), languageFrom.getItems()));
			languageTo.getSelectionModel().select(find(current.getLangTo(), languageTo.getItems()));
		}
	}

	private CodeString find(String langName, ObservableList<CodeString> languages) {
		for (CodeString lang : languages) {
			if (lang.getValue().equalsIgnoreCase(langName)) {
				return lang;
			}
		}
		return null;
	}

	@FXML
	void removeSelected(ActionEvent event) {
		ObservableList<DictionaryData> selected = dictionariesTable.getSelectionModel().getSelectedItems();
		dictionaryRepository.deleteAllById(selected.stream().map(DictionaryData::getId).collect(Collectors.toList()));
		updateView();
	}

	@FXML
	void save(ActionEvent event) {
		save();
	}

	private void save() {
		String newTitle = dictionaryName.getText();
		if (StringUtils.isBlank(newTitle)) {
			return;
		}
		Dictionary entity = null;
		if (current != null) {
			Optional<Dictionary> entityOpt = dictionaryRepository.findById(current.getId());
			if (entityOpt.isPresent()) {
				entity = entityOpt.get();
			}
		}
		if (entity == null) {
			entity = new Dictionary();
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
		save(new Dictionary());
	}

	void save(Dictionary entity) {
		String newTitle = dictionaryName.getText();
		if (StringUtils.isBlank(newTitle)) {
			return;
		}
		entity.setName(newTitle);
		entity.setLanguageFrom(languageService.getByCode(languageFrom.getSelectionModel().getSelectedItem().getCode()));
		entity.setLanguageTo(languageService.getByCode(languageTo.getSelectionModel().getSelectedItem().getCode()));
		dictionaryRepository.save(entity);
		resetEditing();
		updateView();
	}

	private void updateView() {
		getSessionService().loadDictionaries();
		initDictionariesView();
	}

	private void resetEditing() {
		dictionaryName.setText("");
		current = null;
	}

	@FXML
	void close(ActionEvent event) {
		getStage().close();
	}
}
