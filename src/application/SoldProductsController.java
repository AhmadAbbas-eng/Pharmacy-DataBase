package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.*;
import java.util.*;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import java.net.URL;
import Relations.*;

/**
 * 
 * @version 30 January 2022
 * @author Aseel Sabri
 *
 */
public class SoldProductsController implements Initializable {

	@FXML
	private TableView<ArrayList<String>> soldProductsTable;

	@FXML
	private TableColumn<ArrayList<String>, String> amountColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> expiryDateColumn;

	@FXML
	private ComboBox<String> fieldSelector;

	@FXML
	private CheckBox isControlled;

	@FXML
	private CheckBox isDanger;

	@FXML
	private CheckBox isDrug;

	@FXML
	private TableColumn<ArrayList<String>, String> boughtByColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productionDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> saleDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> soldByColumn;

	@FXML
	private TextField searchBox;

	@FXML
	private Label title;

	private String stringToSearch = "";
	private String productID;

	public void setProduct(String productID, String productName) {
		this.productID = productID;
		title.setText(productName + " Sold Batches");
		filterList();
	}

	public void filterList() {
		String searchCondition = "";
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add(productID);
		String query = "select COB.Batch_Production_Date, COB.Batch_Expiry_Date,\r\n"
				+ "CO.Order_Date, E.Employee_Name, C.Customer_Name, COB.Order_amount\r\n"
				+ "from Customer C, Batch B, C_Order_Batch COB, C_Order CO, Employee E\r\n"
				+ "where B.Product_ID=? \r\n" + "and COB.Product_ID=B.Product_ID\r\n"
				+ "and COB.Batch_Production_Date=B.Batch_Production_Date\r\n"
				+ "and COB.Batch_Expiry_Date=B.Batch_Expiry_Date\r\n"
				+ "and COB.Order_ID=CO.Order_ID \r\n" + "and CO.Employee_ID=E.Employee_ID ";

		if (stringToSearch != null && !stringToSearch.isEmpty() && !stringToSearch.isBlank()) {
			parameters.add("%" + stringToSearch + "%");
			if (fieldSelector.getSelectionModel().getSelectedItem() == "Bought By") {
				searchCondition = " and C.Customer_Name like ? ";
			} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Production Date") {
				searchCondition = " and COB.Batch_Production_Date like ? ";
			}
			else if (fieldSelector.getSelectionModel().getSelectedItem() == "Expiry Date") {
				searchCondition = " and COB.Batch_Expiry_Date like ? ";
			}
			else if (fieldSelector.getSelectionModel().getSelectedItem() == "Sale Date") {
				searchCondition = " and CO.Order_Date like ? ";
			}
			else if (fieldSelector.getSelectionModel().getSelectedItem() == "Sold By") {
				searchCondition = " and E.Employee_Name like ? ";
			} else {
				while (parameters.size() < 6) {
					parameters.add("%" + stringToSearch + "%");
				}
				searchCondition = " and (C.Customer_Name like ? " + " or COB.Batch_Production_Date like ? "
						+ " or COB.Batch_Expiry_Date like ? " + " or CO.Order_Date like ? "
						+ " or E.Employee_Name like ? ) ";
			}
		}
		filteredList = (Queries.queryResult(query + searchCondition + ";", parameters));
		soldProductsTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		fieldSelector.setItems(FXCollections.observableArrayList("-Specify Field-", "Production Date", "Expiry Date",
				"Sale Date", "Sold By", "Bought By"));

		productionDateColumn.setCellValueFactory(
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

		expiryDateColumn.setCellValueFactory(
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

		saleDateColumn.setCellValueFactory(
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

		soldByColumn.setCellValueFactory(
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

		boughtByColumn.setCellValueFactory(
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

		amountColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 5) {
							return new SimpleStringProperty(x.get(5));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			this.stringToSearch = newValue.trim();
			filterList();
		});

	}
}
