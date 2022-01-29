package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Relations.Queries;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class MonthlyNetProfitController implements Initializable {
	
    @FXML
    private TableColumn<ArrayList<String>, String> dateColumn;
	
    @FXML
    private TableColumn<ArrayList<String>, String> netProfitColumn;

    @FXML
    private TableView<ArrayList<String>> netProfitTable;

	@FXML
	private LineChart<String, Double> montlyNetProfitChart;
	
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
		XYChart.Series<String, Double> series = new XYChart.Series<>();
		montlyNetProfitChart.setTitle("Pharmacy Net Profit");
		series.setName("Monthly Net Profit");
		int currentMonth, currentYear, startMonth, startYear;

		currentMonth = Integer.parseInt(LocalDate.now().toString().substring(5, 7));
		currentYear = Integer.parseInt(LocalDate.now().toString().substring(0, 4));

		ArrayList<ArrayList<String>> minimunMonthArrayList = null;
		ArrayList<ArrayList<String>> tableData = new ArrayList<>();
		
		minimunMonthArrayList = Queries
				.queryResult("select min(x) from (\r\n" + "select min(I.income_Date) as 'x' from income I\r\n"
						+ "union\r\n" + "select min(p.payment_Date) as 'x' from payment p) as a;", null);

		if (minimunMonthArrayList.get(0).get(0) != null) {

			startMonth = Integer.parseInt(minimunMonthArrayList.get(0).get(0).substring(5, 7));
			startYear = Integer.parseInt(minimunMonthArrayList.get(0).get(0).substring(0, 4));
		} else {
			startMonth = currentMonth;
			startYear = currentYear;
		}
		while (startYear < currentYear || (startMonth <= currentMonth && startYear == currentYear)) {
			Double netProfit = 0.0;

			netProfit = Queries.getNetProfit(startMonth, startYear);
			
			String yearAndMonth = startMonth + "-" + startYear;
			XYChart.Data<String, Double> dataChart = new XYChart.Data<>(yearAndMonth, netProfit );
			series.getData().add(dataChart);
			dataChart.setNode(new HoveredThresholdNode( netProfit));
			ArrayList<String> data = new ArrayList<>();
			data.add(yearAndMonth);
			data.add(netProfit+"");
			tableData.add(data);
			if (startMonth == 12) {
				++startYear;
				startMonth = 1;
			} else {
				++startMonth;
			}

		}
		
		montlyNetProfitChart.getData().add(series);
		
		netProfitTable.setItems(FXCollections.observableArrayList(tableData));
		
		dateColumn.setCellValueFactory(
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
		netProfitColumn.setCellValueFactory(
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
