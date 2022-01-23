package application;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
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

public class PaymentController implements Initializable {

	@FXML
	private Button taxReport;
	// --------------------------------------Disposal-----------------------------------------------
	@FXML
	private TextField searchDisposal;

	@FXML
	private ComboBox<String> disposalOP;

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

	// --------------------------------------tax----------------------------------------------------
	@FXML
	private TableColumn<ArrayList<String>, String> Amount;

	@FXML
	private TableColumn<ArrayList<String>, String> amounttax;

	@FXML
	private TableColumn<ArrayList<String>, String> dateofpaymenttax;

	@FXML
	private TableView<ArrayList<String>> taxTable;

	@FXML
	private TableColumn<ArrayList<String>, String> taxid;

	@FXML
	private TableColumn<ArrayList<String>, String> mnametax;

	@FXML
	private CheckBox cashTax;

	@FXML
	private CheckBox chequeTax;

	@FXML
	private TextField chequeidtax;

	@FXML
	private Label AUTaxLabel;

	@FXML
	private Label BankNameTaxLabel;

	@FXML
	private Label WDTaxLabel;

	@FXML
	private Label chequeTaxLabel;

	@FXML
	private Button addTax;

	@FXML
	private Button clearTax;

	@FXML
	private Label taxWarning;

	@FXML
	private ComboBox<String> taxop;

	@FXML
	private TextField searchtax;

	@FXML
	private TextField newamountTax;

	@FXML
	private TextField banktax;

	@FXML
	private TextField newtaxid;

	@FXML
	private DatePicker udatetax;

	@FXML
	private DatePicker wdatetax;

	ObservableList<String> TaxChoices = FXCollections.observableArrayList("Tax ID", "Manager Name", "Payment Month",
			"Payment Year");

	public void chequeTaxOnAction(ActionEvent event) {
		if (chequeTax.isSelected() == true) {
			banktax.setOpacity(1);
			wdatetax.setOpacity(1);
			chequeidtax.setOpacity(1);
			AUTaxLabel.setOpacity(1);
			BankNameTaxLabel.setOpacity(1);
			WDTaxLabel.setOpacity(1);
			chequeTaxLabel.setOpacity(1);
			udatetax.setOpacity(1);
		} else {
			banktax.setOpacity(0);
			udatetax.setOpacity(0);
			wdatetax.setOpacity(0);
			chequeidtax.setOpacity(0);
			AUTaxLabel.setOpacity(0);
			BankNameTaxLabel.setOpacity(0);
			WDTaxLabel.setOpacity(0);
			chequeTaxLabel.setOpacity(0);
		}
	}

