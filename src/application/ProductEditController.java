package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.TextFieldTableCell;
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

public class ProductEditController implements Initializable {

	ArrayList<ArrayList<String>> product;

	ProductController caller;

	private String oldName;

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

	public void drugSelection() {
		if (isDrug.isSelected()) {
			drugInfoHBox.setOpacity(1);
			drugInfoVBox.setOpacity(1);
		} else {
			drugInfoHBox.setOpacity(0);
			drugInfoVBox.setOpacity(0);
		}
	}

	public void saveOnMousePressed() {
		if (product != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.8);
			saveProduct.setEffect(effect);
			try {
				Queries.queryUpdate("update Name_Manu set Product_Name=? where Product_Name=? ;",
						new ArrayList<>(Arrays.asList(productTable.getItems().get(0).get(1), oldName)));

				Queries.queryUpdate("update Product set Product_Price=? where Product_ID=? ;",
						new ArrayList<>(Arrays.asList(productTable.getItems().get(0).get(2), product.get(0).get(0))));
				if (productTable.getItems().size() > 8) {
					Queries.queryUpdate("update Drug set Drug_Dosage_Form=? where Product_ID=? ;", new ArrayList<>(
							Arrays.asList(productTable.getItems().get(0).get(8), product.get(0).get(0))));
				}

				caller.saveEdits();
				showAndFade(savedLabel);
				showAndFade(savedIcon);

			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void saveOnMouseReleased() {
		if (product != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			saveProduct.setEffect(effect);
		}
	}

	public void saveOnMouseEntered() {
		if (product != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.4);
			saveProduct.setEffect(effect);
		}
	}

	public void saveOnMouseExited() {
		if (product != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			saveProduct.setEffect(effect);
		}
	}

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

		if ((Queries.queryResult("select * from Product where Product_Name=? ;", new ArrayList<>(Arrays.asList(name))))
				.size() != 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Repeated Record");
			alert.setHeaderText(null);
			alert.setContentText("Product With This Name Already Exists");
			alert.showAndWait();

		}

		else if (checkPrice && price > 0 && !name.isBlank() && !name.isBlank() && !manufaturer.isEmpty()
				&& !manufaturer.isBlank()) {
			if (isDrug.isSelected()) {
				if (!scientificName.isBlank() && !scientificName.isEmpty() && pregnencyCategory != null
						&& !drugCategory.isBlank() && !drugCategory.isEmpty() && !dosage.isBlank() && !dosage.isEmpty()
						&& !dosageForm.isBlank() && !dosageForm.isEmpty()) {
					Queries.queryUpdate("Insert into Name_Manu values(? , ?);",
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
					alert.setTitle("Missing Info");
					alert.setHeaderText(null);
					alert.setContentText("All Fields Must Be Filled");
					alert.showAndWait();
				}
			} else {
				Queries.queryUpdate("Insert into Name_Manu values(? ,?);",
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
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("All Fields Must Be Filled");
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

	public void setRow(ArrayList<ArrayList<String>> product, ProductController caller) {
		this.caller = caller;
		this.product = product;
		if (product != null) {
			productTable.setItems(FXCollections.observableArrayList(product));
			oldName = productTable.getItems().get(0).get(1);
		}
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

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		addedIcon.setOpacity(0);
		addedLabel.setOpacity(0);
		drugInfoHBox.setOpacity(0);
		drugInfoVBox.setOpacity(0);
		savedLabel.setOpacity(0);
		savedIcon.setOpacity(0);
		pharmaceticalCategorySelector.setItems(FXCollections.observableArrayList("non", "Controlled", "Danger"));
		pregnencyCategorySelector.setItems(FXCollections.observableArrayList("A", "B", "C"));

		idColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(0));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		nameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		priceColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		dosageFormColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 8) {
							return new SimpleStringProperty(x.get(8));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		nameColumn.setEditable(true);
		nameColumn.setCellFactory(TextFieldTableCell.<ArrayList<String>>forTableColumn());

		nameColumn.setOnEditCommit((CellEditEvent<ArrayList<String>, String> t) -> {
			(t.getTableView().getItems().get(t.getTablePosition().getRow())).set(1, t.getNewValue());
			productTable.refresh();
		});
		nameColumn.setOnEditStart((CellEditEvent<ArrayList<String>, String> t) -> {
			oldName = t.getOldValue();
			productTable.refresh();
		});

		priceColumn.setEditable(true);
		priceColumn.setCellFactory(TextFieldTableCell.<ArrayList<String>>forTableColumn());
		priceColumn.setOnEditStart((CellEditEvent<ArrayList<String>, String> t) -> {
		});

		priceColumn.setOnEditCommit((CellEditEvent<ArrayList<String>, String> t) -> {
			if (t.getNewValue().matches("[0-9]+")) {
				(t.getTableView().getItems().get(t.getTablePosition().getRow())).set(2, t.getNewValue());
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Price Must Be Positive Real Number");
				alert.showAndWait();
			}
			productTable.refresh();
		});

		dosageFormColumn.setEditable(true);
		dosageFormColumn.setCellFactory(TextFieldTableCell.<ArrayList<String>>forTableColumn());

		dosageFormColumn.setOnEditCommit((CellEditEvent<ArrayList<String>, String> t) -> {
			if (t.getOldValue().equals("-")) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Selected Product Cannot Have Dosage Form");
				alert.showAndWait();
			} else {
				(t.getTableView().getItems().get(t.getTablePosition().getRow())).set(8, t.getNewValue());
			}
			productTable.refresh();
		});
	}

}