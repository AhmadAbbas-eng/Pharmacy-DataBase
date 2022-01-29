package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.Customer;
import Relations.Queries;

/**
 * 
 * @version 27 January 2022
 * @author Aseel Sabri
 *
 */
public class CustomersReportController implements Initializable {
	
    @FXML
    private TableView<ArrayList<String>> CustomerTable;

    @FXML
    private TableColumn<ArrayList<String>, String> customerNameColumn;

    @FXML
    private TableColumn<ArrayList<String>, String> employeeIDColumn;

    @FXML
    private TableColumn<ArrayList<String>, String> orderDateColumn;

    @FXML
    private TableColumn<ArrayList<String>, String> productNameColumn;


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		

		CustomerTable.setItems(
				FXCollections.observableArrayList(Queries.queryResult("select c.customer_name ,co.order_date,co.employee_Id, p.product_name\r\n"
						+ "from customer c,customer2order c2o,c_order co,c_order_batch cob,product p \r\n"
						+ "where c.customer_NID=c2o.customer_NID and c2o.order_ID=co.order_ID and co.order_Id=cob.order_Id and p.product_Id=cob.product_ID;",
						null)));

		customerNameColumn.setCellValueFactory(
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

		employeeIDColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		orderDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		productNameColumn.setCellValueFactory(
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

	}
}