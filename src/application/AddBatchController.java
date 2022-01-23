package application;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.Batch;
import Relations.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

	private String pid, product;

	@FXML
	private Label warning;

	ListView<Batch> batchlist;

	public void setPID(String id, String name) {
		this.pid = id;
		this.product = name;
	}

	public void cancelButton(ActionEvent event) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	public void addBatch(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		if (pdate.getValue() != null && edate.getValue() != null) {
			warning.setText("");
			if (pdate.getValue().compareTo(edate.getValue()) > 0 || pdate.getValue().compareTo(edate.getValue()) == 0) {
				warning.setText("Choose Reasonable Dates");
			} else {
				ArrayList<Batch> batch = Batch.getBatchData(Queries.queryResult(
						"select * from Batch " + " where Product_ID = ? " + " and batch_production_date = ? "
								+ " and batch_expiry_date = ? ;",
						new ArrayList<>(
								Arrays.asList(Integer.parseInt(pid)+ "", pdate.getValue().toString(), edate.getValue().toString()))));

				if (batch.isEmpty() == true) {
					Batch.insertBatch(Integer.parseInt(pid), pdate.getValue(), edate.getValue(), 0);
					warning.setText("");
					Stage stage = (Stage) add.getScene().getWindow();
					stage.close();

				} else {
					warning.setText("This Batch Already Exists");
				}
			}
		} else
			warning.setText("Choose Batch Dates");
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
