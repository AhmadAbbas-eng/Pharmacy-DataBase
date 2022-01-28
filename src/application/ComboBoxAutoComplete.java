package application;

import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 
 * Uses a combobox tooltip as the suggestion for auto complete and updates the
 * combo box itens accordingly <br />
 * It does not work with space, space and escape cause the combobox to hide and
 * clean the filter ... Send me a PR if you want it to work with all characters
 * -> It should be a custom controller - I KNOW!
 * 
 * @author wsiqueir
 *
 * @param <T>
 */
public class ComboBoxAutoComplete<T> {

	static boolean answer = false;

	private ComboBox<T> cmb;
	String filter = "";
	private ObservableList<T> originalItems;
	private Object caller;

	public ComboBoxAutoComplete(ComboBox<T> cmb, Object caller) {
		this.cmb = cmb;
		originalItems = FXCollections.observableArrayList(cmb.getItems());
		cmb.setTooltip(new Tooltip());
		cmb.setOnKeyPressed(this::handleOnKeyPressed);
		cmb.setOnHidden(this::handleOnHiding);
		this.caller = caller;
	}

	public void handleOnKeyPressed(KeyEvent e) {
		ObservableList<T> filteredList = FXCollections.observableArrayList();
		KeyCode code = e.getCode();

		if (code.isLetterKey()) {
			filter += e.getText();
		}
		if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
			filter = filter.substring(0, filter.length() - 1);
			cmb.getItems().setAll(originalItems);
		}
		if (code == KeyCode.ESCAPE) {
			filter = "";
		}
		if (filter.length() == 0) {
			filteredList = originalItems;
			cmb.getTooltip().hide();
		} else {
			Stream<T> items = cmb.getItems().stream();
			String txtUsr = filter.toString().toLowerCase();
			items.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
			cmb.getTooltip().setText(txtUsr);
			Window stage = cmb.getScene().getWindow();
			Bounds boundsInScene = cmb.localToScene(cmb.getBoundsInLocal());
			double posX = boundsInScene.getCenterX() + stage.getX();
			double posY = boundsInScene.getCenterY() + stage.getY() - 15;
			cmb.getTooltip().show(stage, posX, posY);
			cmb.show();
		}
		cmb.getItems().setAll(filteredList);
		if (filteredList.isEmpty()) {
			Stage confiemWindow = new Stage();
			confiemWindow.initModality(Modality.APPLICATION_MODAL);
			confiemWindow.setTitle("Add new manufacturer");
			confiemWindow.setMinWidth(500);
			confiemWindow.setHeight(200);
			Label newManufacturerLabel = new Label();
			Label confirmManufacturerLabel = new Label();

			confirmManufacturerLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
			confirmManufacturerLabel.setText("  Enter the full manufacturer name :");
			newManufacturerLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
			confirmManufacturerLabel.setTextFill(Color.valueOf("1d6f84"));
			newManufacturerLabel.setTextFill(Color.valueOf("1d6f84"));
			newManufacturerLabel.setText("  Do you want to add a new manufacturer ?");

			Button yesButton = new Button("Yes");
			Button noButton = new Button("No");

			noButton.setTextFill(Color.WHITE);
			noButton.setStyle(" -fx-background-color:  #1D6F84;\r\n" + "  -fx-background-radius: 10;");
			noButton.setOnMouseEntered(event1 -> {
				noButton.setStyle("-fx-background-color: #278FAA;" + "  -fx-background-radius: 10;");
			});

			noButton.setOnMouseExited(event1 -> {
				noButton.setStyle(" -fx-background-color:  #1D6F84;" + "  -fx-background-radius: 10;");
			});

			noButton.setOnMouseReleased(event1 -> {
				noButton.setStyle(" -fx-background-color:  #1D6F84;" + "  -fx-background-radius: 10;");
			});

			noButton.setOnMousePressed(event1 -> {
				noButton.setStyle(" -fx-background-color:  #1A6477;" + "  -fx-background-radius: 10;");
			});

			yesButton.setTextFill(Color.WHITE);
			yesButton.setStyle(" -fx-background-color:  #1D6F84;\r\n" + "  -fx-background-radius: 10;");
			yesButton.setOnMouseEntered(event1 -> {
				yesButton.setStyle("-fx-background-color: #278FAA;" + "  -fx-background-radius: 10;");
			});

			yesButton.setOnMouseExited(event1 -> {
				yesButton.setStyle(" -fx-background-color:  #1D6F84;" + "  -fx-background-radius: 10;");
			});

			yesButton.setOnMouseReleased(event1 -> {
				yesButton.setStyle(" -fx-background-color:  #1D6F84;" + "  -fx-background-radius: 10;");
			});

			yesButton.setOnMousePressed(event1 -> {
				yesButton.setStyle(" -fx-background-color:  #1A6477;" + "  -fx-background-radius: 10;");
			});

			TextField fullName = new TextField(filter);
			fullName.setText(filter);
			fullName.setMinWidth(151);
			fullName.setMinHeight(27);

			yesButton.setOnAction(event -> {
				answer = true;
				String newName = fullName.getText().toString();
				if (newName.isBlank() == false) {
					if (caller instanceof AddProductController)
						AddProductController.manufacturerObservableList.add(newName);
					else if(caller instanceof ProductEditController)
						ProductEditController.manufacturerObservableList.add(newName);
					originalItems.add((T) newName);
					confiemWindow.close();
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Empty Field");
					alert.setHeaderText(null);
					alert.setContentText("You Have To Fill The Text Field");
					alert.showAndWait();
				}

			});

			noButton.setOnAction(event -> {
				answer = false;
				confiemWindow.close();
			});

			HBox textBoxLayout = new HBox(10);
			VBox layout = new VBox(10);
			HBox buttonsLayout = new HBox(20);

			textBoxLayout.setAlignment(Pos.CENTER_LEFT);
			textBoxLayout.getChildren().setAll(confirmManufacturerLabel, fullName);
			buttonsLayout.setAlignment(Pos.CENTER);
			buttonsLayout.getChildren().addAll(yesButton, noButton);
			layout.getChildren().addAll(newManufacturerLabel, textBoxLayout, buttonsLayout);
			layout.setAlignment(Pos.CENTER_LEFT);
			Scene scene = new Scene(layout);

			confiemWindow.setScene(scene);
			confiemWindow.showAndWait();

		}

	}

	public void handleOnHiding(Event e) {
		filter = "";
		cmb.getTooltip().hide();
		T s = cmb.getSelectionModel().getSelectedItem();
		cmb.getItems().setAll(originalItems);
		cmb.getSelectionModel().select(s);
	}

}