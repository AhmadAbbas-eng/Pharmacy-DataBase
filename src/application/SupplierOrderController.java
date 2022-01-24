
package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.Batch;
import Relations.Employee;
import Relations.Queries;
import Relations.Supplier;
import Relations.SupplierOrder;
import Relations.SupplierOrderBatch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SupplierOrderController implements Initializable {
	@FXML
	private TableView<Supplier> supplierTable;

	@FXML
	private TextField discountTextField;

	@FXML
	private ImageView addProductIcon;

	@FXML
	private Button confirmButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Label costLabel;

	private double costValue;

	@FXML
	private ListView<String> orderdProductsList;

	@FXML
	private TextField searchTextField;

	@FXML
	private TableColumn<Employee, String> employeeNameColumn;

	@FXML
	private TableColumn<Employee, String> nationalIDColumn;

	@FXML
	private DatePicker orderDatePicker;

	@FXML
	private ComboBox<String> searchOperationComboBox;

	@FXML
	private TableColumn<Supplier, String> supplierNameColumn;

	@FXML
	private TableView<Employee> managerTable;

	@FXML
	private ImageView addEmployee;

	@FXML
	private ImageView addSupplierImage;

	@FXML
	private Label orderNOLabel;

	@FXML
	private StackPane mainPane;

	private String stringToSearch = "";

	ObservableList<String> Choices = FXCollections.observableArrayList("Select", "Supplier", "Manager");

	public void addOnMousePressed() throws IOException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addProductIcon.setEffect(effect);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectProduct.fxml"));
		Parent root1 = (Parent) loader.load();
		SelectProductController temp = loader.getController();
		temp.getProductList(new ArrayList<String>(orderdProductsList.getItems()), this);
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void addSupplierOnMousePressed() throws IOException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addProductIcon.setEffect(effect);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSupplier.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void setProductList(ArrayList<String> data, double costValue) {
		this.costValue = costValue;
		costLabel.setText("" + costValue + "");
		orderdProductsList.setItems(FXCollections.observableArrayList(data));
	}

	public void addSupplierOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addSupplierImage.setEffect(effect);
	}

	public void addSupplierOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addSupplierImage.setEffect(effect);
	}

	public void addSupplierOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addSupplierImage.setEffect(effect);
	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProductIcon.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addProductIcon.setEffect(effect);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProductIcon.setEffect(effect);
	}

	public void cancelOnAction(ActionEvent e) throws IOException {
		Parent page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);
	}

	public void confirmOnAction(ActionEvent e)
			throws ClassNotFoundException, SQLException, ParseException, IOException {
		if (managerTable.getSelectionModel().getSelectedItem() != null
				&& supplierTable.getSelectionModel().getSelectedItem() != null && orderDatePicker.getValue() != null
				&& orderdProductsList.getItems().isEmpty() == false) {

			if (orderDatePicker.getValue().compareTo(LocalDate.now()) > 0) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input");
				alert.setHeaderText(null);
				alert.setContentText("The Date Value Is Incorrect!");
				alert.showAndWait();

			} else {
				double Discount = 0.0;
				if (discountTextField.getText().isBlank() == false) {
					Discount = Double.parseDouble(discountTextField.getText());
				}

				SupplierOrder insertOrder = new SupplierOrder(SupplierOrder.getMaxID(), orderDatePicker.getValue(),
						Double.parseDouble(costLabel.getText()), Discount, LocalDate.parse("1111-01-01"),
						supplierTable.getSelectionModel().getSelectedItem().getID(),
						managerTable.getSelectionModel().getSelectedItem().getID(), -1, LocalDate.parse("1111-01-01"));

				double newSupplierDues = supplierTable.getSelectionModel().getSelectedItem().getDues() + costValue
						- Discount;

				Queries.queryUpdate("update Supplier set supplier_dues =? where Supplier_ID =?;",
						new ArrayList<>(Arrays.asList(newSupplierDues + "",
								supplierTable.getSelectionModel().getSelectedItem().getID() + "")));

				SupplierOrder.getData().add(insertOrder);// new order

				SupplierOrder.insertSupplierOrder(orderDatePicker.getValue(), Double.parseDouble(costLabel.getText()),
						Discount, LocalDate.parse("1111-01-01"),
						supplierTable.getSelectionModel().getSelectedItem().getID(),
						managerTable.getSelectionModel().getSelectedItem().getID(), -1, LocalDate.parse("1111-01-01"));

				ArrayList<String> temp = new ArrayList<String>(orderdProductsList.getItems());

				for (int i = 0; i < temp.size(); ++i) {

					String s = temp.get(i);
					String[] data = s.split(",");
					String[] ProductIdTemp = data[0].split("=");
					String[] quantityTemp = data[2].split("=");

					ArrayList<Batch> filteredList = Batch.getBatchData(Queries.queryResult(
							"select * from batch where Product_ID = ? and"
									+ " Batch_Production_Date = '1111-01-01' and Batch_Expiry_Date = '1111-01-01';",
							new ArrayList<>(Arrays.asList(ProductIdTemp[1]))));

					if (filteredList.size()==0) {
						Batch.insertBatch(Integer.parseInt(ProductIdTemp[1]), LocalDate.parse("1111-01-01"),
								LocalDate.parse("1111-01-01"), Integer.parseInt(quantityTemp[1]));
					} else {

						Queries.queryUpdate(
								"update Batch set Batch_Amount = Batch_Amount + ? where Product_ID = ? and "
										+ "Batch_Production_Date = '1111-01-01' and Batch_Expiry_Date = '1111-01-01';",
								new ArrayList<>(Arrays.asList(quantityTemp[1], ProductIdTemp[1])));

					}

					SupplierOrderBatch.insertSupplierOrderBatch(SupplierOrder.getMaxID() + "", ProductIdTemp[1],
							LocalDate.parse("1111-01-01"), LocalDate.parse("1111-01-01"),
							Integer.parseInt(quantityTemp[1]));

					SupplierOrderBatch insertBatch = new SupplierOrderBatch(SupplierOrder.getMaxID(),
							Integer.parseInt(ProductIdTemp[1]), LocalDate.parse("1111-01-01"),
							LocalDate.parse("1111-01-01"), Integer.parseInt(quantityTemp[1]));
					SupplierOrderBatch.getData().add(insertBatch);

				}

				Parent page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
				mainPane.getChildren().removeAll();
				mainPane.getChildren().setAll(page);

			}

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Empty Fields");
			alert.setHeaderText(null);
			alert.setContentText("You Have To Fill Order Fields !!");
			alert.showAndWait();
		}

	}

	public void filterList() throws ParseException {
		ArrayList<Supplier> supplierFilteredList = new ArrayList<>();
		ArrayList<Employee> employeeFilteredList = new ArrayList<>();

		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()
				|| searchOperationComboBox.getSelectionModel().getSelectedItem() == "Manager") {
			try {
				supplierFilteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier;", null));

			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Supplier") {
			try {

				supplierFilteredList = Supplier
						.getSupplierData(Queries.queryResult("select * from Supplier where  supplier_name like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {

				supplierFilteredList = Supplier
						.getSupplierData(Queries.queryResult("select * from Supplier  where  supplier_name like ?;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()
				|| searchOperationComboBox.getSelectionModel().getSelectedItem() == "Supplier") {
			try {

				employeeFilteredList = Employee.getEmployeeData(Queries
						.queryResult("select * from Employee where isManager = 'true' and isActive = 'true';", null));

			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Manager") {
			try {

				employeeFilteredList = Employee.getEmployeeData(Queries.queryResult(
						"select * from Employee where isManager = 'true' "
								+ "and  Employee_name like ? and isActive = 'true' ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				employeeFilteredList = Employee.getEmployeeData(Queries.queryResult(
						"select * from Employee where isManager = 'true' and isActive = 'true' and  Employee_name like ?;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		managerTable.setItems(FXCollections.observableArrayList(employeeFilteredList));
		supplierTable.setItems(FXCollections.observableArrayList(supplierFilteredList));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOperationComboBox.setValue("Select");
		searchOperationComboBox.setItems(Choices);
		supplierNameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		employeeNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		nationalIDColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("NID"));
		int max = SupplierOrder.getMaxID() + 1;
		orderNOLabel.setText("Order NO." + max);

		supplierTable.setItems(Supplier.getDataList());
		try {
			managerTable.setItems(FXCollections.observableArrayList(Employee.getEmployeeData(Queries
					.queryResult("select * from Employee where isManager = 'true' and isActive = 'true';", null))));
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			e1.printStackTrace();
		}

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch = newValue;
			try {
				filterList();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});

	}
}
