package ua.vboden.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ua.vboden.services.EntityService;

public abstract class AbstractEditorController<T, E> extends AbstractController {

	private T current;

	protected abstract void initView();

	protected abstract EntityService<T, E> getService();

	protected abstract TableView<T> getTable();

	protected abstract void resetEditing();

	protected abstract void populateEntity(E entity);

	protected abstract E createNew();

	protected abstract String checkFilledFields();

	protected abstract void populateFields(T current);

	protected ObservableList<T> getSelected() {
		return getTable().getSelectionModel().getSelectedItems();
	}

	@FXML
	void startEditing(MouseEvent event) {
		if (event.getClickCount() == 2) {
			current = getSelected().get(0);
			populateFields(getCurrent());
		}
	}

	protected T getCurrent() {
		return current;
	}

	protected void setCurrent(T current) {
		this.current = current;
	}

	@FXML
	void removeSelected(ActionEvent event) {
		ObservableList<T> selected = getSelected();
		ObservableList<T> forDelete = FXCollections.observableArrayList();
		List<String> deleteNames = new ArrayList<>();
		for (T sel : selected) {
			if (allowedDeleting(sel)) {
				forDelete.add(sel);
				deleteNames.add(sel.toString());
			}
		}

		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete  ?\n" + StringUtils.join(deleteNames, "\n"),
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			getService().deleteSelected(forDelete);
			initView();
		}
	}

	protected boolean allowedDeleting(T sel) {
		return true;
	}

	@FXML
	void cleanFields(ActionEvent event) {
		resetEditionGlobal();
	}

	@FXML
	void saveEnter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			save();
		}
	}

	@FXML
	void save(ActionEvent event) {
		save();
	}

	@FXML
	void saveNew(ActionEvent event) {
		save(createNew());
	}

	protected void save() {
		if (StringUtils.isNotBlank(checkFilledFields())) {
			showInformationAlert(checkFilledFields());
			return;
		}
		E entity = null;
		if (getCurrent() != null) {
			entity = getService().findEntity(getCurrent());
		}
		if (entity == null) {
			entity = createNew();
		}
		save(entity);
	}

	protected void save(E entity) {
		if (StringUtils.isNotBlank(checkFilledFields())) {
			showInformationAlert(checkFilledFields());
			return;
		}
		populateEntity(entity);
		getService().save(entity);
		resetEditionGlobal();
		initView();
	}

	protected void resetEditionGlobal() {
		resetEditing();
		setCurrent(null);
	}

	@FXML
	void close(ActionEvent event) {
		getStage().close();
	}
}
