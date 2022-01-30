package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 * @author Loor Sawalhi
 */
public class SupplierOrderBatchController implements Initializable{

    @FXML
    private TableColumn<ArrayList<String>, String> amountColumn;

    @FXML
    private TableColumn<ArrayList<String>, String> expiryDateColumn;

    @FXML
    private TableView<ArrayList<String>> orderProductsTable;

    @FXML
    private TableColumn<ArrayList<String>, String> productNameColumn;

    @FXML
    private TableColumn<ArrayList<String>, String> productionDateColumn;

    @FXML
    private ComboBox<String> searchOrderProductsComboBox;

    @FXML
    private TextField searchProductsTextField;

    @FXML
    private Label title;
    
	private String stringToSearch = "";
	
	private SupplierOrder SupplierOrder;

	public void setOrder(SupplierOrder SupplierOrder) {
		this.SupplierOrder = SupplierOrder;
		title.setText("Order "+SupplierOrder.getID() + " Products :");
		filterList();
	}
	

	public void filterList() {
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			filteredList = Queries
					.queryResult("select p.product_name,s.batch_production_date,s.batch_expiry_date,s.batch_amount\r\n"
							+ "from product p, s_order_batch s \r\n"
							+ "where p.product_id = s.product_id and s.order_id = ?;", new ArrayList<>(Arrays.asList(SupplierOrder.getID()+"")));
			
		} else if (searchOrderProductsComboBox.getSelectionModel().getSelectedItem() == "Product Name") {
			filteredList = Queries.queryResult("select p.product_name,s.batch_production_date,s.batch_expiry_date,s.batch_amount\r\n"
					+ "from product p, s_order_batch s \r\n"
					+ "where p.product_id = s.product_id and s.order_id = ? and p.product_name like ?;",
							new ArrayList<>(Arrays.asList(SupplierOrder.getID()+"","%" + stringToSearch + "%")));
			
		} else if (searchOrderProductsComboBox.getSelectionModel().getSelectedItem() == "Production Month") {
			filteredList = Queries.queryResult("select p.product_name,s.batch_production_date,s.batch_expiry_date,s.batch_amount\r\n"
					+ "from product p, s_order_batch s \r\n"
					+ "where p.product_id = s.product_id and s.order_id = ? and month(s.batch_production_Date) like ? ;",
					new ArrayList<>(Arrays.asList(SupplierOrder.getID()+"","%" + stringToSearch + "%")));

		} else if (searchOrderProductsComboBox.getSelectionModel().getSelectedItem() == "Production Year") {
			filteredList = Queries.queryResult("select p.product_name,s.batch_production_date,s.batch_expiry_date,s.batch_amount\r\n"
					+ "from product p, s_order_batch s \r\n"
					+ "where p.product_id = s.product_id and s.order_id = ? and year(s.batch_production_Date) like ? ;",
					new ArrayList<>(Arrays.asList(SupplierOrder.getID()+"","%" + stringToSearch + "%")));

			
		} else if (searchOrderProductsComboBox.getSelectionModel().getSelectedItem() == "Expiry Month") {
			filteredList = Queries.queryResult("select p.product_name,s.batch_production_date,s.batch_expiry_date,s.batch_amount\r\n"
					+ "from product p, s_order_batch s \r\n"
					+ "where p.product_id = s.product_id and s.order_id = ? and month(s.batch_expiry_Date) like ? ;",
					new ArrayList<>(Arrays.asList(SupplierOrder.getID()+"","%" + stringToSearch + "%")));

			
		} else if (searchOrderProductsComboBox.getSelectionModel().getSelectedItem() == "Expiry Year") {
			filteredList = Queries.queryResult("select p.product_name,s.batch_production_date,s.batch_expiry_date,s.batch_amount\r\n"
					+ "from product p, s_order_batch s \r\n"
					+ "where p.product_id = s.product_id and s.order_id = ? and year(s.batch_expiry_Date) like ? ;",
					new ArrayList<>(Arrays.asList(SupplierOrder.getID()+"","%" + stringToSearch + "%")));
		} else {
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add(SupplierOrder.getID()+"");
			while (parameters.size() < 6) {
				parameters.add("%" + stringToSearch + "%");
			}
			filteredList = Queries.queryResult("select p.product_name,s.batch_production_date,s.batch_expiry_date,s.batch_amount\r\n"
					+ "from product p, s_order_batch s \r\n"+ "where p.product_id = s.product_id and s.order_id = ? and ( year(s.batch_expiry_Date) like ?"
							+ " or  month(s.batch_expiry_Date)like ? " + " or  month(s.batch_production_Date) like ? "
							+ " or  year(s.batch_production_Date) like ? "+ " or  p.product_name like ? " + ") ;", parameters);
		}
		orderProductsTable.setItems(FXCollections.observableArrayList(filteredList));
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOrderProductsComboBox.setItems(FXCollections.observableArrayList("-Select-", "Product Name", "Production Date",
				"Expiry Date", "Amount"));

		productNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
				
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(0));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		productionDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							if(x.get(1).compareTo("1111-01-01") == 0) {
								return new SimpleStringProperty("Not Specified");
							}
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		expiryDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							if(x.get(2).compareTo("1111-01-01") == 0) {
								return new SimpleStringProperty("Not Specified");
							}
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});


		amountColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 3) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});



		searchProductsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			this.stringToSearch = newValue;
			filterList();
		});

		
	}
}
