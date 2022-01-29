package application;

import javafx.application.Platform;
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
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import Relations.Employee;
import Relations.Queries;

/**
 * 
 * @version 27 January 2022
 * @author Loor Sawalhi
 */
public class LoginController {

	@FXML
	private Button cancelButton;

	@FXML
	private Button logInButton;

	@FXML
	private TextField employeeIDTextField;

	@FXML
	private Button signUpButton;

	@FXML
	private PasswordField passwordTextField;

	public void logIn(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		try {
			ArrayList<Employee> filteredList = new ArrayList<>();
			if (employeeIDTextField.getText().isBlank() == false && passwordTextField.getText().isBlank() == false) {
				try {
					int num = Integer.parseInt(employeeIDTextField.getText());
					filteredList = Employee.getEmployeeData(
							Queries.queryResult("select * from Employee where Employee_ID = ? and isActive = 'true';",
									new ArrayList<>(Arrays.asList(employeeIDTextField.getText()))));

					if (filteredList.isEmpty() == true) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Wrong Input");
						alert.setHeaderText(null);
						alert.setContentText("User Is Not Available");
						alert.showAndWait();

					} else {
						filteredList = Employee.getEmployeeData(Queries
								.queryResult("select * from Employee where Employee_ID = ? and Employee_Password= ?;",
										new ArrayList<>(Arrays.asList(employeeIDTextField.getText(),
												Employee.encryptPassword(Queries.queryResult(
														"select employee_name from Employee where Employee_ID = ?;",
														new ArrayList<>(Arrays.asList(employeeIDTextField.getText())))
														.get(0).get(0), passwordTextField.getText())))));

						if (filteredList.isEmpty() == true) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Wrong Input");
							alert.setHeaderText(null);
							alert.setContentText("Wrong Password");
							alert.showAndWait();
						} else {

							Employee.setCurrentID(num);
							Employee.setAccess(Boolean.parseBoolean(filteredList.get(0).isManager()));
							Employee.setEmployeeName(filteredList.get(0).getName());
							Stage stage = (Stage) logInButton.getScene().getWindow();
							stage.close();
							FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
							Parent root1 = (Parent) loader.load();
							Stage stage2 = new Stage();
							stage2.setScene(new Scene(root1, 1100, 620));
							stage2.setMinWidth(1110);
							stage2.setMinHeight(650);

							stage2.setOnCloseRequest(e -> {
								// Platform.setImplicitExit(false);
								Platform.exit();
								System.exit(0);
							});
							stage2.show();

						}

					}
					employeeIDTextField.clear();
					passwordTextField.clear();
				} catch (NumberFormatException e1) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Wrong Input");
					alert.setHeaderText(null);
					alert.setContentText("Employee ID Consist Of Numbers Only!");
					alert.showAndWait();
					employeeIDTextField.clear();
					passwordTextField.clear();
				}

			} else if (filteredList.isEmpty() == true || passwordTextField.getText().isBlank() == true) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Empty Fields");
				alert.setHeaderText(null);
				alert.setContentText("Please Enter Your ID and Password");
				alert.showAndWait();
				employeeIDTextField.clear();
				passwordTextField.clear();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void signUp(ActionEvent event) {
		try {
			Stage stage1 = (Stage) signUpButton.getScene().getWindow();
			stage1.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmNewUser.fxml"));
			Parent root1 = (Parent) loader.load();
			Stage stage2 = new Stage();
			stage2.setScene(new Scene(root1));
			stage2.setResizable(false);
			stage2.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void cancelButton(ActionEvent e) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
}
