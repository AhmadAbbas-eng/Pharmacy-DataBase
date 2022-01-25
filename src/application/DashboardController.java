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
	private TableColumn<ArrayList<String>, String> outOfStockAmountColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> expiresAmountColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> expiresProductIDColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> expiresProductNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> expiresExpiryDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> expiresProductionDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> outOfStockSoonIDColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> outOfStockSoonNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> outOfStockSoonExpiryDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> outOfStockSoonProductionDateColumn;

	@FXML
	private TableView<ArrayList<String>> outOfStockSoonTable;

	@FXML
	private TableView<ArrayList<String>> expiresTabel;

	@FXML
	private TableColumn<ArrayList<String>, String> toBePaidCostColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> toBePaidDueDateOfPaymentColumn;

	@FXML
	private Label numberOfCustomerLabel;

	@FXML
	private Label numberOfEmployeesLabel;

	@FXML
	private Label numberOfExpiredProductsLabel;

	@FXML
	private Label outOfStockLabel;

	@FXML
	private Label numberOfProductLabel;

	@FXML
	private Label numberOfSupplierLabel;

	@FXML
	private TableColumn<ArrayList<String>, String> toBePaidIDColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> toBePaidSupplierNameColumn;

	@FXML
	private TableView<ArrayList<String>> toBePaidTable;

	@FXML
	private Label totalDeptLabel;

	@FXML
	private Label totalDiscountLabel;

	@FXML
	private Label totalIncomeLabel;

	@FXML
	private Label totalSaleLabel;

	@FXML
	private LineChart<String, Double> monthlySellChart;

	@FXML
	private TableColumn<ArrayList<String>, String> outOfStockNameColumn;

	@FXML
	private TableView<ArrayList<String>> outOfStockTable;

	@FXML
	private ComboBox<String> monthBox;

	@FXML
	private ComboBox<String> yearBox;
	int counter = 0;
	ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November", "December");

	ObservableList<String> years = FXCollections.observableArrayList("2018", "2019", "2020", "2021");

	ArrayList<ArrayList<String>> dailySalesArrayList = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LocalDate current_date = LocalDate.now();
		if (years.indexOf(current_date.getYear() + "") == -1) {
			years.add(current_date.getYear() + "");
		}
		yearBox.setItems(years); 
		monthBox.setItems(months);

		XYChart.Series<String, Double> series = new XYChart.Series<>();
		series.setName("Daily Sales");

		try {
			dailySalesArrayList = Queries.queryResult("select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
					+ "	where  year(order_date) = year(date(now())) and month(order_date) = month(date(now())) \r\n"
					+ "	group by day(order_date) ;", null);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		for (int i = 1; i <= 31; ++i) {

			if (counter < dailySalesArrayList.size()) {
				if (Integer.parseInt(dailySalesArrayList.get(counter).get(1)) == i) {
					series.getData().add(new XYChart.Data<>(i + "", Double.parseDouble(dailySalesArrayList.get(counter).get(0))));
					++counter;
				} else {
					series.getData().add(new XYChart.Data<>(i + "", 0.0));

				}
			} else {
				series.getData().add(new XYChart.Data<>(i + "", 0.0));

			}
		}
		monthlySellChart.getData().add(series);

		yearBox.setOnAction(e -> {
			XYChart.Series<String, Double> series2 = new XYChart.Series<>();
			series2.setName("Daily Sales");
			monthlySellChart.getData().clear();

			int thisMonth;
			if (monthBox.getSelectionModel().getSelectedIndex() == -1) {
				thisMonth = Calendar.MONTH - 1;
			} else {
				thisMonth = monthBox.getSelectionModel().getSelectedIndex() + 1;
			}
			if (yearBox.getSelectionModel().getSelectedItem() == null ) {
				try {
					dailySalesArrayList = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = year(date(now())) and month(order_date) = ? \r\n"
									+ "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList("" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				int currentYear = yearBox.getSelectionModel().getSelectedIndex() + 2018;
				try {
					dailySalesArrayList = Queries.queryResult("select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
							+ "	where  year(order_date) = ? and month(order_date) = ?" + "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList(currentYear + "", "" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}

			counter = 0;
			for (int i = 1; i <= 31; ++i) {
				if (counter < dailySalesArrayList.size()) {
					if (Integer.parseInt(dailySalesArrayList.get(counter).get(1)) == i) {
						series2.getData()
								.add(new XYChart.Data<>(i + "", Double.parseDouble(dailySalesArrayList.get(counter).get(0))));
						++counter;
					} else {
						series2.getData().add(new XYChart.Data<>(i + "", 0.0));

					}
				} else {
					series2.getData().add(new XYChart.Data<>(i + "", 0.0));

				}
			}

			monthlySellChart.getData().add(series2);

		});

		monthBox.setOnAction(e -> {

			XYChart.Series<String, Double> series2 = new XYChart.Series<>();
			series2.setName("Daily Sales");
			monthlySellChart.getData().clear();

			int thisMonth;
			if (monthBox.getSelectionModel().getSelectedIndex() == -1) {
				thisMonth = Calendar.MONTH - 1;
			} else {
				thisMonth = monthBox.getSelectionModel().getSelectedIndex() + 1;
			}
			if (yearBox.getSelectionModel().getSelectedItem() == null ) {
				try {
					dailySalesArrayList = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = year(date(now())) and month(order_date) = ? \r\n"
									+ "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList("" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				int currentYear = yearBox.getSelectionModel().getSelectedIndex() + 2018;
				try {
					dailySalesArrayList = Queries.queryResult("select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
							+ "	where  year(order_date) = ? and month(order_date) = ?" + "	group by day(order_date) ;",
							new ArrayList<String>(Arrays.asList(currentYear + "", "" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}

			counter = 0;

			for (int i = 1; i <= 31; ++i) {

				if (counter < dailySalesArrayList.size()) {
					if (Integer.parseInt(dailySalesArrayList.get(counter).get(1)) == i) {
						series2.getData()
								.add(new XYChart.Data<>(i + "", Double.parseDouble(dailySalesArrayList.get(counter).get(0))));
						++counter;
					} else {
						series2.getData().add(new XYChart.Data<>(i + "", 0.0));

					}
				} else {
					series2.getData().add(new XYChart.Data<>(i + "", 0.0));
				}
			}

			monthlySellChart.getData().add(series2);

		});

		try {
			expiresTabel.setItems(FXCollections.observableArrayList(Queries.expiryDate()));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			outOfStockSoonTable.setItems(FXCollections.observableArrayList(Queries.amountToFinish()));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			toBePaidTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
					"select o.order_id,s.supplier_name,o.due_date_for_payment,o.order_cost\r\n"
							+ "from s_order o,supplier s\r\n"
							+ "where o.supplier_Id=s.supplier_ID and o.due_date_for_payment >DATE_ADD(DATE(NOW()), INTERVAL 30 DAY);",
					null)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			outOfStockTable.setItems(FXCollections.observableArrayList(Queries.queryResult("select P.product_name\r\n"
					+ "		from product p where p.product_id not in(\r\n" + " select b.product_id\r\n"
					+ "		from batch b\r\n" + "		where  b.batch_amount>0\r\n" + "		);", null)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			int numc = 0, productNum = 0;
			ArrayList<ArrayList<String>> numberOfExpiredProductsArrayList = Queries.queryResult("SELECT count(*)\r\n"
					+ "		from batch b\r\n"
					+ "		where b.batch_amount>0 and b.batch_expiry_date <DATE(NOW()) and b.batch_production_date <>'1111-01-01';",
					null);

			ArrayList<ArrayList<String>> numberOfEmployeesArrayList = Queries
					.queryResult("SELECT count(*)\r\n" + "	from employee\r\n" + "	where isActive='true';\r\n", null);

			ArrayList<ArrayList<String>> numberOfSuppliersArrayList = Queries.queryResult("SELECT count(*)\r\n" + "from supplier;", null);

			ArrayList<ArrayList<String>> numberOfCustomersArrayList = Queries.queryResult("SELECT count(*)" + " from customer;", null);

			ArrayList<ArrayList<String>> numberOfAvailableProductsArrayList = Queries.queryResult("SELECT count(*)" + " from product;", null);

			ArrayList<ArrayList<String>> todaysTotalDiscountArrayList = Queries.queryResult(
					"select sum(order_discount)\r\n" + "from c_order\r\n" + "where order_date = date(now());", null);

			ArrayList<ArrayList<String>> todaysTotalPaidAmountArrayList = Queries.queryResult(
					"select sum(order_price)\r\n" + "	from c_order\r\n" + "	where order_date = date(now());", null);

			ArrayList<ArrayList<String>> numberOfOutOfStockArrayList = Queries.queryResult("select count(*) \r\n"
					+ "from product p where p.product_id not in(\r\n" + "select b.product_id\r\n" + "from batch b\r\n"
					+ "where  b.batch_amount > 0 and b.batch_production_date <>'1111-01-01');", null);

			ArrayList<ArrayList<String>> todaysTotalIncomeAmountArrayList = Queries
					.queryResult("select sum(income_amount) from income where income_Date=date(now());", null);

			
			double totalSoled =0.0, totalIncome =0.0,totalDiscount =0.0;
			
			if (todaysTotalDiscountArrayList.get(0).get(0)==null) {
				totalDiscountLabel.setText("0.0");
			} else {
				System.out.println(todaysTotalDiscountArrayList.toString());
				totalDiscountLabel.setText(todaysTotalDiscountArrayList.get(0).get(0));
				totalDiscount = Double.parseDouble(todaysTotalDiscountArrayList.get(0).get(0));
			}

			if (todaysTotalPaidAmountArrayList.get(0).get(0)==null) {
				totalSaleLabel.setText("0.0");
			} else {
				totalSaleLabel.setText(todaysTotalPaidAmountArrayList.get(0).get(0));
				totalSoled = Double.parseDouble(todaysTotalPaidAmountArrayList.get(0).get(0));
			}

			if (todaysTotalIncomeAmountArrayList.get(0).get(0)==null) {
				totalIncomeLabel.setText("0.0");

			} else {
				totalIncome = Double.parseDouble(todaysTotalIncomeAmountArrayList.get(0).get(0));
				totalIncomeLabel.setText(totalIncome + "" );
				
			}

			if (numberOfCustomersArrayList.get(0).get(0)==null) {
				numberOfCustomerLabel.setText("0");
			} else {
				numc = Integer.parseInt(numberOfCustomersArrayList.get(0).get(0)) - 1;
				numberOfCustomerLabel.setText("" + numc);
			}

			if (numberOfExpiredProductsArrayList.get(0).get(0)==null) {
				numberOfExpiredProductsLabel.setText("0");
			} else {
				numberOfExpiredProductsLabel.setText(numberOfExpiredProductsArrayList.get(0).get(0));
			}

			if (numberOfEmployeesArrayList.get(0).get(0)==null) {
				numberOfEmployeesLabel.setText("0");
			} else {
				numberOfEmployeesLabel.setText(numberOfEmployeesArrayList.get(0).get(0));
			}

			if (numberOfSuppliersArrayList.get(0).get(0)==null) {
				numberOfSupplierLabel.setText("0");
			} else {
				numberOfSupplierLabel.setText(numberOfSuppliersArrayList.get(0).get(0));
			}

			if (numberOfAvailableProductsArrayList.get(0).get(0)==null) {
				numberOfProductLabel.setText("0");
			} else {
				if (numberOfExpiredProductsArrayList.get(0).get(0) != null)
					productNum = Integer.parseInt(numberOfAvailableProductsArrayList.get(0).get(0)) - Integer.parseInt(numberOfExpiredProductsArrayList.get(0).get(0));
				else
					productNum = Integer.parseInt(numberOfAvailableProductsArrayList.get(0).get(0));

				numberOfProductLabel.setText(productNum + "");
			}

			if (numberOfOutOfStockArrayList.get(0).get(0)==null) {
				outOfStockLabel.setText("0");
			} else {
				outOfStockLabel.setText(numberOfOutOfStockArrayList.get(0).get(0));
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		outOfStockNameColumn.setCellValueFactory(
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

		toBePaidIDColumn.setCellValueFactory(
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
		toBePaidSupplierNameColumn.setCellValueFactory(
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
		toBePaidDueDateOfPaymentColumn.setCellValueFactory(
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
		toBePaidCostColumn.setCellValueFactory(
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

		expiresProductIDColumn.setCellValueFactory(
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
		expiresProductNameColumn.setCellValueFactory(
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
		expiresProductionDateColumn.setCellValueFactory(
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
		expiresExpiryDateColumn.setCellValueFactory(
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
		expiresAmountColumn.setCellValueFactory(
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
		outOfStockSoonIDColumn.setCellValueFactory(
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
		outOfStockSoonNameColumn.setCellValueFactory(
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
		outOfStockSoonProductionDateColumn.setCellValueFactory(
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
		outOfStockSoonExpiryDateColumn.setCellValueFactory(
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
		outOfStockAmountColumn.setCellValueFactory(
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
