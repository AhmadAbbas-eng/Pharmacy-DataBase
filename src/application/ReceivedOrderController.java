package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.Employee;
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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ReceivedOrderController implements Initializable {

	@FXML
	private ImageView newOrderIcon;

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
	private Button receivedOrdersButton;
	
	@FXML
	private Button reportSupplierOrders;

	@FXML
	private TextField searchTextField;

	@FXML
	private ComboBox<String> searchOperationComboBox;

	@FXML
	private TableView<SupplierOrder> ordersTable;

	@FXML
	private Button unreceivedOrdersButton;

	MainPageController mainPageController;

	private String stringToSearch="";
	
	@FXML
	private StackPane mainPane;

	public void newOrderOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		newOrderIcon.setEffect(effect);
	}

	public void newOrderOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		newOrderIcon.setEffect(effect);
	}

	public void newOrderOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		newOrderIcon.setEffect(effect);
	}

	public void newOrderOnMousePressed() throws IOException {
		
		if (Employee.hasAccess()) {
			Region page = FXMLLoader.load(getClass().getResource("SupplierOrder.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
			page.prefWidthProperty().bind(mainPane.widthProperty());
			page.prefHeightProperty().bind(mainPane.heightProperty());
		} else {
			Region page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
			page.prefWidthProperty().bind(mainPane.widthProperty());
			page.prefHeightProperty().bind(mainPane.heightProperty());
		}
	}

	public void receivedOrdersOnAction(ActionEvent event) throws IOException {
		
		if (Employee.hasAccess()) {
			Region page = FXMLLoader.load(getClass().getResource("RecievedOrders.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
			page.prefWidthProperty().bind(mainPane.widthProperty());
			page.prefHeightProperty().bind(mainPane.heightProperty());
		} else {
			Region page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
			page.prefWidthProperty().bind(mainPane.widthProperty());
			page.prefHeightProperty().bind(mainPane.heightProperty());
		}
		
	}

	public void unReceiveOrderOnAction(ActionEvent event)
			throws IOException, ClassNotFoundException, SQLException, ParseException {
		ArrayList<SupplierOrder> unRecievedOrdersArrayList = SupplierOrder
				.getSupplierOrderData(Queries.queryResult("select * from S_Order where Recieved_By = '-1' ;", null));
		if (unRecievedOrdersArrayList.size()!=0) {
			Region page = FXMLLoader.load(getClass().getResource("UnReceivedOrders.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
			page.prefWidthProperty().bind(mainPane.widthProperty());
			page.prefHeightProperty().bind(mainPane.heightProperty());

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Alert");
			alert.setHeaderText(null);
			alert.setContentText("All Orders Are Received");
			alert.showAndWait();
		}

	}
	
	public void filterList() {
		ArrayList<SupplierOrder> filteredList = new ArrayList<>();
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			try {
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult("select * from S_Order ;", null));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Id") {
			try {
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult("select * from S_Order where Order_ID like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order From") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(
						Queries.queryResult("select * from S_Order where Supplier_ID like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order By") {
			try {
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult("select * from S_Order where Manager_id like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Month") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(
						Queries.queryResult("select * from S_Order where month(Date_Of_Order) like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Year") {
			try {
				filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
						"select * from S_Order " + " where  Year(Date_Of_Order) like ? order by Order_ID;",
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
				filteredList = SupplierOrder
						.getSupplierOrderData(Queries.queryResult(
								"select * from S_Order " + " where year(Date_Of_Order) like ? "
										+ " or month(Date_Of_Order) like ? " + " or Manager_id like ? "
										+ " or Order_ID like ? ;",
								new ArrayList<>(Arrays.asList("%" + stringToSearch + "%","%" + stringToSearch + "%","%" + stringToSearch + "%","%" + stringToSearch + "%"))));
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				e.printStackTrace();
			}
		}
		ordersTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	ObservableList<String> searchChoices = FXCollections.observableArrayList("Select","Order Id", "Order From", "Order By",
			"Order Month", "Order Year");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOperationComboBox.setValue("Select Order");
		searchOperationComboBox.setItems(searchChoices);

		orderIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("ID"));
		dateOfOrderColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dateOfOrder"));
		costColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("cost"));
		discountColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("discount"));
		orderByIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("managerID"));
		orderFromIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("supplierID"));

		ordersTable.setItems(SupplierOrder.getDataList());

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch=newValue;
			filterList();
		});

	}

}
