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
public class CustomerProductsController implements Initializable {

	@FXML
	private TableView<ArrayList<String>> customerProductsTable;

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
	private TableColumn<ArrayList<String>, String> productNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productionDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> saleDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> soldByColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> pharmaceticalCategoryColumn;

	@FXML
	private TextField searchBox;

	@FXML
	private Label title;

	private String stringToSearch = "";
	private Customer customer;

	public void setCustomer(Customer customer) {
		this.customer = customer;
		title.setText(customer.getName().trim() + " Bought Products");
		filterList();
	}

	public void dangerSelection() {
		if (isDanger.isSelected()) {
			isDrug.setSelected(true);
			isControlled.setSelected(false);
		}
		filterList();
	}

	public void controlledSelection() {
		if (isControlled.isSelected()) {
			isDrug.setSelected(true);
			isDanger.setSelected(false);
		}
		filterList();
	}

	public void drugSelection() {
		if (!isDrug.isSelected()) {
			isControlled.setSelected(false);
			isDanger.setSelected(false);
			if (fieldSelector.getSelectionModel().getSelectedItem() != null && fieldSelector.getSelectionModel()
					.getSelectedItem().matches("(Scientific Name|Risk Pregnency Category|Drug Category)")) {
				fieldSelector.setValue("-Specify Field-");
			}
		}
		filterList();
	}

	public void filterList(){
		String drugCondition = "";
		String searchCondition = "";
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add(customer.getNationalID().trim());
		String nonDrugsQuery = "select P.Product_Name, COB.Batch_Production_Date, COB.Batch_Expiry_Date,\r\n"
				+ "CO.Order_Date, E.Employee_Name, COB.Order_amount\r\n"
				+ "from Product P, Batch B, C_Order_Batch COB, C_Order CO, Employee E\r\n"
				+ "where CO.Customer_NID=? \r\n" + "and P.Product_ID not in (select D.Product_ID from Drug D)\r\n"
				+ "and P.Product_ID=B.Product_ID and COB.Product_ID=B.Product_ID\r\n"
				+ "and COB.Batch_Production_Date=B.Batch_Production_Date "
				+ "and COB.Batch_Expiry_Date=B.Batch_Expiry_Date \r\n"
				+ "and COB.Order_ID=CO.Order_ID \r\n" + "and CO.Employee_ID=E.Employee_ID ";

		if (isDanger.isSelected()) {
			drugCondition = " and D.Drug_Pharmacetical_Category='Danger'";
		} else if (isControlled.isSelected()) {
			drugCondition = " and D.Drug_Pharmacetical_Category='Controlled'";
		}
		String drugsQuery = "select P.Product_Name, COB.Batch_Production_Date, COB.Batch_Expiry_Date,\r\n"
				+ "CO.Order_Date, E.Employee_Name, COB.Order_amount, D.Drug_Pharmacetical_Category\r\n"
				+ "from Product P, Batch B, C_Order_Batch COB, C_Order CO, Employee E, Drug D\r\n"
				+ "where CO.Customer_NID=? \r\n"
				+ "and P.Product_ID=B.Product_ID and COB.Product_ID=B.Product_ID and P.Product_ID = D.Product_ID\r\n "
				+ "and COB.Batch_Production_Date=B.Batch_Production_Date "
				+ "and COB.Batch_Expiry_Date=B.Batch_Expiry_Date \r\n"
				+ "and COB.Order_ID=CO.Order_ID " + "and CO.Employee_ID=E.Employee_ID "
				+ drugCondition;

		if (stringToSearch != null && !stringToSearch.isEmpty() && !stringToSearch.isBlank()) {
			parameters.add("%" + stringToSearch + "%");
			if (fieldSelector.getSelectionModel().getSelectedItem() == "Product Name") {
				searchCondition = " and P.Product_Name like ? ";
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
				searchCondition = " and (P.Product_Name like ? " + " or COB.Batch_Production_Date like ? "
						+ " or COB.Batch_Expiry_Date like ? " + " or CO.Order_Date like ? "
						+ " or E.Employee_Name like ? ) ";
			}
		}
		if (!isDrug.isSelected()) {
			filteredList.addAll(Queries.queryResult(nonDrugsQuery + searchCondition + ";", parameters));
		}
		filteredList.addAll(Queries.queryResult(drugsQuery + drugCondition + searchCondition + ";", parameters));
		customerProductsTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fieldSelector.setItems(FXCollections.observableArrayList("-Specify Field-", "Product Name", "Production Date",
				"Expiry Date", "Sale Date", "Sold By"));

		productNameColumn.setCellValueFactory(
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

		productionDateColumn.setCellValueFactory(
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

		expiryDateColumn.setCellValueFactory(
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

		saleDateColumn.setCellValueFactory(
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

		soldByColumn.setCellValueFactory(
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

		pharmaceticalCategoryColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 6) {
							return new SimpleStringProperty(x.get(6));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			this.stringToSearch = newValue;
			filterList();
		});

	}
}
