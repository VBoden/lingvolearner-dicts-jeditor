package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ua.vboden.dto.CodeString;
import ua.vboden.entities.Language;
import ua.vboden.services.EntityService;
import ua.vboden.services.LanguageService;

@Component
@Scope(value="prototype")
public class LanguageEditorController extends AbstractEditorController<CodeString, Language> {

	@FXML
	private TableView<CodeString> languagesTable;

	@FXML
	private TableColumn<CodeString, String> codeColumn;

	@FXML
	private TableColumn<CodeString, String> titleColumn;

	@FXML
	private TextField languageCode;

	@FXML
	private TextField languageTitle;

	@FXML
	private Label statusMessage;

	@FXML
	private Button saveAsNewButton;

	@Autowired
	private LanguageService languageService;

	@Override
	String getFXML() {
		return "/fxml/languageEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.languages");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		languagesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		codeColumn.setCellValueFactory(new PropertyValueFactory<CodeString, String>("code"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<CodeString, String>("value"));
		initView();
	}

	@Override
	protected void initView() {
		languageService.loadLanguages();
		ObservableList<CodeString> languages = getSessionService().getLanguages();
		languagesTable.setItems(languages);
		statusMessage.setText(MessageFormat.format(getResources().getString("language.status"), languages.size()));
	}

	@Override
	protected EntityService<CodeString, Language> getService() {
		return languageService;
	}

	@Override
	protected Language createNew() {
		return new Language();
	}

	@Override
	protected String checkFilledFields() {
		String newCode = languageCode.getText();
		String newTitle = languageTitle.getText();
		return StringUtils.isBlank(newTitle) || StringUtils.isBlank(newCode) ? "fill" : null;
	}

	protected void populateFields(CodeString current) {
		languageCode.setText(current.getCode());
		languageCode.setEditable(false);
		saveAsNewButton.setDisable(true);
		languageTitle.setText(current.getValue());
	}

	@Override
	protected void populateEntity(Language entity) {
		String newCode = languageCode.getText();
		String newTitle = languageTitle.getText();
		entity.setCode(newCode);
		entity.setName(newTitle);
	}

	@Override
	protected void resetEditing() {
		languageCode.setText("");
		languageTitle.setText("");
		languageCode.setEditable(true);
		saveAsNewButton.setDisable(false);
	}

	@Override
	protected TableView<CodeString> getTable() {
		return languagesTable;
	}
}
