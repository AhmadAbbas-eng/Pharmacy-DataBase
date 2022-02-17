package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import Relations.Batch;
import Relations.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 * 
 */
public class AddBatchController {

	@FXML
	private Button addButton;

	@FXML
	private Button cancelButton;

	@FXML
	private DatePicker expiryDateDatePicker;

	@FXML
	private DatePicker productionDateDatePicker;

	private int productID = 1;

	private ReceiveOrdersController caller;

	/**
	 * Set the product for the batch
	 * 
	 * @param id     The ID of the product
	 * @param name   The name of the product
	 * @param caller The controller which receives orders
	 */
	public void setProduct(int id, ReceiveOrdersController caller) {
		setPid(id);
		this.caller = caller;		
	}

	public void cancelButton(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void addBatch(ActionEvent event) {

		// Check the values of text fields and assign entered data
		if (productionDateDatePicker.getValue() != null && expiryDateDatePicker.getValue() != null) {
			if (productionDateDatePicker.getValue().compareTo(expiryDateDatePicker.getValue()) >= 0
					|| productionDateDatePicker.getValue().compareTo(LocalDate.now()) > 0
					|| expiryDateDatePicker.getValue().compareTo(LocalDate.now()) <= 0) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Choose Reasonable Dates");
				alert.showAndWait();
			} else {

				// Check if the batch is already exists
				ArrayList<Batch> batch = Batch.getBatchData(Queries.queryResult(
						"select * from Batch " + " where Product_ID = ? " + " and batch_production_date = ? "
								+ " and batch_expiry_date = ? ;",
						new ArrayList<>(Arrays.asList(productID + "", productionDateDatePicker.getValue().toString(),
								expiryDateDatePicker.getValue().toString()))));

				// Insert data into the table
				if (batch.isEmpty()) {
					Batch.insertBatch(productID, productionDateDatePicker.getValue(), expiryDateDatePicker.getValue(),
							0);

					Stage stage = (Stage) addButton.getScene().getWindow();
					stage.close();
					caller.saveUpdates();
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
	
	public void setPid(int pid) {
		this.productID = pid;
	}

}