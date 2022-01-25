package application;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.Batch;
import Relations.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AddBatchController implements Initializable {

	@FXML
	private Button addButton;

	@FXML
	private Button cancelButton;

	@FXML
	private DatePicker expiryDateDatePicker;

	@FXML
	private DatePicker productionDateDatePicker;

	@FXML
	private Label productNameLabel;

	private String product;

	private int productID=1;

	ListView<Batch> batchlist;

	private ReceiveOrdersController caller;

	public void setProduct(int id, String name, ReceiveOrdersController caller) {
		setPid(id);
		this.product = name;
		this.caller = caller;
	}

	public void cancelButton(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void addBatch(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		if (productionDateDatePicker.getValue() != null && expiryDateDatePicker.getValue() != null) {
			if (productionDateDatePicker.getValue().compareTo(expiryDateDatePicker.getValue()) > 0 || productionDateDatePicker.getValue().compareTo(expiryDateDatePicker.getValue()) == 0
					|| productionDateDatePicker.getValue().compareTo(LocalDate.now()) > 0) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Choose Reasonable Dates");
				alert.showAndWait();
			} else {
				ArrayList<Batch> batch = Batch.getBatchData(Queries.queryResult(
						"select * from Batch " + " where Product_ID = ? " + " and batch_production_date = ? "
								+ " and batch_expiry_date = ? ;",
						new ArrayList<>(
								Arrays.asList(productID + "", productionDateDatePicker.getValue().toString(), expiryDateDatePicker.getValue().toString()))));

				if (batch.isEmpty()) {
					Batch.insertBatch(productID, productionDateDatePicker.getValue(), expiryDateDatePicker.getValue(), 0);
					Stage stage = (Stage) addButton.getScene().getWindow();
					stage.close();
					caller.saveUpdates();
					// -----------------------------

				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("This Batch Already Exists");
					alert.showAndWait();
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Choose Batch Dates");
			alert.showAndWait();
		}
	}

	public int getPid() {
		return productID;
	}

	public void setPid(int pid) {
		this.productID = pid;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (product == null) {
			productNameLabel.setText("");
		} else {
			productNameLabel.setText(product);
		}

	}

}
