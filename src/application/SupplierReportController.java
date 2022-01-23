package application;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

import Relations.*;

public class SupplierReportController implements Initializable {

	@FXML
	private TableColumn<Supplier, String> addressColumn;

	@FXML
	private TableColumn<Supplier, Double> duesColumn;

	@FXML
	private TableColumn<Supplier, String> emaiColumn;

	@FXML
	private TableView<Supplier> supplierTable;

	@FXML
	private TableColumn<Supplier, String> idColumn;

	@FXML
	private TableColumn<Supplier, String> nameColumn;	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		idColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("ID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
		emaiColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
		duesColumn.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("dues"));
		supplierTable.setItems(Supplier.getDataList());

	}

}