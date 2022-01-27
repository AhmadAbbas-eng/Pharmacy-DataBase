package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Relations.Batch;
import Relations.Cheque;
import Relations.Customer;
import Relations.Payment;
import Relations.Product;
import Relations.Queries;
import Relations.Supplier;
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

	public void saveOnAction(ActionEvent e) {
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
			try {
				Payment.report(selectedDirectory.toString() + "/Payment");
				showAndFade(saveIcon);
				showAndFade(savedLabel);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}
		} else if (reportType.getSelectionModel().getSelectedItem() == "Suppliers") {
			try {
				Supplier.report(selectedDirectory.toString() + "/Suppliers");
				showAndFade(saveIcon);
				showAndFade(savedLabel);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}

		} else if (reportType.getSelectionModel().getSelectedItem() == "Cheques") {
			try {
				Cheque.report(selectedDirectory.toString() + "/Cheques");
				showAndFade(saveIcon);
				showAndFade(savedLabel);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}

		} else if (reportType.getSelectionModel().getSelectedItem() == "Disposal") {
			try {
				Queries.reportQuerey(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
								+ "from batch b,product p\r\n"
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW()) \r\n"
								+ "order by b.batch_expiry_date;",
						selectedDirectory.toString() + "/Disposal");
				showAndFade(saveIcon);
				showAndFade(savedLabel);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}

		} else if (reportType.getSelectionModel().getSelectedItem() == "Customers") {
			try {
				Customer.report(selectedDirectory.toString() + "/Customers");
				showAndFade(saveIcon);
				showAndFade(savedLabel);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}

		} else if (reportType.getSelectionModel().getSelectedItem() == "Products Information") {
			try {
				Product.report(selectedDirectory.toString() + "/Products Information");
				showAndFade(saveIcon);
				showAndFade(savedLabel);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}

		} else if (reportType.getSelectionModel().getSelectedItem() == "Product Batches") {
			try {
				Batch.report(selectedDirectory.toString() + "/Product Batches");
				showAndFade(saveIcon);
				showAndFade(savedLabel);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	ObservableList<String> reports = FXCollections.observableArrayList("-Select-", "Payment", "Suppliers", "Cheques",
			"Disposal", "Customers", "Products Information", "Product Batches");

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

			} else if (reportType.getSelectionModel().getSelectedItem() == "Suppliers") {
				Region page;
				try {
					page = FXMLLoader.load(getClass().getResource("SuppliersReport.fxml"));
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

			} else if (reportType.getSelectionModel().getSelectedItem() == "Customers") {
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
