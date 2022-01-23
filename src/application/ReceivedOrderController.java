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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ReceivedOrderController implements Initializable {

	@FXML
	private ImageView add;

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
	private Button reportSupplierOrders;

	@FXML
	private TextField search;

	@FXML
	private ComboBox<String> searchOP;

	@FXML
	private TableView<SupplierOrder> table;

	@FXML
	private Button unreceived;

	@FXML
	private DatePicker sdate;

	@FXML
	private Label warning;

	MainPageController mainPageController;

	@FXML
	private StackPane mainPane;

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		add.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		add.setEffect(effect);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		add.setEffect(effect);
	}

	public void addOnMousePressed() throws IOException {
		
		if (Employee.hasAccess()) {
			Parent page = FXMLLoader.load(getClass().getResource("SupplierOrder.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
		} else {
			Parent page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
		}
	}

	public void ReceivedOrders(ActionEvent event) throws IOException {
		
		if (Employee.hasAccess()) {
			Parent page = FXMLLoader.load(getClass().getResource("RecievedOrders.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
		} else {
			Parent page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);
		}
		
	}

	public void UnReceiveOrder(ActionEvent event)
			throws IOException, ClassNotFoundException, SQLException, ParseException {
		ArrayList<SupplierOrder> temp = SupplierOrder
				.getSupplierOrderData(Queries.queryResult("select * from S_Order where Recieved_By = '-1' ;", null));
		if (temp != null) {
			Parent page = FXMLLoader.load(getClass().getResource("UnReceivedOrders.fxml"));
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(page);

		} else {
			warning.setText("All Orders Are Received");
		}

	}

	ObservableList<String> Choices = FXCollections.observableArrayList("Order Id", "Order From", "Order By",
			"Order Month", "Order Year");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOP.setValue("Select Order");
		searchOP.setItems(Choices);

		id.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("ID"));
		date.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dateOfOrder"));
		cost.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("cost"));
		discount.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("discount"));
		orderby.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("managerID"));
		orderfrom.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("supplierID"));

		table.setItems(SupplierOrder.getDataList());

		search.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<SupplierOrder> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult("select * from S_Order ;", null));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order Id") {
				try {
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult("select * from S_Order where Order_ID like ? ;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order From") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(
							Queries.queryResult("select * from S_Order where Supplier_ID like ? ;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order By") {
				try {
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult("select * from S_Order where Manager_id like ? ;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order Month") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(
							Queries.queryResult("select * from S_Order where month(Date_Of_Order) like ? ;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Year") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
							"select * from S_Order " + " where  Year(Date_Of_Order) like ? order by Order_ID;",
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
					filteredList = SupplierOrder
							.getSupplierOrderData(Queries.queryResult(
									"select * from S_Order " + " where year(Date_Of_Order) like ? "
											+ " or month(Date_Of_Order) like ? " + " or Manager_id like ? "
											+ " or Order_ID like ? ;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%","%" + newValue + "%","%" + newValue + "%","%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			}
			table.setItems(FXCollections.observableArrayList(filteredList));
		});

	}

}
