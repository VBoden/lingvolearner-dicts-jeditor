package ua.vboden.controllers;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ua.vboden.dto.IdString;
import ua.vboden.entities.Category;
import ua.vboden.services.CategoryService;
import ua.vboden.services.EntityService;

@Component
public class CategoryEditorController extends AbstractEditorController<IdString, Category> {

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
	private CategoryService categoryService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		generalInit(resources);
		categoriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		initView();
	}

	@Override
	protected void initView() {
		categoryService.loadCategories();
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

	@Override
	protected void resetEditing() {
		categoryName.setText("");
		setCurrent(null);
	}

	@Override
	protected EntityService<IdString, Category> getService() {
		return categoryService;
	}

	@Override
	protected ObservableList<IdString> getSelected() {
		return categoriesList.getSelectionModel().getSelectedItems();
	}

	@Override
	protected void populateEntity(Category entity) {
		String newTitle = categoryName.getText();
		entity.setName(newTitle);
	}

	@Override
	protected Category createNew() {
		return new Category();
	}

	@Override
	protected boolean isNotFilledFields() {
		String newTitle = categoryName.getText();
		return StringUtils.isBlank(newTitle);
	}

	@Override
	protected void populateFields(IdString current) {
		categoryName.setText(current.getValue());
	}

	@Override
	protected TableView<IdString> getTable() {
		return null;
	}
}
