package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.Queries;

/**
 * 
 * @version 27 January 2022
 * @author Loor Sawalhi
 */
public class SupplierOrderReportController implements Initializable {
	@FXML
	private TableColumn<ArrayList<String>, String> supplierNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> orderCostColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> orderDiscountColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> recievedDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> supplierDuesColumn;

	@FXML
	private TableView<ArrayList<String>> supplierOrderTable;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// TODO
		supplierOrderTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
				"select s.supplier_name,so.order_cost,so.order_discount,so.recieved_date,s.supplier_dues \r\n"
						+ "from s_order so,supplier s\r\n" + "where s.supplier_Id=so.supplier_ID;",
				null)));

		supplierNameColumn.setCellValueFactory(
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

		orderCostColumn.setCellValueFactory(
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

		orderDiscountColumn.setCellValueFactory(
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

		recievedDateColumn.setCellValueFactory(
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
		
		supplierDuesColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 4) {
							return new SimpleStringProperty(x.get(4));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

	}

}