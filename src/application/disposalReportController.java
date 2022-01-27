package application;

import java.net.URL;
import java.sql.SQLException;
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
 * @version 27 January 2022
 * @author Ahmad Abbas
 */
public class disposalReportController implements Initializable {

	@FXML
	private TableColumn<ArrayList<String>, String> disposalAmountColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalCostColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalExpiryDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalEmployeeNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalProductNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalProductionDateColumn;

	@FXML
	private TableView<ArrayList<String>> disposal;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalDateColumn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			disposal.setItems(FXCollections.observableArrayList(Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount\r\n"
							+ "from product p, employee e, payment pay,drug_disposal d\r\n"
							+ "where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id;",
					null)));
			
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		disposalCostColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(6));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		disposalDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(4));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		disposalEmployeeNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(5));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		disposalExpiryDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		disposalAmountColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		disposalProductNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(0));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		disposalProductionDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});
	}
}
