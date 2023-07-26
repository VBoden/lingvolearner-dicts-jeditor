package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ua.vboden.dto.CodeString;
import ua.vboden.dto.WordData;
import ua.vboden.entities.Word;
import ua.vboden.services.EntityService;
import ua.vboden.services.WordService;

@Component
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
	private TableColumn<WordData, String> notesColumn;

	@FXML
	private TextField word;

	@FXML
	private TextField wordNotes;

	@FXML
	private ComboBox<CodeString> language;

	@FXML
	private Label statusMessage;

	@Autowired
	private WordService wordService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wordsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		wordColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("word"));
		languageColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("language"));
		categoriesColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("categories"));
		notesColumn.setCellValueFactory(new PropertyValueFactory<WordData, String>("notes"));
		initView();
	}

	@Override
	protected void initView() {
		wordService.loadData();
		ObservableList<WordData> words = getSessionService().getWords();
		wordsTable.setItems(words);
		statusMessage.setText(MessageFormat.format(getResources().getString("word.status"), words.size()));
	}

	@Override
	protected EntityService<WordData, Word> getService() {
		return wordService;
	}

	@Override
	protected void resetEditing() {
		word.setText("");
		wordNotes.setText("");
		
	}

	@Override
	protected void populateEntity(Word entity) {
		entity.setWord(word.getText());
		entity.setNotes(word.getText());
		
	}

	@Override
	protected Word createNew() {
		return new Word();
	}

	@Override
	protected boolean isNotFilledFields() {
		return StringUtils.isBlank(word.getText());
	}

	@Override
	protected void populateFields(WordData current) {
		word.setText(current.getWord());
		wordNotes.setText(current.getNotes());
		
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


}
