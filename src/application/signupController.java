package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.Employee;
import Relations.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class signupController implements Initializable {

	int flag = 0;

	@FXML
	private Button addNewUserButton;

	@FXML
	private Button cancelButton;

	@FXML
	private DatePicker dateOfWorkPicker;

	@FXML
	private TextField hourlyPaidTextField;

	@FXML
	private TextField employeeNameTextField;

	@FXML
	private TextField nationalIDTextField;

	@FXML
	private CheckBox isManager;

	@FXML
	private PasswordField passwordTextField;

	@FXML
	private ListView<String> phoneList;

	@FXML
	private ImageView addPhone;

	@FXML
	private ImageView deletePhone;

	@FXML
	private TextField phoneTextField;

	public void listOnEditCommit(ListView.EditEvent<String> editedPhone) {
		if (editedPhone.getNewValue().matches("[0-9]{10}")) {
			phoneList.getItems().set(editedPhone.getIndex(), editedPhone.getNewValue());
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Contain 10 Digits");
			alert.showAndWait();
		}
	}

	public void textOnEnter(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			String phone = phoneTextField.getText();
			if (phone.matches("[0-9]{10}")) {
				phoneList.getItems().add(phone);
				phoneTextField.setText("");
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input");
				alert.setHeaderText(null);
				alert.setContentText("Phone Number Must Contain 10 Digits");
				alert.showAndWait();
			}
		}
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
		if (phone.matches("[0-9]{10}")) {
			phoneList.getItems().add(phone);
			phoneTextField.setText("");
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Contain 10 Digits");
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

	public void cancelButton(ActionEvent e) throws IOException {

		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void addUser(ActionEvent e) throws IOException, ClassNotFoundException, SQLException, ParseException {

		boolean flag = false;

		if (employeeNameTextField.getText().isBlank() == false && nationalIDTextField.getText().isBlank() == false
				&& dateOfWorkPicker.getValue().toString().isBlank() == false
				&& hourlyPaidTextField.getText().isBlank() == false && passwordTextField.getText().isBlank() == false) {
			if (nationalIDTextField.getText().matches("[0-9]{9}")) {
				ArrayList<Employee> List = new ArrayList<>();
				List = Employee.getEmployeeData(Queries.queryResult(
						"select * from Employee where Employee_National_ID = ? order by Employee_ID;",
						new ArrayList<>(Arrays.asList(nationalIDTextField.getText()))));
				if (List.isEmpty() == false) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Wrong Input");
					alert.setHeaderText(null);
					alert.setContentText(" Employee Already Exists!");
					alert.showAndWait();
				} else {
					try {
						Double num = Double.parseDouble(hourlyPaidTextField.getText());
						if (num < 0) {
							flag = true;
						}
					} catch (NumberFormatException e1) {
						flag = true;
					}
					String str = employeeNameTextField.getText().replaceAll("\\s", "");
					if (str.matches("[a-zA-Z]+") == false) {
						flag = true;
					}

					if (flag) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Wrong Input");
						alert.setHeaderText(null);
						alert.setContentText("Wrong Input Format!");
						alert.showAndWait();
					} else {
						ArrayList<String> phones = new ArrayList<>(phoneList.getItems());

						String ismanager = "false";
						if (isManager.isSelected()) {
							ismanager = "true";
						}

						Employee temp = new Employee(Employee.getMaxID(), employeeNameTextField.getText(),
								nationalIDTextField.getText(), dateOfWorkPicker.getValue(),
								Double.parseDouble(hourlyPaidTextField.getText()), phones, passwordTextField.getText(),
								ismanager, "true");
						Employee.getData().add(temp);

						Employee.insertEmployee(employeeNameTextField.getText(), nationalIDTextField.getText(),
								dateOfWorkPicker.getValue(), Double.parseDouble(hourlyPaidTextField.getText()), phones,
								passwordTextField.getText(), ismanager, "true");

						Stage stage = (Stage) addNewUserButton.getScene().getWindow();
						stage.close();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
						Parent root1 = (Parent) loader.load();
						Stage stage2 = new Stage();
						stage2.setScene(new Scene(root1));
						stage2.show();
					}
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input");
				alert.setHeaderText(null);
				alert.setContentText("Wrong National ID!!");
				alert.showAndWait();
			}

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Empty Fields");
			alert.setHeaderText(null);
			alert.setContentText("Fields Need To BE Full!!");
			alert.showAndWait();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		phoneList.setEditable(true);
		phoneList.setCellFactory(TextFieldListCell.forListView());
	}

}