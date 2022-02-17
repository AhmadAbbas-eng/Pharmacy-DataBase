package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Relations.Queries;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * 
 * @version 30 January 2022
 * @author Aseel Sabri
 *
 */
public class CustomersReportController implements Initializable {

	@FXML
	private TableView<ArrayList<String>> CustomerTable;

	@FXML
	private TableColumn<ArrayList<String>, String> customerNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> employeeNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> orderDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productNameColumn;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		CustomerTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
				"select c.customer_name as 'Customer Name',co.order_date as 'Order Date',e.employee_name as 'Employee name', p.product_name as 'Product Name'\r\n"
						+ "from customer c, c_order co,c_order_batch cob,product p,employee e \r\n"
						+ "where co.employee_Id=e.employee_Id and c.customer_NID=co.customer_NID and co.order_Id=cob.order_Id and p.product_Id=cob.product_ID;",
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

		orderDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		employeeNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(2));
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