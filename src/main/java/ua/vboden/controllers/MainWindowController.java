package ua.vboden.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ua.vboden.components.ListChoiceDialog;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.Category;
import ua.vboden.entities.Dictionary;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Word;
import ua.vboden.services.CategoryService;
import ua.vboden.services.DictionaryService;
import ua.vboden.services.EntryService;
import ua.vboden.services.IOService;
import ua.vboden.services.WordService;

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
	private TableColumn<TranslationRow, String> transCategoryColumn;

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
	private PrefsEditorController prefsEditorController;

	@Autowired
	private EntryService entryService;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private WordService wordService;

	@Autowired
	private IOService ioService;

	private boolean filtered;

	private String selectedCatOrDictName;

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

		numberColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, Integer>("number"));
		wordColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("word"));
		translationColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("translation"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("categories"));
		transCategoryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("transCategories"));
		dictionaryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("dictionaries"));
		updateTranslationsView();
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

	private void updateTranslationsView() {
		ObservableList<TranslationRow> translations = getSessionService().getTranslations();
		updateTranslations(translations);
//		mainTable.getSelectionModel().select(translations.size() - 1);
//		mainTable.scrollTo(translations.size());
	}

	private void updateTranslations(ObservableList<TranslationRow> translations) {
//		if (getSessionService().isDisplayDefaultLanguagesOnly()) {
//			FilteredList<TranslationRow> filtered = new FilteredList<>(translations);
//			filtered.setPredicate(
//					row -> row.getWordLangCode().equals(getSessionService().getDefaultLanguageFrom().getCode()) && row
//							.getTranslationLangCode().equals(getSessionService().getDefaultLanguageTo().getCode()));
//			mainTable.setItems(filtered);
//		} else {
		mainTable.setItems(translations);
//		}
		statusMessage1
				.setText(MessageFormat.format(getResources().getString("translations.status"), translations.size()));
	}

	private void loadCategories() {
		ObservableList<IdString> categories = getSessionService().getCategoriesWithEmpty();
		catDictsList.setItems(categories);
		catDictsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		statusMessage2.setText(MessageFormat.format(getResources().getString("category.status"), categories.size()));
	}

	private void loadDictionaries() {
		ObservableList<IdString> dictionaries = getSessionService().getDictionariesWithEmpty();
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
			entryService.loadTranslationsByCategories(selectedIds, condition);
			if (selectedIds.get(0) != null)
				selectedCatOrDictName = categoryService.findEntity(selectedIds.get(0)).getName();
		} else {
			entryService.loadTranslationsByDictionaries(selectedIds, condition);
			if (selectedIds.get(0) != null)
				selectedCatOrDictName = dictionaryService.findEntity(selectedIds.get(0)).getName();
		}
		updateTranslationsView();
		filtered = true;
	}

	@FXML
	void resetFilters(ActionEvent event) {
		entryService.loadTranslations();
		updateTranslationsView();
		filtered = false;
		findButton.setSelected(false);
		selectedCatOrDictName = null;
	}

	@FXML
	void onCatDictMouseClick(MouseEvent event) {
		if (event.getClickCount() == 2) {
			IdString selectedItem = catDictsList.getSelectionModel().getSelectedItem();
			doFiltering(Collections.singletonList(selectedItem.getId()), false);
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
	void startEditing(MouseEvent event) throws IOException {
		if (event.getClickCount() == 2) {
			TranslationRow selected = mainTable.getSelectionModel().getSelectedItem();
			dictionaryEntryEditorController.showStage(null, selected);
		}
	}

	@FXML
	void addToCategory(ActionEvent event) {
		checkSelectedAndExecute(getSessionService()::getCategories, this::doAddToCategory);
	}

	@FXML
	void removeFromCategory(ActionEvent event) {
		checkSelectedAndExecute(getSessionService()::getCategories, this::doRemoveFromCategory);
	}

	@FXML
	void addToDictionary(ActionEvent event) {
		checkSelectedAndExecute(getSessionService()::getDictionaries, this::doAddToDictionary);
	}

	@FXML
	void removeFromDictionary(ActionEvent event) {
		checkSelectedAndExecute(getSessionService()::getDictionaries, this::doRemoveFromDictionary);
	}

	private ObservableList<TranslationRow> checkSelectedAndExecute(Supplier<ObservableList<IdString>> itemsGetter,
			BiConsumer<TranslationRow, List<IdString>> translationAction) {
		ObservableList<TranslationRow> selectedEntries = mainTable.getSelectionModel().getSelectedItems();
		if (selectedEntries.size() == 0) {
			showInformationAlert("No items selected!");
		} else {
			ListChoiceDialog<IdString> dialog = new ListChoiceDialog<>(itemsGetter.get());
			Optional<List<IdString>> result = dialog.showAndWait();
			if (result.isPresent()) {
				for (TranslationRow translation : selectedEntries) {
					translationAction.accept(translation, result.get());
				}
				entryService.loadTranslations(getSessionService().getTranslationIds());
				updateTranslationsView();
			}
		}
		return selectedEntries;
	}

	private <T> List<T> findNewOnly(List<T> collection1, List<T> collection2) {
		return collection1.stream().filter(el -> !collection2.contains(el)).collect(Collectors.toList());
	}

	private void doAddToCategory(TranslationRow trnaslation, List<IdString> selectedCategories) {
		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
		List<Category> categoryEntities = categoryService.findEntities(selectedCategories);
		Word wordEntity = entryEntity.getWord();
		wordEntity.getCategory().addAll(findNewOnly(categoryEntities, wordEntity.getCategory()));
		wordService.save(wordEntity);
		Word transEntity = entryEntity.getTranslation();
		transEntity.getCategory().addAll(findNewOnly(categoryEntities, transEntity.getCategory()));
		wordService.save(transEntity);
	}

	private void doRemoveFromCategory(TranslationRow trnaslation, List<IdString> selectedCategories) {
		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
		List<Category> categoryEntities = categoryService.findEntities(selectedCategories);
		Word wordEntity = entryEntity.getWord();
		wordEntity.getCategory().removeAll(categoryEntities);
		wordService.save(wordEntity);
		Word transEntity = entryEntity.getTranslation();
		transEntity.getCategory().removeAll(categoryEntities);
		wordService.save(transEntity);
	}

	private void doAddToDictionary(TranslationRow trnaslation, List<IdString> selectedDictionaries) {
		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
		List<Dictionary> dictionaryEntities = dictionaryService.findEntities(selectedDictionaries);
		entryEntity.getDictionary().addAll(findNewOnly(dictionaryEntities, entryEntity.getDictionary()));
		entryService.save(entryEntity);
	}

	private void doRemoveFromDictionary(TranslationRow trnaslation, List<IdString> selectedDictionaries) {
		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
		List<Dictionary> dictionaryEntities = dictionaryService.findEntities(selectedDictionaries);
		entryEntity.getDictionary().removeAll(dictionaryEntities);
		entryService.save(entryEntity);
	}

	@FXML
	void removeSelected(ActionEvent event) {
		ObservableList<TranslationRow> selectedEntries = mainTable.getSelectionModel().getSelectedItems();
		if (selectedEntries.size() == 0) {
			showInformationAlert("No items selected!");
		} else {
			List<String> deleteNames = new ArrayList<>();
			selectedEntries.stream().forEach(entry -> {
				deleteNames.add(entry.toString());

			});
			Alert alert = new Alert(AlertType.CONFIRMATION, "Delete  ?\n" + StringUtils.join(deleteNames, "\n"),
					ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				List<Integer> usedWords = new ArrayList<>();
				entryService.getAllBySelected(selectedEntries).stream().forEach(entity -> {
					usedWords.add(entity.getWord().getId());
					usedWords.add(entity.getTranslation().getId());
				});
				entryService.deleteSelected(selectedEntries);

				for (Word word : wordService.getAllByIds(usedWords)) {
					if (entryService.getWordUsages(word) == 0) {
						wordService.delete(word);
					} else {
						getSessionService().decreaseUsages(word.getId());
					}
				}

				entryService.loadTranslations(getSessionService().getTranslationIds());
				updateTranslationsView();
			}
		}
	}

	@FXML
	void exportEntries(ActionEvent event) {
		ObservableList<TranslationRow> selectedEntries = mainTable.getSelectionModel().getSelectedItems();
		if (selectedEntries.size() == 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Not selected items, all displayed will be exported.",
					ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				selectedEntries = mainTable.getItems();
			} else {
				return;
			}
		}
		DictionaryEntry firstEntity = entryService.findEntity(selectedEntries.get(0));
		String name = getLanguage(firstEntity.getWord()) + "-" + getLanguage(firstEntity.getTranslation()) + '_';
		if (selectedCatOrDictName != null) {
			name += selectedCatOrDictName;
		}
		name += "_" + getCurrentDate();
		TextInputDialog dialog = new TextInputDialog(name);
		dialog.setTitle("Export to file");
		dialog.setHeaderText("Please enter file name. Will be saved with extension .vcb");
		dialog.setContentText("File name:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			name = result.get() + ".vcb";
			ioService.exportToFile(selectedEntries, name);
		}
		showInformationAlert(getResources().getString("export.finished"));
	}

	private String getLanguage(Word word) {
		return word.getLanguage().getCode();
	}

	@FXML
	void exportEntriesAllCategories(ActionEvent event) {
		ObservableList<IdString> categories = getSessionService().getCategories();
		categories.add(0, new IdString(null, "No_Category"));
		for (IdString category : categories) {
			List<DictionaryEntry> entries = entryService.getAllByCategoryId(category.getId());
			String dirName = "cagegories/";
			ioService.exportEntries(category.getValue(), entries, dirName);
		}
		showInformationAlert(getResources().getString("export.finished"));
	}

	private String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}

	@FXML
	void exportEntriesAllDictionaries(ActionEvent event) {
		ObservableList<IdString> dictionaries = getSessionService().getDictionaries();
		dictionaries.add(0, new IdString(null, "No_Dictionary"));
		for (IdString dictionary : dictionaries) {
			List<DictionaryEntry> entries = entryService.getAllByDictionaryId(dictionary.getId());
			String dirName = "dictionaries/";
			ioService.exportEntries(dictionary.getValue(), entries, dirName);
		}
		showInformationAlert(getResources().getString("export.finished"));
	}

	@FXML
	void openSettings(ActionEvent event) throws IOException {
		prefsEditorController.showStage(null);
	}

	@FXML
	void reloadTranslations(ActionEvent event) {
		entryService.loadTranslations(getSessionService().getTranslationIds());
	}

	@FXML
	void doEditTranslationWord(ActionEvent event) throws IOException {
		TranslationRow selected = mainTable.getSelectionModel().getSelectedItem();
		wordEditorController.showStage(null, wordService.getDataById(selected.getTranslationId()));
	}

	@FXML
	void doEidtWord(ActionEvent event) throws IOException {
		TranslationRow selected = mainTable.getSelectionModel().getSelectedItem();
		wordEditorController.showStage(null, wordService.getDataById(selected.getWordId()));
	}

	@FXML
	void doSpeakWord(ActionEvent event) {

	}

}
