package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * 
 * @version 30 January 2022
 * @author Loor Sawalhi
 */
public class PaymentController implements Initializable {
	
	// --------------------------------------Disposal-----------------------------------------------
	@FXML
	private TextField searchDisposalTextField;

	@FXML
	private ComboBox<String> disposalOperationComboBox;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalAmountColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalCostColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalExpiryDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalEmployeeNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalProductName;

	@FXML
	private TableColumn<ArrayList<String>, String> disposalProductionDateColumn;

	@FXML
	private TableView<ArrayList<String>> disposalTable;

	@FXML
	private TableColumn<ArrayList<String>, String> dateOfDisposalColumn;
	
	String disposalNewValue = "";
	
	public void disposalFilterList() {
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		if (disposalNewValue == null || disposalNewValue.isEmpty() || disposalNewValue.isBlank()) {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id;",
					null);
		} else if (disposalOperationComboBox.getSelectionModel().getSelectedItem() == "Product Name") {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
							+ " and p.product_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%")));
		} else if (disposalOperationComboBox.getSelectionModel().getSelectedItem() == "Expired Month") {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
							+ " and  month(d.batch_expiry_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%")));

		} else if (disposalOperationComboBox.getSelectionModel().getSelectedItem() == "Expired Year") {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
							+ " and  year(d.batch_expiry_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%")));
		} else if (disposalOperationComboBox.getSelectionModel().getSelectedItem() == "Production Month") {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
							+ " and  month(d.batch_production_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%")));

		} else if (disposalOperationComboBox.getSelectionModel().getSelectedItem() == "Production Year") {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
							+ " and  year(d.batch_production_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%")));

		} else if (disposalOperationComboBox.getSelectionModel().getSelectedItem() == "Disposal Month") {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
							+ " and  month(d.Disposal_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%")));

		} else if (disposalOperationComboBox.getSelectionModel().getSelectedItem() == "Disposal Year") {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
							+ " and  year(d.Disposal_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%")));
		} else {
			filteredList = Queries.queryResult(
					"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
							+ " from product p, employee e, payment pay,drug_disposal d "
							+ " where (p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id) "
							+ " and  (year(d.Disposal_date) like ? or month(d.Disposal_date) like ? or p.product_name like ? "
							+ " or year(d.Batch_Production_Date) like ? or month(d.Batch_Production_Date) like ? or "
							+ " year(d.Batch_Expiry_Date) like ? or month(d.Batch_Expiry_Date) like ?);",
					new ArrayList<>(Arrays.asList("%" + disposalNewValue + "%", "%" + disposalNewValue + "%",
							"%" + disposalNewValue + "%", "%" + disposalNewValue + "%", "%" + disposalNewValue + "%",
							"%" + disposalNewValue + "%", "%" + disposalNewValue + "%")));
		}
		disposalTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	// --------------------------------------tax----------------------------------------------------

	@FXML
	private TableColumn<ArrayList<String>, String> amountaTaxColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> dateOfPaymentTaxColumn;

	@FXML
	private TableView<ArrayList<String>> taxTable;

	@FXML
	private TableColumn<ArrayList<String>, String> taxIDColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> managerNametaxColumn;

	@FXML
	private CheckBox chequeTaxCheckBox;

	@FXML
	private TextField chequeIDTaxTextField;

	@FXML
	private Label availableUnitlTaxLabel;

	@FXML
	private Label bankNameTaxLabel;

	@FXML
	private Label writindDateTaxLabel;

	@FXML
	private Label chequeTaxLabel;

	@FXML
	private Button addTaxButton;

	@FXML
	private Button clearTaxButton;

	@FXML
	private ComboBox<String> taxOperationComboBox;

	@FXML
	private TextField searchTaxTextField;

	@FXML
	private TextField amountTaxTextField;

	@FXML
	private TextField bankNameTaxTextField;

	@FXML
	private TextField taxIDTextField;

	@FXML
	private DatePicker availableUntilDateTax;

	@FXML
	private DatePicker writingDateTax;
	
	String taxNewValue = "";

	ObservableList<String> TaxChoices = FXCollections.observableArrayList("-Specify Field-","Tax ID", "Manager Name", "Payment Month",
			"Payment Year");
	
	public void taxFilterList() { 
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		if (taxNewValue == null || taxNewValue.isEmpty() || taxNewValue.isBlank()) {
			filteredList = Queries.queryResult(
					"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
							+ " from employee e , tax t, payment p, taxes_payment tp "
							+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
					null);
		} else if (taxOperationComboBox.getSelectionModel().getSelectedItem() == "Tax ID") {
			filteredList = Queries
					.queryResult("select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
							+ " from employee e , tax t, payment p, taxes_payment tp "
							+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id"
							+ " and t.tax_id like ? ;", new ArrayList<>(Arrays.asList("%" + taxNewValue + "%")));
		} else if (taxOperationComboBox.getSelectionModel().getSelectedItem() == "Manager Name") {
			filteredList = Queries.queryResult(
					"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
							+ " from employee e , tax t, payment p, taxes_payment tp "
							+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id"
							+ " and  e.employee_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + taxNewValue + "%")));

		} else if (taxOperationComboBox.getSelectionModel().getSelectedItem() == "Payment Month") {
			filteredList = Queries.queryResult(
					"select  distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
							+ " from employee e , tax t, payment p, taxes_payment tp "
							+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id "
							+ " and  month(t.tax_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + taxNewValue + "%")));

		} else if (taxOperationComboBox.getSelectionModel().getSelectedItem() == "Payment Year") {
			filteredList = Queries.queryResult(
					"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
							+ " from employee e , tax t, payment p, taxes_payment tp "
							+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id "
							+ "and  year(t.tax_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + taxNewValue + "%")));

		} else {
			ArrayList<String> parameters = new ArrayList<>();
			while (parameters.size() < 4) {
				parameters.add("%" + taxNewValue + "%");
			}
			filteredList = Queries
					.queryResult("select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
							+ " from employee e , tax t, payment p, taxes_payment tp "
							+ " where ( tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id )"
							+ "and  (year(t.tax_date) like ? " + " or month(t.tax_date) like ? "
							+ " or  e.employee_name like ? " + " or t.tax_id like ?) ;", parameters);
		}
		taxTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	public void chequeTaxOnAction(ActionEvent event) {
		if (chequeTaxCheckBox.isSelected() == true) {
			bankNameTaxTextField.setOpacity(1);
			writingDateTax.setOpacity(1);
			chequeIDTaxTextField.setOpacity(1);
			availableUnitlTaxLabel.setOpacity(1);
			bankNameTaxLabel.setOpacity(1);
			writindDateTaxLabel.setOpacity(1);
			chequeTaxLabel.setOpacity(1);
			availableUntilDateTax.setOpacity(1);
		} else {
			bankNameTaxTextField.setOpacity(0);
			availableUntilDateTax.setOpacity(0);
			writingDateTax.setOpacity(0);
			chequeIDTaxTextField.setOpacity(0);
			availableUnitlTaxLabel.setOpacity(0);
			bankNameTaxLabel.setOpacity(0);
			writindDateTaxLabel.setOpacity(0);
			chequeTaxLabel.setOpacity(0);
		}
	}

	public void addTaxOnAction(ActionEvent event){
		if (taxIDTextField.getText().isBlank() == false && amountTaxTextField.getText().isBlank() == false) {

			if (chequeTaxCheckBox.isSelected() == true) {
				if (chequeIDTaxTextField.getText().isBlank() == false && bankNameTaxTextField.getText().isBlank() == false
						&& writingDateTax.getValue() != null && availableUntilDateTax.getValue() != null) {
					boolean flag = false;
					if (writingDateTax.getValue().compareTo(availableUntilDateTax.getValue()) == 0
							|| writingDateTax.getValue().compareTo(availableUntilDateTax.getValue()) > 0) {
						flag = true;
					}
					
					ArrayList<Tax> List = new ArrayList<>();
					List = Tax.getTaxData(Queries.queryResult("select * from Tax where tax_ID = ? ;",
							new ArrayList<>(Arrays.asList(taxIDTextField.getText().toString()))));
					
					if (List.isEmpty() == true) {
						ArrayList<Cheque> List2 = new ArrayList<>();
						List2 = Cheque.getChequeData(Queries.queryResult("select * from cheque where cheque_ID = ? ;",
								new ArrayList<>(Arrays.asList(chequeIDTaxTextField.getText().toString()))));
						if (List2.isEmpty() == true) {
							try {
								Double num = Double.parseDouble(amountTaxTextField.getText());
								if (num <= 0) {
									flag = true;
								}
							} catch (NumberFormatException e1) {
								flag = true;
							}
							String str = bankNameTaxTextField.getText().replaceAll("\\s", "");
							if (str.matches("[a-zA-Z]+") == false) {
								flag = true;
							}
							if (flag == false) {
								Payment.insertPayment(java.time.LocalDate.now(),
										Double.parseDouble(amountTaxTextField.getText()), "Cheque");
								
								Cheque.insertCheque(chequeIDTaxTextField.getText(), bankNameTaxTextField.getText(), writingDateTax.getValue(),
										availableUntilDateTax.getValue(), Payment.getMaxID(), Employee.getCurrentID());
								
								Tax.insertTax(taxIDTextField.getText().toString(), java.time.LocalDate.now(),
										Double.parseDouble(amountTaxTextField.getText()));
								
								Queries.queryUpdate(
										"insert into taxes_payment (payment_id, tax_id, manager_id) "
												+ "values (?, ?, ?) ;",
										new ArrayList<>(Arrays.asList(Payment.getMaxID() + "",
												taxIDTextField.getText().toString() + "", Employee.getCurrentID() + "")));

								taxTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
										"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
												+ " from  employee e , tax t, payment p, taxes_payment tp "
												+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
										null)));
								taxIDTextField.clear();
								amountTaxTextField.clear();
								bankNameTaxTextField.clear();
								chequeIDTaxTextField.clear();
								writingDateTax.setValue(null);
								availableUntilDateTax.setValue(null);
								chequeTaxCheckBox.setSelected(false);
							}
							else {
								Alert alert = new Alert(Alert.AlertType.ERROR);
								alert.setTitle(null);
								alert.setHeaderText(null);
								alert.setContentText("Wrong Data Format");
								alert.showAndWait();
							}
						} else {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle(null);
							alert.setHeaderText(null);
							alert.setContentText("This Cheque ID Already Exists!");
							alert.showAndWait();
						}
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("This Tax ID Already Exists!");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Fill The Cheque Fields");
					alert.showAndWait();
				}
			} else {
				ArrayList<Tax> List = new ArrayList<>();
				List = Tax.getTaxData(Queries.queryResult("select * from Tax where tax_ID = ? ;",
						new ArrayList<>(Arrays.asList(taxIDTextField.getText().toString()))));
				if (List.isEmpty() == true) {
					boolean flag = false;
					try {

						Double num = Double.parseDouble(amountTaxTextField.getText());
						if (num <= 0) {
							flag = true;
						}
					} catch (NumberFormatException e1) {
						flag = true;
					}
					if (!flag) {
						Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(amountTaxTextField.getText()),
								"Cash");
						
						Tax.insertTax(taxIDTextField.getText().toString(), java.time.LocalDate.now(),
								Double.parseDouble(amountTaxTextField.getText()));
						
						Queries.queryUpdate(
								"insert into taxes_payment " + " (payment_id, tax_id, manager_id)"
										+ "Values (?, ?, ?) ;",
								new ArrayList<>(Arrays.asList(Payment.getMaxID() + "",
										taxIDTextField.getText().toString() + "", Employee.getCurrentID() + "")));
						taxTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
								"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
										+ " from employee e , tax t, payment p, taxes_payment tp "
										+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
								null)));
						taxIDTextField.clear();
						amountTaxTextField.clear();
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("Wrong Information!");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("This Tax Already Exists");
					alert.showAndWait();
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Fill The Required Fields");
			alert.showAndWait();
		}

	}

	public void clearTaxOnAction(ActionEvent event) {
		taxIDTextField.clear();
		amountTaxTextField.clear();
		bankNameTaxTextField.clear();
		chequeIDTaxTextField.clear();
		writingDateTax.setValue(null);
		availableUntilDateTax.setValue(null);
		chequeTaxCheckBox.setSelected(false);
		bankNameTaxTextField.setOpacity(0);
		availableUntilDateTax.setOpacity(0);
		writingDateTax.setOpacity(0);
		chequeIDTaxTextField.setOpacity(0);
		availableUnitlTaxLabel.setOpacity(0);
		bankNameTaxLabel.setOpacity(0);
		writindDateTaxLabel.setOpacity(0);
		chequeTaxLabel.setOpacity(0);
	}

	// ----------------------------------------supplier------------------------------------------------
	@FXML
	private TableColumn<ArrayList<String>, String> supplierPaymentNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> supplierManagerNameColum;

	@FXML
	private TableColumn<ArrayList<String>, String> supplierPaymentDateColumn;
	
	@FXML
	private TableColumn<ArrayList<String>, String> supplierAmountColumn;

	@FXML
	private TableView<ArrayList<String>> supplierPaymentTable;

	@FXML
	private TableView<Supplier> supplierDataTable;

	@FXML
	private TableColumn<Supplier, String> supplierNameColumn;

	@FXML
	private TableColumn<Supplier, Double> supplierDuesColumn;

	@FXML
	private TextField searchSupplierTextField;

	@FXML
	private ComboBox<String> searchSupplierOperationComboBox;

	@FXML
	private TextField searchSupplierPaymentTextField;

	@FXML
	private DatePicker availablUntilDateSupplier;

	@FXML
	private DatePicker writingDateSupplier;

	@FXML
	private Button addSupplierButton;

	@FXML
	private Button clearSupplierButton;

	@FXML
	private TextField amountSupplierTextField;

	@FXML
	private TextField bankNameSupplierTextField;

	@FXML
	private CheckBox chequeSupplierCheckBox;

	@FXML
	private TextField chequeIDSupplierTextField;

	@FXML
	private Label supplierAvailableUntilLabel;

	@FXML
	private Label supplierWritingDateLabel;

	@FXML
	private Label supplierBankNameLabel;

	@FXML
	private Label supplierChequeIDLabel;
	
	String supplierNewValue = "";
	
	String supplierPaymentNewValue = "";
	
	public void supplierPaymentFilterList() {
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		if (supplierPaymentNewValue == null || supplierPaymentNewValue.isEmpty() || supplierPaymentNewValue.isBlank()) {
			filteredList = Queries.queryResult(
					"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
							+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
							+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
							+ " and so.supplier_id=s.supplier_id;",
					null);
		} else if (searchSupplierOperationComboBox.getSelectionModel().getSelectedItem() == "Supplier Name") {
			filteredList = Queries.queryResult(
					"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
							+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
							+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
							+ " and so.supplier_id=s.supplier_id and  s.supplier_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + supplierPaymentNewValue + "%")));
		} else if (searchSupplierOperationComboBox.getSelectionModel().getSelectedItem() == "Manager Name") {
			filteredList = Queries.queryResult(
					"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
							+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
							+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
							+ " and so.supplier_id=s.supplier_id and  e.employee_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + supplierPaymentNewValue + "%")));

		} else if (searchSupplierOperationComboBox.getSelectionModel().getSelectedItem() == "Payment Month") {
			filteredList = Queries.queryResult(
					"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
							+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
							+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
							+ " and so.supplier_id=s.supplier_id and  month(p.payment_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + supplierPaymentNewValue + "%")));

		} else if (searchSupplierOperationComboBox.getSelectionModel().getSelectedItem() == "Payment Year") {
			filteredList = Queries.queryResult(
					"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
							+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
							+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
							+ " and so.supplier_id=s.supplier_id and  year(p.payment_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + supplierPaymentNewValue + "%")));
		} else {
			ArrayList<String> parameters = new ArrayList<>();
			while (parameters.size() < 4) {
				parameters.add("%" + supplierPaymentNewValue + "%");
			}
			filteredList = Queries.queryResult(
					"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
							+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
							+ " where  (op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
							+ " and so.supplier_id=s.supplier_id )and  (year(p.payment_date) like ? "
							+ " or month(p.payment_date) like ? " + " or  e.employee_name like ? "
							+ " or s.supplier_name like ? );",
					parameters);
		}
		supplierPaymentTable.setItems(FXCollections.observableArrayList(filteredList));
	}
	
	public void supplierFilterList() {
		ArrayList<Supplier> filteredList = new ArrayList<>();
		if (supplierNewValue == null || supplierNewValue.isEmpty() || supplierNewValue.isBlank()) {
			filteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier;", null));
		} else {
			filteredList = Supplier.getSupplierData(Queries.queryResult(
					"select * from Supplier " + " where  supplier_name like ? " + " order by supplier_ID ;",
					new ArrayList<>(Arrays.asList("%" + supplierNewValue + "%"))));
		}
		supplierDataTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	ObservableList<String> SupplierPaymentChoices = FXCollections.observableArrayList("-Specify Field-","Supplier Name", "Manager Name",
			"Payment Month", "Payment Year");

	public void chequeSupplierOnAction(ActionEvent event) {
		if (chequeSupplierCheckBox.isSelected() == true) {
			bankNameSupplierTextField.setOpacity(1);
			writingDateSupplier.setOpacity(1);
			availablUntilDateSupplier.setOpacity(1);
			chequeIDSupplierTextField.setOpacity(1);
			supplierAvailableUntilLabel.setOpacity(1);
			supplierWritingDateLabel.setOpacity(1);
			supplierBankNameLabel.setOpacity(1);
			supplierChequeIDLabel.setOpacity(1);
		} else {
			bankNameSupplierTextField.setOpacity(0);
			writingDateSupplier.setOpacity(0);
			availablUntilDateSupplier.setOpacity(0);
			chequeIDSupplierTextField.setOpacity(0);
			supplierAvailableUntilLabel.setOpacity(0);
			supplierWritingDateLabel.setOpacity(0);
			supplierBankNameLabel.setOpacity(0);
			supplierChequeIDLabel.setOpacity(0);
		}
	}

	public void clearSupplierOnAction(ActionEvent event) {
		amountSupplierTextField.clear();
		bankNameSupplierTextField.clear();
		chequeIDSupplierTextField.clear();
		availablUntilDateSupplier.setValue(null);
		writingDateSupplier.setValue(null);
		chequeSupplierCheckBox.setSelected(false);
		bankNameSupplierTextField.setOpacity(0);
		writingDateSupplier.setOpacity(0);
		availablUntilDateSupplier.setOpacity(0);
		chequeIDSupplierTextField.setOpacity(0);
		supplierAvailableUntilLabel.setOpacity(0);
		supplierWritingDateLabel.setOpacity(0);
		supplierBankNameLabel.setOpacity(0);
		supplierChequeIDLabel.setOpacity(0);
	}

	public void addSupplierOnAction(ActionEvent event)  {

		if (amountSupplierTextField.getText().isBlank() == false && supplierDataTable.getSelectionModel().getSelectedItem() != null) {
			if (chequeSupplierCheckBox.isSelected() == true) {
				if (chequeIDSupplierTextField.getText().isBlank() == false && bankNameSupplierTextField.getText().isBlank() == false
						&& availablUntilDateSupplier.getValue() != null && writingDateSupplier.getValue() != null) {
					boolean flag = false;
					if (writingDateSupplier.getValue().compareTo(availablUntilDateSupplier.getValue()) == 0
							|| writingDateSupplier.getValue().compareTo(availablUntilDateSupplier.getValue()) > 0) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("Set Reasonable Date!");
						alert.showAndWait();
					} else {

						ArrayList<Cheque> List2 = new ArrayList<>();
						List2 = Cheque.getChequeData(Queries.queryResult("select * from cheque where cheque_ID = ? ;",
								new ArrayList<>(Arrays.asList(chequeIDSupplierTextField.getText().toString()))));
						if (List2.isEmpty() == true) {

							try {
								Double num = Double.parseDouble(amountSupplierTextField.getText());
								if (num <= 0) {
									flag = true;
								}
							} catch (NumberFormatException e1) {
								flag = true;
							}
							String str = bankNameSupplierTextField.getText().replaceAll("\\s", "");
							if (str.matches("[a-zA-Z]+") == false) {
								flag = true;
							}
							if (!flag) {
								Payment.insertPayment(java.time.LocalDate.now(),
										Double.parseDouble(amountSupplierTextField.getText()), "Cheque");

								Cheque.insertCheque(chequeIDSupplierTextField.getText(), bankNameSupplierTextField.getText(),
										writingDateSupplier.getValue(), availablUntilDateSupplier.getValue(), Payment.getMaxID(),
										Employee.getCurrentID());

								Queries.queryUpdate(
										"insert into supplier_payment (supplier_id, manager_id, payment_id)"
												+ " Values (?, ?, ?);",
										new ArrayList<>(Arrays.asList(
												supplierDataTable.getSelectionModel().getSelectedItem().getID() + "",
												Employee.getCurrentID() + "", Payment.getMaxID() + "")));
								Queries.queryUpdate(
										"update Supplier set supplier_dues = supplier_dues -? where Supplier_ID =?;",
										new ArrayList<>(Arrays.asList(amountSupplierTextField.getText(),
												supplierDataTable.getSelectionModel().getSelectedItem().getID() + "")));

								supplierPaymentTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
										"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
												+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
												+ " where op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
												+ " and so.supplier_id=s.supplier_id;",
										null)));
								
								supplierDataTable.setItems(FXCollections.observableArrayList(Supplier
										.getSupplierData(Queries.queryResult("select * from Supplier;", null))));
								amountSupplierTextField.clear();
								bankNameSupplierTextField.clear();
								chequeIDSupplierTextField.clear();
								availablUntilDateSupplier.setValue(null);
								writingDateSupplier.setValue(null);
								chequeSupplierCheckBox.setSelected(false);
							} else {
								Alert alert = new Alert(Alert.AlertType.ERROR);
								alert.setTitle(null);
								alert.setHeaderText(null);
								alert.setContentText("Wrong Information!");
								alert.showAndWait();
							}
						} else {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle(null);
							alert.setHeaderText(null);
							alert.setContentText("This Cheque ID Already Exists!");
							alert.showAndWait();
						}
					}

				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Fill The Cheque Fields");
					alert.showAndWait();
				}
			} else {

				boolean flag = false;
				try {
					Double num = Double.parseDouble(amountSupplierTextField.getText());
					if (num <= 0) {
						flag = true;
					}
				} catch (NumberFormatException e1) {
					flag = true;
				}
				if (!flag) {
					Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(amountSupplierTextField.getText()),
							"Cash");
					
					Queries.queryUpdate(
							"insert into supplier_payment (supplier_id, manager_id, payment_id) Values (?, ?, ?) ;",
							new ArrayList<>(
									Arrays.asList(supplierDataTable.getSelectionModel().getSelectedItem().getID() + "",
											Employee.getCurrentID() + "", Payment.getMaxID() + "")));
					
					Queries.queryUpdate("update Supplier set supplier_dues = supplier_dues -? where Supplier_ID =?;",
							new ArrayList<>(Arrays.asList(amountSupplierTextField.getText(),
									supplierDataTable.getSelectionModel().getSelectedItem().getID() + "")));

					supplierPaymentTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
							"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
									+ " from employee e, supplier s, payment p, supplier_payment op, s_order so"
									+ " where op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
									+ " and so.supplier_id=s.supplier_id;",
							null)));
					
					supplierDataTable.setItems(FXCollections.observableArrayList(
							Supplier.getSupplierData(Queries.queryResult("select * from Supplier;", null))));
					amountSupplierTextField.clear();
				}
				else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Wrong amount fromat");
					alert.showAndWait();
				}
				

			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Fill The Required Fields And Select A Supplier");
			alert.showAndWait();
		}
	}

	// --------------------------------------Employee-----------------------------------------------------

	@FXML
	private TableColumn<ArrayList<String>, String> employeePaymentAmountColumn;
	@FXML
	private TableColumn<ArrayList<String>, String> employeePaymentNameColumn;

	@FXML
	private TableColumn<Employee, String> employeeNameColumn;

	@FXML
	private TableColumn<Employee, String> employeeIDColumn;

	@FXML
	private TableView<ArrayList<String>> employeePaymentTable;

	@FXML
	private TableView<Employee> employeeTable;
	
	@FXML
	private TableColumn<ArrayList<String>, String> employeePaymentDateColumn;
	
	@FXML
	private TableColumn<ArrayList<String>, String> employeePaymentManagerName;
	
	@FXML
	private Button clearEmployeeFieldsButton;

	@FXML
	private Button addNewEmployeePaymentButton;

	@FXML
	private TextField amountEmployeeTextField;

	@FXML
	private CheckBox chequeEmployeeCheckBox;

	@FXML
	private DatePicker employeeAvailableUntilDatePicker;

	@FXML
	private TextField employeeBankNameTextField;

	@FXML
	private DatePicker employeeWritindDatePicker;
	
	@FXML
	private TextField searchEmployeeTextField;

	@FXML
	private TextField employeeChequeIDTextField;

	@FXML
	private ComboBox<String> employeeOperationComboBox;

	@FXML
	private TextField searchEmployeePaymentTextField;

	@FXML
	private Label writingDateEmployeeLabel;

	@FXML
	private Label availableUntilDateEmployeeLabel;
	
	@FXML
	private Label chequeIDEmployeeLabel;
	
	@FXML
	private Label bankNameLabel;
	
	String employeePaymentNewValue = "";
	
	ObservableList<String> EmployeeChoices = FXCollections.observableArrayList("-Specify Field-","Manager Name","Employee Name", "Payment Month",
			"Payment Year");

	public void employeePaymentFilterList() {
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		if (employeePaymentNewValue == null || employeePaymentNewValue.isEmpty() || employeePaymentNewValue.isBlank()) {
			filteredList = Queries
					.queryResult("select distinct e1.employee_name,e.employee_name,p.payment_date,p.payment_amount  \n"
							+ "from e_salary es,payment p, employee e, employee e1 \n"
							+ " where p.payment_Id=es.payment_Id and e1.employee_Id=es.manager_Id and e.employee_Id=es.Employee_ID;",
							null);
		} else if (employeeOperationComboBox.getSelectionModel().getSelectedItem() == "Employee Name") {
			filteredList = Queries.queryResult(
					"select distinct e1.employee_name,e.employee_name,p.payment_date,p.payment_amount  "
							+ " from e_salary es,payment p, employee e, employee e1"
							+ " where p.payment_Id=es.payment_Id and e1.employee_Id=es.manager_Id and e.employee_Id=es.Employee_ID and  e.employee_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + employeePaymentNewValue + "%")));
		} else if (employeeOperationComboBox.getSelectionModel().getSelectedItem() == "Manager Name") {
			filteredList = Queries.queryResult(
					"select distinct e1.employee_name,e.employee_name,p.payment_date,p.payment_amount  "
							+ " from e_salary es,payment p, employee e, employee e1"
							+ " where p.payment_Id=es.payment_Id and e1.employee_Id=es.manager_Id and e.employee_Id=es.Employee_ID and  e1.employee_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + employeePaymentNewValue + "%")));
		} else if (employeeOperationComboBox.getSelectionModel().getSelectedItem() == "Payment Month") {
			filteredList = Queries.queryResult("select distinct e1.employee_name,e.employee_name,p.payment_date,p.payment_amount  "
					+ " from e_salary es,payment p, employee e, employee e1"
					+ " where p.payment_Id=es.payment_Id and e1.employee_Id=es.manager_Id and e.employee_Id=es.Employee_ID and  month(p.payment_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + employeePaymentNewValue + "%")));
		} else if (employeeOperationComboBox.getSelectionModel().getSelectedItem() == "Payment Year") {
			filteredList = Queries.queryResult("select distinct e1.employee_name,e.employee_name,p.payment_date,p.payment_amount  "
					+ " from e_salary es,payment p, employee e, employee e1"
					+ " where p.payment_Id=es.payment_Id and e1.employee_Id=es.manager_Id and e.employee_Id=es.Employee_ID and  year(p.payment_date) like ? ;",
					new ArrayList<>(Arrays.asList("%" + employeePaymentNewValue + "%")));

		} else {
			ArrayList<String> parameters = new ArrayList<>();
			while (parameters.size() < 4) {
				parameters.add("%" + employeePaymentNewValue + "%");
			}
			filteredList = Queries.queryResult("select distinct e1.employee_name,e.employee_name,p.payment_date,p.payment_amount  "
					+ " from e_salary es,payment p, employee e, employee e1"
					+ " where( p.payment_Id=es.payment_Id and e1.employee_Id=es.manager_Id and e.employee_Id=es.Employee_ID) and  (year(p.payment_date) like ? "
							+ " or month(p.payment_date) like ? " + " or  e.employee_name like ? or  e1.employee_name like ?) ;",
					parameters);
		}
		employeePaymentTable.setItems(FXCollections.observableArrayList(filteredList));
	}
	
	public void chequeEmployeeOnAction(ActionEvent event) {
		if (chequeEmployeeCheckBox.isSelected() == true) {
			employeeBankNameTextField.setOpacity(1);
			employeeWritindDatePicker.setOpacity(1);
			employeeAvailableUntilDatePicker.setOpacity(1);
			employeeChequeIDTextField.setOpacity(1);
			bankNameLabel.setOpacity(1);
			availableUntilDateEmployeeLabel.setOpacity(1);
			chequeIDEmployeeLabel.setOpacity(1);
			writingDateEmployeeLabel.setOpacity(1);
		} else {
			employeeBankNameTextField.setOpacity(0);
			employeeWritindDatePicker.setOpacity(0);
			employeeAvailableUntilDatePicker.setOpacity(0);
			employeeChequeIDTextField.setOpacity(0);
			bankNameLabel.setOpacity(0);
			availableUntilDateEmployeeLabel.setOpacity(0);
			chequeIDEmployeeLabel.setOpacity(0);
			writingDateEmployeeLabel.setOpacity(0);
		}
	}

	public void clearEmployeeOnAction(ActionEvent event) {
		amountEmployeeTextField.clear();
		employeeBankNameTextField.clear();
		employeeChequeIDTextField.clear();
		employeeWritindDatePicker.setValue(null);
		employeeAvailableUntilDatePicker.setValue(null);
		chequeEmployeeCheckBox.setSelected(false);
		employeeBankNameTextField.setOpacity(0);
		employeeWritindDatePicker.setOpacity(0);
		employeeAvailableUntilDatePicker.setOpacity(0);
		employeeChequeIDTextField.setOpacity(0);
		bankNameLabel.setOpacity(0);
		availableUntilDateEmployeeLabel.setOpacity(0);
		chequeIDEmployeeLabel.setOpacity(0);
		writingDateEmployeeLabel.setOpacity(0);
	}

	public void addEmployeeOnAction(ActionEvent event)  {
		if (amountEmployeeTextField.getText().isBlank() == false
				&& employeeTable.getSelectionModel().getSelectedItem() != null) {
			if (chequeEmployeeCheckBox.isSelected() == true) {
				if (employeeChequeIDTextField.getText().isBlank() == false && employeeBankNameTextField.getText().isBlank() == false
						&& employeeAvailableUntilDatePicker.getValue() != null && employeeWritindDatePicker.getValue() != null) {
					boolean flag = false;
					if (employeeWritindDatePicker.getValue().compareTo(employeeAvailableUntilDatePicker.getValue()) == 0
							|| employeeWritindDatePicker.getValue().compareTo(employeeAvailableUntilDatePicker.getValue()) > 0) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("set reasonable Date!");
						alert.showAndWait();
					} else {
						ArrayList<Cheque> List2 = new ArrayList<>();
						List2 = Cheque.getChequeData(Queries.queryResult("select * from cheque where cheque_ID = ? ;",
								new ArrayList<>(Arrays.asList(employeeChequeIDTextField.getText().toString()))));
						
						if (List2.isEmpty() == true) {
							try {
								Double amount = Double.parseDouble(amountEmployeeTextField.getText());
								if (amount <= 0) {
									flag = true;
								}
							} catch (NumberFormatException e1) {
								flag = true;
							}
							String str = employeeBankNameTextField.getText().replaceAll("\\s", "");						
							if (str.matches("[a-zA-Z]+") == false) {
								flag = true;
							}
							if (flag == false) {
								Payment.insertPayment(java.time.LocalDate.now(),
										Double.parseDouble(amountEmployeeTextField.getText()), "Cheque");
								
								Cheque.insertCheque(employeeChequeIDTextField.getText(), employeeBankNameTextField.getText(),
										employeeWritindDatePicker.getValue(), employeeAvailableUntilDatePicker.getValue(), Payment.getMaxID(),
										Employee.getCurrentID());
								
								Queries.queryUpdate(
										"insert into E_salary " + " (manager_id, employee_id, payment_id) "
												+ " Values(?, ?, ?) ;",
										new ArrayList<>(Arrays.asList(Employee.getCurrentID() + "",
												employeeTable.getSelectionModel().getSelectedItem().getID() + "",
												Payment.getMaxID() + "")));
								employeePaymentTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
										"select distinct e.employee_name,p.payment_date,p.payment_amount "
												+ " from e_salary es,payment p, employee e "
												+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.Employee_ID",
										null)));
							}else {
								Alert alert = new Alert(Alert.AlertType.ERROR);
								alert.setTitle(null);
								alert.setHeaderText(null);
								alert.setContentText("Wrong Data Format!");
								alert.showAndWait();
							}
							amountEmployeeTextField.clear();
							employeeBankNameTextField.clear();
							employeeChequeIDTextField.clear();
							employeeWritindDatePicker.setValue(null);
							employeeAvailableUntilDatePicker.setValue(null);
							chequeEmployeeCheckBox.setSelected(false);
						} else {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle(null);
							alert.setHeaderText(null);
							alert.setContentText("This Cheque ID Already Exists!");
							alert.showAndWait();
						}
					}
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Fill The Cheque Fields");
					alert.showAndWait();
				}
			} else {

				boolean flag = false;
				try {
					Double num = Double.parseDouble(amountEmployeeTextField.getText());
					if (num <= 0) {
						flag = true;
					}
				} catch (NumberFormatException e1) {
					flag = true;
				}
				if (flag == false) {
					Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(amountEmployeeTextField.getText()),
							"Cash");
					
					Queries.queryUpdate("insert into E_salary (manager_id, employee_id, payment_id) Values (?, ?, ?) ;",
							new ArrayList<>(Arrays.asList(Employee.getCurrentID() + "",
									employeeTable.getSelectionModel().getSelectedItem().getID() + "",
									Payment.getMaxID() + "")));
					
					employeePaymentTable
							.setItems(FXCollections.observableArrayList(Queries.queryResult(
									"select distinct e.employee_name,p.payment_date,p.payment_amount "
											+ " from e_salary es,payment p, employee e "
											+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.Employee_ID;",
									null)));
					amountEmployeeTextField.clear();
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Wrong Information!");
					alert.showAndWait();
					amountEmployeeTextField.clear();
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Fill The Required Fields And Select Employee");
			alert.showAndWait();
		}
	}

	ObservableList<String> Choices = FXCollections.observableArrayList("-Specify Field-","Product Name", "Expired Month", "Expired Year",
			"Production Month", "Production Year", "Disposal Month", "Disposal Year");

	// -----------------------------------------Disposal---------------------------------------------------------

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		disposalOperationComboBox.setItems(Choices);
		Supplier.getSupplierData();
		Payment.getPaymentData();
		disposalTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
				"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount\r\n"
						+ "from product p, employee e, payment pay,drug_disposal d\r\n"
						+ "where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id;",
				null)));

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

		dateOfDisposalColumn.setCellValueFactory(
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

		disposalProductName.setCellValueFactory(
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

		searchDisposalTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			disposalNewValue = newValue;
			disposalFilterList();

		});

		// ----------------------------------------Employee----------------------------------------------------------
		employeeBankNameTextField.setOpacity(0);
		employeeWritindDatePicker.setOpacity(0);
		employeeAvailableUntilDatePicker.setOpacity(0);
		employeeChequeIDTextField.setOpacity(0);
		bankNameLabel.setOpacity(0);
		availableUntilDateEmployeeLabel.setOpacity(0);
		chequeIDEmployeeLabel.setOpacity(0);
		writingDateEmployeeLabel.setOpacity(0);
		employeeOperationComboBox.setValue("-Specify Field-");
		employeeOperationComboBox.setItems(EmployeeChoices);
		employeeNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		employeeIDColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("iD"));
		employeeTable.setItems(FXCollections.observableArrayList(Employee
				.getEmployeeData(Queries.queryResult("select * from Employee where employee_id <> -1 ;", null))));

		searchEmployeeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Employee> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				filteredList = Employee.getEmployeeData(
						Queries.queryResult("select * from Employee where employee_id <> -1 ;", null));
			} else {
				filteredList = Employee
						.getEmployeeData(Queries.queryResult("select * from Employee where Employee_Name like ? and employee_id <> -1  ;",
								new ArrayList<>(Arrays.asList("%" + newValue + "%"))));

			}
			employeeTable.setItems(FXCollections.observableArrayList(filteredList));
		});

		employeePaymentTable
				.setItems(
						FXCollections.observableArrayList(Queries.queryResult("select distinct e1.employee_name,e.employee_name,p.payment_date,p.payment_amount  \n"
								+ "from e_salary es,payment p, employee e, employee e1 \n"
								+ " where p.payment_Id=es.payment_Id and e1.employee_Id=es.manager_Id and e.employee_Id=es.Employee_ID;",
								null)));

		employeePaymentManagerName.setCellValueFactory(
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
		
		employeePaymentNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});
		
		employeePaymentDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		employeePaymentAmountColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 3) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		searchEmployeePaymentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			employeePaymentNewValue = newValue;
			employeePaymentFilterList();
		});

		// ----------------------------------------supplier----------------------------------------------------------
		searchSupplierOperationComboBox.setValue("-Specify Field-");
		searchSupplierOperationComboBox.setItems(SupplierPaymentChoices);
		bankNameSupplierTextField.setOpacity(0);
		writingDateSupplier.setOpacity(0);
		availablUntilDateSupplier.setOpacity(0);
		chequeIDSupplierTextField.setOpacity(0);
		supplierAvailableUntilLabel.setOpacity(0);
		supplierWritingDateLabel.setOpacity(0);
		supplierBankNameLabel.setOpacity(0);
		supplierChequeIDLabel.setOpacity(0);
		supplierNameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("sname"));
		supplierDuesColumn.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("dues"));
		supplierDataTable.setItems(Supplier.getDataList());

		supplierPaymentTable.setItems(FXCollections.observableArrayList(Queries
				.queryResult("select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
						+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
						+ " where op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
						+ " and so.supplier_id=s.supplier_id;", null)));

		supplierManagerNameColum.setCellValueFactory(
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
		
		supplierPaymentNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		supplierPaymentDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		supplierAmountColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() >3) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});
		searchSupplierPaymentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			supplierPaymentNewValue = newValue;
			supplierPaymentFilterList();
		});

		searchSupplierTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			supplierNewValue = newValue;
			supplierFilterList();
		});

		// ------------------------------------------tax-----------------------------------------------------------------------
		taxOperationComboBox.setValue("-Specify Field-");
		taxOperationComboBox.setItems(TaxChoices);
		bankNameTaxTextField.setOpacity(0);
		availableUntilDateTax.setOpacity(0);
		writingDateTax.setOpacity(0);
		chequeIDTaxTextField.setOpacity(0);
		availableUnitlTaxLabel.setOpacity(0);
		bankNameTaxLabel.setOpacity(0);
		writindDateTaxLabel.setOpacity(0);
		chequeTaxLabel.setOpacity(0);
		taxTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
				"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
						+ " from employee e , tax t, payment p, taxes_payment tp "
						+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
				null)));

		taxIDColumn.setCellValueFactory(
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

		managerNametaxColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		dateOfPaymentTaxColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		amountaTaxColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
				@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 3) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		searchTaxTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			taxNewValue = newValue;
			taxFilterList();
		});
		
	}

}
