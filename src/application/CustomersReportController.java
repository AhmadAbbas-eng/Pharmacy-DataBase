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
				FXCollections.observableArrayList(Queries.queryResult(" select c.*,sum(co.order_price) \r\n"
						+ "	 from customer c,customer2order c2o,c_order co\r\n"
						+ "	 where c.customer_NID=c2o.customer_NID and c2o.order_ID=co.order_ID\r\n"
						+ "	 group by c.customer_name \r\n" + "	 having sum(co.order_price) >0\r\n"
						+ "	 union (select *,customer_debt\r\n" + "	 from customer\r\n" + "	 where customer_NID not in (\r\n"
						+ "	 select customer2order.customer_NID\r\n" + "	 from customer2order))\r\n" + "	 order by 2;",
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