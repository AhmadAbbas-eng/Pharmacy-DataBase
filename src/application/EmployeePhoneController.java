package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.*;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 */
public class EmployeePhoneController implements Initializable {

	private Employee employee;

	@FXML
	private ListView<String> phoneList;

	@FXML
	private ImageView addPhone;

	@FXML
	private ImageView deletePhone;

	@FXML
	private ImageView savePhone;

	@FXML
	private Label title;

	@FXML
	private TextField phoneTextField;

	@FXML
	private Label savedLabel;
	@FXML
	private ImageView savedIcon;

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

	public void listOnEditCommit(ListView.EditEvent<String> editedPhone) {
		if (phoneList.getItems().contains(editedPhone.getNewValue().replaceAll("(-|\\s)", "").replaceAll(" ", ""))
				&& !editedPhone.getNewValue().replaceAll("(-|\\s)", "").replaceAll(" ", "")
						.equals(phoneList.getItems().get(editedPhone.getIndex()))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Already Exists");
			((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
			alert.showAndWait();
		} else if ((editedPhone.getNewValue().replaceAll("(-|\\s)", "").replaceAll(" ", "")).matches("[0-9]{10}")) {
			phoneList.getItems().set(editedPhone.getIndex(),
					editedPhone.getNewValue().replaceAll("(-|\\s)", "").replaceAll(" ", ""));
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Contain 10 Digits");
			((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
			alert.showAndWait();
		}
	}

	public void textOnEnter(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			String phone = phoneTextField.getText();
			if (phoneList.getItems().contains(phone.replaceAll("(-|\\s)", "").replaceAll(" ", ""))) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Phone Number Already Exists");
				((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
				alert.showAndWait();
			} else if ((phone.replaceAll("(-|\\s)", "").replaceAll(" ", "")).matches("[0-9]{10}")) {
				phoneList.getItems().add(phone.replaceAll("(-|\\s)", "").replaceAll(" ", ""));
				phoneTextField.setText("");
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
				alert.showAndWait();
			}
		}
	}

	public void saveOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		savePhone.setEffect(effect);
		employee.clearEmployeePhones();
		Employee.insertEmployeePhone(new ArrayList<>(phoneList.getItems()), employee.getID());
		Employee.getEmployeePhone(employee);
		showAndFade(savedLabel);
		showAndFade(savedIcon);
	}

	public void saveOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		savePhone.setEffect(effect);
	}

	public void saveOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		savePhone.setEffect(effect);
	}

	public void saveOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		savePhone.setEffect(effect);
	}

	public void deleteOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		deletePhone.setEffect(effect);
		String phone = phoneList.getSelectionModel().getSelectedItem();
		phoneList.getItems().remove(phone);
	}

	public void deleteOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deletePhone.setEffect(effect);
	}

	public void deleteOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		deletePhone.setEffect(effect);
	}

	public void deleteOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deletePhone.setEffect(effect);
	}

	public void addOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addPhone.setEffect(effect);
		String phone = phoneTextField.getText();
		if (phoneList.getItems().contains(phone.replaceAll("(-|\\s)", "").replaceAll(" ", ""))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Already Exists");
			((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
			alert.showAndWait();
		} else if (phone.replaceAll("(-|\\s)", "").replaceAll(" ", "").matches("[0-9]{10}")) {
			phoneList.getItems().add(phone.replaceAll("(-|\\s)", ""));
			phoneTextField.setText("");
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
			alert.showAndWait();
		}
	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addPhone.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addPhone.setEffect(effect);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addPhone.setEffect(effect);
	}

	public void setPhoneNumbers(Employee employee) {
		this.employee = employee;
		title.setText(employee.getName() + " Phones");
		phoneList.setItems(FXCollections.observableArrayList(employee.getPhones()));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		savedLabel.setOpacity(0);
		savedIcon.setOpacity(0);

		phoneList.setEditable(true);
		phoneList.setCellFactory(TextFieldListCell.forListView());
	}
}