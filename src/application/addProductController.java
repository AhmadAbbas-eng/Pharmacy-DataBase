package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.*;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.*;

public class addProductController implements Initializable {

	ArrayList<ArrayList<String>> product;

	ProductController caller;

	@FXML
	private TableView<ArrayList<String>> productTable;

	@FXML
	private TableColumn<ArrayList<String>, String> idColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> nameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> priceColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> dosageFormColumn;

	@FXML
	private ImageView addProduct;

	@FXML
	private ImageView saveProduct;


	@FXML
	private TextField dosageTextField;

	@FXML
	private TextField dosageFormTextField;

	@FXML
	private TextField drugCategoryTextField;

	@FXML
	private HBox drugInfoHBox;

	@FXML
	private VBox drugInfoVBox;

	@FXML
	private CheckBox isDrug;

	@FXML
	private TextField manufaturerTextField;

	@FXML
	private TextField nameTextField;

	@FXML
	private ComboBox<String> pharmaceticalCategorySelector;

	@FXML
	private ComboBox<String> pregnencyCategorySelector;

	@FXML
	private TextField priceTextField;

	@FXML
	private ImageView savedIcon;

	@FXML
	private Label savedLabel;

	@FXML
	private TextField scientificNameTextField;

	@FXML
	private ImageView addedIcon;

	@FXML
	private Label addedLabel;

	public void addProductOnMousePressed() throws ClassNotFoundException, SQLException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addProduct.setEffect(effect);
		Double price = 0.0;
		String name = nameTextField.getText();
		String manufaturer = manufaturerTextField.getText();
		String scientificName = scientificNameTextField.getText();
		String pregnencyCategory = pregnencyCategorySelector.getSelectionModel().getSelectedItem();
		String pharmaceticalCategory = pharmaceticalCategorySelector.getSelectionModel().getSelectedItem();
		if (pharmaceticalCategory == null) {
			pharmaceticalCategory = "non";
		}
		String drugCategory = drugCategoryTextField.getText();
		String dosage = dosageTextField.getText();
		String dosageForm = dosageFormTextField.getText();
		Boolean checkPrice = true;
		try {
			price = Double.parseDouble(priceTextField.getText());
		} catch (NumberFormatException e) {
			checkPrice = false;
		}

		if ((Queries.queryResult("select * from Product where Product_Name=?;", new ArrayList<>(Arrays.asList(name))))
				.size() != 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Product with this name already exists!");
			alert.showAndWait();
		}

		else if (checkPrice && price > 0 && !name.isBlank() && !name.isBlank() && !manufaturer.isEmpty()
				&& !manufaturer.isBlank()) {
			if (isDrug.isSelected()) {
				if (!scientificName.isBlank() && !scientificName.isEmpty() && pregnencyCategory != null
						&& !drugCategory.isBlank() && !drugCategory.isEmpty() && !dosage.isBlank() && !dosage.isEmpty()
						&& !dosageForm.isBlank() && !dosageForm.isEmpty()) {
					Queries.queryUpdate("Insert into Name_Manu values(?, ?);",
							new ArrayList<>(Arrays.asList(name, manufaturer)));
					Drug.insertDrug(name, price, scientificName, pregnencyCategory, dosage, drugCategory, dosageForm,
							pharmaceticalCategory);
					caller.saveEdits();					
					showAndFade(addedLabel);
					showAndFade(addedIcon);
					nameTextField.setText("");
					manufaturerTextField.setText("");
					scientificNameTextField.setText("");
					drugCategoryTextField.setText("");
					dosageTextField.setText("");
					dosageFormTextField.setText("");
					priceTextField.setText("");
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Fill all required fields");
					alert.showAndWait();
				}
			} else {
				Queries.queryUpdate("Insert into Name_Manu values(?, ?);",
						new ArrayList<>(Arrays.asList(name, manufaturer)));
				Product.insertProduct(name, price);
				caller.saveEdits();
				showAndFade(addedLabel);
				showAndFade(addedIcon);
				nameTextField.setText("");
				manufaturerTextField.setText("");
				scientificNameTextField.setText("");
				drugCategoryTextField.setText("");
				dosageTextField.setText("");
				dosageFormTextField.setText("");
				priceTextField.setText("");

			}

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Fill all required fields");
			alert.showAndWait();
		}
	}

	public void addProductOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProduct.setEffect(effect);
	}

	public void addProductOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addProduct.setEffect(effect);
	}

	public void addProductOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProduct.setEffect(effect);
	}

	public void showAndFade(Node node) {

		Timeline show = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
				new KeyFrame(Duration.seconds(0.5), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
				new KeyFrame(Duration.seconds(1), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)));
		FadeTransition fade = new FadeTransition(Duration.seconds(0.5), node);
		fade.setFromValue(1);
		fade.setToValue(0);

		SequentialTransition blinkFade = new SequentialTransition(node, show, fade);
		blinkFade.play();

	}

	public void drugSelection() {
		if (isDrug.isSelected()) {
			drugInfoHBox.setOpacity(1);
			drugInfoVBox.setOpacity(1);
		} else {
			drugInfoHBox.setOpacity(0);
			drugInfoVBox.setOpacity(0);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		addedIcon.setOpacity(0);
		addedLabel.setOpacity(0);
		drugInfoHBox.setOpacity(0);
		drugInfoVBox.setOpacity(0);
		pharmaceticalCategorySelector.setItems(FXCollections.observableArrayList("non", "Controlled", "Danger"));
		pregnencyCategorySelector.setItems(FXCollections.observableArrayList("A", "B", "C"));

	}
}