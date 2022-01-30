package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import Relations.*;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 *
 */
public class SupplierPhonesNoAccessController implements Initializable {

	@FXML
	private ListView<String> phoneList;

	@FXML
	private Label title;

	public void setPhoneNumbers(Supplier supplier) {
		title.setText("Supplier With ID " + supplier.getID() + " Phones");
		phoneList.setItems(FXCollections.observableArrayList(supplier.getPhones()));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

}