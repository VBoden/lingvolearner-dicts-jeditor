package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.repositories.DictionaryRepository;
import ua.vboden.services.EntityService;
import ua.vboden.services.LanguageService;

@Component
public class DictionaryEntryEditorController extends AbstractEditorController<TranslationRow, DictionaryEntry> {

	@FXML
	private TableView<TranslationRow> entriesTable;

	@FXML
	private TableColumn<TranslationRow, String> wordColumn;

	@FXML
	private TableColumn<TranslationRow, String> translationColumn;

	@FXML
	private TableColumn<TranslationRow, String> languageColumn;

	@FXML
	private TextField wordField;

	@FXML
	private TextField translationField;

	@FXML
	private TextField transcriptionField;

	@FXML
	private TextField notesField;

	@FXML
	private ComboBox<CodeString> languageFrom;

	@FXML
	private ComboBox<CodeString> languageTo;

	@FXML
	private ListView<IdString> categoriesList;

	@FXML
	private ListView<IdString> dictionariesList;

	@FXML
	private Label statusMessage;

	@Autowired
	private DictionaryRepository dictionaryRepository;

	@Autowired
	private LanguageService languageService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		categoriesList.setItems(getSessionService().getCategories());
		categoriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		dictionariesList.setItems(getSessionService().getDictionaries());
		dictionariesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		languageService.loadLanguages();
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languageFrom.setItems(languages);
		languageTo.setItems(languages);
		statusMessage.setText("");
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected EntityService<TranslationRow, DictionaryEntry> getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TableView<TranslationRow> getTable() {
		return entriesTable;
	}

	@Override
	protected void resetEditing() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populateEntity(DictionaryEntry entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected DictionaryEntry createNew() {
		return new DictionaryEntry();
	}

	@Override
	protected boolean isNotFilledFields() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void populateFields(TranslationRow current) {
		// TODO Auto-generated method stub

	}

	@Override
	String getFXML() {
		return "/fxml/dictionaryEntryEditor.fxml";
	}

	@Override
	String getTitle() {
		return "menu.edit.dictionaryEntries";
	}

}
