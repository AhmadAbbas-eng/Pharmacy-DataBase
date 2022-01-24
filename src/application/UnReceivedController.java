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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class UnReceivedController implements Initializable {

	@FXML
	private TableColumn<SupplierOrder, Double> orderCostColumn;

	@FXML
	private TableColumn<SupplierOrder, String> dateOfOrderColumn;

	@FXML
	private TableColumn<SupplierOrder, Double> orderDiscountColumn;

	@FXML
	private TableColumn<SupplierOrder, String> unreceivedOrderIDColumn;

	@FXML
	private TableColumn<SupplierOrder, String> orderByIDColumn;

	@FXML
	private TableColumn<SupplierOrder, String> orderFromIDColumn;

	@FXML
	private Button receiveButton;

	@FXML
	private TextField searchTextField;

	@FXML
	private ComboBox<String> searchOperationComboBox;

	@FXML
	private TableView<SupplierOrder> unreceivedOrdersTable;

	private FXMLLoader editLoader;

	@FXML
	private ImageView backImage;

	@FXML
	private StackPane mainPane;
	
	private String stringToSearch="";

	ObservableList<String> searchChoices = FXCollections.observableArrayList("Select", "Order Id", "Order From",
			"Order By", "Order Month", "Order Year");

	public void backOnAction() throws IOException {

		Parent page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);

	}

	public void receiveOnAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, ParseException {

		try {
			if (unreceivedOrdersTable.getSelectionModel().getSelectedItem() != null) {

				editLoader = new FXMLLoader(getClass().getResource("ReceiveOrder.fxml"));
				Parent editPane = (Parent) editLoader.load();
				ReceiveOrdersController edit = editLoader.getController();

				edit.setRow(unreceivedOrdersTable.getSelectionModel().getSelectedItem());

				mainPane.getChildren().removeAll();
				mainPane.getChildren().setAll(editPane);
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Unselected Feild");
				alert.setHeaderText(null);
				alert.setContentText("Select An Order");
				alert.showAndWait();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void filterList() {
		ArrayList<SupplierOrder> filteredList = new ArrayList<>();
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(
						Queries.queryResult("select * from S_Order where Recieved_By = '-1';", null));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Id") {
			try {

				filteredList = SupplierOrder.getSupplierOrderData(
						Queries.queryResult("select * from S_Order where Recieved_By = '-1' and Order_ID like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order From") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order where Recieved_By = '-1' and supplier_ID like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order By") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order where Recieved_By = '-1' and manager_ID like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Month") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order where Recieved_By = '-1' and month(Date_Of_Order) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Year") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order where Recieved_By = '-1' " + "and Year(Date_Of_Order) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Select") {
			try {

				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order where Recieved_By = '-1' and (Order_ID like ? or Year(Date_Of_Order) like ? "
								+ " or month(Date_Of_Order) like ?  or supplier_ID like ? or Order_ID like ? ) ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%", "%" + stringToSearch + "%",
								"%" + stringToSearch + "%", "%" + stringToSearch + "%", "%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {

				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order where Recieved_By = '-1' and (Order_ID like ? or Year(Date_Of_Order) like ? "
								+ " or month(Date_Of_Order) like ?  or supplier_ID like ? or Order_ID like ? ) ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%", "%" + stringToSearch + "%",
								"%" + stringToSearch + "%", "%" + stringToSearch + "%", "%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		}
		unreceivedOrdersTable.setItems(FXCollections.observableArrayList(filteredList));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOperationComboBox.setItems(searchChoices);

		unreceivedOrderIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("iD"));
		dateOfOrderColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dateOfOrder"));
		orderCostColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("cost"));
		orderDiscountColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("discount"));
		orderByIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("managerID"));
		orderFromIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("supplierID"));

		try {
			unreceivedOrdersTable.setItems(FXCollections.observableArrayList(SupplierOrder.getSupplierOrderData(
					Queries.queryResult("select * from S_Order where Recieved_By = '-1';", null))));
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			e1.printStackTrace();
		}

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch=newValue;
			filterList();
		});
	}
}