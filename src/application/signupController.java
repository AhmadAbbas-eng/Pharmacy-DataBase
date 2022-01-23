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
	private Label warning;

	@FXML
	private Button add;

	@FXML
	private Button cancel;

	@FXML
	private DatePicker date;

	@FXML
	private TextField hourlypaid;

	@FXML
	private TextField name;

	@FXML
	private TextField nid;

	@FXML
	private TextField phone;

	@FXML
	private CheckBox isManager;

	@FXML
	private PasswordField password;

	@FXML
	private Button ccancel;

	@FXML
	private Button yes;

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

	public void listOnEditCommit(ListView.EditEvent<String> editedPhone) {
		if (editedPhone.getNewValue().matches("[0-9]{10}")) {
			phoneList.getItems().set(editedPhone.getIndex(), editedPhone.getNewValue());
			warning.setText("");
		} else {
			warning.setText("Phone Number Must Contain 10 Digits");
		}
	}

	public void textOnEnter(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			String phone = phoneTextField.getText();
			if (phone.matches("[0-9]{10}")) {
				phoneList.getItems().add(phone);
				phoneTextField.setText("");
				warning.setText("");
			} else {
				warning.setText("Phone Number Must Contain 10 Digits");
			}
		}
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
		warning.setText("");

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
			warning.setText("");
		} else {
			warning.setText("Phone Number must contain 10 digits");
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

		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void addUser(ActionEvent e) throws IOException, ClassNotFoundException, SQLException, ParseException {

		boolean flag = false;

		if (name.getText().isBlank() == false && nid.getText().isBlank() == false
				&& date.getValue().toString().isBlank() == false && hourlypaid.getText().isBlank() == false
				&& password.getText().isBlank() == false) {
			if (nid.getText().matches("[0-9]{9}")) {
				ArrayList<Employee> List = new ArrayList<>();
				List = Employee.getEmployeeData(Queries.queryResult(
						"select * from Employee where Employee_National_ID = ? order by Employee_ID;",
						new ArrayList<>(Arrays.asList(nid.getText()))));
				if (List.isEmpty() == false) {
					warning.setText("This Employee Already Exists!!");
				} else {
					try {
						Double num = Double.parseDouble(hourlypaid.getText());
						if (num < 0) {
							flag = true;
						}
					} catch (NumberFormatException e1) {
						flag = true;
					}
					String str = name.getText().replaceAll("\\s", "");
					if (str.matches("[a-zA-Z]+") == false) {
						flag = true;
					}

					if (flag) {
						warning.setText("Wrong Input Format!");
					} else {
						ArrayList<String> phones = new ArrayList<>(phoneList.getItems());

						String ismanager = "false";
						if (isManager.isSelected()) {
							ismanager = "true";
						}

						Employee temp = new Employee(Employee.getMaxID(), name.getText(), nid.getText(),
								date.getValue(), Double.parseDouble(hourlypaid.getText()), phones, password.getText(),
								ismanager, "true");
						Employee.getData().add(temp);

						Employee.insertEmployee(name.getText(), nid.getText(), date.getValue(),
								Double.parseDouble(hourlypaid.getText()), phones, password.getText(), ismanager,
								"true");

						Stage stage = (Stage) add.getScene().getWindow();
						stage.close();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
						Parent root1 = (Parent) loader.load();
						Stage stage2 = new Stage();
						stage2.setScene(new Scene(root1));
						stage2.show();
					}
				}
			} else {
				warning.setText("Wrong National ID!!");
			}

		} else {
			warning.setText("Fields Need To BE Full!!");
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		phoneList.setEditable(true);
		phoneList.setCellFactory(TextFieldListCell.forListView());
	}

}