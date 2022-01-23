package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.Employee;
import Relations.Queries;

public class loginController implements Initializable {

	@FXML
	private Button cancel;

	@FXML
	private Button login;

	@FXML
	private TextField employeeid;

	@FXML
	private Button signup;

	@FXML
	private Label warning;

	@FXML
	private PasswordField password;

	public void logIn(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		try {
			ArrayList<Employee> filteredList = new ArrayList<>();
			if (employeeid.getText().isBlank() == false && password.getText().isBlank() == false) {
				try {
					int  num = Integer.parseInt(employeeid.getText());
					filteredList = Employee
							.getEmployeeData(Queries.queryResult("select * from Employee where Employee_ID = ? and isActive = 'true';",
									new ArrayList<>(Arrays.asList(employeeid.getText()))));
					if (filteredList.isEmpty() == true) {
						warning.setText("This User Is Not Available");

					} else {
						filteredList = Employee.getEmployeeData(Queries.queryResult(
								"select * from Employee where Employee_ID = ? and Employee_Password= ?;",
								new ArrayList<>(Arrays.asList(employeeid.getText(), password.getText()))));
						if (filteredList.isEmpty() == true) {
							warning.setText("Wrong Password!!");
						} else {
							
							Employee.setCurrentID(num);
							Employee.setAccess(Boolean.parseBoolean(filteredList.get(0).isManager()));
							Employee.setEmployeeName(filteredList.get(0).getName());
							Stage stage = (Stage) login.getScene().getWindow();
							stage.close();
							FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
							Parent root1 = (Parent) loader.load();
							Stage stage2 = new Stage();
							stage2.setScene(new Scene(root1));
							stage2.show();
						}

					}
					employeeid.clear();
					password.clear();
				} catch (NumberFormatException e1) {
					warning.setText("Employee ID Consist Of Numbers Only! ");
					employeeid.clear();
					password.clear();
				}
				
			} else if (filteredList.isEmpty() == true || password.getText().isBlank() == true) {
				warning.setText("Please Enter Your ID and Password");
				employeeid.clear();
				password.clear();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void signUp(ActionEvent event) {
		try {
			Stage stage1 = (Stage) signup.getScene().getWindow();
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
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
