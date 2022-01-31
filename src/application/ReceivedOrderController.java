package application;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.Scene;
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
import javafx.stage.Stage;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 * @author Loor Sawalhi
 */
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

	private String stringToSearch = "";
	
	@FXML
	private Button showProductsButton;
	
	public void showProductsOnAction() {
		if (ordersTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("An Order Must Be Chosen First");
			alert.setContentText("Choose An Order From The Table");
			alert.showAndWait();
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SupplierOrderBatch.fxml"));
			Parent products = (Parent) loader.load();
			SupplierOrderBatchController show = loader.getController();
			show.setOrder(ordersTable.getSelectionModel().getSelectedItem());
			Stage productsStage = new Stage();
			productsStage.setAlwaysOnTop(true);
			Scene scene = new Scene(products);
			productsStage.setScene(scene);
			productsStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



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

	public void newOrderOnMousePressed() {

		if (Employee.hasAccess()) {
			Region page;
			try {
				page = FXMLLoader.load(getClass().getResource("SupplierOrder.fxml"));

				MainPageController.pane.getChildren().removeAll();
				MainPageController.pane.getChildren().setAll(page);
				page.prefWidthProperty().bind(MainPageController.pane.widthProperty());
				page.prefHeightProperty().bind(MainPageController.pane.heightProperty());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Region page;
			try {
				page = FXMLLoader.load(getClass().getResource("NotAvailable.fxml"));

				MainPageController.pane.getChildren().removeAll();
				MainPageController.pane.getChildren().setAll(page);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void receivedOrdersOnAction(ActionEvent event) {
		// contentArea
		if (Employee.hasAccess()) {
			Region page;
			try {
				page = FXMLLoader.load(getClass().getResource("RecievedOrders.fxml"));

				MainPageController.pane.getChildren().removeAll();
				MainPageController.pane.getChildren().setAll(page);
				page.prefWidthProperty().bind(MainPageController.pane.widthProperty());
				page.prefHeightProperty().bind(MainPageController.pane.heightProperty());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Region page;
			try {
				page = FXMLLoader.load(getClass().getResource("NotAvailable.fxml"));

				MainPageController.pane.getChildren().removeAll();
				MainPageController.pane.getChildren().setAll(page);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void unReceiveOrderOnAction(ActionEvent event) {
		ArrayList<SupplierOrder> unRecievedOrdersArrayList = SupplierOrder
				.getSupplierOrderData(Queries.queryResult("select * from S_Order where Recieved_By = '-1' ;", null));
		if (unRecievedOrdersArrayList.size() != 0) {
			Region page;
			try {
				page = FXMLLoader.load(getClass().getResource("UnReceivedOrders.fxml"));

				MainPageController.pane.getChildren().removeAll();
				MainPageController.pane.getChildren().setAll(page);
				page.prefWidthProperty().bind(MainPageController.pane.widthProperty());
				page.prefHeightProperty().bind(MainPageController.pane.heightProperty());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order ;", null));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Id") {
			filteredList = SupplierOrder
					.getSupplierOrderData(Queries.queryResult("select * from S_Order where Order_ID like ? ;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order From") {
			filteredList = SupplierOrder
					.getSupplierOrderData(Queries.queryResult("select * from S_Order where Supplier_ID like ? ;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order By") {
			filteredList = SupplierOrder
					.getSupplierOrderData(Queries.queryResult("select * from S_Order where Manager_id like ? ;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Order Month") {
			filteredList = SupplierOrder.getSupplierOrderData(
					Queries.queryResult("select * from S_Order where month(Date_Of_Order) like ? ;",
							new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Year") {
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order " + " where  Year(Date_Of_Order) like ? order by Order_ID;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else {
			ArrayList<String> parameters = new ArrayList<>();
			while (parameters.size() < 4) {
				parameters.add("%" + stringToSearch + "%");
			}
			filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult(
					"select * from S_Order " + " where year(Date_Of_Order) like ? " + " or month(Date_Of_Order) like ? "
							+ " or Manager_id like ? " + " or Order_ID like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%", "%" + stringToSearch + "%",
							"%" + stringToSearch + "%", "%" + stringToSearch + "%"))));
		}
		ordersTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	ObservableList<String> searchChoices = FXCollections.observableArrayList("-Specify Field-", "Order Id", "Order From",
			"Order By", "Order Month", "Order Year");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SupplierOrder.getSupplierOrderData();

		searchOperationComboBox.setValue("-Specify Field-");
		searchOperationComboBox.setItems(searchChoices);

		orderIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("ID"));
		dateOfOrderColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dateOfOrder"));
		costColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("cost"));
		discountColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("discount"));
		orderByIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("managerID"));
		orderFromIDColumn.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("supplierID"));

		ordersTable.setItems(SupplierOrder.getDataList());

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch = newValue;
			filterList();
		});

	}

}
