package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.*;

/**
 * 
 * @version 27 January 2022
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

	public void saveOnMousePressed() {
		if (Employee.hasAccess()) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.8);
			saveSupplier.setEffect(effect);
			for (int i = 0; i < supplierTable.getItems().size(); i++) {
				try {
					ArrayList<String> parameters = new ArrayList<>();
					parameters.add(supplierTable.getItems().get(i).getEmail());
					parameters.add(supplierTable.getItems().get(i).getID() + "");
					Queries.queryUpdate("update Supplier set Supplier_Email=? where Supplier_ID=? ;", parameters);
					Supplier.getData().get(i).setEmail(supplierTable.getItems().get(i).getEmail());
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void saveOnMouseReleased() {
		if (Employee.hasAccess()) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			saveSupplier.setEffect(effect);
		}
	}

	public void saveOnMouseEntered() {
		if (Employee.hasAccess()) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.4);
			saveSupplier.setEffect(effect);
		}
	}

	public void saveOnMouseExited() {
		if (Employee.hasAccess()) {
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
		if (Employee.hasAccess()) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("SupplierPhones.fxml"));
				Parent contactInfo = (Parent) loader.load();
				SupplierPhoneController show = loader.getController();
				show.setPhoneNumbers(supplierTable.getSelectionModel().getSelectedItem());
				Stage contactInfoStage = new Stage();
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
		if (!Employee.hasAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addButton.setEffect(effect);
		try {
			FXMLLoader addLoader = new FXMLLoader(getClass().getResource("AddSupplier.fxml"));
			Parent addPane = (Parent) addLoader.load();
			Scene scene = new Scene(addPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage editStage = new Stage();
			editStage.setResizable(false);
			editStage.setScene(scene);
			editStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addOnMouseReleased() {
		if (!Employee.hasAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addButton.setEffect(effect);
	}

	public void addOnMouseEntered() {
		if (!Employee.hasAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addButton.setEffect(effect);
	}

	public void addOnMouseExited() {
		if (!Employee.hasAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addButton.setEffect(effect);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fieldSelector.setItems(FXCollections.observableArrayList("-Specify Field-", "ID", "Name", "Address"));
		idColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("ID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
		duesColumn.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("dues"));
		if (Employee.hasAccess()) {
			emailColumn.setEditable(true);
			supplierTable.setEditable(true);
			emailColumn.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
		}
		emailColumn.setOnEditCommit((CellEditEvent<Supplier, String> t) -> {
			((Supplier) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
		});

		try {
			Supplier.getSupplierData();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		supplierTable.setItems(Supplier.getDataList());

		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Supplier> filteredList = new ArrayList<>();
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add("%" + newValue + "%");
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Supplier
							.getSupplierData(Queries.queryResult("select * from Supplier order by Supplier_ID;", null));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (fieldSelector.getSelectionModel().getSelectedItem() == "ID") {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult(
							"select * fromSupplier where Supplier_ID like ? order by Supplier_ID;", parameters));
					
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Name") {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult(
							"select * from Supplier where Supplier_Name like ? order by Supplier_ID;", parameters));
			
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Address") {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult(
							"select * from Supplier where Supplier_Address like ? order by Supplier_ID;", parameters));
			
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					while (parameters.size() < 3) {
						parameters.add("%" + newValue + "%");
					}
					filteredList = Supplier.getSupplierData(Queries.queryResult(
							"select * from Supplier where Supplier_Name like ? " + " or Supplier_ID like ? "
									+ " or Supplier_Address like ? " + " order by Supplier_ID;",
							parameters));
					
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			supplierTable.setItems(FXCollections.observableArrayList(filteredList));
		});
	}
}