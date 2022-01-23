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

public class disposalReportController implements Initializable {


	@FXML
	private TableColumn<ArrayList<String>, String> disAmount;

	@FXML
	private TableColumn<ArrayList<String>, String> disCost;

	@FXML
	private TableColumn<ArrayList<String>, String> disED;

	@FXML
	private TableColumn<ArrayList<String>, String> disEN;

	@FXML
	private TableColumn<ArrayList<String>, String> disName;

	@FXML
	private TableColumn<ArrayList<String>, String> disPD;

	@FXML
	private TableView<ArrayList<String>> disposal;

	@FXML
	private TableColumn<ArrayList<String>, String> DateOfDis;


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

		disCost.setCellValueFactory(
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

		DateOfDis.setCellValueFactory(
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

		disEN.setCellValueFactory(
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

		disED.setCellValueFactory(
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

		disAmount.setCellValueFactory(
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

		disName.setCellValueFactory(
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

		disPD.setCellValueFactory(
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
