package application;

import java.io.IOException;
import java.net.URL;
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

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
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
	
	ObservableList<String> searchChoices = FXCollections.observableArrayList("-Specify Field-","Order Id", "Order By", "Order From",
			"Order Month", "Order Year", "Recieved Month", "Recieved Year", "Recieved By");

	public void backOnAction() throws IOException {
		Region page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		MainPageController.pane.getChildren().removeAll();
		MainPageController.pane.getChildren().setAll(page);
		page.prefWidthProperty().bind(MainPageController.pane.widthProperty());
		page.prefHeightProperty().bind(MainPageController.pane.heightProperty());

	}
	
	public void filterList() {
		ArrayList<SupplierOrder> filteredList = new ArrayList<>();
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			filteredList = SupplierOrder.getSupplierOrderData(Queries
					.queryResult("select * from S_Order where Recieved_By <> '-1' order by Order_ID;", null));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Id") {
			filteredList = SupplierOrder
					.getSupplierOrderData(Queries.queryResult(
							"select * from S_Order " + " where Recieved_By <> '-1' and Order_ID like ? "
									+ "order by Order_ID;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order From") {
			filteredList = SupplierOrder
					.getSupplierOrderData(Queries.queryResult(
							"select * from S_Order " + " where Recieved_By <> '-1' and Supplier_ID like ? "
									+ " order by Order_ID;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order By") {
			filteredList = SupplierOrder
					.getSupplierOrderData(Queries.queryResult(
							"select * from S_Order " + " where Recieved_By <> '-1' and Manager_id like ? "
									+ " order by Order_ID;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Recieved Month") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
					+ " where Recieved_By <> '-1' and month(Recieved_Date) like ? " + " order by Order_ID;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Recieved Year") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
					+ " where Recieved_By <> '-1' and year(Recieved_Date) like ? " + " order by Order_ID;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Recieved By") {
			filteredList = SupplierOrder
					.getSupplierOrderData(Queries.queryResult(
							"select * from S_Order " + " where Recieved_By <> '-1' and Recieved_By like ? "
									+ " order by Order_ID;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Month") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order " + " where Recieved_By <> '-1' and month(Date_Of_Order) like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Year") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order "
					+ " where Recieved_By <> '-1' and Year(Date_Of_Order) like ? " + " order by Order_ID;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else {
			ArrayList<String> parameters = new ArrayList<>();
			while (parameters.size() < 4) {
				parameters.add("%" + stringToSearch + "%");
			}
			filteredList = SupplierOrder.getSupplierOrderData(
					Queries.queryResult("select * from S_Order " + " where ( year(Date_Of_Order) like ? "
							+ " or month(Date_Of_Order) like ? " + " or Manager_id like ? "
							+ " or Order_ID like ? " + ") and Recieved_By <> '-1' ;", parameters));
		}
		recievedOrdersTable.setItems(FXCollections.observableArrayList(filteredList));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOperationComboBox.setValue("-Specify Field-");
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
		recievedOrdersTable.setItems(FXCollections.observableArrayList(SupplierOrder.getSupplierOrderData(
				Queries.queryResult("select * from S_Order where Recieved_By <> '-1' order by Order_ID;", null))));

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch=newValue.trim();
			filterList();
			
		});
	}
}
