package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Relations.Employee;
import Relations.Queries;
import Relations.Supplier;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 *
 */
public class SupplierController implements Initializable {

	@FXML
	private ImageView addButton;

	@FXML
	private TableColumn<Supplier, String> addressColumn;

	@FXML
	private TableColumn<Supplier, Double> duesColumn;

	@FXML
	private TableColumn<Supplier, String> emailColumn;

	@FXML
	private TableView<Supplier> supplierTable;

	@FXML
	private ComboBox<String> fieldSelector;

	@FXML
	private TableColumn<Supplier, String> idColumn;

	@FXML
	private TableColumn<Supplier, String> nameColumn;

	@FXML
	private TextField searchBox;

	@FXML
	private Button showInfoButton;

	@FXML
	private ImageView saveSupplier;

	private boolean editedFlag = false;
	private String stringToSearch = "";
	private String prevStringToSearch = "";
	private String prevSelectedFeild = "-Specify Field-";
	private boolean searchBoxFlag = true;

	public void filterData() {
		ArrayList<Supplier> filteredList = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + stringToSearch + "%");
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			filteredList = Supplier
					.getSupplierData(Queries.queryResult("select * from Supplier order by Supplier_ID;", null));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "ID") {
			filteredList = Supplier.getSupplierData(Queries
					.queryResult("select * from Supplier where Supplier_ID like ? order by Supplier_ID;", parameters));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Name") {
			filteredList = Supplier.getSupplierData(Queries.queryResult(
					"select * from Supplier where Supplier_Name like ? order by Supplier_ID;", parameters));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Address") {
			filteredList = Supplier.getSupplierData(Queries.queryResult(
					"select * from Supplier where Supplier_Address like ? order by Supplier_ID;", parameters));
		} else {
			while (parameters.size() < 3) {
				parameters.add("%" + stringToSearch + "%");
			}
			filteredList = Supplier.getSupplierData(
					Queries.queryResult("select * from Supplier where Supplier_Name like ? " + " or Supplier_ID like ? "
							+ " or Supplier_Address like ? " + " order by Supplier_ID;", parameters));
		}
		supplierTable.setItems(FXCollections.observableArrayList(filteredList));
		supplierTable.refresh();
		prevSelectedFeild = fieldSelector.getSelectionModel().getSelectedItem();
		searchBoxFlag = true;
	}

	public void confirmSave() {
		if (editedFlag) {
			if ((!fieldSelector.getSelectionModel().getSelectedItem().equals(prevSelectedFeild)
					|| !prevStringToSearch.equals(stringToSearch))) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Unsaved Edited Data");
				alert.setHeaderText(null);
				alert.setContentText("Data has been edited, do you wish to save edits first?");
				ButtonType saveButtonType = new ButtonType("Save");
				ButtonType continueButtonType = new ButtonType("Continue anyway");
				ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				alert.getButtonTypes().setAll(saveButtonType, continueButtonType, cancelButtonType);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == saveButtonType) {
					saveOnMousePressed();
					saveOnMouseReleased();
					editedFlag = false;
					filterData();

				} else if (result.get() == continueButtonType) {
					filterData();
					editedFlag = false;
				} else {
					if (!fieldSelector.getSelectionModel().getSelectedItem().equals(prevSelectedFeild)) {
						prevStringToSearch = stringToSearch;
						fieldSelector.setValue(prevSelectedFeild);
					} else {
						System.err.println("ZFTTT");
						searchBoxFlag = false;
						searchBox.setText(prevStringToSearch);
					}

				}
			}
		} else {
			filterData();
		}
	}

	public void saveOnMousePressed() {
		if (Employee.isAccess()) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.8);
			saveSupplier.setEffect(effect);
			for (int i = 0; i < supplierTable.getItems().size(); i++) {
				ArrayList<String> parameters = new ArrayList<>();
				parameters.add(supplierTable.getItems().get(i).getEmail());
				parameters.add(supplierTable.getItems().get(i).getID() + "");
				Queries.queryUpdate("update Supplier set Supplier_Email=? where Supplier_ID=? ;", parameters);
			}
		}
	}

	public void saveOnMouseReleased() {
		if (Employee.isAccess()) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			saveSupplier.setEffect(effect);
		}
	}

	public void saveOnMouseEntered() {
		if (Employee.isAccess()) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.4);
			saveSupplier.setEffect(effect);
		}
	}

	public void saveOnMouseExited() {
		if (Employee.isAccess()) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			saveSupplier.setEffect(effect);
		}
	}

	public void showOnAction() {
		if (supplierTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Supplier Must Be Chosen First");
			alert.setContentText("Choose A Supplier From The Table");
			alert.showAndWait();
			return;
		}
		if (Employee.isAccess()) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("SupplierPhones.fxml"));
				Parent contactInfo = (Parent) loader.load();
				SupplierPhoneController show = loader.getController();
				show.setPhoneNumbers(supplierTable.getSelectionModel().getSelectedItem());
				Stage contactInfoStage = new Stage();
				contactInfoStage.setAlwaysOnTop(true);
				Scene scene = new Scene(contactInfo, 500, 500);
				contactInfoStage.setScene(scene);
				contactInfoStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("SupplierPhonesNoAccess.fxml"));
				Parent contactInfo;
				contactInfo = (Parent) loader.load();
				SupplierPhonesNoAccessController show = loader.getController();
				show.setPhoneNumbers(supplierTable.getSelectionModel().getSelectedItem());
				Stage contactInfoStage = new Stage();
				Scene scene = new Scene(contactInfo, 500, 500);
				contactInfoStage.setScene(scene);
				contactInfoStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void showOnMousePressed() {
		if (supplierTable.getSelectionModel().getSelectedItem() != null) {
			showInfoButton.setStyle("-fx-background-color: #1A6477");
		}
	}

	public void addOnMousePressed() {
		if (!Employee.isAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addButton.setEffect(effect);
		try {
			FXMLLoader addLoader = new FXMLLoader(getClass().getResource("AddSupplier.fxml"));
			Parent addPane = (Parent) addLoader.load();
			AddSupplierController addController = addLoader.getController();
			addController.setCaller(this);
			Scene scene = new Scene(addPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage editStage = new Stage();
			editStage.setResizable(false);
			editStage.initModality(Modality.APPLICATION_MODAL);
			editStage.setScene(scene);
			editStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addOnMouseReleased() {
		if (!Employee.isAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addButton.setEffect(effect);
	}

	public void addOnMouseEntered() {
		if (!Employee.isAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addButton.setEffect(effect);
	}

	public void addOnMouseExited() {
		if (!Employee.isAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addButton.setEffect(effect);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fieldSelector.setItems(FXCollections.observableArrayList("-Specify Field-", "ID", "Name", "Address"));
		fieldSelector.setValue("-Specify Field-");
		idColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("ID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
		duesColumn.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("dues"));
		if (Employee.isAccess()) {
			emailColumn.setEditable(true);
			supplierTable.setEditable(true);
			emailColumn.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
		}
		emailColumn.setOnEditCommit((CellEditEvent<Supplier, String> t) -> {
			if (!t.getOldValue().equals(t.getNewValue())) {
				editedFlag = true;
			}
			((Supplier) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());

		});

		filterData();

		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			if (searchBoxFlag) {
				stringToSearch = newValue;
				prevStringToSearch = oldValue;
				confirmSave();
			} else {
				searchBoxFlag = true;
			}
		});
	}
}