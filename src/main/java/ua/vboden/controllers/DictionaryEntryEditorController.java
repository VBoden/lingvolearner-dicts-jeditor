package ua.vboden.controllers;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
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
import javafx.scene.input.MouseEvent;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.dto.WordData;
import ua.vboden.entities.Category;
import ua.vboden.entities.Dictionary;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Word;
import ua.vboden.fxservices.EntrySearchService;
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
		entity.setTranscription(transcriptionField.getText());
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
	protected String checkFilledFields() {
		if (!useWordCheck.isSelected() && isBlank(wordField.getText())) {
			return "Fill word";
		}
		if (!useTranslationCheck.isSelected() && isBlank(translationField.getText()))
			return "Fill word";
		if (useWordCheck.isSelected() && wordsSuggestionTable.getSelectionModel().getSelectedIndex() == -1)
			return "Fill word";
		if (!useWordCheck.isSelected() && languageFrom.getSelectionModel().getSelectedIndex() == -1)
			return "Fill word";
		if (useTranslationCheck.isSelected() && transSuggestionTable.getSelectionModel().getSelectedIndex() == -1)
			return "Fill word";
		if (!useTranslationCheck.isSelected() && languageTo.getSelectionModel().getSelectedIndex() == -1)
			return "Fill word";
		if (getCurrent() == null) {
			String word = useWordCheck.isSelected()
					? wordsSuggestionTable.getSelectionModel().getSelectedItem().getWord()
					: wordField.getText();
			String translation = useTranslationCheck.isSelected()
					? transSuggestionTable.getSelectionModel().getSelectedItem().getWord()
					: translationField.getText();
			for (TranslationRow entry : entriesTable.getItems()) {
				if (word.equalsIgnoreCase(entry.getWord())
						&& translation.equalsIgnoreCase(entry.getTranslation().split("\n")[0])) {
					return "Already exists";
				}
			}
		}
		return null;
	}

	@Override
	protected void populateFields(TranslationRow current) {
		DictionaryEntry entity = getService().findEntity(current);
		if (entity.getWord() != null) {
			String word = fillWordFields(entity.getWord(), wordField, wordsSuggestionTable, useWordCheck);
			languageFrom.getSelectionModel()
					.select(find(entity.getWord().getLanguage().getName(), languageFrom.getItems()));
			List<Category> categories = entity.getWord().getCategory();
			categoriesList.getSelectionModel().clearSelection();
			if (categories != null && !categories.isEmpty()) {
				int[] indexes = find(categories.stream().map(Category::getId).collect(Collectors.toList()),
						categoriesList.getItems());
				if (indexes.length > 0) {
					categoriesList.getSelectionModel().selectIndices(-1, indexes);
					categoriesList.scrollTo(indexes[0] + 1);
				}
			}
			entriesTable.setItems(FXCollections.observableArrayList(entryService.getAllByWord(word)));
		}
		fillWordFields(entity.getTranslation(), translationField, transSuggestionTable, useTranslationCheck);

		transcriptionField.setText(current.getTranscription());
		notesField.setText(entity.getTranslation().getNotes());
		languageTo.getSelectionModel()
				.select(find(entity.getTranslation().getLanguage().getName(), languageTo.getItems()));
		dictionariesList.getSelectionModel().clearSelection();
		List<Dictionary> dictionaries = entity.getDictionary();
		if (dictionaries != null && !dictionaries.isEmpty()) {
			int[] indexes = find(dictionaries.stream().map(Dictionary::getId).collect(Collectors.toList()),
					dictionariesList.getItems());
			if (indexes.length > 0) {
				dictionariesList.getSelectionModel().selectIndices(-1, indexes);
				dictionariesList.scrollTo(indexes[0] + 1);
			}
		}
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
	protected void save(DictionaryEntry entity) {
		super.save(entity);
//		getSessionService().getTranslations().add(0, entryService.getRowById(entity.getId()));
		getSessionService().getTranslationIds().add(0, entity.getId());
		entryService.loadTranslations(getSessionService().getTranslationIds());
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
		categoriesList.getSelectionModel().clearSelection();
		boolean isSelected = useTranslationCheck.isSelected();
		transSuggestionTable.setDisable(!isSelected);
		if (isSelected) {
			fillTransBySelection();
		} else {
			notesField.setText("");
			languageTo.getSelectionModel().clearSelection();
		}
	}

	private void fillTransBySelection() {
		Word wordEntity = wordService.findEntity(transSuggestionTable.getSelectionModel().getSelectedItem());
		notesField.setText(wordEntity.getNotes());
		languageTo.getSelectionModel().select(find(wordEntity.getLanguage().getName(), languageTo.getItems()));
		List<Category> categories = wordEntity.getCategory();
		if (categories != null && !categories.isEmpty())
			categoriesList.getSelectionModel().selectIndices(-1, find(
					categories.stream().map(Category::getId).collect(Collectors.toList()), categoriesList.getItems()));
	}

	@FXML
	void useWordChecked(ActionEvent event) {
		categoriesList.getSelectionModel().clearSelection();
		boolean isSelected = useWordCheck.isSelected();
		wordsSuggestionTable.setDisable(!isSelected);
		if (isSelected) {
			fillWordBySelection();
		} else {
			languageFrom.getSelectionModel().clearSelection();
		}
	}

	private void fillWordBySelection() {
		Word wordEntity = wordService.findEntity(wordsSuggestionTable.getSelectionModel().getSelectedItem());
		languageFrom.getSelectionModel().select(find(wordEntity.getLanguage().getName(), languageFrom.getItems()));
		List<Category> categories = wordEntity.getCategory();
		if (categories != null && !categories.isEmpty())
			categoriesList.getSelectionModel().selectIndices(-1, find(
					categories.stream().map(Category::getId).collect(Collectors.toList()), categoriesList.getItems()));
	}

	@FXML
	void wordEntering(KeyEvent event) {
		executeSuggestionSearch(wordField, wordsSuggestionTable, useWordCheck);
		fillEntriesAfterSuggestions(wordField.getText(), entryService::getAllByWord);
	}

	@FXML
	void translationEntering(KeyEvent event) {
		executeSuggestionSearch(translationField, transSuggestionTable, useTranslationCheck);
		fillEntriesAfterSuggestions(translationField.getText(), entryService::getAllByTranslation);
	}

	private void fillEntriesAfterSuggestions(String word, Function<String, List<TranslationRow>> finder) {
		if (StringUtils.isNotBlank(word) && word.length() > 2) {
			EntrySearchService searchSevice = new EntrySearchService(finder, word);
			searchSevice.start();
			searchSevice.setOnSucceeded(
					e -> entriesTable.setItems(FXCollections.observableArrayList(searchSevice.getValue())));
		}
	}

	private void executeSuggestionSearch(TextField textField, TableView<WordData> suggestionTable, CheckBox useCheck) {
		String word = textField.getText();
		if (StringUtils.isNotBlank(word) && word.length() > 2) {
			fillSuggestions(suggestionTable, useCheck, word);
		}
	}

	@FXML
	void wordsSuggestionClicked(MouseEvent event) {
		fillWordBySelection();
	}

	@FXML
	void transSuggestionClicked(MouseEvent event) {
		fillTransBySelection();
	}
}
