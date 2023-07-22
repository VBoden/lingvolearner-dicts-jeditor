package ua.vboden.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
		catOrDictSelector.setItems(FXCollections.observableArrayList("Categories", "Ditctionaries"));
		catOrDictSelector.getSelectionModel().select(0);
		loadCategories();
		statusMessage3.setText(MessageFormat.format(resources.getString("dictionary.status"),
				getSessionService().getDictionaries().size()));

	}

	private void loadTranslations() {
		ObservableList<TranslationRow> translations = getSessionService().getTranslations();
		mainTable.setItems(translations);
		statusMessage1
				.setText(MessageFormat.format(getResources().getString("translations.status"), translations.size()));
	}

	private void loadCategories() {
		ObservableList<IdString> categories = getSessionService().getCategories();
		catDictsList.setItems(categories);
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
		if ("Categories".equals(selected)) {
			loadCategories();
		} else {
			loadDictionaries();
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
