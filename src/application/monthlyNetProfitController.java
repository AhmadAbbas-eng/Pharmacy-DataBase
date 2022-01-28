package application;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Relations.Queries;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class monthlyNetProfitController implements Initializable {
	
    @FXML
    private TableColumn<ArrayList<String>, String> dateColumn;
	
    @FXML
    private TableColumn<ArrayList<String>, String> netProfitColumn;

    @FXML
    private TableView<ArrayList<String>> netProfitTable;

	@FXML
	private LineChart<String, Double> montlyNetProfitChart;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		XYChart.Series<String, Double> series = new XYChart.Series<>();
		series.setName("Monthly Net Profit");
		int currentMonth, currentYear, startMonth, startYear;

		currentMonth = Integer.parseInt(LocalDate.now().toString().substring(5, 7));
		currentYear = Integer.parseInt(LocalDate.now().toString().substring(0, 4));

		ArrayList<ArrayList<String>> minimunMonthArrayList = null;
		ArrayList<ArrayList<String>> tableData = new ArrayList<>();
		
		try {
			minimunMonthArrayList = Queries
					.queryResult("select min(x) from (\r\n" + "select min(I.income_Date) as 'x' from income I\r\n"
							+ "union\r\n" + "select min(p.payment_Date) as 'x' from payment p) as a;", null);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		if (minimunMonthArrayList.get(0).get(0) != null) {

			startMonth = Integer.parseInt(minimunMonthArrayList.get(0).get(0).substring(5, 7));
			startYear = Integer.parseInt(minimunMonthArrayList.get(0).get(0).substring(0, 4));
		} else {
			startMonth = currentMonth;
			startYear = currentYear;
		}
		while (startYear < currentYear || (startMonth <= currentMonth && startYear == currentYear)) {
			Double netProfit = 0.0;

			try {
				netProfit = Queries.getNetProfit(startMonth, startYear);
			} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
			String yearAndMonth = startMonth + "-" + startYear;
			series.getData().add(new XYChart.Data<>(yearAndMonth, netProfit ));
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
