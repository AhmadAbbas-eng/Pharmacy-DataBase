package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.Queries;
import Relations.SupplierOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ReceivedController implements Initializable {
	@FXML
	private TableColumn<SupplierOrder, Double> cost;

	@FXML
	private TableColumn<SupplierOrder, String> date;

	@FXML
	private TableColumn<SupplierOrder, Double> discount;

	@FXML
	private ImageView edit;

	@FXML
	private TableColumn<SupplierOrder, String> id;

	@FXML
	private TableColumn<SupplierOrder, String> orderby;

	@FXML
	private TableColumn<SupplierOrder, String> orderfrom;

	@FXML
	private Button received;

	@FXML
	private StackPane mainPane;

	@FXML
	private TextField search;

	@FXML
	private ComboBox<String> searchOP;

	@FXML
	private TableView<SupplierOrder> table;

	@FXML
	private TableColumn<SupplierOrder, String> PayDueDate;

	@FXML
	private TableColumn<SupplierOrder, String> Recivedby;

	@FXML
	private TableColumn<SupplierOrder, String> RecivedDate;

	ObservableList<String> Choices = FXCollections.observableArrayList("Order Id", "Order By", "Order From",
			"Order Month", "Order Year", "Recieved Month", "Recieved Year", "Recieved By");

	public void backOnAction() throws IOException {

		Parent page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);

	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOP.setValue("Select");
		searchOP.setItems(Choices);
		id.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("iD"));
		date.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dateOfOrder"));
		cost.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("cost"));
		discount.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("discount"));
		orderby.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("managerID"));
		orderfrom.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("supplierID"));
		Recivedby.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("recievedBy"));
		RecivedDate.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("recDate"));
		PayDueDate.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dueDateOfPayment"));

		try {
			table.setItems(FXCollections.observableArrayList(SupplierOrder.getSupplierOrderData(
					Queries.queryResult("select * from S_Order where Recieved_By <> '-1' order by Order_ID;", null))));
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			e1.printStackTrace();
		}

		search.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<SupplierOrder> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries
							.queryResult("select * from S_Order where Recieved_By <> '-1' order by Order_ID;", null));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order Id") {
				try {
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult(
									"select * from S_Order " + " where Recieved_By <> '-1' and Order_ID like ? "
											+ "order by Order_ID;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order From") {
				try {
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult(
									"select * from S_Order " + " where Recieved_By <> '-1' and Supplier_ID like ? "
											+ " order by Order_ID;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order By") {
				try {
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult(
									"select * from S_Order " + " where Recieved_By <> '-1' and Manager_id like ? "
											+ " order by Order_ID;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
				// "Recieved Month","Recieved Year""
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Recieved Month") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
							+ " where Recieved_By <> '-1' and month(Recieved_Date) like ? " + " order by Order_ID;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Recieved Year") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
							+ " where Recieved_By <> '-1' and year(Recieved_Date) like ? " + " order by Order_ID;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Recieved By") {
				try {
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult(
									"select * from S_Order " + " where Recieved_By <> '-1' and Recieved_By like ? "
											+ " order by Order_ID;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order Month") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
							"select * from S_Order " + " where Recieved_By <> '-1' and month(Date_Of_Order) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order Year") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
							+ " where Recieved_By <> '-1' and Year(Date_Of_Order) like ? " + " order by Order_ID;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else {
				try {
					ArrayList<String> parameters = new ArrayList<>();
					while (parameters.size() < 4) {
						parameters.add("%" + newValue + "%");
					}
					filteredList = SupplierOrder.getSupplierOrderData(
							Queries.queryResult("select * from S_Order " + " where ( year(Date_Of_Order) like ? "
									+ " or month(Date_Of_Order) like ? " + " or Manager_id like ? "
									+ " or Order_ID like ? " + ") and Recieved_By <> '-1' ;", parameters));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			}
			table.setItems(FXCollections.observableArrayList(filteredList));
		});

	}

}
