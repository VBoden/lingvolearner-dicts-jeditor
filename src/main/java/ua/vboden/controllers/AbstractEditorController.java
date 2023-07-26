package ua.vboden.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ua.vboden.dto.CodeString;
import ua.vboden.services.EntityService;

public abstract class AbstractEditorController<T, E> extends AbstractController {

	private T current;

	protected abstract void initView();

	protected abstract EntityService<T, E> getService();

	protected abstract ObservableList<T> getSelected();

	protected abstract void resetEditing();

	protected abstract void populateEntity(E entity);

	protected abstract E createNew();

	protected abstract boolean isNotFilledFields();

	protected abstract void populateFields(T current);

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
		getService().deleteSelected(selected);
		initView();
	}

	@FXML
	void cleanFields(ActionEvent event) {
		resetEditing();
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
		if (isNotFilledFields()) {
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
		if (isNotFilledFields()) {
			return;
		}
		populateEntity(entity);
		getService().save(entity);
		resetEditing();
		initView();
	}

	@FXML
	void close(ActionEvent event) {
		getStage().close();
	}

}
