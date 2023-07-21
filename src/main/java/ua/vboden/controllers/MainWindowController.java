package ua.vboden.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.Category;
import ua.vboden.entities.Dictionary;
import ua.vboden.repositories.CategoryRepository;
import ua.vboden.repositories.DictionaryRepository;
import ua.vboden.services.EntryService;

@Component
public class MainWindowController implements ApplicationContextAware, Initializable {

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
	private EntryService entryService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private DictionaryRepository dictionaryRepository;

	private ApplicationContext applicationContext;

	private ObservableList<TranslationRow> translations;

	private ObservableList<IdString> categories;

	private ObservableList<IdString> dictionaries;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext != null) {
			return;
		}
		this.applicationContext = applicationContext;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectionRow.setCellValueFactory(cd -> cd.getValue().getSelected());
		selectionRow.setCellFactory(CheckBoxTableCell.forTableColumn(selectionRow));
//		selectionRow.setOnEditStart(e -> {
//			System.out.println(translations.get(0));
//			System.out.println(translations.get(1));
//			System.out.println(translations.get(2));
//            System.out.println("The check box was clicked!");
//        });
//		selectionRow.setOnEditCommit(e -> {
//			System.out.println(translations.get(0));
//			System.out.println(translations.get(1));
//			System.out.println(translations.get(2));
//            System.out.println("The check box was commit!");
//        });
//		selectionRow.setOnEditCancel(e -> {
//			System.out.println(translations.get(0));
//			System.out.println(translations.get(1));
//			System.out.println(translations.get(2));
//			System.out.println("The check box was cancel!");
//        });
		wordRow.setCellValueFactory((new PropertyValueFactory<TranslationRow, String>("word")));
		translationWord.setCellValueFactory((new PropertyValueFactory<TranslationRow, String>("translation")));
		loadTranslations();
		catOrDictSelector.setItems(FXCollections.observableArrayList("Categories", "Ditctionaries"));
		catOrDictSelector.getSelectionModel().select(0);
//		catDictsList.setCellFactory(new Callback<ListView<TranslationRow>, ListCell<TranslationRow>>() {
//			
//			@Override
//			public ListCell<TranslationRow> call(ListView<TranslationRow> param) {
//				System.out.println(param);
//				final ListCell<TranslationRow> cell = new ListCell<TranslationRow>() {
//		              @Override
//		              public void updateItem(TranslationRow item, boolean empty) {
//		                super.updateItem(item, empty);
//		                if (item != null) {
//		                  setText(item.getWord());
//		                }
//		              }
//		            }; // ListCell
//		            return cell;
//			}
//		});
		loadCategories();

	}

	private void loadTranslations() {
		translations = FXCollections.observableArrayList();
		entryService.getAllEntries().forEach(entry -> translations
				.add(new TranslationRow(entry.getId(), entry.getWord().getWord(), entry.getTranslation().getWord())));
//		translations.add(new TranslationRow(0L, "www", "ttt"));
//		translations.add(new TranslationRow(1L, "www1", "ttt1"));
//		translations.add(new TranslationRow(2L, "www2", "ttt2"));
		mainTable.setItems(translations);
		statusMessage1.setText("Всього слів у словнику: " + String.valueOf(translations.size()));
	}

	private void loadCategories() {
		categories = FXCollections.observableArrayList();
		categoryRepository.findAll()
				.forEach(entry -> categories.add(new IdString(entry.getId(), entry.getName())));
		catDictsList.setItems(categories);
		statusMessage2.setText("Всього категорій: " + String.valueOf(categories.size()));
	}

	private void loadDictionaries() {
		dictionaries = FXCollections.observableArrayList();
		dictionaryRepository.findAll()
				.forEach(entry -> dictionaries.add(new IdString(entry.getId(), entry.getName()+" ("+entry.getLanguageFrom().getName()+"-"+entry.getLanguageTo().getName()+")")));
		catDictsList.setItems(dictionaries);
		statusMessage3.setText("Всього словників: " + String.valueOf(dictionaries.size()));
	}

	@FXML
	void catOrDictChanged(ActionEvent event) {
		String selected = catOrDictSelector.getSelectionModel().getSelectedItem();
		if("Categories".equals(selected)) {
			loadCategories();
		}else {
			loadDictionaries();
		}
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
