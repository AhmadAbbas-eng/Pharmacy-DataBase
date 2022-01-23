package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Relations.Employee;

public class MainPageController implements Initializable {

	@FXML
	private Label choosenButton;

	@FXML
	private Button EmployeesButton;

	@FXML
	private Button OrdersButton;

	@FXML
	private Button PaymentButton;

	@FXML
	private Button PatientsButton;

	@FXML
	private Button ProductsButton;

	@FXML
	private Button SupplierButton;

	@FXML
	private StackPane contentArea;

	@FXML
	private Button dashboardButton;

	@FXML
	private Button logOutButton;
	
	@FXML
	private Button reportButton;

	@FXML
	private Button sellButton;

	@FXML
	private Button dispose;
	
	@FXML
	private Button payingOff;

	@FXML
	private Label title;

	@FXML
	private Label userName;
	
	public void PayingOffOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Paying Off");
		Parent page = FXMLLoader.load(getClass().getResource("PayingOff.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(page);
	}

	public void DisposeOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Dispose");
		Parent page = FXMLLoader.load(getClass().getResource("dissipation.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(page);
	}
	
	public void ReportOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Reports");
		
		if (Employee.hasAccess()) {
			Parent page = FXMLLoader.load(getClass().getResource("Reports.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		} else {
			Parent page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		}
	}

	public void logOutAction(ActionEvent e) throws IOException {
		Stage stage = (Stage) logOutButton.getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setResizable(false);
		stage2.setScene(new Scene(root1));
		stage2.show();
	}

	public void SellOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("New Sell");

		Parent page = FXMLLoader.load(getClass().getResource("Sell.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(page);
	}
	
	public void CustomerOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Customers");

		Parent page = FXMLLoader.load(getClass().getResource("Customer.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(page);
	}

	public void DashboardOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Dashboard");

		Parent page = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(page);
	}

	public void ProductsOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Storage");

		Parent page = FXMLLoader.load(getClass().getResource("Product.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(page);
	}

	public void EmployeesOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Employees");
		if (Employee.hasAccess()) {
			Parent page = FXMLLoader.load(getClass().getResource("Employee.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		} else {
			Parent page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		}

	}

	public void SupplierOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Suppliers");
		if (Employee.hasAccess()) {
			Parent page = FXMLLoader.load(getClass().getResource("Supplier.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		} else {
			Parent page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		}

	}

	public void PaymentOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Payment");
		if (Employee.hasAccess()) {
			Parent page = FXMLLoader.load(getClass().getResource("Payment.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		} else {
			Parent page = FXMLLoader.load(getClass().getResource("notAvailable.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(page);
		}

	}

	public void OrdersOnAction(ActionEvent e) throws IOException {
		choosenButton.setText("Orders");
		Parent page = FXMLLoader.load(getClass().getResource("SupplierReceivedOrder.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(page);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		if (Employee.getCurrentID() != 0) {
			userName.setText(Employee.getEmployeeName());
		} else {
			userName.setText("");
		}

		Parent page;
		try {
			choosenButton.setText("Dashboard");
			page = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
			contentArea.getChildren().setAll(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}