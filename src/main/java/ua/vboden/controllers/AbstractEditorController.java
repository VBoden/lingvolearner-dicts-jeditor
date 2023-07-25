package ua.vboden.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ua.vboden.services.EntityService;

public abstract class AbstractEditorController<T, E> extends AbstractController {

	protected abstract void initView();

	protected abstract EntityService<T, E> getService();

	protected abstract ObservableList<T> getSelected();

	protected abstract void resetEditing();

	protected abstract void populateEntity(E entity);

	protected abstract E createNew();

	protected abstract T getCurrent();

	protected abstract boolean isNotFilledFields();

	@FXML
	void removeSelected(ActionEvent event) {
		ObservableList<T> selected = getSelected();
		getService().deleteSelected(selected);
		initView();
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
