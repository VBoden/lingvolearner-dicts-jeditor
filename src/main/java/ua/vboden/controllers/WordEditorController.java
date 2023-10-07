package ua.vboden.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.IdString;
import ua.vboden.dto.TranslationRow;
import ua.vboden.dto.WordData;
import ua.vboden.entities.Word;
import ua.vboden.services.CategoryService;
import ua.vboden.services.EntityService;
import ua.vboden.services.EntryService;
import ua.vboden.services.LanguageService;
import ua.vboden.services.WordService;

@Component
@Scope(value="prototype")
public class WordEditorController extends AbstractEditorController<WordData, Word> {

	@FXML
	private TableView<WordData> wordsTable;

	@FXML
	private TableColumn<WordData, String> wordColumn;

	@FXML
	private TableColumn<WordData, String> languageColumn;

	@FXML
	private TableColumn<WordData, String> categoriesColumn;

	@FXML
	private TableColumn<WordData, Integer> usagesColumn;

	@FXML
	private TableColumn<WordData, String> notesColumn;

	@FXML
	private TextField word;

	@FXML
	private TextField wordNotes;

	@FXML
	private ListView<IdString> categoryList;

	@FXML
	private ComboBox<CodeString> language;

	@FXML
	private Label statusMessage;

	@FXML
	private TextField searchField;

	@FXML
	private ToggleButton findButton;

	@Autowired
	private WordService wordService;

	@Autowired
	private LanguageService languageService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private EntryService entryService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wordsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		wordColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("word"));
		languageColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("language"));
		categoriesColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("categories"));
		usagesColumn.setCellValueFactory(new PropertyValueFactory<WordData, Integer>("usages"));
		notesColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("notes"));
		languageService.loadLanguages();
		categoryService.loadCategories();
		categoryList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		initView();
	}

	@Override
	protected void initView() {
//		wordService.loadData();
//		ObservableList<WordData> words = getSessionService().getWords();
//		wordsTable.setItems(words);
		findWords();
		categoryList.setItems(getSessionService().getCategories());
		language.setItems(getSessionService().getLanguages());
//		statusMessage.setText(MessageFormat.format(getResources().getString("word.status"), words.size()));
		if (getCurrent() != null) {
			populateFields(getCurrent());
		}
	}

	public void showStage(Object object, WordData selected) throws IOException {
		setCurrent(selected);
		super.showStage(null);
	}

	@Override
	protected EntityService<WordData, Word> getService() {
		return wordService;
	}

	@Override
	protected void resetEditing() {
		wordsTable.getSelectionModel().clearSelection();
		word.setText("");
		wordNotes.setText("");
		language.getSelectionModel().clearSelection();
		categoryList.getSelectionModel().clearSelection();
	}

	@Override
	protected void populateEntity(Word entity) {
		entity.setWord(word.getText());
		entity.setNotes(wordNotes.getText());
		entity.setLanguage(languageService.getByCode(language.getSelectionModel().getSelectedItem().getCode()));
		entity.setCategory(categoryService.findEntities(categoryList.getSelectionModel().getSelectedItems()));

	}

	@Override
	protected Word createNew() {
		return new Word();
	}

	@Override
	protected String checkFilledFields() {
		return StringUtils.isBlank(word.getText()) ? "fill" : null;
	}

	@Override
	protected void populateFields(WordData current) {
		word.setText(current.getWord());
		wordNotes.setText(current.getNotes());
		language.getSelectionModel().select(find(current.getLanguage(), language.getItems()));
		categoryList.getSelectionModel().clearSelection();
		String categories = current.getCategories();
		if (categories != null && !categories.isEmpty())
			categoryList.getSelectionModel().selectIndices(-1, findCategories(categories, categoryList.getItems()));
	}

	private int[] findCategories(String categories, ObservableList<IdString> items) {
		String[] categs = categories.split("\n");
		List<Integer> indices = new ArrayList<>();
		for (String cat : categs) {
			for (IdString item : items) {
				if (item.getValue().equalsIgnoreCase(cat.trim())) {
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
		return "/fxml/wordEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.words");
	}

	@Override
	protected TableView<WordData> getTable() {
		return wordsTable;
	}

	@FXML
	void findWordsEnter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			findButton.setSelected(true);
			findWords();
		}
	}

	@FXML
	void findWord(ActionEvent event) {
		findWords();
	}

	private void findWords() {
		if (findButton.isSelected()) {
			String word = searchField.getText();
			if (StringUtils.isNoneBlank(word)) {
				ObservableList<WordData> words = FXCollections.observableArrayList(wordService.getAllByWord(word));
				wordsTable.setItems(words);
				statusMessage.setText(MessageFormat.format(getResources().getString("word.status"), words.size()));
			}
		} else {
			wordService.loadData();
			ObservableList<WordData> words = getSessionService().getWords();
			wordsTable.setItems(words);
			statusMessage.setText(MessageFormat.format(getResources().getString("word.status"), words.size()));
		}
	}

	@Override
	protected boolean allowedDeleting(WordData sel) {
		return entryService.getWordUsages(wordService.findEntity(sel)) == 0;
	}
}
