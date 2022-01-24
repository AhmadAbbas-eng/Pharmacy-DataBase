package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ReportController implements Initializable{
    @FXML
    private Button choose;

    @FXML
    private StackPane display;
    
    @FXML
    private TextField path;

    @FXML
    private ComboBox<String> reportType;

    @FXML
    private Button save;

    public void saveOnAction(ActionEvent e) {
    	
    }
	ObservableList<String> reports = FXCollections.observableArrayList("Payment","Suppliers","Cheques","Disposal","Customers", 
			"Products Information", "Product Batches");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		reportType.setItems(reports);
		
		reportType.setOnAction(e -> {
			if(reportType.getSelectionModel().getSelectedItem() == "Payment") {
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
				
			}else if(reportType.getSelectionModel().getSelectedItem() == "Suppliers") {
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
				
			}else if(reportType.getSelectionModel().getSelectedItem() == "Cheques") {
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
				
			}else if(reportType.getSelectionModel().getSelectedItem() == "Disposal") {
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
				
			}else if(reportType.getSelectionModel().getSelectedItem() == "Customers") {
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
				
			}else if(reportType.getSelectionModel().getSelectedItem() == "Products Information") {
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
				
			}else if(reportType.getSelectionModel().getSelectedItem() == "Product Batches") {
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
				
			}else {

					display.getChildren().removeAll();
		
				}
		});
	}
}
