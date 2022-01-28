package application;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ResourceBundle;

import Relations.Queries;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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

	@FXML
	private HBox iconsHBox;

	@FXML
	private AnchorPane employeePane;

	@FXML
	private ImageView employeeImage;

	@FXML
	private ImageView customerImage;

	@FXML
	private ImageView supplierImage;

	@FXML
	private ImageView productImage;

	@FXML
	private ImageView stockImage;

	@FXML
	private ImageView expiredImage;

	@FXML
	private Label totalEmployeesLabel;

	@FXML
	private Label totalProductsLabel;

	@FXML
	private Label stockLabel;

	@FXML
	private Label totalSuppliersLabel;

	@FXML
	private Label totalCustomersLabel;

	@FXML
	private Label expiredLabel;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private VBox mainVBox;

	@FXML
	private VBox leftVBox;

	@FXML
	private Line line1;

	@FXML
	private Line line2;

	@FXML
	private VBox reportVBox;
	
	int counter = 0;
	
	ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November", "December");

	ObservableList<String> years = FXCollections.observableArrayList("2018", "2019", "2020", "2021");

	ArrayList<ArrayList<String>> dailySalesArrayList = null;

	class HoveredThresholdNode extends StackPane {
		HoveredThresholdNode(double value) {
			setPrefSize(12, 12);

			final Label label = createDataThresholdLabel(value);

			setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					getChildren().setAll(label);
					setCursor(Cursor.NONE);
					toFront();
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					getChildren().clear();
					setCursor(Cursor.CROSSHAIR);
				}
			});
		}

		private Label createDataThresholdLabel(double value) {
			final Label label = new Label(value + "");
			label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
			label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");

			label.setTextFill(Color.DARKGRAY);

			label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			return label;
		}
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		leftVBox.prefWidthProperty().bind(mainPane.widthProperty().multiply(0.75));
		iconsHBox.spacingProperty().bind(iconsHBox.widthProperty().divide(20.0));

		iconsHBox.paddingProperty()
				.bind(Bindings
						.createObjectBinding(
								() -> new Insets(0, iconsHBox.widthProperty().divide(20).doubleValue(), 0,
										iconsHBox.widthProperty().divide(20).doubleValue()),
								iconsHBox.widthProperty()));

		iconsHBox.minHeightProperty().bind(iconsHBox.widthProperty().multiply(13).divide(120));
		iconsHBox.prefHeightProperty().bind(employeePane.widthProperty());

		line1.startXProperty().bind(iconsHBox.layoutXProperty());

		line1.endXProperty().bind(iconsHBox.layoutXProperty().add(iconsHBox.widthProperty()));

		line2.startXProperty().bind(reportVBox.widthProperty().multiply(-0.5));
		line2.endXProperty().bind(reportVBox.widthProperty().multiply(0.5));

		mainVBox.paddingProperty()
				.bind(Bindings.createObjectBinding(
						() -> new Insets(mainPane.prefHeightProperty().multiply(0.01).doubleValue(),
								mainPane.prefWidthProperty().multiply(0.01).doubleValue(),
								mainPane.prefHeightProperty().multiply(0.01).doubleValue(),
								mainPane.prefWidthProperty().multiply(0.01).doubleValue()),
						mainPane.prefWidthProperty(), mainPane.prefHeightProperty()));

		employeeImage.fitWidthProperty().bind(employeePane.widthProperty().multiply(0.65));
		totalEmployeesLabel.prefWidthProperty().bind(employeePane.widthProperty().multiply(0.86));
		totalEmployeesLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.3));
		numberOfEmployeesLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.35));

		totalEmployeesLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalEmployeesLabel.widthProperty().divide(12)));

		numberOfEmployeesLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalEmployeesLabel.widthProperty().divide(5)));

		supplierImage.fitWidthProperty().bind(employeePane.widthProperty().multiply(0.65));
		totalSuppliersLabel.prefWidthProperty().bind(employeePane.widthProperty().multiply(0.86));
		totalSuppliersLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.3));
		numberOfSupplierLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.35));

		totalSuppliersLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalSuppliersLabel.widthProperty().divide(12)));

		numberOfSupplierLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalSuppliersLabel.widthProperty().divide(5)));

		customerImage.fitWidthProperty().bind(employeePane.widthProperty().multiply(0.65));
		totalCustomersLabel.prefWidthProperty().bind(employeePane.widthProperty().multiply(0.86));
		totalCustomersLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.3));
		numberOfCustomerLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.35));

		totalCustomersLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalCustomersLabel.widthProperty().divide(12)));

		numberOfCustomerLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalCustomersLabel.widthProperty().divide(5)));

		productImage.fitWidthProperty().bind(employeePane.widthProperty().multiply(0.65));
		totalProductsLabel.prefWidthProperty().bind(employeePane.widthProperty().multiply(0.86));
		totalProductsLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.3));
		numberOfProductLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.35));

		totalProductsLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalProductsLabel.widthProperty().divide(12)));

		numberOfProductLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						totalProductsLabel.widthProperty().divide(5)));

		stockImage.fitWidthProperty().bind(employeePane.widthProperty().multiply(0.65));
		stockLabel.prefWidthProperty().bind(employeePane.widthProperty().multiply(0.86));
		stockLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.3));
		outOfStockLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.35));

		stockLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						stockLabel.widthProperty().divide(12)));

		outOfStockLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						stockLabel.widthProperty().divide(5)));

		expiredImage.fitWidthProperty().bind(employeePane.widthProperty().multiply(0.65));
		expiredLabel.prefWidthProperty().bind(employeePane.widthProperty().multiply(0.86));
		expiredLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.3));
		numberOfExpiredProductsLabel.prefHeightProperty().bind(employeePane.widthProperty().multiply(0.35));

		expiredLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						expiredLabel.widthProperty().divide(12)));

		numberOfExpiredProductsLabel.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.2fpt; -fx-margin: 0px; -fx-font-weight: bold; ",
						expiredLabel.widthProperty().divide(5)));

		LocalDate current_date = LocalDate.now();
		if (years.indexOf(current_date.getYear() + "") == -1) {
			years.add(current_date.getYear() + "");
		}
		yearBox.setItems(years);
		monthBox.setItems(months);

		XYChart.Series<String, Double> series = new XYChart.Series<>();
		series.setName("Daily Sales");
		monthlySellChart.setTitle("Pharmacy Sales");
		try {
			dailySalesArrayList = Queries.queryResult("select sum(order_price), day(order_date)\r\n"
					+ "	from c_order\r\n"
					+ "	where  year(order_date) = year(date(now())) and month(order_date) = month(date(now())) \r\n"
					+ "	group by day(order_date) order by day(order_date);", null);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		for (int i = 1; i <= 31; ++i) {
			XYChart.Data<String, Double> data ;
			if (counter < dailySalesArrayList.size()) {
				if (Integer.parseInt(dailySalesArrayList.get(counter).get(1)) == i) {
					data = new XYChart.Data<>(i + "", Double.parseDouble(dailySalesArrayList.get(counter).get(0)));
					series.getData().add(data);
					data.setNode(new HoveredThresholdNode( Double.parseDouble(dailySalesArrayList.get(counter).get(0))));

					++counter;
				} else {
					data = new XYChart.Data<>(i + "", 0.0);
					series.getData().add(data);
					data.setNode(new HoveredThresholdNode(0.0));
				}
			} else {
				data = new XYChart.Data<>(i + "", 0.0);
				series.getData().add(data);
				data.setNode(new HoveredThresholdNode(0.0));

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
			if (yearBox.getSelectionModel().getSelectedItem() == null) {
				try {
					dailySalesArrayList = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = year(date(now())) and month(order_date) = ? \r\n"
									+ "	group by day(order_date) order by day(order_date);",
							new ArrayList<String>(Arrays.asList("" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				int currentYear = yearBox.getSelectionModel().getSelectedIndex() + 2018;
				try {
					dailySalesArrayList = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = ? and month(order_date) = ?"
									+ "	group by day(order_date) order by day(order_date);",
							new ArrayList<String>(Arrays.asList(currentYear + "", "" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}

			counter = 0;
			for (int i = 1; i <= 31; ++i) {
				XYChart.Data<String, Double> data ;
				if (counter < dailySalesArrayList.size()) {
					if (Integer.parseInt(dailySalesArrayList.get(counter).get(1)) == i) {
						data = new XYChart.Data<>(i + "", Double.parseDouble(dailySalesArrayList.get(counter).get(0)));
						series2.getData().add(data);
						data.setNode(new HoveredThresholdNode( Double.parseDouble(dailySalesArrayList.get(counter).get(0))));

						++counter;
					} else {
						data = new XYChart.Data<>(i + "", 0.0);
						series2.getData().add(data);
						data.setNode(new HoveredThresholdNode(0.0));
					}
				} else {
					data = new XYChart.Data<>(i + "", 0.0);
					series2.getData().add(data);
					data.setNode(new HoveredThresholdNode(0.0));

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
			if (yearBox.getSelectionModel().getSelectedItem() == null) {
				try {
					dailySalesArrayList = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = year(date(now())) and month(order_date) = ? \r\n"
									+ "	group by day(order_date) order by day(order_date); ",
							new ArrayList<String>(Arrays.asList("" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				int currentYear = yearBox.getSelectionModel().getSelectedIndex() + 2018;
				try {
					dailySalesArrayList = Queries.queryResult(
							"select sum(order_price), day(order_date)\r\n" + "	from c_order\r\n"
									+ "	where  year(order_date) = ? and month(order_date) = ?"
									+ "	group by day(order_date) order by day(order_date);",
							new ArrayList<String>(Arrays.asList(currentYear + "", "" + thisMonth)));
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}

			counter = 0;

			for (int i = 1; i <= 31; ++i) {
				XYChart.Data<String, Double> data ;
				if (counter < dailySalesArrayList.size()) {
					if (Integer.parseInt(dailySalesArrayList.get(counter).get(1)) == i) {
						data = new XYChart.Data<>(i + "", Double.parseDouble(dailySalesArrayList.get(counter).get(0)));
						series2.getData().add(data);
						data.setNode(new HoveredThresholdNode( Double.parseDouble(dailySalesArrayList.get(counter).get(0))));

						++counter;
					} else {
						data = new XYChart.Data<>(i + "", 0.0);
						series2.getData().add(data);
						data.setNode(new HoveredThresholdNode(0.0));
					}
				} else {
					data = new XYChart.Data<>(i + "", 0.0);
					series2.getData().add(data);
					data.setNode(new HoveredThresholdNode(0.0));

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
			int numc = 0, productNum = 0, outOfStock = 0;
			ArrayList<ArrayList<String>> numberOfExpiredProductsArrayList = Queries.queryResult("SELECT count(*)\r\n"
					+ "		from batch b\r\n"
					+ "		where b.batch_amount>0 and b.batch_expiry_date <DATE(NOW()) and b.batch_production_date <>'1111-01-01';",
					null);

			ArrayList<ArrayList<String>> numberOfEmployeesArrayList = Queries
					.queryResult("SELECT count(*)\r\n" + "	from employee\r\n" + "	where isActive='true';\r\n", null);

			ArrayList<ArrayList<String>> numberOfSuppliersArrayList = Queries
					.queryResult("SELECT count(*)\r\n" + "from supplier;", null);

			ArrayList<ArrayList<String>> numberOfCustomersArrayList = Queries
					.queryResult("SELECT count(*)" + " from customer;", null);

			ArrayList<ArrayList<String>> numberOfAvailableProductsArrayList = Queries
					.queryResult("SELECT count(*)" + " from product;", null);

			ArrayList<ArrayList<String>> todaysTotalDiscountArrayList = Queries.queryResult(
					"select sum(order_discount)\r\n" + "from c_order\r\n" + "where order_date = date(now());", null);

			ArrayList<ArrayList<String>> todaysTotalPaidAmountArrayList = Queries.queryResult(
					"select sum(order_price)\r\n" + "	from c_order\r\n" + "	where order_date = date(now());", null);

			ArrayList<ArrayList<String>> numberOfOutOfStockArrayList = Queries.queryResult("select count(*) \r\n"
					+ "from product p where p.product_id not in(\r\n" + "select b.product_id\r\n" + "from batch b\r\n"
					+ "where  b.batch_amount > 0 and b.batch_production_date <>'1111-01-01');", null);

			ArrayList<ArrayList<String>> todaysTotalIncomeAmountArrayList = Queries
					.queryResult("select sum(income_amount) from income where income_Date=date(now());", null);

			double totalIncome = 0.0;

			if (todaysTotalDiscountArrayList.get(0).get(0) == null) {
				totalDiscountLabel.setText("0.0");
			} else {
				System.out.println(todaysTotalDiscountArrayList.toString());
				totalDiscountLabel.setText(todaysTotalDiscountArrayList.get(0).get(0));
				Double.parseDouble(todaysTotalDiscountArrayList.get(0).get(0));
			}

			if (todaysTotalPaidAmountArrayList.get(0).get(0) == null) {
				totalSaleLabel.setText("0.0");
			} else {
				totalSaleLabel.setText(todaysTotalPaidAmountArrayList.get(0).get(0));
				Double.parseDouble(todaysTotalPaidAmountArrayList.get(0).get(0));
			}

			if (todaysTotalIncomeAmountArrayList.get(0).get(0) == null) {
				totalIncomeLabel.setText("0.0");

			} else {
				totalIncome = Double.parseDouble(todaysTotalIncomeAmountArrayList.get(0).get(0));
				totalIncomeLabel.setText(totalIncome + "");

			}

			if (numberOfCustomersArrayList.get(0).get(0) == null) {
				numberOfCustomerLabel.setText("0");
			} else {
				numc = Integer.parseInt(numberOfCustomersArrayList.get(0).get(0)) - 1;
				numberOfCustomerLabel.setText("" + numc);
			}

			if (numberOfExpiredProductsArrayList.get(0).get(0) == null) {
				numberOfExpiredProductsLabel.setText("0");
			} else {
				numberOfExpiredProductsLabel.setText(numberOfExpiredProductsArrayList.get(0).get(0));
			}

			if (numberOfEmployeesArrayList.get(0).get(0) == null) {
				numberOfEmployeesLabel.setText("0");
			} else {
				numberOfEmployeesLabel.setText(numberOfEmployeesArrayList.get(0).get(0));
			}

			if (numberOfSuppliersArrayList.get(0).get(0) == null) {
				numberOfSupplierLabel.setText("0");
			} else {
				numberOfSupplierLabel.setText(numberOfSuppliersArrayList.get(0).get(0));
			}
			
			if (numberOfOutOfStockArrayList.get(0).get(0) == null) {
				outOfStockLabel.setText("0");
			} else {
				outOfStock =  Integer.parseInt(numberOfOutOfStockArrayList.get(0).get(0));
				outOfStockLabel.setText(numberOfOutOfStockArrayList.get(0).get(0));
			}

			if (numberOfAvailableProductsArrayList.get(0).get(0) == null) {
				numberOfProductLabel.setText("0");
			} else {
				if (numberOfExpiredProductsArrayList.get(0).get(0) != null)
					
					productNum = Integer.parseInt(numberOfAvailableProductsArrayList.get(0).get(0))
							- Integer.parseInt(numberOfExpiredProductsArrayList.get(0).get(0)) - outOfStock;
				else
					productNum = Integer.parseInt(numberOfAvailableProductsArrayList.get(0).get(0)) - outOfStock;

				numberOfProductLabel.setText(productNum + "");
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
	
		outOfStockSoonNameColumn.setCellValueFactory(
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
		outOfStockSoonProductionDateColumn.setCellValueFactory(
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
		outOfStockSoonExpiryDateColumn.setCellValueFactory(
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
		outOfStockAmountColumn.setCellValueFactory(
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

	}
}
