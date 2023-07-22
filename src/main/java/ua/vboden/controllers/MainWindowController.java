package ua.vboden.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;

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
	private TableColumn<TranslationRow, Boolean> selectionRow;

	@FXML
	private TableColumn<TranslationRow, String> wordRow;

	@FXML
	private TableColumn<TranslationRow, String> translationWord;

	@Autowired
	private CategoryEditorController categoryEditorController;

	@Override
	String getFXML() {
		return "/fxml/main.fxml";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getSessionService().loadData();

		selectionRow.setCellValueFactory(cd -> cd.getValue().getSelected());
		selectionRow.setCellFactory(CheckBoxTableCell.forTableColumn(selectionRow));
		wordRow.setCellValueFactory((new PropertyValueFactory<TranslationRow, String>("word")));
		translationWord.setCellValueFactory((new PropertyValueFactory<TranslationRow, String>("translation")));
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

	}

	private void loadTranslations() {
		ObservableList<TranslationRow> translations = getSessionService().getTranslations();
		mainTable.setItems(translations);
		mainTable.getSelectionModel().select(translations.size() - 1);
		mainTable.scrollTo(translations.size());
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
		String selected = catOrDictSelector.getSelectionModel().getSelectedItem();
		if (getResources().getString("filters.selection.categories").equals(selected)) {
			getSessionService().loadTranslationsByCategories(selectedIds, condition);
		} else {
			getSessionService().loadTranslationsByDictionaries(selectedIds, condition);
		}
		loadTranslations();
	}

	@FXML
	void resetFilters(ActionEvent event) {
		getSessionService().loadTranslations();
		loadTranslations();
	}

	@FXML
	void onCatDictMouseClick(MouseEvent event) {
		if (event.getClickCount() == 2) {
			IdString selectedItem = catDictsList.getSelectionModel().getSelectedItem();
			doFiltering(List.of(selectedItem.getId()), false);
		}
	}

	@FXML
	void manageCategories(ActionEvent event) throws IOException {
		categoryEditorController.showStage(null);
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
