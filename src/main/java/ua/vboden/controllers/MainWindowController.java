package ua.vboden.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ua.vboden.dto.TranslationRow;

@Component
public class MainWindowController implements ApplicationContextAware, Initializable {

	@FXML
	private MenuItem menuQuit;

	@FXML
	private TableView<TranslationRow> mainTable;

	@FXML
	private TableColumn<TranslationRow, Boolean> selectionRow;

	@FXML
	private TableColumn<TranslationRow, String> wordRow;

	@FXML
	private TableColumn<TranslationRow, String> translationWord;

	private ApplicationContext applicationContext;

	private ObservableList<TranslationRow> translations;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext != null) {
			return;
		}
		this.applicationContext = applicationContext;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectionRow.setCellValueFactory(cd -> cd.getValue().getSelected());
		selectionRow.setCellFactory(CheckBoxTableCell.forTableColumn(selectionRow));
//		selectionRow.setOnEditStart(e -> {
//			System.out.println(translations.get(0));
//			System.out.println(translations.get(1));
//			System.out.println(translations.get(2));
//            System.out.println("The check box was clicked!");
//        });
//		selectionRow.setOnEditCommit(e -> {
//			System.out.println(translations.get(0));
//			System.out.println(translations.get(1));
//			System.out.println(translations.get(2));
//            System.out.println("The check box was commit!");
//        });
//		selectionRow.setOnEditCancel(e -> {
//			System.out.println(translations.get(0));
//			System.out.println(translations.get(1));
//			System.out.println(translations.get(2));
//			System.out.println("The check box was cancel!");
//        });
		wordRow.setCellValueFactory((new PropertyValueFactory<TranslationRow, String>("word")));
		translationWord.setCellValueFactory((new PropertyValueFactory<TranslationRow, String>("translation")));
		translations = FXCollections.observableArrayList();
		translations.add(new TranslationRow(0L, "www", "ttt"));
		translations.add(new TranslationRow(1L, "www1", "ttt1"));
		translations.add(new TranslationRow(2L, "www2", "ttt2"));
		mainTable.setItems(translations);

	}

	@FXML
	public void quit() {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	public void changeRowSelection(ActionEvent event) {
		Object source = event.getSource();
		System.out.println("The check box dfssdwas clicked!");
	}

}
