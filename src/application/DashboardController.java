package application;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ResourceBundle;
import Relations.Queries;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class DashboardController implements Initializable {
	@FXML
	private TableColumn<ArrayList<String>, String> amount;

	@FXML
	private TableColumn<ArrayList<String>, String> eAmount;

	@FXML
	private TableColumn<ArrayList<String>, String> eID;

	@FXML
	private TableColumn<ArrayList<String>, String> eName;

	@FXML
	private TableColumn<ArrayList<String>, String> eedate;

	@FXML
	private TableColumn<ArrayList<String>, String> epdate;

	@FXML
	private TableColumn<ArrayList<String>, String> oID;

	@FXML
	private TableColumn<ArrayList<String>, String> oName;

	@FXML
	private TableColumn<ArrayList<String>, String> oedate;

	@FXML
	private TableColumn<ArrayList<String>, String> opdate;

	@FXML
	private TableView<ArrayList<String>> outOfStock;

	@FXML
	private TableView<ArrayList<String>> Expires;

	@FXML
	private TableColumn<ArrayList<String>, String> cost;

	@FXML
	private TableColumn<ArrayList<String>, String> dueDate;

	@FXML
	private Label numberCustomer;

	@FXML
	private Label numberEmployees;

	@FXML
	private Label numberExpired;

	@FXML
	private Label numberOFS;

	@FXML
	private Label numberProduct;

	@FXML
	private Label numberSupplier;

	@FXML
	private TableColumn<ArrayList<String>, String> orderID;

	@FXML
	private TableColumn<ArrayList<String>, String> supplierName;

	@FXML
	private TableView<ArrayList<String>> toBePaid;

	@FXML
	private Label totalDept;

	@FXML
	private Label totalDiscount;

	@FXML
	private Label totalReceived;

	@FXML
	private Label totalSale;

	@FXML
	private LineChart<String, Double> monthlySell;

	@FXML
	private TableColumn<ArrayList<String>, String> outOSName;

	@FXML
	private TableView<ArrayList<String>> OFS;

	@FXML
	private ComboBox<String> month;

	@FXML
	private ComboBox<String> year;
	int counter = 0;
	ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November", "December");

	ObservableList<String> years = FXCollections.observableArrayList("2018", "2019", "2020", "2021");

	ArrayList<ArrayList<String>> dailyS = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LocalDate current_date = LocalDate.now();
		if (years.indexOf(current_date.getYear() + "") == -1) {
			years.add(current_date.getYear() + "");
		}
		year.setItems(years);
		month.setItems(months);

		XYChart.Series<String, Double> series = new XYChart.Series<>();
		series.setName("Daily Sales");

		try {
			dailyS = Queries.queryResult("select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
					+ "	where  year(order_date) = year(date(now())) and month(order_date) = month(date(now())) \r\n"
					+ "	group by day(order_date) ;", null);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		for (int i = 1; i <= 31; ++i) {

			if (counter < dailyS.size()) {
				if (Integer.parseInt(dailyS.get(counter).get(1)) == i) {
					series.getData().add(new XYChart.Data<>(i + "", Double.parseDouble(dailyS.get(counter).get(0))));
					++counter;
				} else {
					series.getData().add(new XYChart.Data<>(i + "", 0.0));

				}
			} else {
				series.getData().add(new XYChart.Data<>(i + "", 0.0));

			}
		}
		monthlySell.getData().add(series);

		year.setOnAction(e -> {
			XYChart.Series<String, Double> series2 = new XYChart.Series<>();
			series2.setName("Daily Sales");
			monthlySell.getData().clear();

			int thisMonth;
			if (month.getSelectionModel().getSelectedIndex() == -1) {
				thisMonth = Calendar.MONTH - 1;
			} else {
				thisMonth = month.getSelectionModel().getSelectedIndex() + 1;
			}
			if (year.getSelectionModel().getSelectedItem() == null) {
				try {
					dailyS = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = year(date(now())) and month(order_date) = ? \r\n"
									+ "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList("" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				int currentYear = year.getSelectionModel().getSelectedIndex() + 2018;
				try {
					dailyS = Queries.queryResult("select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
							+ "	where  year(order_date) = ? and month(order_date) = ?" + "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList(currentYear + "", "" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}

			counter = 0;
			for (int i = 1; i <= 31; ++i) {
				if (counter < dailyS.size()) {
					if (Integer.parseInt(dailyS.get(counter).get(1)) == i) {
						series2.getData()
								.add(new XYChart.Data<>(i + "", Double.parseDouble(dailyS.get(counter).get(0))));
						++counter;
					} else {
						series2.getData().add(new XYChart.Data<>(i + "", 0.0));

					}
				} else {
					series2.getData().add(new XYChart.Data<>(i + "", 0.0));

				}
			}

			monthlySell.getData().add(series2);

		});

		month.setOnAction(e -> {

			XYChart.Series<String, Double> series2 = new XYChart.Series<>();
			series2.setName("Daily Sales");
			monthlySell.getData().clear();

			int thisMonth;
			if (month.getSelectionModel().getSelectedIndex() == -1) {
				thisMonth = Calendar.MONTH - 1;
			} else {
				thisMonth = month.getSelectionModel().getSelectedIndex() + 1;
			}
			if (year.getSelectionModel().getSelectedItem() == null) {
				try {
					dailyS = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = year(date(now())) and month(order_date) = ? \r\n"
									+ "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList("" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				int currentYear = year.getSelectionModel().getSelectedIndex() + 2018;
				try {
					dailyS = Queries.queryResult("select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
							+ "	where  year(order_date) = ? and month(order_date) = ?" + "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList(currentYear + "", "" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}

			counter = 0;

			for (int i = 1; i <= 31; ++i) {

				if (counter < dailyS.size()) {
					if (Integer.parseInt(dailyS.get(counter).get(1)) == i) {
						series2.getData()
								.add(new XYChart.Data<>(i + "", Double.parseDouble(dailyS.get(counter).get(0))));
						++counter;
					} else {
						series2.getData().add(new XYChart.Data<>(i + "", 0.0));

					}
				} else {
					series2.getData().add(new XYChart.Data<>(i + "", 0.0));
				}
			}

			monthlySell.getData().add(series2);

		});

		try {
			Expires.setItems(FXCollections.observableArrayList(Queries.expiryDate()));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			outOfStock.setItems(FXCollections.observableArrayList(Queries.amountToFinish()));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			toBePaid.setItems(FXCollections.observableArrayList(Queries.queryResult(
					"select o.order_id,s.supplier_name,o.due_date_for_payment,o.order_cost\r\n"
							+ "from s_order o,supplier s\r\n"
							+ "where o.supplier_Id=s.supplier_ID and o.due_date_for_payment >DATE_ADD(DATE(NOW()), INTERVAL 30 DAY);",
					null)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			OFS.setItems(FXCollections.observableArrayList(Queries.queryResult("select P.product_name\r\n"
					+ "		from product p where p.product_id not in(\r\n" + " select b.product_id\r\n"
					+ "		from batch b\r\n" + "		where  b.batch_amount>0\r\n" + "		);", null)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			int numc = 0, productNum = 0;
			ArrayList<ArrayList<String>> expires = Queries.queryResult("SELECT count(*)\r\n"
					+ "		from batch b\r\n"
					+ "		where b.batch_amount>0 and b.batch_expiry_date <DATE(NOW()) and b.batch_production_date <>'1111-01-01';",
					null);

			ArrayList<ArrayList<String>> employee = Queries
					.queryResult("SELECT count(*)\r\n" + "	from employee\r\n" + "	where isActive='true';\r\n", null);

			ArrayList<ArrayList<String>> supplier = Queries.queryResult("SELECT count(*)\r\n" + "from supplier;", null);

			ArrayList<ArrayList<String>> customer = Queries.queryResult("SELECT count(*)" + " from customer;", null);

			ArrayList<ArrayList<String>> product = Queries.queryResult("SELECT count(*)" + " from product;", null);

			ArrayList<ArrayList<String>> totalDis = Queries.queryResult(
					"select sum(order_discount)\r\n" + "from c_order\r\n" + "where order_date = date(now());", null);

			ArrayList<ArrayList<String>> totalp = Queries.queryResult(
					"select sum(order_price)\r\n" + "	from c_order\r\n" + "	where order_date = date(now());", null);

			ArrayList<ArrayList<String>> outOS = Queries.queryResult("select count(*) \r\n"
					+ "from product p where p.product_id not in(\r\n" + "select b.product_id\r\n" + "from batch b\r\n"
					+ "where  b.batch_amount > 0 and b.batch_production_date <>'1111-01-01');", null);

			ArrayList<ArrayList<String>> income = Queries
					.queryResult("select sum(income_amount) from income where income_Date=date(now());", null);

			
			double totalSaled =0.0, totalIncome =0.0,discount =0.0;
			
			if (totalDis.size() == 0) {
				totalDiscount.setText("0.0");
			} else {
				totalDiscount.setText(totalDis.get(0).get(0));
				discount = Double.parseDouble(totalDis.get(0).get(0));
			}

			if (totalp.size() == 0) {
				totalSale.setText("0.0");
			} else {
				totalSale.setText(totalp.get(0).get(0));
				totalSaled = Double.parseDouble(totalp.get(0).get(0));
			}

			if (income.size() == 0) {
				totalReceived.setText("0.0");
				totalDept.setText(totalSaled + "");

			} else {
				totalIncome = Double.parseDouble(income.get(0).get(0));
				totalDept.setText((totalSaled -totalIncome - discount) + "");
				totalReceived.setText(totalIncome + "" );
				
			}
			

			if (customer.size() == 0) {
				numberCustomer.setText("0");
			} else {
				numc = Integer.parseInt(customer.get(0).get(0)) - 1;
				numberCustomer.setText("" + numc);
			}

			if (expires.size() == 0) {
				numberExpired.setText("0");
			} else {
				numberExpired.setText(expires.get(0).get(0));
			}

			if (employee.size() == 0) {
				numberEmployees.setText("0");
			} else {
				numberEmployees.setText(employee.get(0).get(0));
			}

			if (supplier.size() == 0) {
				numberSupplier.setText("0");
			} else {
				numberSupplier.setText(supplier.get(0).get(0));
			}

			if (product.size() == 0) {
				numberProduct.setText("0");
			} else {
				if (expires.get(0).get(0) != null)
					productNum = Integer.parseInt(product.get(0).get(0)) - Integer.parseInt(expires.get(0).get(0));
				else
					productNum = Integer.parseInt(product.get(0).get(0));

				numberProduct.setText(productNum + "");
			}

			if (outOS.size() == 0) {
				numberOFS.setText("0");
			} else {
				numberOFS.setText(outOS.get(0).get(0));
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		outOSName.setCellValueFactory(
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

		orderID.setCellValueFactory(
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
		dueDate.setCellValueFactory(
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
		cost.setCellValueFactory(
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

		eID.setCellValueFactory(
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
		eName.setCellValueFactory(
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
		epdate.setCellValueFactory(
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
		eedate.setCellValueFactory(
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
		eAmount.setCellValueFactory(
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
		oID.setCellValueFactory(
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
		oName.setCellValueFactory(
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
		opdate.setCellValueFactory(
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
		oedate.setCellValueFactory(
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
		amount.setCellValueFactory(
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

	}
}