	public void addTaxButton(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		if (newtaxid.getText().isBlank() == false && newamountTax.getText().isBlank() == false) {

			if (chequeTax.isSelected() == true) {
				if (chequeidtax.getText().isBlank() == false && banktax.getText().isBlank() == false
						&& wdatetax.getValue() != null && udatetax.getValue() != null) {
					boolean flag = false;
					if (wdatetax.getValue().compareTo(udatetax.getValue()) == 0
							|| wdatetax.getValue().compareTo(udatetax.getValue()) > 0) {
						flag = true;
					}
					ArrayList<Tax> List = new ArrayList<>();
					List = Tax.getTaxData(Queries.queryResult("select * from Tax where tax_ID = ? ;",
							new ArrayList<>(Arrays.asList(newtaxid.getText().toString()))));
					if (List.isEmpty() == true) {
						ArrayList<Cheque> List2 = new ArrayList<>();
						List2 = Cheque.getChequeData(Queries.queryResult("select * from cheque where cheque_ID = ? ;",
								new ArrayList<>(Arrays.asList(chequeidtax.getText().toString()))));
						if (List2.isEmpty() == true) {
							try {

								Double num = Double.parseDouble(newamountTax.getText());
								if (num == 0) {
									flag = true;
								}
							} catch (NumberFormatException e1) {
								flag = true;
							}

							String str = banktax.getText().replaceAll("\\s", "");
							if (str.matches("[a-zA-Z]+") == false) {
								flag = true;
							}
							if (flag == false) {
								Payment.insertPayment(java.time.LocalDate.now(),
										Double.parseDouble(newamountTax.getText()), "Cheque");
								Cheque.insertCheque(chequeidtax.getText(), banktax.getText(), wdatetax.getValue(),
										udatetax.getValue(), Payment.getMaxID(), Employee.getCurrentID());
								Tax.insertTax(newtaxid.getText().toString(), java.time.LocalDate.now(),
										Double.parseDouble(newamountTax.getText()));
								Queries.queryUpdate(
										"insert into taxes_payment (payment_id, tax_id, manager_id) "
												+ "values (?, ?, ?) ;",
										new ArrayList<>(Arrays.asList(Payment.getMaxID() + "",
												newtaxid.getText().toString() + "", Employee.getCurrentID() + "")));

								try {
									taxTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
											"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
													+ " from  employee e , tax t, payment p, taxes_payment tp "
													+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
											null)));
								} catch (ClassNotFoundException | SQLException e) {
									e.printStackTrace();
								}
								taxWarning.setText("");
								newtaxid.clear();
								newamountTax.clear();
								banktax.clear();
								chequeidtax.clear();
								chequeTax.setSelected(false);
							}
						} else {
							taxWarning.setText("This Cheque ID Already Exists!");
						}
					} else {
						taxWarning.setText("This Tax ID Already Exists!");
					}
				} else {
					taxWarning.setText("Fill The Cheque Fields");
				}
			} else {
				ArrayList<Tax> List = new ArrayList<>();
				List = Tax.getTaxData(Queries.queryResult("select * from Tax where tax_ID = ? ;",
						new ArrayList<>(Arrays.asList(newtaxid.getText().toString()))));
				if (List.isEmpty() == true) {
					boolean flag = false;
					try {

						Double num = Double.parseDouble(newamountTax.getText());
						if (num == 0) {
							flag = true;
						}
					} catch (NumberFormatException e1) {
						flag = true;
					}
					if (!flag) {
						Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(newamountTax.getText()),
								"Cash");
						Tax.insertTax(newtaxid.getText().toString(), java.time.LocalDate.now(),
								Double.parseDouble(newamountTax.getText()));
						Queries.queryUpdate(
								"insert into taxes_payment " + " (payment_id, tax_id, manager_id)"
										+ "Values (?, ?, ?) ;",
								new ArrayList<>(Arrays.asList(Payment.getMaxID() + "",
										newtaxid.getText().toString() + "", Employee.getCurrentID() + "")));
						try {
							taxTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
									"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
											+ " from employee e , tax t, payment p, taxes_payment tp "
											+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
									null)));
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
						newtaxid.clear();
						newamountTax.clear();
					} else {
						taxWarning.setText("Wrong Information!");
					}
					// add payment
				} else {
					taxWarning.setText("This Tax Already Exists");
				}
			}
		} else {
			taxWarning.setText("Fill The Required Fields");
		}

	}

	public void clearTaxButton(ActionEvent event) {
		taxWarning.setText("");
		newtaxid.clear();
		newamountTax.clear();
		banktax.clear();
		chequeidtax.clear();
		chequeTax.setSelected(false);
		banktax.setOpacity(0);
		udatetax.setOpacity(0);
		wdatetax.setOpacity(0);
		chequeidtax.setOpacity(0);
		AUTaxLabel.setOpacity(0);
		BankNameTaxLabel.setOpacity(0);
		WDTaxLabel.setOpacity(0);
		chequeTaxLabel.setOpacity(0);
	}

	// ----------------------------------------supplier------------------------------------------------
	@FXML
	private TableColumn<ArrayList<String>, String> supplierName;

	@FXML
	private TableColumn<ArrayList<String>, String> supplierMName;

	@FXML
	private TableColumn<ArrayList<String>, String> paymentDate;

	@FXML
	private TableView<ArrayList<String>> supplierPayment;

	@FXML
	private TableView<Supplier> supplierData;

	@FXML
	private TableColumn<Supplier, String> sname;

	@FXML
	private TableColumn<Supplier, Double> sDues;

	@FXML
	private TextField searchSupplier;

	@FXML
	private ComboBox<String> searchSupplierOp;

	@FXML
	private TextField searchSupplierPayment;

	@FXML
	private Label supplierWarning;

	@FXML
	private DatePicker udateSupplier;

	@FXML
	private DatePicker wdateSupplier;

	@FXML
	private Button addSupplier;

	@FXML
	private Button clearSupplier;

	@FXML
	private TextField amountSupplier;

	@FXML
	private TextField bankSupplier;

	@FXML
	private CheckBox chequeSupplier;

	@FXML
	private TextField chequeidSupplier;

	@FXML
	private Label aulabel;

	@FXML
	private Label wdlabel;

	@FXML
	private Label blabel;

	@FXML
	private Label idlabel;

	ObservableList<String> SupplierPaymentChoices = FXCollections.observableArrayList("Supplier Name", "Manager Name",
			"Payment Month", "Payment Year");

	public void chequeSupplierOnAction(ActionEvent event) {
		supplierWarning.setText("");
		if (chequeSupplier.isSelected() == true) {
			bankSupplier.setOpacity(1);
			wdateSupplier.setOpacity(1);
			udateSupplier.setOpacity(1);
			chequeidSupplier.setOpacity(1);
			aulabel.setOpacity(1);
			wdlabel.setOpacity(1);
			blabel.setOpacity(1);
			idlabel.setOpacity(1);
		} else {
			bankSupplier.setOpacity(0);
			wdateSupplier.setOpacity(0);
			udateSupplier.setOpacity(0);
			chequeidSupplier.setOpacity(0);
			aulabel.setOpacity(0);
			wdlabel.setOpacity(0);
			blabel.setOpacity(0);
			idlabel.setOpacity(0);
		}
	}

	public void clearSupplierButton(ActionEvent event) {
		supplierWarning.setText("");
		amountSupplier.clear();
		bankSupplier.clear();
		chequeidSupplier.clear();
		chequeSupplier.setSelected(false);
		bankSupplier.setOpacity(0);
		wdateSupplier.setOpacity(0);
		udateSupplier.setOpacity(0);
		chequeidSupplier.setOpacity(0);
		aulabel.setOpacity(0);
		wdlabel.setOpacity(0);
		blabel.setOpacity(0);
		idlabel.setOpacity(0);
	}

	public void addSupplierButton(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		supplierWarning.setText("");

		if (amountSupplier.getText().isBlank() == false && supplierData.getSelectionModel().getSelectedItem() != null) {
			if (chequeSupplier.isSelected() == true) {
				if (chequeidSupplier.getText().isBlank() == false && bankSupplier.getText().isBlank() == false
						&& udateSupplier.getValue() != null && wdateSupplier.getValue() != null) {
					boolean flag = false;
					if (wdateSupplier.getValue().compareTo(udateSupplier.getValue()) == 0
							|| wdateSupplier.getValue().compareTo(udateSupplier.getValue()) > 0) {
						supplierWarning.setText("Set Reasonable Date!");
					} else {

						ArrayList<Cheque> List2 = new ArrayList<>();
						List2 = Cheque.getChequeData(Queries.queryResult("select * from cheque where cheque_ID = ? ;",
								new ArrayList<>(Arrays.asList(chequeidSupplier.getText().toString()))));
						if (List2.isEmpty() == true) {

							try {
								Double num = Double.parseDouble(amountSupplier.getText());
								if (num == 0) {
									flag = true;
								}
							} catch (NumberFormatException e1) {
								flag = true;
							}

							String str = bankSupplier.getText().replaceAll("\\s", "");
							if (str.matches("[a-zA-Z]+") == false) {
								flag = true;
							}
							if (!flag) {
								Payment.insertPayment(java.time.LocalDate.now(),
										Double.parseDouble(amountSupplier.getText()), "Cheque");

								Cheque.insertCheque(chequeidSupplier.getText(), bankSupplier.getText(),
										wdateSupplier.getValue(), udateSupplier.getValue(), Payment.getMaxID(),
										Employee.getCurrentID());

								Queries.queryUpdate(
										"insert into supplier_payment (supplier_id, manager_id, payment_id)"
												+ " Values (?, ?, ?);",
										new ArrayList<>(Arrays.asList(
												supplierData.getSelectionModel().getSelectedItem().getID() + "",
												Employee.getCurrentID() + "", Payment.getMaxID() + "")));
								Queries.queryUpdate(
										"update Supplier set supplier_dues = supplier_dues -? where Supplier_ID =?;",
										new ArrayList<>(Arrays.asList(amountSupplier.getText(),
												supplierData.getSelectionModel().getSelectedItem().getID() + "")));

								try {
									supplierPayment.setItems(FXCollections.observableArrayList(Queries.queryResult(
											"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
													+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
													+ " where op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
													+ " and so.supplier_id=s.supplier_id;",
											null)));
									supplierData.setItems(FXCollections.observableArrayList(Supplier
											.getSupplierData(Queries.queryResult("select * from Supplier;", null))));

								} catch (ClassNotFoundException | SQLException e) {
									e.printStackTrace();
								}
								supplierWarning.setText("");
								amountSupplier.clear();
								bankSupplier.clear();
								chequeidSupplier.clear();
								chequeSupplier.setSelected(false);
							} else {
								supplierWarning.setText("Wrong Information!");
							}
						} else {
							supplierWarning.setText("This Cheque ID Already Exists!");
						}
					}

				} else {
					supplierWarning.setText("Fill The Cheque Fields");
				}
			} else {

				boolean flag = false;
				try {

					Double num = Double.parseDouble(amountSupplier.getText());
					if (num == 0) {
						flag = true;
					}
				} catch (NumberFormatException e1) {
					flag = true;
				}
				if (!flag) {
					Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(amountSupplier.getText()),
							"Cash");
					Queries.queryUpdate(
							"insert into supplier_payment (supplier_id, manager_id, payment_id) Values (?, ?, ?) ;",
							new ArrayList<>(
									Arrays.asList(supplierData.getSelectionModel().getSelectedItem().getID() + "",
											Employee.getCurrentID() + "", Payment.getMaxID() + "")));
					Queries.queryUpdate("update Supplier set supplier_dues = supplier_dues -? where Supplier_ID =?;",
							new ArrayList<>(Arrays.asList(amountSupplier.getText(),
									supplierData.getSelectionModel().getSelectedItem().getID() + "")));

					try {
						supplierPayment.setItems(FXCollections.observableArrayList(Queries.queryResult(
								"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
										+ " from employee e, supplier s, payment p, supplier_payment op, s_order so"
										+ " where op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
										+ " and so.supplier_id=s.supplier_id;",
								null)));
						supplierData.setItems(FXCollections.observableArrayList(
								Supplier.getSupplierData(Queries.queryResult("select * from Supplier;", null))));
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
					supplierWarning.setText("");
					amountSupplier.clear();

				}

			}
		} else {
			supplierWarning.setText("Fill The Required Fields And Select A Supplier");
		}
	}

	// --------------------------------------Employee-----------------------------------------------------

	@FXML
	private TableColumn<ArrayList<String>, String> amountep;
	@FXML
	private TableColumn<ArrayList<String>, String> eName;

	@FXML
	private TableColumn<Employee, String> employeeName;

	@FXML
	private TableColumn<Employee, String> employeeID;

	@FXML
	private TableView<ArrayList<String>> employeePayment;

	@FXML
	private TableView<Employee> employeeTable;
	@FXML
	private TableColumn<ArrayList<String>, String> pDate;
	@FXML
	private Button ClearEmployeeFields;

	@FXML
	private Button addNewEmployeePayment;

	@FXML
	private TextField amountEmployee;

	@FXML
	private CheckBox chequeE;

	@FXML
	private DatePicker employeeADate;

	@FXML
	private TextField employeeBank;

	@FXML
	private DatePicker employeeWDate;

	@FXML
	private Label employeeWarning;
	@FXML
	private TextField searchEmployee;

	@FXML
	private TextField employeeIDp;

	@FXML
	private ComboBox<String> searchEmployeeOp;

	@FXML
	private TextField searchEmployeePayment;

	@FXML
	private Label wemp;

	@FXML
	private Label uemp;
	@FXML
	private Label idemp;
	@FXML
	private Label bankemp;
	ObservableList<String> EmployeeChoices = FXCollections.observableArrayList("Employee Name", "Payment Month",
			"Payment Year");

	public void chequeEmployeeOnAction(ActionEvent event) {
		employeeWarning.setText("");
		if (chequeE.isSelected() == true) {
			employeeBank.setOpacity(1);
			employeeWDate.setOpacity(1);
			employeeADate.setOpacity(1);
			employeeIDp.setOpacity(1);
			bankemp.setOpacity(1);
			uemp.setOpacity(1);
			idemp.setOpacity(1);
			wemp.setOpacity(1);
		} else {
			employeeBank.setOpacity(0);
			employeeWDate.setOpacity(0);
			employeeADate.setOpacity(0);
			employeeIDp.setOpacity(0);
			bankemp.setOpacity(0);
			uemp.setOpacity(0);
			idemp.setOpacity(0);
			wemp.setOpacity(0);
		}
	}

	public void clearEmployeeButton(ActionEvent event) {
		employeeWarning.setText("");
		amountEmployee.clear();
		employeeBank.clear();
		employeeIDp.clear();
		chequeE.setSelected(false);
		employeeBank.setOpacity(0);
		employeeWDate.setOpacity(0);
		employeeADate.setOpacity(0);
		employeeIDp.setOpacity(0);
		bankemp.setOpacity(0);
		uemp.setOpacity(0);
		idemp.setOpacity(0);
		wemp.setOpacity(0);
	}

	public void addEmployeeButton(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		employeeWarning.setText("");

		if (amountEmployee.getText().isBlank() == false
				&& employeeTable.getSelectionModel().getSelectedItem() != null) {
			if (chequeE.isSelected() == true) {
				if (employeeIDp.getText().isBlank() == false && employeeBank.getText().isBlank() == false
						&& employeeADate.getValue() != null && employeeWDate.getValue() != null) {
					boolean flag = false;
					if (employeeWDate.getValue().compareTo(employeeADate.getValue()) == 0
							|| employeeWDate.getValue().compareTo(employeeADate.getValue()) > 0) {
						employeeWarning.setText("set reasonable Date!");
					} else {
						ArrayList<Cheque> List2 = new ArrayList<>();
						List2 = Cheque.getChequeData(Queries.queryResult("select * from cheque where cheque_ID = ? ;",
								new ArrayList<>(Arrays.asList(employeeIDp.getText().toString()))));
						if (List2.isEmpty() == true) {

							try {

								Double amount = Double.parseDouble(amountEmployee.getText());
								if (amount == 0) {
									flag = true;
								}
							} catch (NumberFormatException e1) {
								flag = true;
							}

							String str = employeeBank.getText().replaceAll("\\s", "");
							
							if (str.matches("[a-zA-Z]+") == false) {
								flag = true;
							}

							if (flag == false) {
								Payment.insertPayment(java.time.LocalDate.now(),
										Double.parseDouble(amountEmployee.getText()), "Cheque");
								Cheque.insertCheque(employeeIDp.getText(), employeeBank.getText(),
										employeeWDate.getValue(), employeeADate.getValue(), Payment.getMaxID(),
										Employee.getCurrentID());
								Queries.queryUpdate(
										"insert into E_salary " + " (manager_id, employee_id, payment_id) "
												+ " Values(?, ?, ?) ;",
										new ArrayList<>(Arrays.asList(Employee.getCurrentID() + "",
												employeeTable.getSelectionModel().getSelectedItem().getID() + "",
												Payment.getMaxID() + "")));
								try {
									employeePayment.setItems(FXCollections.observableArrayList(Queries.queryResult(
											"select distinct e.employee_name,p.payment_date,p.payment_amount "
													+ " from e_salary es,payment p, employee e "
													+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id",
											null)));
								} catch (ClassNotFoundException | SQLException e) {
									e.printStackTrace();
								}
                             employeeWarning.setText("");
							}else {
	                             employeeWarning.setText("Wrong Data Format!");

							}
							amountEmployee.clear();
							employeeBank.clear();
							employeeIDp.clear();
							chequeE.setSelected(false);

						} else {
							employeeWarning.setText("This Cheque ID Already Exists!");
						}
					}
				} else {
					employeeWarning.setText("Fill The Cheque Fields");
				}
			} else {

				boolean flag = false;
				try {
					Double num = Double.parseDouble(amountEmployee.getText());
					if (num == 0) {
						flag = true;
					}
				} catch (NumberFormatException e1) {
					flag = true;
				}
				if (flag == false) {
					Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(amountEmployee.getText()),
							"Cash");
					Queries.queryUpdate("insert into E_salary (manager_id, employee_id, payment_id) Values (?, ?, ?) ;",
							new ArrayList<>(Arrays.asList(Employee.getCurrentID() + "",
									employeeTable.getSelectionModel().getSelectedItem().getID() + "",
									Payment.getMaxID() + "")));
					try {
						employeePayment
								.setItems(FXCollections.observableArrayList(Queries.queryResult(
										"select distinct e.employee_name,p.payment_date,p.payment_amount "
												+ " from e_salary es,payment p, employee e "
												+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id;",
										null)));
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
					employeeWarning.setText("");
					amountEmployee.clear();
				} else {
					employeeWarning.setText("Wrong Information!");
					amountEmployee.clear();
				}
			}
		} else {
			employeeWarning.setText("Fill The Required Fields And Select Employee");
		}
	}

	ObservableList<String> Choices = FXCollections.observableArrayList("Product Name", "Expired Month", "Expired Year",
			"Production Month", "Production Year", "Disposal Month", "Disposal Year");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// -----------------------------------------Disposal---------------------------------------------------------
		disposalOP.setItems(Choices);

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

		searchDisposal.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id;",
							null);

				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (disposalOP.getSelectionModel().getSelectedItem() == "Product Name") {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
									+ " and p.product_name like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (disposalOP.getSelectionModel().getSelectedItem() == "Expired Month") {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
									+ " and  month(d.batch_expiry_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (disposalOP.getSelectionModel().getSelectedItem() == "Expired Year") {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
									+ " and  year(d.batch_expiry_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (disposalOP.getSelectionModel().getSelectedItem() == "Production Month") {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
									+ " and  month(d.batch_production_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (disposalOP.getSelectionModel().getSelectedItem() == "Production Year") {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
									+ " and  year(d.batch_production_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (disposalOP.getSelectionModel().getSelectedItem() == "Disposal Month") {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
									+ " and  month(d.Disposal_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (disposalOP.getSelectionModel().getSelectedItem() == "Disposal Year") {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id"
									+ " and  year(d.Disposal_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					filteredList = Queries.queryResult(
							"select distinct p.product_name,d.Batch_Production_Date,d.Batch_Expiry_Date, d.Disposal_amount, d.Disposal_date,e.employee_name,pay.Payment_Amount "
									+ " from product p, employee e, payment pay,drug_disposal d "
									+ " where (p.product_id = d.product_id and e.employee_id=d.employee_id and pay.payment_id = d.payment_id) "
									+ " and  (year(d.Disposal_date) like ? or month(d.Disposal_date) like ? or p.product_name like ? "
									+ " or year(d.Batch_Production_Date) like ? or month(d.Batch_Production_Date) like ? or "
									+ " year(d.Batch_Expiry_Date) like ? or month(d.Batch_Expiry_Date) like ?);",
							new ArrayList<>(Arrays.asList("%" + newValue + "%", "%" + newValue + "%",
									"%" + newValue + "%", "%" + newValue + "%", "%" + newValue + "%",
									"%" + newValue + "%", "%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			disposal.setItems(FXCollections.observableArrayList(filteredList));

		});

		// ----------------------------------------Employee----------------------------------------------------------

		employeeBank.setOpacity(0);
		employeeWDate.setOpacity(0);
		employeeADate.setOpacity(0);
		employeeIDp.setOpacity(0);
		bankemp.setOpacity(0);
		uemp.setOpacity(0);
		idemp.setOpacity(0);
		wemp.setOpacity(0);

		searchEmployeeOp.setValue("select");
		searchEmployeeOp.setItems(EmployeeChoices);
		employeeName.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		employeeID.setCellValueFactory(new PropertyValueFactory<Employee, String>("iD"));

		try {
			employeeTable.setItems(FXCollections.observableArrayList(Employee
					.getEmployeeData(Queries.queryResult("select * from Employee where employee_id <> -1 ;", null))));
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		searchEmployee.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Employee> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Employee.getEmployeeData(
							Queries.queryResult("select * from Employee where employee_id <> -1 ;", null));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				try {
					filteredList = Employee
							.getEmployeeData(Queries.queryResult("select * from Employee where Employee_Name like ? ;",
									new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
			employeeTable.setItems(FXCollections.observableArrayList(filteredList));
		});

		try {
			employeePayment
					.setItems(
							FXCollections.observableArrayList(Queries.queryResult(
									"select distinct e.employee_name,p.payment_date,p.payment_amount "
											+ " from e_salary es,payment p, employee e "
											+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id;",
									null)));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		eName.setCellValueFactory(
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
		pDate.setCellValueFactory(
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

		amountep.setCellValueFactory(
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

		searchEmployeePayment.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Queries
							.queryResult(
									"select distinct e.employee_name,p.payment_date,p.payment_amount "
											+ " from e_salary es,payment p, employee e "
											+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id",
									null);

				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (taxop.getSelectionModel().getSelectedItem() == "Employee Name") {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name,p.payment_date,p.payment_amount "
									+ " from e_salary es,payment p, employee e "
									+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id and  e.employee_name like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (taxop.getSelectionModel().getSelectedItem() == "Payment Month") {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name,p.payment_date,p.payment_amount "
									+ " from e_salary es,payment p, employee e "
									+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id and  month(p.payment_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (taxop.getSelectionModel().getSelectedItem() == "Payment Year") {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name,p.payment_date,p.payment_amount "
									+ " from e_salary es,payment p, employee e "
									+ " where p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id and year(p.payment_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));

				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else {
				try {
					ArrayList<String> parameters = new ArrayList<>();
					while (parameters.size() < 3) {
						parameters.add("%" + newValue + "%");
					}
					filteredList = Queries.queryResult(
							"select distinct e.employee_name,p.payment_date,p.payment_amount "
									+ " from e_salary es,payment p, employee e "
									+ " where  (p.payment_Id=es.payment_Id and e.employee_Id=es.manager_Id) and  (year(p.payment_date) like ? "
									+ " or month(p.payment_date) like ? " + " or  e.employee_name like ?) ;",
							parameters);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			employeePayment.setItems(FXCollections.observableArrayList(filteredList));
		});

		// ----------------------------------------supplier----------------------------------------------------------

		supplierWarning.setText("");
		searchSupplierOp.setValue("select");
		searchSupplierOp.setItems(SupplierPaymentChoices);

		bankSupplier.setOpacity(0);
		wdateSupplier.setOpacity(0);
		udateSupplier.setOpacity(0);
		chequeidSupplier.setOpacity(0);
		aulabel.setOpacity(0);
		wdlabel.setOpacity(0);
		blabel.setOpacity(0);
		idlabel.setOpacity(0);

		sname.setCellValueFactory(new PropertyValueFactory<Supplier, String>("sname"));
		sDues.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("dues"));
		supplierData.setItems(Supplier.getDataList());

		try {
			supplierPayment.setItems(FXCollections.observableArrayList(Queries
					.queryResult("select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
							+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
							+ " where op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
							+ " and so.supplier_id=s.supplier_id;", null)));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		supplierMName.setCellValueFactory(
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
		supplierName.setCellValueFactory(
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

		paymentDate.setCellValueFactory(
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

		Amount.setCellValueFactory(
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
		searchSupplierPayment.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
									+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
									+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
									+ " and so.supplier_id=s.supplier_id;",
							null);

				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (taxop.getSelectionModel().getSelectedItem() == "Supplier Name") {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
									+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
									+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
									+ " and so.supplier_id=s.supplier_id and  s.supplier_name like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (taxop.getSelectionModel().getSelectedItem() == "Manager Name") {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
									+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
									+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
									+ " and so.supplier_id=s.supplier_id and  e.employee_name like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (taxop.getSelectionModel().getSelectedItem() == "Payment Month") {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
									+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
									+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
									+ " and so.supplier_id=s.supplier_id and  month(p.payment_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (taxop.getSelectionModel().getSelectedItem() == "Payment Year") {
				try {
					filteredList = Queries.queryResult(
							"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
									+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
									+ " where  op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
									+ " and so.supplier_id=s.supplier_id and  year(p.payment_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));

				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else {
				try {
					ArrayList<String> parameters = new ArrayList<>();
					while (parameters.size() < 4) {
						parameters.add("%" + newValue + "%");
					}
					filteredList = Queries.queryResult(
							"select distinct e.employee_name, s.supplier_name,p.payment_date,p.payment_amount "
									+ " from employee e, supplier s, payment p, supplier_payment op, s_order so "
									+ " where  (op.manager_ID=e.employee_ID and op.supplier_Id = so.supplier_id and p.payment_id= op.payment_id "
									+ " and so.supplier_id=s.supplier_id )and  (year(p.payment_date) like ? "
									+ " or month(p.payment_date) like ? " + " or  e.employee_name like ? "
									+ " or s.supplier_name like ? );",
							parameters);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			supplierPayment.setItems(FXCollections.observableArrayList(filteredList));
		});

		searchSupplier.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Supplier> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier;", null));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult(
							"select * from Supplier " + " where  supplier_name like ? " + " order by supplier_ID ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			supplierData.setItems(FXCollections.observableArrayList(filteredList));
		});

		// ------------------------------------------tax-----------------------------------------------------------------------
		taxop.setValue("select");
		taxop.setItems(TaxChoices);

		banktax.setOpacity(0);
		udatetax.setOpacity(0);
		wdatetax.setOpacity(0);
		chequeidtax.setOpacity(0);
		AUTaxLabel.setOpacity(0);
		BankNameTaxLabel.setOpacity(0);
		WDTaxLabel.setOpacity(0);
		chequeTaxLabel.setOpacity(0);
		try {
			taxTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
					"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
							+ " from employee e , tax t, payment p, taxes_payment tp "
							+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
					null)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		taxid.setCellValueFactory(
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

		mnametax.setCellValueFactory(
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

		dateofpaymenttax.setCellValueFactory(
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

		amounttax.setCellValueFactory(
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

		searchtax.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Queries.queryResult(
							"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
									+ " from employee e , tax t, payment p, taxes_payment tp "
									+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
							null);

				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (taxop.getSelectionModel().getSelectedItem() == "Tax ID") {
				try {
					filteredList = Queries
							.queryResult("select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
									+ " from employee e , tax t, payment p, taxes_payment tp "
									+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id"
									+ " and t.tax_id like ? ;", new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (taxop.getSelectionModel().getSelectedItem() == "Manager Name") {
				try {
					filteredList = Queries.queryResult(
							"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
									+ " from employee e , tax t, payment p, taxes_payment tp "
									+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id"
									+ " and  e.employee_name like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (taxop.getSelectionModel().getSelectedItem() == "Payment Month") {
				try {
					filteredList = Queries.queryResult(
							"select  distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
									+ " from employee e , tax t, payment p, taxes_payment tp "
									+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id "
									+ " and  month(t.tax_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (taxop.getSelectionModel().getSelectedItem() == "Payment Year") {
				try {
					filteredList = Queries.queryResult(
							"select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
									+ " from employee e , tax t, payment p, taxes_payment tp "
									+ " where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id "
									+ "and  year(t.tax_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else {
				try {
					ArrayList<String> parameters = new ArrayList<>();
					while (parameters.size() < 4) {
						parameters.add("%" + newValue + "%");
					}
					filteredList = Queries
							.queryResult("select distinct t.tax_id,e.employee_name,t.tax_date,p.payment_amount "
									+ " from employee e , tax t, payment p, taxes_payment tp "
									+ " where ( tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id )"
									+ "and  (year(t.tax_date) like ? " + " or month(t.tax_date) like ? "
									+ " or  e.employee_name like ? " + " or t.tax_id like ?) ;", parameters);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			taxTable.setItems(FXCollections.observableArrayList(filteredList));
		});

		// ---------------------------------------------------------------------------------------------------------------------

	}

}
