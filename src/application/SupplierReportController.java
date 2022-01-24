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
	private TableColumn<Supplier, String> supplierAddressColumn;

	@FXML
	private TableColumn<Supplier, Double> supplierDuesColumn;

	@FXML
	private TableColumn<Supplier, String> supplierEmaiColumn;

	@FXML
	private TableView<Supplier> supplierTable;

	@FXML
	private TableColumn<Supplier, String> supplierIdColumn;

	@FXML
	private TableColumn<Supplier, String> supplierNameColumn;	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		supplierIdColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("ID"));
		supplierNameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		supplierAddressColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
		supplierEmaiColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
		supplierDuesColumn.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("dues"));
		supplierTable.setItems(Supplier.getDataList());

	}

}