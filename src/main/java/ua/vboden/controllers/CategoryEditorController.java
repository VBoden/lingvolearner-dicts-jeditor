package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ua.vboden.dto.IdString;

@Component
public class CategoryEditorController extends AbstractController {

	@FXML
	private ListView<IdString> categoriesList;

	@FXML
	private TextField categoryName;

	@FXML
	private Button closeButton;

	@FXML
	private Button removeCategory;

	@FXML
	private Button saveAsNewCategory;

	@FXML
	private Button saveCategory;

	@FXML
	private Label statusMessage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		generalInit(resources);
		ObservableList<IdString> categories = getSessionService().getCategories();
		categoriesList.setItems(categories);
		statusMessage.setText(MessageFormat.format(resources.getString("category.status"), categories.size()));
	}

	@Override
	String getFXML() {
		return "/fxml/categoryEditor.fxml";
	}

	@FXML
	void close(ActionEvent event) {
		getStage().close();
	}
}
