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

public class BatchReportController implements Initializable {
	
    @FXML
    private TableView<ArrayList<String>> batch;

    @FXML
    private TableColumn<ArrayList<String>, String> expiryDateColumn;

    @FXML
    private TableColumn<ArrayList<String>, String> productNameColumn;

    @FXML
    private TableColumn<ArrayList<String>, String> productionDateColumn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			batch.setItems(FXCollections.observableArrayList(Queries.queryResult("select p.product_name,b.batch_production_date,b.batch_expiry_date\r\n"
					+ "from product p,batch b\r\n" + "where p.product_ID=b.product_Id and b.batch_production_date <> '1111-01-01';",
					null)));
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		productNameColumn.setCellValueFactory(
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
		productionDateColumn.setCellValueFactory(
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

		expiryDateColumn.setCellValueFactory(
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
	}
}
