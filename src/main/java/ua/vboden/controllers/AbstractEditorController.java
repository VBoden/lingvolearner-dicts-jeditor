package ua.vboden.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class AbstractEditorController extends AbstractController {

	@FXML
	void save(ActionEvent event) {
		save();
	}

	protected abstract void save();
}
