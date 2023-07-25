package ua.vboden.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.services.EntryService;

@Component
public class MainWindowController extends AbstractController {

	@FXML
	private MenuItem menuManageCategories;

	@FXML
	private MenuItem menuQuit;

	@FXML
	private TableView<TranslationRow> mainTable;

	@FXML
	private ListView<IdString> catDictsList;

	@FXML
	private ComboBox<String> catOrDictSelector;

	@FXML
	private ComboBox<String> conditionSelector;

	@FXML
	private ToggleButton findButton;

	@FXML
	private TextField findWordField;

	@FXML
	private CheckBox inTranslationsCheck;

	@FXML
	private Button filterButton;

	@FXML
	private Button resetButton;

	@FXML
	private Label statusMessage1;

	@FXML
	private Label statusMessage2;

	@FXML
	private Label statusMessage3;

	@FXML
	private TableColumn<TranslationRow, Integer> numberColumn;

	@FXML
	private TableColumn<TranslationRow, String> wordColumn;

	@FXML
	private TableColumn<TranslationRow, String> translationColumn;

	@FXML
	private TableColumn<TranslationRow, String> categoryColumn;

	@FXML
	private TableColumn<TranslationRow, String> dictionaryColumn;

	@Autowired
	private CategoryEditorController categoryEditorController;

	@Autowired
	private DictionaryEditorController dictionaryEditorController;

	@Autowired
	private DictionaryEntryEditorController dictionaryEntryEditorController;

	@Autowired
	private WordEditorController wordEditorController;

	@Autowired
	private LanguageEditorController languageEditorController;

	@Autowired
	private EntryService entryService;

	private boolean filtered;

	@Override
	String getFXML() {
		return "/fxml/main.fxml";
	}

	@Override
	String getTitle() {
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getSessionService().loadData();

		numberColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, Integer>("number"));
		wordColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("word"));
		translationColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("translation"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("categories"));
		dictionaryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("dictionaries"));
		loadTranslations();
		catOrDictSelector
				.setItems(FXCollections.observableArrayList(getResources().getString("filters.selection.categories"),
						getResources().getString("filters.selection.dictionaries")));
		conditionSelector.setItems(FXCollections.observableArrayList(getResources().getString("filters.selection.or"),
				getResources().getString("filters.selection.and")));
		catOrDictSelector.getSelectionModel().select(0);
		conditionSelector.getSelectionModel().select(0);
		loadCategories();
		statusMessage3.setText(MessageFormat.format(resources.getString("dictionary.status"),
				getSessionService().getDictionaries().size()));
		mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	}

	private void loadTranslations() {
		ObservableList<TranslationRow> translations = getSessionService().getTranslations();
		updateTranslations(translations);
//		mainTable.getSelectionModel().select(translations.size() - 1);
//		mainTable.scrollTo(translations.size());
	}

	private void updateTranslations(ObservableList<TranslationRow> translations) {
		mainTable.setItems(translations);
		statusMessage1
				.setText(MessageFormat.format(getResources().getString("translations.status"), translations.size()));
	}

	private void loadCategories() {
		ObservableList<IdString> categories = getSessionService().getCategories();
		catDictsList.setItems(categories);
		catDictsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		statusMessage2.setText(MessageFormat.format(getResources().getString("category.status"), categories.size()));
	}

	private void loadDictionaries() {
		ObservableList<IdString> dictionaries = getSessionService().getDictionaries();
		catDictsList.setItems(dictionaries);
		statusMessage3
				.setText(MessageFormat.format(getResources().getString("dictionary.status"), dictionaries.size()));
	}

	@FXML
	void catOrDictChanged(ActionEvent event) {
		String selected = catOrDictSelector.getSelectionModel().getSelectedItem();
		if (getResources().getString("filters.selection.categories").equals(selected)) {
			loadCategories();
		} else {
			loadDictionaries();
		}
	}

	@FXML
	void doFiltering(ActionEvent event) {
		ObservableList<IdString> selectedItems = catDictsList.getSelectionModel().getSelectedItems();
		if (!selectedItems.isEmpty()) {

			List<Integer> selectedIds = selectedItems.stream().map(IdString::getId).collect(Collectors.toList());
			boolean condition = getResources().getString("filters.selection.and")
					.equals(conditionSelector.getSelectionModel().getSelectedItem());
			doFiltering(selectedIds, condition);
		}
	}

	private void doFiltering(List<Integer> selectedIds, boolean condition) {
		findButton.setSelected(false);
		String selected = catOrDictSelector.getSelectionModel().getSelectedItem();
		if (getResources().getString("filters.selection.categories").equals(selected)) {
			getSessionService().loadTranslationsByCategories(selectedIds, condition);
		} else {
			getSessionService().loadTranslationsByDictionaries(selectedIds, condition);
		}
		loadTranslations();
		filtered = true;
	}

	@FXML
	void resetFilters(ActionEvent event) {
		getSessionService().loadTranslations();
		loadTranslations();
		filtered = false;
		findButton.setSelected(false);
	}

	@FXML
	void onCatDictMouseClick(MouseEvent event) {
		if (event.getClickCount() == 2) {
			IdString selectedItem = catDictsList.getSelectionModel().getSelectedItem();
			doFiltering(List.of(selectedItem.getId()), false);
		}
	}

	@FXML
	void findWords(ActionEvent event) {
		findWords();
	}

	@FXML
	void findWordsEnter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			findButton.setSelected(true);
			findWords();
		}
	}

	private void findWords() {
		if (findButton.isSelected()) {
			ObservableList<TranslationRow> translations = FXCollections.observableArrayList();
			List<TranslationRow> searchResults;
			String word = findWordField.getText();
			if (inTranslationsCheck.isSelected()) {
				if (filtered) {
					searchResults = findInDisplayed(TranslationRow::getTranslation, word);
				} else {
					searchResults = entryService.getAllByTranslation(word);
				}
			} else {
				if (filtered) {
					searchResults = findInDisplayed(TranslationRow::getWord, word);
				} else {
					searchResults = entryService.getAllByWord(word);
				}
			}
			translations.addAll(searchResults);
			updateTranslations(translations);
		} else {
			ObservableList<TranslationRow> translations = getSessionService().getTranslations();
			updateTranslations(translations);
		}
	}

	private List<TranslationRow> findInDisplayed(Function<TranslationRow, String> getter, String word) {
		return getSessionService().getTranslations().stream()
				.filter(row -> getter.apply(row).toLowerCase().contains(word.toLowerCase()))
				.collect(Collectors.toList());
	}

	@FXML
	void manageCategories(ActionEvent event) throws IOException {
		categoryEditorController.showStage(null);
	}

	@FXML
	void manageDictionaries(ActionEvent event) throws IOException {
		dictionaryEditorController.showStage(null);
	}

	@FXML
	void manageDictionaryEntries(ActionEvent event) throws IOException {
		dictionaryEntryEditorController.showStage(null);
	}

	@FXML
	void manageWords(ActionEvent event) throws IOException {
		wordEditorController.showStage(null);
	}

	@FXML
	void manageLanguages(ActionEvent event) throws IOException {
		languageEditorController.showStage(null);
	}

	@FXML
	public void quit() {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	public void changeRowSelection(ActionEvent event) {
		Object source = event.getSource();
		System.out.println("The check box dfssdwas clicked!");
	}

}
