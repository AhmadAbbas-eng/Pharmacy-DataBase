package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import Relations.Employee;
import Relations.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 *
 */
public class ConfirmNewUserController {
	@FXML
	private Button cancelButton;

	@FXML
	private Button confirmButton;

	@FXML
	private TextField idTextField;

	@FXML
	private PasswordField passwordPasswordField;

	public void cancelButton(ActionEvent e) {

		try {
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
			Parent root1;
			root1 = (Parent) loader.load();
			Stage stage2 = new Stage();
			stage2.setResizable(false);
			stage2.setScene(new Scene(root1));
			stage2.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void confirm(ActionEvent event) {

		ArrayList<Employee> filteredList = new ArrayList<>();
		if (idTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false) {
			try {
				Integer.parseInt(idTextField.getText().trim());
				filteredList = Employee.getEmployeeData(
						Queries.queryResult("select * from Employee where Employee_ID = ? order by Employee_ID;",
								new ArrayList<>(Arrays.asList(idTextField.getText().trim()))));

				if (filteredList.isEmpty() == true) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("This User ID is Not Available!!");
					alert.showAndWait();
				} else {
					String name = Queries.queryResult("select employee_name from Employee where Employee_ID = ?",
							new ArrayList<>(Arrays.asList(idTextField.getText().trim()))).get(0).get(0);
					filteredList = Employee.getEmployeeData(Queries.queryResult(
							"select * from Employee where Employee_ID = ? "
									+ " and Employee_password = ? order by Employee_ID;",
							new ArrayList<>(Arrays.asList(idTextField.getText().trim(),
									Employee.encryptPassword(name, passwordPasswordField.getText())))));

					if (filteredList.isEmpty() == true) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("Wrong Password!!");
						alert.showAndWait();
					} else {
						filteredList = Employee.getEmployeeData(Queries.queryResult(
								"select * from Employee where Employee_ID = ? and isManager = "
										+ "'true' and Employee_password = ? order by Employee_ID;",
								new ArrayList<>(Arrays.asList(idTextField.getText().trim(),
										Employee.encryptPassword(name, passwordPasswordField.getText())))));

						if (filteredList.isEmpty() == true) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle(null);
							alert.setHeaderText(null);
							alert.setContentText("You Have To Be The Manager To Add Users!!");
							alert.showAndWait();
						} else {
							Stage stage1 = (Stage) confirmButton.getScene().getWindow();
							stage1.close();
							FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
							Parent root1 = (Parent) loader.load();
							Stage stage2 = new Stage();
							stage2.setScene(new Scene(root1));
							stage2.setResizable(false);
							stage2.show();
						}
					}

				}
				idTextField.clear();
				passwordPasswordField.clear();
			} catch (NumberFormatException | IOException e1) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(null);
				alert.setHeaderText(null);
				alert.setContentText("Employee ID Consist Of Numbers Only! ");
				alert.showAndWait();
				idTextField.clear();
				passwordPasswordField.clear();
			}
		} else if (filteredList.isEmpty() == true || passwordPasswordField.getText().isBlank() == true) {
			idTextField.clear();
			passwordPasswordField.clear();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter Your ID & Password");
			alert.showAndWait();
		}
	}
}
