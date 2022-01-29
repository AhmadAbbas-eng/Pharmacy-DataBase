package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL; 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Relations.*;

/**
 * 
 * @version 27 January 2022
 * @author Ahmad Abbas
 */
public class EmployeeController implements Initializable {

	@FXML
	private TableView<Employee> employeeTable;

	@FXML
	private TableColumn<Employee, Double> hourlyPaidColumn;

	@FXML
	private TableColumn<Employee, String> idColumn;

	@FXML
	private TableColumn<Employee, String> isActiveColumn;

	@FXML
	private TableColumn<Employee, String> isManagerColumn;

	@FXML
	private TableColumn<Employee, String> nameColumn;

	@FXML
	private TableColumn<Employee, String> nidColumn;

	@FXML
	private TableColumn<Employee, String> passwordColumn;

	@FXML
	private TableColumn<Employee, LocalDate> dateOfWorkColumn;

	@FXML
	private ImageView editButton;

	@FXML
	private ComboBox<String> fieldSelector;

	@FXML
	private TextField searchBox;

	@FXML
	private Button showInfoButton;

	@FXML
	private Button showWorkButton;

	@FXML
	private CheckBox isActive;

	@FXML
	private CheckBox isManager;

	private FXMLLoader editLoader;
	private String stringToSearch = "";

	public void showWorkOnAction() {
		if (employeeTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Employee Must Be Chosen First");
			alert.setContentText("Choose An Employee From The Table");
			alert.showAndWait();
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeWorkedHours.fxml"));
			Parent workedHours = (Parent) loader.load();
			EmployeeWorkedHoursController show = loader.getController();
			show.setEmployee(employeeTable.getSelectionModel().getSelectedItem());
			Stage workedHoursStage = new Stage();
			workedHoursStage.setAlwaysOnTop(true);
			Scene scene = new Scene(workedHours);
			workedHoursStage.setScene(scene);
			workedHoursStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showInfoOnAction() {
		if (employeeTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Employee Must Be Chosen First");
			alert.setContentText("Choose An Employee From The Table");
			alert.showAndWait();
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeePhone.fxml"));
			Parent contactInfo = (Parent) loader.load();
			EmployeePhoneController show = loader.getController();
			show.setPhoneNumbers(employeeTable.getSelectionModel().getSelectedItem());
			Stage contactInfoStage = new Stage();
			contactInfoStage.setAlwaysOnTop(true);
			Scene scene = new Scene(contactInfo, 500, 500);
			contactInfoStage.setScene(scene);
			contactInfoStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		editButton.setEffect(effect);
		try {
			editLoader = new FXMLLoader(getClass().getResource("EmployeeEdit.fxml"));
			Parent editPane = (Parent) editLoader.load();
			EmployeeEditController edit = editLoader.getController();
			edit.setRow(employeeTable.getSelectionModel().getSelectedItem(), this);
			Scene scene = new Scene(editPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage editStage = new Stage();
			editStage.initModality(Modality.APPLICATION_MODAL);
			editStage.setResizable(false);
			editStage.setScene(scene);
			editStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		editButton.setEffect(effect);
	}

	public void editOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		editButton.setEffect(effect);
	}

	public void editOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		editButton.setEffect(effect);
	}

	public void saveEdits() {
		employeeTable.setItems(Employee.getDataList());
	}

	public void filterList() {
		String isActiveCondition = "";
		String isManagerCondition = "";
		if (isActive.isSelected()) {
			isActiveCondition = " and isActive='true' ";
		}
		if (isManager.isSelected()) {
			isManagerCondition = " and isManager='true' ";
		}
		ArrayList<Employee> filteredList = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + stringToSearch + "%");
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			filteredList = Employee
					.getEmployeeData(Queries.queryResult("select * from Employee where Employee_ID<>-1 "
							+ isActiveCondition + isManagerCondition + " order by Employee_ID;", null));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "National ID") {
			filteredList = Employee
					.getEmployeeData(
							Queries.queryResult(
									"select * from Employee where Employee_ID<>-1 and Employee_National_ID like ? "
											+ isActiveCondition + isManagerCondition + " order by Employee_ID;",
									null));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Name") {
			filteredList = Employee
					.getEmployeeData(Queries.queryResult(
							"select * from Employee where Employee_ID<>-1 and Employee_Name like ? "
									+ isActiveCondition + isManagerCondition + " order by Employee_ID;",
							parameters));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "ID") {
			filteredList = Employee
					.getEmployeeData(Queries.queryResult(
							"select * from Employee where Employee_ID<>-1 and Employee_ID like ? "
									+ isActiveCondition + isManagerCondition + " order by Employee_ID;",
							parameters));

		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Date of Work") {
			filteredList = Employee
					.getEmployeeData(Queries.queryResult(
							"select * from Employee where Employee_ID<>-1 and Employee_Date_Of_Work like ? "
									+ isActiveCondition + isManagerCondition + " order by Employee_ID;",
							parameters));

		} else {
			while (parameters.size() < 4) {
				parameters.add("%" + stringToSearch + "%");
			}
			filteredList = Employee.getEmployeeData(Queries.queryResult("select * from Employee "
					+ " where (Employee_Name like ? or Employee_National_ID  like ? or Employee_ID like ? "
					+ " or Employee_Date_Of_Work like ? ) " + isActiveCondition + isManagerCondition
					+ " order by Employee_ID;", parameters));
		}
		employeeTable.setItems(FXCollections.observableArrayList(filteredList));
		employeeTable.refresh();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fieldSelector.setItems(
				FXCollections.observableArrayList("-Specify Field-", "National ID", "Name", "ID", "Date of Work"));

		idColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("ID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		nidColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("NID"));
		dateOfWorkColumn.setCellValueFactory(new PropertyValueFactory<Employee, LocalDate>("dateOfWork"));
		hourlyPaidColumn.setCellValueFactory(new PropertyValueFactory<Employee, Double>("hourlyPaid"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("password"));
		isManagerColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("isManager"));
		isActiveColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("isActive"));
		Employee.getEmployeeData();
		employeeTable.setItems(Employee.getDataList());
		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch = newValue;
			filterList();
		});

	}
}