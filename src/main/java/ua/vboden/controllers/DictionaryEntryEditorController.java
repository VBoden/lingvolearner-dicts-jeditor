package ua.vboden.controllers;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.dto.WordData;
import ua.vboden.entities.Category;
import ua.vboden.entities.Dictionary;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Word;
import ua.vboden.repositories.DictionaryRepository;
import ua.vboden.services.CategoryService;
import ua.vboden.services.DictionaryService;
import ua.vboden.services.EntityService;
import ua.vboden.services.EntryService;
import ua.vboden.services.LanguageService;
import ua.vboden.services.WordService;

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

	@FXML
	private TableColumn<WordData, String> transCategoryColumn;

	@FXML
	private TableColumn<WordData, String> transLangColumn;

	@FXML
	private TableView<WordData> transSuggestionTable;

	@FXML
	private TableColumn<WordData, String> transWordColumn;

	@FXML
	private CheckBox useTranslationCheck;

	@FXML
	private CheckBox useWordCheck;

	@FXML
	private TableColumn<WordData, String> wordCategoryColumn;

	@FXML
	private TableColumn<WordData, String> wordLangColumn;

	@FXML
	private TableColumn<WordData, String> wordWordColumn;

	@FXML
	private TableView<WordData> wordsSuggestionTable;

	@Autowired
	private LanguageService languageService;

	@Autowired
	private EntryService entryService;

	@Autowired
	private WordService wordService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wordColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("word"));
		translationColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("translation"));
		categoriesList.setItems(getSessionService().getCategories());
		categoriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		dictionariesList.setItems(getSessionService().getDictionaries().sorted((a, b) -> {
			return b.getId() - a.getId();
		}));
		dictionariesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		languageService.loadLanguages();
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languageFrom.setItems(languages);
		languageTo.setItems(languages);
		statusMessage.setText("");
		wordWordColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("word"));
		wordLangColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("language"));
		wordCategoryColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("categories"));
		transWordColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("word"));
		transLangColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("language"));
		transCategoryColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("categories"));
		useWordCheck.setDisable(true);
		useTranslationCheck.setDisable(true);
		wordsSuggestionTable.setDisable(true);
		transSuggestionTable.setDisable(true);
		initView();
	}

	@Override
	protected void initView() {
		if (getCurrent() != null) {
			populateFields(getCurrent());
		}
	}

	@Override
	protected EntityService<TranslationRow, DictionaryEntry> getService() {
		return entryService;
	}

	@Override
	protected TableView<TranslationRow> getTable() {
		return entriesTable;
	}

	@Override
	protected void resetEditing() {
		wordField.setText("");
		translationField.setText("");
		transcriptionField.setText("");
		notesField.setText("");
		languageFrom.getSelectionModel().clearSelection();
		languageTo.getSelectionModel().clearSelection();
		categoriesList.getSelectionModel().clearSelection();
		dictionariesList.getSelectionModel().select(0);
		wordsSuggestionTable.getItems().clear();
		transSuggestionTable.getItems().clear();
		useWordCheck.setSelected(false);
		useTranslationCheck.setSelected(false);
		useWordCheck.setDisable(true);
		useTranslationCheck.setDisable(true);
		wordsSuggestionTable.setDisable(true);
		transSuggestionTable.setDisable(true);
	}

	@Override
	protected void populateEntity(DictionaryEntry entity) {
		Word wordEntity = createWordEntity(wordsSuggestionTable, useWordCheck, wordField, languageFrom);
		entity.setWord(wordEntity);
		Word translationEntity = createWordEntity(transSuggestionTable, useTranslationCheck, translationField,
				languageTo);
		String notes = notesField.getText();
		if (translationEntity.getId() == 0 && StringUtils.isNotBlank(notes)) {
			translationEntity.setNotes(notes);
		}
		entity.setTranslation(translationEntity);
		ObservableList<IdString> selectedDictionaries = dictionariesList.getSelectionModel().getSelectedItems();
		if (selectedDictionaries != null && selectedDictionaries.size() > 0) {
			entity.setDictionary(dictionaryService.findEntities(selectedDictionaries));
		}
		wordService.save(wordEntity);
		wordService.save(translationEntity);

	}

	private Word createWordEntity(TableView<WordData> suggestionTable, CheckBox useWordCheck, TextField wordField,
			ComboBox<CodeString> language) {
		WordData selectedWord = suggestionTable.getSelectionModel().getSelectedItem();
		Word wordEntity;
		if (useWordCheck.isSelected() && selectedWord != null) {
			wordEntity = wordService.findEntity(selectedWord);
		} else {
			wordEntity = new Word();
			wordEntity.setWord(wordField.getText());
			CodeString languageItem = language.getSelectionModel().getSelectedItem();
			wordEntity.setLanguage(languageService.findEntity(languageItem));
			List<IdString> selectedCategories = categoriesList.getSelectionModel().getSelectedItems();
			if (selectedCategories != null && selectedCategories.size() > 0) {
				wordEntity.setCategory(categoryService.findEntities(selectedCategories));
			}
		}
		return wordEntity;
	}

	@Override
	protected DictionaryEntry createNew() {
		return new DictionaryEntry();
	}

	@Override
	protected boolean isNotFilledFields() {
		if (isBlank(wordField.getText()))
			return true;
		if (isBlank(translationField.getText()))
			return true;
		if (useWordCheck.isSelected() && wordsSuggestionTable.getSelectionModel().getSelectedIndex() == -1)
			return true;
		if (!useWordCheck.isSelected() && languageFrom.getSelectionModel().getSelectedIndex() == -1)
			return true;
		if (useTranslationCheck.isSelected() && transSuggestionTable.getSelectionModel().getSelectedIndex() == -1)
			return true;
		if (!useTranslationCheck.isSelected() && languageTo.getSelectionModel().getSelectedIndex() == -1)
			return true;
		String word = useWordCheck.isSelected() ? wordsSuggestionTable.getSelectionModel().getSelectedItem().getWord()
				: wordField.getText();
		String translation = useTranslationCheck.isSelected()
				? transSuggestionTable.getSelectionModel().getSelectedItem().getWord()
				: translationField.getText();
		for (TranslationRow entry : entriesTable.getItems()) {
			if (word.equalsIgnoreCase(entry.getWord())
					&& translation.equalsIgnoreCase(entry.getTranslation().split("\n")[0])) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void populateFields(TranslationRow current) {
		DictionaryEntry entity = getService().findEntity(current);
		String word = fillWordFields(entity.getWord(), wordField, wordsSuggestionTable, useWordCheck);
		fillWordFields(entity.getTranslation(), translationField, transSuggestionTable, useTranslationCheck);

		transcriptionField.setText(current.getTranscription());
		notesField.setText(entity.getTranslation().getNotes());
		languageFrom.getSelectionModel()
				.select(find(entity.getWord().getLanguage().getName(), languageFrom.getItems()));
		languageTo.getSelectionModel()
				.select(find(entity.getTranslation().getLanguage().getName(), languageTo.getItems()));
		categoriesList.getSelectionModel().clearSelection();
		List<Category> categories = entity.getWord().getCategory();
		if (categories != null && !categories.isEmpty())
			categoriesList.getSelectionModel().selectIndices(-1, find(
					categories.stream().map(Category::getId).collect(Collectors.toList()), categoriesList.getItems()));
		List<Dictionary> dictionaries = entity.getDictionary();
		if (dictionaries != null && !dictionaries.isEmpty())
			dictionariesList.getSelectionModel().selectIndices(-1,
					find(dictionaries.stream().map(Dictionary::getId).collect(Collectors.toList()),
							dictionariesList.getItems()));
		entriesTable.setItems(FXCollections.observableArrayList(entryService.getAllByWord(word)));
		useWordCheck.setDisable(false);
		useTranslationCheck.setDisable(false);
		useWordCheck.setSelected(true);
		useTranslationCheck.setSelected(true);
		wordsSuggestionTable.setDisable(false);
		transSuggestionTable.setDisable(false);
	}

	protected String fillWordFields(Word wordEntity, TextField textField, TableView<WordData> suggestionTable,
			CheckBox useWordCheck) {
		String word = wordEntity.getWord();
		textField.setText(word);
		ObservableList<WordData> wordSuggestions = fillSuggestions(suggestionTable, useWordCheck, word);
		suggestionTable.getSelectionModel().select(find(wordEntity.getId(), wordSuggestions));
		return word;
	}

	protected ObservableList<WordData> fillSuggestions(TableView<WordData> suggestionTable, CheckBox useWordCheck,
			String word) {
		ObservableList<WordData> wordSuggestions = FXCollections.observableArrayList(wordService.getAllByWord(word));
		suggestionTable.setItems(wordSuggestions);
		if (wordSuggestions.size() > 0) {
			suggestionTable.getSelectionModel().select(0);
		}
		useWordCheck.setDisable(wordSuggestions.size() == 0);
		useWordCheck.setSelected(false);
		return wordSuggestions;
	}

	private int find(int id, ObservableList<WordData> items) {
		for (WordData item : items) {
			if (item.getId() == id) {
				return items.indexOf(item);
			}
		}
		return -1;
	}

	private int[] find(List<Integer> values, ObservableList<IdString> items) {
		List<Integer> indices = new ArrayList<>();
		for (int cat : values) {
			for (IdString item : items) {
				if (item.getId() == cat) {
					indices.add(items.indexOf(item));
					break;
				}
			}
		}
		return indices.stream().mapToInt(Integer::intValue).toArray();
	}

	private CodeString find(String langName, ObservableList<CodeString> languages) {
		for (CodeString lang : languages) {
			if (lang.getValue().equalsIgnoreCase(langName)) {
				return lang;
			}
		}
		return null;
	}

	@Override
	String getFXML() {
		return "/fxml/dictionaryEntryEditor.fxml";
	}

	@Override
	String getTitle() {
		return "menu.edit.dictionaryEntries";
	}

	public void showStage(Object object, TranslationRow selected) throws IOException {
		setCurrent(selected);
		super.showStage(null);
	}

	@FXML
	void useTranslationChecked(ActionEvent event) {
		transSuggestionTable.setDisable(!useTranslationCheck.isSelected());
	}

	@FXML
	void useWordChecked(ActionEvent event) {
		wordsSuggestionTable.setDisable(!useWordCheck.isSelected());
	}

	@FXML
	void wordEntering(KeyEvent event) {
		executeSuggestionSearch(wordField, wordsSuggestionTable, useWordCheck);
		String word = wordField.getText();
		if (StringUtils.isNotBlank(word) && word.length() > 2) {
			entriesTable.setItems(FXCollections.observableArrayList(entryService.getAllByWord(word)));
		}
	}

	@FXML
	void translationEntering(KeyEvent event) {
		executeSuggestionSearch(translationField, transSuggestionTable, useTranslationCheck);
		String word = translationField.getText();
		if (StringUtils.isNotBlank(word) && word.length() > 2) {
			entriesTable.setItems(FXCollections.observableArrayList(entryService.getAllByTranslation(word)));
		}
	}

	private void executeSuggestionSearch(TextField textField, TableView<WordData> suggestionTable, CheckBox useCheck) {
		String word = textField.getText();
		if (StringUtils.isNotBlank(word) && word.length() > 2) {
			fillSuggestions(suggestionTable, useCheck, word);
		}
	}

}
