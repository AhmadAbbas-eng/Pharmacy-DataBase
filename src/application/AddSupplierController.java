package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.*;

public class AddSupplierController implements Initializable {

	SupplierController caller;

	@FXML
	private ImageView addSupplier;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField addressTextField;

	@FXML
	private TextField duesTextField;

	@FXML
	private TextField emailTextField;

	@FXML
	private ListView<String> phoneList;

	@FXML
	private TextField phoneTextField;

	@FXML
	private ImageView addPhone;

	@FXML
	private ImageView deletePhone;

	public void phoneTextOnEnter(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			String phone = phoneTextField.getText();

			if (phoneList.getItems().contains(phone.replaceAll("-", ""))) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Phone Number Already Exists");
				alert.showAndWait();
			} else if ((phone.replaceAll("-", "")).matches("[0-9]{10}")) {
				phoneList.getItems().add(phone.replaceAll("-", ""));
				phoneTextField.setText("");
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Phone Number Must Contain 10 Digits");
				alert.showAndWait();
			}
		}
	}

	public void addPhoneOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addPhone.setEffect(effect);
		String phone = phoneTextField.getText();

		if (phoneList.getItems().contains(phone.replaceAll("-", ""))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Already Exists");
			alert.showAndWait();
		} else if ((phone.replaceAll("-", "")).matches("[0-9]{10}")) {
			phoneList.getItems().add(phone.replaceAll("-", ""));
			phoneTextField.setText("");
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Contain 10 Digits");
			alert.showAndWait();
		}
	}

	public void addPhoneOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addPhone.setEffect(effect);
	}

	public void addPhoneOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addPhone.setEffect(effect);
	}

	public void addPhoneOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addPhone.setEffect(effect);
	}

	public void addSupplierOnMousePressed() throws ClassNotFoundException, SQLException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addSupplier.setEffect(effect);
		Double dues = 0.0;
		String name = nameTextField.getText();
		String address = addressTextField.getText();
		String email = emailTextField.getText();

		Boolean checkDues = true;
		try {
			dues = Double.parseDouble(duesTextField.getText());
		} catch (NumberFormatException e) {
			checkDues = false;
		}

		if (!checkDues || name.isEmpty() || name.isBlank() || address.isEmpty() || address.isBlank() || email.isBlank()
				|| email.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText("Wrong Input Format");
			alert.setContentText("Name Must Consist of Alphabetical Characters\n" + "Dues Must Be A Real Number \n"
					+ "All Fields Must Be Filled");
			alert.showAndWait();

		} else if (phoneList.getItems().size() == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Be Added");
			alert.showAndWait();
		} else {
			Supplier.insertSupplier(name, address, email, dues, new ArrayList<>(phoneList.getItems()));
			Supplier.getData().add(new Supplier(Supplier.getMaxID(), name, address, email, dues,
					new ArrayList<>(phoneList.getItems())));
			Supplier.getDataList().add(new Supplier(Supplier.getMaxID(), name, address, email, dues,
					new ArrayList<>(phoneList.getItems())));
			nameTextField.setText("");
			addressTextField.setText("");
			duesTextField.setText("");
			emailTextField.setText("");
			phoneTextField.setText("");
			phoneList.getItems().clear();
		}
	}

	public void addSupplierOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addSupplier.setEffect(effect);
	}

	public void addSupplierOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addSupplier.setEffect(effect);
	}

	public void addSupplierOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addSupplier.setEffect(effect);
	}

	public void listOnEditCommit(ListView.EditEvent<String> editedPhone) {
		if (phoneList.getItems().contains(editedPhone.getNewValue().replaceAll("-", "")) && !editedPhone.getNewValue()
				.replaceAll("-", "").equals(phoneList.getItems().get(editedPhone.getIndex()))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Already Exists");
			alert.showAndWait();
		} else if ((editedPhone.getNewValue().replaceAll("-", "")).matches("[0-9]{10}")) {
			phoneList.getItems().set(editedPhone.getIndex(), editedPhone.getNewValue().replaceAll("-", ""));
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Contain 10 Digits");
			alert.showAndWait();
		}
	}

	public void deletePhoneOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		deletePhone.setEffect(effect);
		String phone = phoneList.getSelectionModel().getSelectedItem();
		phoneList.getItems().remove(phone);
	}

	public void deletePhoneOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deletePhone.setEffect(effect);
	}

	public void deletePhoneOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		deletePhone.setEffect(effect);
	}

	public void deletePhoneOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deletePhone.setEffect(effect);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

}