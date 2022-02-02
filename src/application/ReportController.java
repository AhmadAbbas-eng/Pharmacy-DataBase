package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Relations.*;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 * @author Aseel Sabri
 * @author Loor Sawalhi
 */
public class ReportController implements Initializable {

	@FXML
	private StackPane display;

	@FXML
	private ComboBox<String> reportType;

	@FXML
	private Button saveReportButton;

	@FXML
	private Label savedLabel;

	@FXML
	private ImageView saveIcon;

	public void showAndFade(Node node) {

		Timeline show = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
				new KeyFrame(Duration.seconds(0.5), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
				new KeyFrame(Duration.seconds(1), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)));
		FadeTransition fade = new FadeTransition(Duration.seconds(0.5), node);
		fade.setFromValue(1);
		fade.setToValue(0);

		SequentialTransition blinkFade = new SequentialTransition(node, show, fade);
		blinkFade.play();

	}

	public void saveOnAction(ActionEvent e) throws IOException {
		if (reportType.getSelectionModel().getSelectedItem() == "-Select-") {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Report Topic Must Be Specified");
			alert.showAndWait();
			return;
		}

		Path path = Paths.get(System.getProperty("user.home") + "/Desktop/Pharmacy/"
				+ reportType.getSelectionModel().getSelectedItem());

		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(path.toFile());
		File selectedDirectory = directoryChooser.showDialog(null);

		if (reportType.getSelectionModel().getSelectedItem() == "Payment") {
			Payment.report(selectedDirectory.toString() + "/Payment");
			showAndFade(saveIcon);
			showAndFade(savedLabel);
		} else if (reportType.getSelectionModel().getSelectedItem() == "Suppliers' Orders") {
			SupplierOrder.report(selectedDirectory.toString() + "/Suppliers Orders");
			showAndFade(saveIcon);
			showAndFade(savedLabel);

		} else if (reportType.getSelectionModel().getSelectedItem() == "Cheques") {
			Cheque.report(selectedDirectory.toString() + "/Cheques");
			showAndFade(saveIcon);
			showAndFade(savedLabel);

		} else if (reportType.getSelectionModel().getSelectedItem() == "Disposal") {
			Queries.reportQuery(
					"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
							+ "from batch b,product p\r\n"
							+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW()) \r\n"
							+ "order by b.batch_expiry_date;",
					selectedDirectory.toString() + "/Disposal");
			showAndFade(saveIcon);
			showAndFade(savedLabel);

		} else if (reportType.getSelectionModel().getSelectedItem() == "Customers' Orders") {
			CustomerOrder.report(selectedDirectory.toString() + "/Customers Orders");
			showAndFade(saveIcon);
			showAndFade(savedLabel);

		} else if (reportType.getSelectionModel().getSelectedItem() == "Products Information") {
			Product.report(selectedDirectory.toString() + "/Products Information");
			showAndFade(saveIcon);
			showAndFade(savedLabel);

		} else if (reportType.getSelectionModel().getSelectedItem() == "Product Batches") {
			Batch.report(selectedDirectory.toString() + "/Product Batches");
			showAndFade(saveIcon);
			showAndFade(savedLabel);

		} else if (reportType.getSelectionModel().getSelectedItem() == "Net Profit") {
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
				ArrayList<String> data = new ArrayList<>();
				data.add(yearAndMonth);
				data.add(netProfit + "");
				tableData.add(data);
				if (startMonth == 12) {
					++startYear;
					startMonth = 1;
				} else {
					++startMonth;
				}

			}

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"); // format date as current date
			LocalDateTime now = LocalDateTime.now();

			FileWriter fileWriter = new FileWriter(
					selectedDirectory.toString() + "/Net Profit" + dtf.format(now) + ".csv"); // Write on the path the
																								// user asked
			fileWriter.write("Date" + "," + "net Profit" + "\n");
			// Write the names of attributes on file
			for (int i = 0; i < tableData.size(); i++) {
				fileWriter.write(tableData.get(i).get(0) + "," + tableData.get(i).get(1) + "\n");
			}
			fileWriter.close();
			showAndFade(saveIcon);
			showAndFade(savedLabel);
		}
	}

	ObservableList<String> reports = FXCollections.observableArrayList("-Select-", "Payment", "Suppliers' Orders",
			"Cheques", "Disposal", "Customers' Orders", "Products Information", "Product Batches", "Net Profit");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		saveIcon.setOpacity(0);
		savedLabel.setOpacity(0);
		reportType.setItems(reports);
		reportType.setValue("-Select-");

		try {
			Region page;
			page = FXMLLoader.load(getClass().getResource("EmptyScene.fxml"));
			display.getChildren().setAll(page);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		reportType.setOnAction(e -> {
			if (reportType.getSelectionModel().getSelectedItem() == "Payment") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("paymentReport.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (reportType.getSelectionModel().getSelectedItem() == "Suppliers' Orders") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("SupplierOrderReport.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (reportType.getSelectionModel().getSelectedItem() == "Net Profit") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("NetProfit.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (reportType.getSelectionModel().getSelectedItem() == "Cheques") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("ChequeReport.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (reportType.getSelectionModel().getSelectedItem() == "Disposal") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("disposalReport.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (reportType.getSelectionModel().getSelectedItem() == "Customers' Orders") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("CustomersReport.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (reportType.getSelectionModel().getSelectedItem() == "Products Information") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("ProductsReport.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (reportType.getSelectionModel().getSelectedItem() == "Product Batches") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("BatchReport.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);
					page.prefWidthProperty().bind(display.widthProperty());
					page.prefHeightProperty().bind(display.heightProperty());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("EmptyScene.fxml"));
					display.getChildren().removeAll();
					display.getChildren().setAll(page);

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
	}
}
