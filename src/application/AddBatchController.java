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
	private Button add;

	@FXML
	private Button cancel;

	@FXML
	private DatePicker edate;

	@FXML
	private DatePicker pdate;

	@FXML
	private Label pname;

	private String product;

	private int pid=1;

	ListView<Batch> batchlist;

	private ReceiveOrdersController caller;

	public void setProduct(int id, String name, ReceiveOrdersController caller) {
		setPid(id);
		this.product = name;
		this.caller = caller;
	}

	public void cancelButton(ActionEvent event) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	public void addBatch(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		if (pdate.getValue() != null && edate.getValue() != null) {
			if (pdate.getValue().compareTo(edate.getValue()) > 0 || pdate.getValue().compareTo(edate.getValue()) == 0
					|| pdate.getValue().compareTo(LocalDate.now()) > 0) {
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
								Arrays.asList(pid + "", pdate.getValue().toString(), edate.getValue().toString()))));

				if (batch.isEmpty()) {
					Batch.insertBatch(pid, pdate.getValue(), edate.getValue(), 0);
					Stage stage = (Stage) add.getScene().getWindow();
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
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (product == null) {
			pname.setText("");
		} else {
			pname.setText(product);
		}

	}

}
