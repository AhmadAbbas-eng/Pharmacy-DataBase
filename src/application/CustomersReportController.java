package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import Relations.Customer;

public class CustomersReportController implements Initializable {

	@FXML
	private TableView<Customer> CustomerTable;

	@FXML
	private TableColumn<Customer, Double> Debt;

	@FXML
	private TableColumn<Customer, String> NID;

	@FXML
	private TableColumn<Customer, String> Name;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		NID.setCellValueFactory(new PropertyValueFactory<Customer, String>("NID"));
		Name.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		Debt.setCellValueFactory(new PropertyValueFactory<Customer, Double>("debt"));

		CustomerTable.setItems(Customer.getDataList());
	}

}