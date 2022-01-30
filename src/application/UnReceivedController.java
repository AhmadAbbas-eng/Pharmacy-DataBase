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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
	
	private String stringToSearch="";

	ObservableList<String> searchChoices = FXCollections.observableArrayList("Select", "Order Id", "Order From",
			"Order By", "Order Month", "Order Year");

	public void backOnAction() {
		Region page;
		try {
			page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		
		MainPageController.pane.getChildren().removeAll();
		MainPageController.pane.getChildren().setAll(page);
		page.prefWidthProperty().bind(MainPageController.pane.widthProperty());
		page.prefHeightProperty().bind(MainPageController.pane.heightProperty());
} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receiveOnAction(ActionEvent event){

		try {
			if (unreceivedOrdersTable.getSelectionModel().getSelectedItem() != null) {

				editLoader = new FXMLLoader(getClass().getResource("ReceiveOrder.fxml"));
				Region editPane = (Region) editLoader.load();
			
				ReceiveOrdersController edit = editLoader.getController();
				edit.setRow(unreceivedOrdersTable.getSelectionModel().getSelectedItem());
				MainPageController.pane.getChildren().removeAll();
				MainPageController.pane.getChildren().setAll(editPane);
				editPane.prefWidthProperty().bind(MainPageController.pane.widthProperty());
				editPane.prefHeightProperty().bind(MainPageController.pane.heightProperty());
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
			filteredList = SupplierOrder.getSupplierOrderData(
					Queries.queryResult("select * from S_Order where Recieved_By = '-1';", null));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Id") {
			filteredList = SupplierOrder.getSupplierOrderData(
					Queries.queryResult("select * from S_Order where Recieved_By = '-1' and Order_ID like ? ;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order From") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order where Recieved_By = '-1' and supplier_ID like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order By") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order where Recieved_By = '-1' and manager_ID like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Month") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order where Recieved_By = '-1' and month(Date_Of_Order) like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Year") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order where Recieved_By = '-1' " + "and Year(Date_Of_Order) like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Select") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order where Recieved_By = '-1' and (Order_ID like ? or Year(Date_Of_Order) like ? "
							+ " or month(Date_Of_Order) like ?  or supplier_ID like ? or Order_ID like ? ) ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%", "%" + stringToSearch + "%",
							"%" + stringToSearch + "%", "%" + stringToSearch + "%", "%" + stringToSearch + "%"))));
		} else {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order where Recieved_By = '-1' and (Order_ID like ? or Year(Date_Of_Order) like ? "
							+ " or month(Date_Of_Order) like ?  or supplier_ID like ? or Order_ID like ? ) ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%", "%" + stringToSearch + "%",
							"%" + stringToSearch + "%", "%" + stringToSearch + "%", "%" + stringToSearch + "%"))));
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

		unreceivedOrdersTable.setItems(FXCollections.observableArrayList(SupplierOrder.getSupplierOrderData(
				Queries.queryResult("select * from S_Order where Recieved_By = '-1';", null))));

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch=newValue;
			filterList();
		});
	}
}