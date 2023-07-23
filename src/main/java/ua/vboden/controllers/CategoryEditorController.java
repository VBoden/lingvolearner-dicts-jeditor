package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ua.vboden.dto.IdString;
import ua.vboden.entities.Category;
import ua.vboden.repositories.CategoryRepository;

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

	@Autowired
	private CategoryRepository categoryRepository;

	private IdString current;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		generalInit(resources);
		categoriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		initCategoriesView();
	}

	private void initCategoriesView() {
		ObservableList<IdString> categories = getSessionService().getCategories();
		categoriesList.setItems(categories);
		statusMessage.setText(MessageFormat.format(getResources().getString("category.status"), categories.size()));
	}

	@Override
	String getFXML() {
		return "/fxml/categoryEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.categories");
	}

	@FXML
	void startEditing(MouseEvent event) {
		if (event.getClickCount() == 2) {
			current = categoriesList.getSelectionModel().getSelectedItem();
			categoryName.setText(current.getValue());
		}
	}

	@FXML
	void removeSelected(ActionEvent event) {
		ObservableList<IdString> selected = categoriesList.getSelectionModel().getSelectedItems();
		categoryRepository.deleteAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()));
		updateView();
	}

	@FXML
	void save(ActionEvent event) {
		save();
	}

	private void save() {
		String newTitle = categoryName.getText();
		if (StringUtils.isBlank(newTitle)) {
			return;
		}
		Category entity = null;
		if (current != null) {
			Optional<Category> entityOpt = categoryRepository.findById(current.getId());
			if (entityOpt.isPresent()) {
				entity = entityOpt.get();
			}
		}
		if (entity == null) {
			entity = new Category();
		}
		save(entity);
	}

	@FXML
	void saveEnter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			save();
		}
	}

	@FXML
	void saveNew(ActionEvent event) {
		save(new Category());
	}

	void save(Category entity) {
		String newTitle = categoryName.getText();
		if (StringUtils.isBlank(newTitle)) {
			return;
		}
		entity.setName(newTitle);
		categoryRepository.save(entity);
		resetEditing();
		updateView();
	}

	private void updateView() {
		getSessionService().loadCategories();
		initCategoriesView();
	}

	private void resetEditing() {
		categoryName.setText("");
		current = null;
	}

	@FXML
	void close(ActionEvent event) {
		getStage().close();
	}
}
