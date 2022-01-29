package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import Relations.Customer;

/**
 * 
 * @version 27 January 2022
 * @author Aseel Sabri
 *
 */
public class CustomersReportController implements Initializable {

	@FXML
	private TableView<Customer> CustomerTable;

	@FXML
	private TableColumn<Customer, Double> customerDebtColumn;

	@FXML
	private TableColumn<Customer, String> nationalIDColumn;

	@FXML
	private TableColumn<Customer, String> customerNameColumn;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		nationalIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("nationalID"));
		customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		customerDebtColumn.setCellValueFactory(new PropertyValueFactory<Customer, Double>("debt"));
		CustomerTable.setItems(Customer.getDataList());
	}
}