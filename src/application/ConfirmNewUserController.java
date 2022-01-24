package application;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfirmNewUserController {
	@FXML
	private Button cancel;

	@FXML
	private Button confirm;

	@FXML
	private TextField id;

	@FXML
	private PasswordField password;

	public void cancelButton(ActionEvent e) throws IOException {

		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setResizable(false);
		stage2.setScene(new Scene(root1));
		stage2.show();
	}

	public void confirm(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, ParseException {

		ArrayList<Employee> filteredList = new ArrayList<>();
		if (id.getText().isBlank() == false && password.getText().isBlank() == false) {
			try {
			Integer.parseInt(id.getText());
			filteredList = Employee.getEmployeeData(
					Queries.queryResult("select * from Employee where Employee_ID = ? order by Employee_ID;",
							new ArrayList<>(Arrays.asList(id.getText()))));
			if (filteredList.isEmpty() == true) {

				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(null);
				alert.setHeaderText(null);
				alert.setContentText("This User ID is Not Available!!");
				alert.showAndWait();

			} else {
				filteredList = Employee.getEmployeeData(Queries.queryResult(
						"select * from Employee where Employee_ID = ? "
								+ " and Employee_password = ? order by Employee_ID;",
						new ArrayList<>(Arrays.asList(id.getText(), password.getText()))));
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
							new ArrayList<>(Arrays.asList(id.getText(), password.getText()))));
					if (filteredList.isEmpty() == true) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("You Have To Be The Manager To Add Users!!");
						alert.showAndWait();
					} else {
						Stage stage1 = (Stage) confirm.getScene().getWindow();
						stage1.close();
						Stage stage = (Stage) confirm.getScene().getWindow();
						stage.close();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
						Parent root1 = (Parent) loader.load();
						Stage stage2 = new Stage();
						stage2.setScene(new Scene(root1));
						stage2.setResizable(false);

						stage2.show();
					}
				}
			

			}
			id.clear();
			password.clear();
			} catch (NumberFormatException e1) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(null);
				alert.setHeaderText(null);
				alert.setContentText("Employee ID Consist Of Numbers Only! ");
				alert.showAndWait();
				id.clear();
				password.clear();
			}
		} else if (filteredList.isEmpty() == true || password.getText().isBlank() == true) {

			id.clear();
			password.clear();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter Your ID & Password");
			alert.showAndWait();
		}

	}

}
