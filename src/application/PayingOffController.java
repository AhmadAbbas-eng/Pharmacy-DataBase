package application;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.*;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PayingOffController implements Initializable {

	@FXML
	private Button approvePaymentButton;

	@FXML
	private Label customerNIDLabel;

	@FXML
	private TableView<Customer> customerTable;

	@FXML
	private Label dateLabel;

	@FXML
	private TableColumn<Customer, Double> debtColumn;

	@FXML
	private ComboBox<String> fieldSelector;

	@FXML
	private TableColumn<Customer, String> nameColumn;

	@FXML
	private TableColumn<Customer, String> nidColumn;

	@FXML
	private ImageView paidIcon;

	@FXML
	private Label paidLabel;

	@FXML
	private TextField paidTextField;

	@FXML
	private TextField searchBox;

	@FXML
	private Button selectCustomerButton;
	String stringToSearch = "";

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

	public void filterList() {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + stringToSearch + "%");
		ArrayList<Customer> filteredList = new ArrayList<>();
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			try {
				filteredList = Customer.getCustomerData(Queries
						.queryResult("select * from Customer where Customer_Debt>0 order by Customer_name;", null));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "National ID") {
			try {
				filteredList = Customer
						.getCustomerData(Queries.queryResult("select * from Customer " + " where Customer_NID like ?"
								+ " and Customer_Debt>0 " + " order by Customer_name;", parameters));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Name") {
			try {
				filteredList = Customer
						.getCustomerData(Queries.queryResult("select * from Customer " + " where Customer_Name like ? "
								+ " and Customer_Debt>0 " + " order by Customer_name;", parameters));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
				parameters.add("%" + stringToSearch + "%");
				filteredList = Customer.getCustomerData(Queries
						.queryResult("select * from Customer where Customer_Name like ? " + " or Customer_NID like ? "
								+ " and Customer_Debt>0 " + " order by Customer_name;", parameters));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		customerTable.setItems(FXCollections.observableArrayList(filteredList));
		customerTable.refresh();
	}

	public void selectCustomerOnAction() {
		if (customerTable.getSelectionModel().getSelectedItem() != null) {
			customerNIDLabel.setText(customerTable.getSelectionModel().getSelectedItem().getNID());
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Customer Must Be Chosen First");
			alert.setContentText("Choose A Customer From The Table");
			alert.showAndWait();
		}
	}

	public void approveOnAction() {
		String paidStr = paidTextField.getText();
		double paidAmount = 0.0;
		boolean checkPaid = true;
		try {
			paidAmount = Double.parseDouble(paidStr);
		} catch (NumberFormatException e) {
			checkPaid = false;
		}
		if (customerNIDLabel.getText().equals("-")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("Customer Must Be Chosen");
			alert.showAndWait();
		} else if (paidStr == null || paidStr.isBlank() || paidStr.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Paid Amount Must Be Entered");
			alert.showAndWait();

		} else if (!checkPaid || paidAmount <= 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Paid Amount Must Be A Positive Real Number");
			alert.showAndWait();

		} else {

			try {
				Queries.queryUpdate(
						"Insert into Income"
								+ " (Income_amount, Income_Date, Employee_ID, Customer_NID) Values(?, ?, ?, ?);",
						new ArrayList<>(Arrays.asList(paidAmount + "", dateLabel.getText(),
								Employee.getCurrentID() + "", customerNIDLabel.getText())));
				Queries.queryUpdate("Update Customer set Customer_Debt=Customer_Debt-? where Customer_NID=? ;",
						new ArrayList<>(Arrays.asList(paidStr, customerNIDLabel.getText())));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			showAndFade(paidIcon);
			showAndFade(paidLabel);
			customerNIDLabel.setText("-");
			paidTextField.setText("");
			filterList();
		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		paidIcon.setOpacity(0);
		paidLabel.setOpacity(0);
		dateLabel.setText(LocalDate.now().toString());

		fieldSelector.setItems(FXCollections.observableArrayList("-Specify Field-", "National ID", "Name"));
		nidColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("NID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		debtColumn.setCellValueFactory(new PropertyValueFactory<Customer, Double>("debt"));

		filterList();

		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch = newValue;
			filterList();
		});

	}

}
