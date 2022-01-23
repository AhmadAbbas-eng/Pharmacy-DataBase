
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
	private TableView<Supplier> Table1;

	@FXML
	private TextField discount;

	@FXML
	private ImageView addProduct;

	@FXML
	private Button confirm;

	@FXML
	private Button cancel;

	@FXML
	private Label cost;

	private double costValue;

	@FXML
	private ListView<String> list;

	@FXML
	private TextField search;

	@FXML
	private TableColumn<Employee, String> name;

	@FXML
	private TableColumn<Employee, String> nid;

	@FXML
	private DatePicker orderDate;

	@FXML
	private ComboBox<String> selectOP;

	@FXML
	private TableColumn<Supplier, String> sname;

	@FXML
	private TableView<Employee> table2;

	@FXML
	private ImageView addEmployee;

	@FXML
	private ImageView addSupplier;

	@FXML
	private Label warning;

	@FXML
	private Label orderno;

	@FXML
	private StackPane mainPane;
	
	private String stringToSearch="";
	
	ObservableList<String> Choices = FXCollections.observableArrayList("Select", "Supplier", "Manager");

	public void addOnMousePressed() throws IOException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addProduct.setEffect(effect);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectProduct.fxml"));
		Parent root1 = (Parent) loader.load();
		SelectProductController temp = loader.getController();
		temp.getProductList(new ArrayList<String>(list.getItems()), this);
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void addSupplierOnMousePressed() throws IOException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addProduct.setEffect(effect);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSupplier.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void setProductList(ArrayList<String> data, double costValue) {
		this.costValue = costValue;
		cost.setText("" + costValue + "");
		list.setItems(FXCollections.observableArrayList(data));
	}

	public void addSupplierOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addSupplier.setEffect(effect);
	}

	public void addSupplierOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addSupplier.setEffect(effect);
	}

	public void addSupplierOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addSupplier.setEffect(effect);
	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProduct.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addProduct.setEffect(effect);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProduct.setEffect(effect);
	}

	public void cancelButton(ActionEvent e) throws IOException {
		Parent page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);
	}

	public void confirmButton(ActionEvent e) throws ClassNotFoundException, SQLException, ParseException, IOException {
		if (table2.getSelectionModel().getSelectedItem() != null && Table1.getSelectionModel().getSelectedItem() != null
				&& orderDate.getValue() != null && list.getItems().isEmpty() == false) {

			if (orderDate.getValue().compareTo(LocalDate.now()) > 0) {
				warning.setText("Wrong Date!!");

			} else {
				double Discount = 0.0;
				if (discount.getText().isBlank() == false) {
					Discount = Double.parseDouble(discount.getText());
				}

				SupplierOrder insertOrder = new SupplierOrder(SupplierOrder.getMaxID(), orderDate.getValue(),
						Double.parseDouble(cost.getText()), Discount, LocalDate.parse("1111-01-01"),
						Table1.getSelectionModel().getSelectedItem().getID(),
						table2.getSelectionModel().getSelectedItem().getID(), -1, LocalDate.parse("1111-01-01"));

				double newSupplierDues = Table1.getSelectionModel().getSelectedItem().getDues() + costValue - Discount;

				Queries.queryUpdate("update Supplier set supplier_dues =? where Supplier_ID =?;", new ArrayList<>(Arrays
						.asList(newSupplierDues + "", Table1.getSelectionModel().getSelectedItem().getID() + "")));

				SupplierOrder.getData().add(insertOrder);// new order

				SupplierOrder.insertSupplierOrder(orderDate.getValue(), Double.parseDouble(cost.getText()), Discount,
						LocalDate.parse("1111-01-01"), Table1.getSelectionModel().getSelectedItem().getID(),
						table2.getSelectionModel().getSelectedItem().getID(), -1, LocalDate.parse("1111-01-01"));

				ArrayList<String> temp = new ArrayList<String>(list.getItems());// add order products

				for (int i = 0; i < temp.size(); ++i) {

					String s = temp.get(i);
					String[] data = s.split(",");
					String[] ProductIdTemp = data[0].split("=");
					String[] quantityTemp = data[2].split("=");

					ArrayList<Batch> filteredList = Batch.getBatchData(Queries.queryResult(
							"select * from batch where Product_ID = ? and"
									+ " Batch_Production_Date = '1111-01-01' and Batch_Expiry_Date = '1111-01-01';",
							new ArrayList<>(Arrays.asList(ProductIdTemp[1]))));

					if (filteredList == null) {
						Batch.insertBatch(Integer.parseInt(ProductIdTemp[1]), LocalDate.parse("1111-01-01"),
								LocalDate.parse("1111-01-01"), Integer.parseInt(quantityTemp[1]));
					} else {

						Queries.queryUpdate(
								"update Batch set Batch_Amount = Batch_Amount + ? where Product_ID = ? and "
										+ "Batch_Production_Date = '1111-01-01' and Batch_Expiry_Date = '1111-01-01;",
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
			warning.setText("You Have To Fill Order Fields !!");
		}

	}
	
	public void filterList() {
		ArrayList<Supplier> filteredList1 = new ArrayList<>();
		ArrayList<Employee> filteredList2 = new ArrayList<>();

		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()
				|| selectOP.getSelectionModel().getSelectedItem() == "Manager") {
			try {
				filteredList1 = Supplier.getSupplierData(Queries.queryResult("select * from Supplier;", null));

			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (selectOP.getSelectionModel().getSelectedItem() == "Supplier") {
			try {

				filteredList1 = Supplier
						.getSupplierData(Queries.queryResult("select * from Supplier where  supplier_name like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {

				filteredList1 = Supplier.getSupplierData(Queries.queryResult("select * from Supplier;", null));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()
				|| selectOP.getSelectionModel().getSelectedItem() == "Supplier") {
			try {

				filteredList2 = Employee.getEmployeeData(
						Queries.queryResult("select * from Employee where isManager = 'true' and isActive = 'true';", null));

			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (selectOP.getSelectionModel().getSelectedItem() == "Manager") {
			try {

				filteredList2 = Employee.getEmployeeData(Queries.queryResult(
						"select * from Employee where isManager = 'true' " + "and  Employee_name like and isActive = 'true'? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {

				filteredList1 = Supplier.getSupplierData(Queries.queryResult("select * from employee;", null));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		table2.setItems(FXCollections.observableArrayList(filteredList2));
		Table1.setItems(FXCollections.observableArrayList(filteredList1));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		selectOP.setValue("Select");
		selectOP.setItems(Choices);
		sname.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		name.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		nid.setCellValueFactory(new PropertyValueFactory<Employee, String>("NID"));
		int max = SupplierOrder.getMaxID() + 1;
		orderno.setText("Order NO." + max);

		Table1.setItems(Supplier.getDataList());
		try {
			table2.setItems(FXCollections.observableArrayList(Employee
					.getEmployeeData(Queries.queryResult("select * from Employee where isManager = 'true' and isActive = 'true';", null))));
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			e1.printStackTrace();
		}

		search.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch=newValue;
			filterList();
			
		});

	}
}
