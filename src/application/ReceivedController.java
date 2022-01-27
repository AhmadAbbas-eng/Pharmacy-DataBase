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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * 
 * @version 27 January 2022
 * @author Loor Sawalhi
 */
public class ReceivedController implements Initializable {
	@FXML
	private TableColumn<SupplierOrder, Double> costColumn;

	@FXML
	private TableColumn<SupplierOrder, String> dateOfOrderColumn;

	@FXML
	private TableColumn<SupplierOrder, Double> discountColumn;

	@FXML
	private TableColumn<SupplierOrder, String> orderIDColumn;

	@FXML
	private TableColumn<SupplierOrder, String> orderByIDColumn;

	@FXML
	private TableColumn<SupplierOrder, String> orderFromIDColumn;

	@FXML
	private StackPane mainPane;

	@FXML
	private TextField searchTextField;

	@FXML
	private ComboBox<String> searchOperationComboBox;

	@FXML
	private TableView<SupplierOrder> recievedOrdersTable;

	@FXML
	private TableColumn<SupplierOrder, String> payDueDateColumn;

	@FXML
	private TableColumn<SupplierOrder, String> recivedByIDColumn;

	@FXML
	private TableColumn<SupplierOrder, String> recivedDateColumn;
	
	@FXML
	private ImageView backIcon;
	
	private String stringToSearch="";

	ObservableList<String> searchChoices = FXCollections.observableArrayList("Select","Order Id", "Order By", "Order From",
			"Order Month", "Order Year", "Recieved Month", "Recieved Year", "Recieved By");

	public void backOnAction() throws IOException {
		Region page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);
		page.prefWidthProperty().bind(mainPane.widthProperty());
		page.prefHeightProperty().bind(mainPane.heightProperty());

	}
	
	public void filterList() {
		ArrayList<SupplierOrder> filteredList = new ArrayList<>();
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries
						.queryResult("select * from S_Order where Recieved_By <> '-1' order by Order_ID;", null));
				
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Id") {
			try {
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult(
								"select * from S_Order " + " where Recieved_By <> '-1' and Order_ID like ? "
										+ "order by Order_ID;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
				
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order From") {
			try {
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult(
								"select * from S_Order " + " where Recieved_By <> '-1' and Supplier_ID like ? "
										+ " order by Order_ID;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
				
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order By") {
			try {
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult(
								"select * from S_Order " + " where Recieved_By <> '-1' and Manager_id like ? "
										+ " order by Order_ID;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
				
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
			
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Recieved Month") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
						+ " where Recieved_By <> '-1' and month(Recieved_Date) like ? " + " order by Order_ID;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
				
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Recieved Year") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
						+ " where Recieved_By <> '-1' and year(Recieved_Date) like ? " + " order by Order_ID;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
				
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Recieved By") {
			try {
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult(
								"select * from S_Order " + " where Recieved_By <> '-1' and Recieved_By like ? "
										+ " order by Order_ID;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Month") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order " + " where Recieved_By <> '-1' and month(Date_Of_Order) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Year") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
						+ " where Recieved_By <> '-1' and Year(Date_Of_Order) like ? " + " order by Order_ID;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ArrayList<String> parameters = new ArrayList<>();
				while (parameters.size() < 4) {
					parameters.add("%" + stringToSearch + "%");
				}
				filteredList = SupplierOrder.getSupplierOrderData(
						Queries.queryResult("select * from S_Order " + " where ( year(Date_Of_Order) like ? "
								+ " or month(Date_Of_Order) like ? " + " or Manager_id like ? "
								+ " or Order_ID like ? " + ") and Recieved_By <> '-1' ;", parameters));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		}
		recievedOrdersTable.setItems(FXCollections.observableArrayList(filteredList));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOperationComboBox.setValue("Select");
		searchOperationComboBox.setItems(searchChoices);
		orderIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("iD"));
		dateOfOrderColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dateOfOrder"));
		costColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("cost"));
		discountColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("discount"));
		orderByIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("managerID"));
		orderFromIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("supplierID"));
		recivedByIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("recievedBy"));
		recivedDateColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("recDate"));
		payDueDateColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dueDateOfPayment"));
		try {
			recievedOrdersTable.setItems(FXCollections.observableArrayList(SupplierOrder.getSupplierOrderData(
					Queries.queryResult("select * from S_Order where Recieved_By <> '-1' order by Order_ID;", null))));
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			e1.printStackTrace();
		}

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch=newValue;
			filterList();
			
		});
	}
}
