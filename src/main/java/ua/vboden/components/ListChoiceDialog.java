package ua.vboden.components;

import java.util.List;

import com.sun.javafx.scene.control.skin.resources.ControlResources;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ListChoiceDialog<T> extends Dialog<List<T>> {
	private final GridPane grid;
	private final Label label;
	private ListView<T> dataList;

	public ListChoiceDialog(ObservableList<T> items) {
		final DialogPane dialogPane = getDialogPane();

		this.grid = new GridPane();
		this.grid.setHgap(10);
		this.grid.setMaxWidth(Double.MAX_VALUE);
		this.grid.setAlignment(Pos.CENTER_LEFT);

		label = createContentLabel(dialogPane.getContentText());
		label.setPrefWidth(Region.USE_COMPUTED_SIZE);
		label.textProperty().bind(dialogPane.contentTextProperty());

		dialogPane.contentTextProperty().addListener(o -> updateGrid());

		setTitle(ControlResources.getString("Dialog.confirm.title"));
		dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
		dialogPane.getStyleClass().add("choice-dialog");
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		final double MIN_WIDTH = 150;

		dataList = new ListView<T>();
		dataList.setMinWidth(MIN_WIDTH);
		dataList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		if (dataList != null) {
			dataList.setItems(items);
		}
		dataList.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(dataList, Priority.ALWAYS);
		GridPane.setFillWidth(dataList, true);

		updateGrid();

		setResultConverter((dialogButton) -> {
			ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
			return data == ButtonData.OK_DONE ? getSelectedItem() : null;
		});
	}

	static Label createContentLabel(String text) {
		Label label = new Label(text);
		label.setMaxWidth(Double.MAX_VALUE);
		label.setMaxHeight(Double.MAX_VALUE);
		label.getStyleClass().add("content");
		label.setWrapText(true);
		label.setPrefWidth(360);
		return label;
	}

	public final List<T> getSelectedItem() {
		return dataList.getSelectionModel().getSelectedItems();
	}

	private void updateGrid() {
		grid.getChildren().clear();

		grid.add(label, 0, 0);
		grid.add(dataList, 1, 0);
		getDialogPane().setContent(grid);

		Platform.runLater(() -> dataList.requestFocus());
	}
}
