package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.DictionaryData;
import ua.vboden.entities.Dictionary;
import ua.vboden.services.DictionaryService;
import ua.vboden.services.EntityService;
import ua.vboden.services.LanguageService;

@Component
@Scope(value="prototype")
public class DictionaryEditorController extends AbstractEditorController<DictionaryData, Dictionary> {

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
	private LanguageService languageService;

	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		languageService.loadLanguages();
		dictionariesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		titleColumn.setCellValueFactory(new PropertyValueFactory<DictionaryData, String>("title"));
		languageFromColumn.setCellValueFactory(new PropertyValueFactory<DictionaryData, String>("langFrom"));
		languageToColumn.setCellValueFactory(new PropertyValueFactory<DictionaryData, String>("langTo"));
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languageFrom.setItems(languages);
		languageTo.setItems(languages);
		initView();
	}

	@Override
	String getFXML() {
		return "/fxml/dictionaryEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.dictionaries");
	}

	@Override
	protected void initView() {
		dictionaryService.loadData();
		ObservableList<DictionaryData> dictionaries = getSessionService().getDictionaryData();
		dictionariesTable.setItems(dictionaries);
		statusMessage.setText(MessageFormat.format(getResources().getString("dictionary.status"), dictionaries.size()));
	}

	@Override
	protected EntityService<DictionaryData, Dictionary> getService() {
		return dictionaryService;
	}

	@Override
	protected TableView<DictionaryData> getTable() {
		return dictionariesTable;
	}

	@Override
	protected void resetEditing() {
		dictionaryName.setText("");
		languageFrom.getSelectionModel().clearSelection();
		languageTo.getSelectionModel().clearSelection();
	}

	@Override
	protected void populateEntity(Dictionary entity) {
		entity.setName(dictionaryName.getText());
		entity.setLanguageFrom(languageService.getByCode(languageFrom.getSelectionModel().getSelectedItem().getCode()));
		entity.setLanguageTo(languageService.getByCode(languageTo.getSelectionModel().getSelectedItem().getCode()));
	}

	@Override
	protected Dictionary createNew() {
		return new Dictionary();
	}

	@Override
	protected String checkFilledFields() {

		if (languageFrom.getSelectionModel().getSelectedIndex() == -1)
			return "Fill word";
		if (languageTo.getSelectionModel().getSelectedIndex() == -1)
			return "Fill word";
		return StringUtils.isBlank(dictionaryName.getText()) ? "fill" : null;
	}

	@Override
	protected void populateFields(DictionaryData current) {
		dictionaryName.setText(current.getTitle());
		languageFrom.getSelectionModel().select(find(current.getLangFrom(), languageFrom.getItems()));
		languageTo.getSelectionModel().select(find(current.getLangTo(), languageTo.getItems()));
	}

	private CodeString find(String langName, ObservableList<CodeString> languages) {
		for (CodeString lang : languages) {
			if (lang.getValue().equalsIgnoreCase(langName)) {
				return lang;
			}
		}
		return null;
	}
}
