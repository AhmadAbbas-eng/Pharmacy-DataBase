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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class UnReceivedController implements Initializable {
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
	private Button receive;

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
	
	private FXMLLoader editLoader;
	
    @FXML
    private Label warning;
    
	@FXML
	private ImageView back;
	
    @FXML
    private StackPane mainPane;
    
	public void backOnAction() throws IOException {
		
		Parent page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);

	}
	
	public void Receive(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, ParseException {

		try {
            if(table.getSelectionModel().getSelectedItem()!=null) {
            	
            editLoader = new FXMLLoader(getClass().getResource("ReceiveOrder.fxml"));
			Parent editPane = (Parent) editLoader.load();
			ReceiveOrdersController edit = editLoader.getController();

			edit.setRow(table.getSelectionModel().getSelectedItem());
			
			mainPane.getChildren().removeAll();
			mainPane.getChildren().setAll(editPane);
            }else {
            	warning.setText("Select An Order");
            }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	ObservableList<String> Choices = FXCollections.observableArrayList("Order Id", "Order From", "Order By",
			"Order Month", "Order Year");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOP.setValue("Select Order");
		searchOP.setItems(Choices);

		id.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("iD"));
		date.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("dateOfOrder"));
		cost.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("cost"));
		discount.setCellValueFactory(new PropertyValueFactory<SupplierOrder, Double>("discount"));
		orderby.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("managerID"));
		orderfrom.setCellValueFactory(new PropertyValueFactory<SupplierOrder, String>("supplierID"));
		
		try {
			table.setItems(FXCollections.observableArrayList(SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order where Recieved_By = '-1';"
	                ,null))));
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			e1.printStackTrace();
		}

		search.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<SupplierOrder> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order where Recieved_By = '-1';" ,null));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order Id") {
				try {
					
					filteredList = SupplierOrder.getSupplierOrderData(
							Queries.queryResult("select * from S_Order where Recieved_By = '-1' and Order_ID like ? ;"
					                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order From") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(
							Queries.queryResult("select * from S_Order where Recieved_By = '-1' and supplier_ID like ? ;"
					                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order By") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(
							Queries.queryResult("select * from S_Order where Recieved_By = '-1' and manager_ID like ? ;"
					                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Order Month") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(
							Queries.queryResult("select * from S_Order where Recieved_By = '-1' and month(Date_Of_Order) like ? ;"
					                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Year") {
				try {
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order where Recieved_By = '-1' "
							+ "and Year(Date_Of_Order) like ? ;"
			                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else {
				try {
					
					filteredList = SupplierOrder.getSupplierOrderData(Queries.queryResult("select * from S_Order where Recieved_By = '-1' and (Order_ID like ? or Year(Date_Of_Order) like ? "
							+ " or month(Date_Of_Order) like ?  or supplier_ID like ? or Order_ID like ? ) ;"
			                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%","%" + newValue + "%","%" + newValue + "%","%" + newValue + "%","%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			}
			table.setItems(FXCollections.observableArrayList(filteredList));
		});
	}
}