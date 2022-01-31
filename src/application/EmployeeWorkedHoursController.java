package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.text.DateFormatSymbols;
import Relations.*;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 */
public class EmployeeWorkedHoursController implements Initializable {

	@FXML
	private ImageView addButton;

	@FXML
	private TableView<ArrayList<String>> workedHoursTable;

	@FXML
	private TableColumn<ArrayList<String>, String> monthColumn;

	@FXML
	private ComboBox<String> monthAddSelector;

	@FXML
	private ComboBox<String> monthSearchSelector;

	@FXML
	private TableColumn<ArrayList<String>, String> numOfHoursColumn;

	@FXML
	private TextField numOfHoursTextField;

	@FXML
	private TableColumn<ArrayList<String>, String> salaryColumn;

	@FXML
	private Label title;

	@FXML
	private TableColumn<ArrayList<String>, String> yearColumn;

	@FXML
	private ComboBox<String> yearSearchSelector;

	@FXML
	private ComboBox<Integer> yearAddSelector;

	@FXML
	private ImageView addedIcon;

	@FXML
	private Label addedLabel;

	@FXML
	private Button showWorkButton;

	@FXML
	private ImageView saveButton;

	private Employee employee;
	private boolean editedFlag = false;
	private String prevSelectedMonth = "All Months";
	private String prevSelectedYear = "All Years";

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

	public void saveOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		saveButton.setEffect(effect);
		for (int i = 0; i < workedHoursTable.getItems().size(); i++) {
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add(workedHoursTable.getItems().get(i).get(2));
			parameters.add(employee.getID() + "");
			parameters.add(workedHoursTable.getItems().get(i).get(0));
			parameters.add(workedHoursTable.getItems().get(i).get(1));
			Queries.queryUpdate("Update Work_Hours set Employee_Worked_Hours=? "
					+ " where Employee_ID=? and Worked_Month=? and Worked_Year=? ;", parameters);

		}
	}

	public void saveOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		saveButton.setEffect(effect);
	}

	public void saveOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		saveButton.setEffect(effect);
	}

	public void saveOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		saveButton.setEffect(effect);
	}

	public void addOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addButton.setEffect(effect);
		String numOfHoursStr = numOfHoursTextField.getText();
		double numOfHours = 0.0;
		int month = monthAddSelector.getSelectionModel().getSelectedIndex() + 1;
		int year = yearAddSelector.getSelectionModel().getSelectedItem();
		boolean checkNumOfHours = true;
		try {
			numOfHours = Double.parseDouble(numOfHoursStr);
		} catch (NumberFormatException e) {
			checkNumOfHours = false;
		}
		if (Queries.queryResult(
				"select * from Work_Hours " + " where Worked_Year=? " + " and Worked_Month=? "
						+ " and Employee_ID= ? ;",
				new ArrayList<>(Arrays.asList(year + "", month + "", employee.getID() + ""))).size() != 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Repeated Record");
			alert.setHeaderText(null);
			alert.setContentText("Record For This Date Already Exists");
			((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(false);
			alert.showAndWait();
			((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(true);
		}

		else if (numOfHoursStr == null || numOfHoursStr.isEmpty() || numOfHoursStr.isBlank() || !checkNumOfHours
				|| numOfHours < 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Number Of Hours Must Be A Nonnegative Real Number");
			((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(false);
			alert.showAndWait();
			((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(true);
		} else {
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add(employee.getID() + "");
			parameters.add(month + "");
			parameters.add(year + "");
			parameters.add(numOfHoursStr);
			Queries.queryUpdate("Insert into Work_Hours Values(?, ?, ?, ?) ;", parameters);
			filterData();
			numOfHoursTextField.setText("");
			showAndFade(addedIcon);
			showAndFade(addedLabel);
		}
	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addButton.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addButton.setEffect(effect);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addButton.setEffect(effect);
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
		title.setText(employee.getName() + " Worked Hours");
		filterData();
	}

	public void filterData() {
		String dateCondition = "";
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add(employee.getID() + "");
		if (!monthSearchSelector.getSelectionModel().getSelectedItem().equals("All Months")
				&& !yearSearchSelector.getSelectionModel().getSelectedItem().equals("All Years")) {
			dateCondition = " and Worked_Month=? and Worked_Year=? ";
			parameters.add(monthSearchSelector.getSelectionModel().getSelectedIndex() + "");
			parameters.add(yearSearchSelector.getSelectionModel().getSelectedItem());
		} else if (!monthSearchSelector.getSelectionModel().getSelectedItem().equals("All Months")) {
			dateCondition = " and Worked_Month=? ";
			parameters.add(monthSearchSelector.getSelectionModel().getSelectedIndex() + "");
		} else if (!yearSearchSelector.getSelectionModel().getSelectedItem().equals("All Years")) {
			dateCondition = " and Worked_Year=? ";
			parameters.add(yearSearchSelector.getSelectionModel().getSelectedItem());
		}

		ArrayList<ArrayList<String>> filteredList = Queries.queryResult("select W.Worked_Month,W.Worked_Year, "
				+ "W.Employee_Worked_Hours, (E.Employee_Hourly_Paid*W.Employee_Worked_Hours) "
				+ "from Work_hours W, Employee E " + "where E.Employee_ID=W.Employee_ID and E.Employee_ID= ? "
				+ dateCondition + ";", parameters);

		workedHoursTable.setItems(FXCollections.observableArrayList(filteredList));
		prevSelectedMonth = monthSearchSelector.getSelectionModel().getSelectedItem();
		prevSelectedYear = yearSearchSelector.getSelectionModel().getSelectedItem();
	}

	public void confirmSave() {
		if (editedFlag && (monthSearchSelector.getSelectionModel().getSelectedItem() != prevSelectedMonth
				|| yearSearchSelector.getSelectionModel().getSelectedItem() != prevSelectedYear)) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Unsaved Edited Data");
			alert.setHeaderText(null);
			alert.setContentText("Data has been edited, do you wish to save edits first?");
			ButtonType saveButtonType = new ButtonType("Save");
			ButtonType continueButtonType = new ButtonType("Continue anyway");
			ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(saveButtonType, continueButtonType, cancelButtonType);
			((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(false);
			Optional<ButtonType> result = alert.showAndWait();
			((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(true);
			if (result.get() == saveButtonType) {
				saveOnMousePressed();
				saveOnMouseReleased();
				editedFlag = false;
				filterData();
			} else if (result.get() == continueButtonType) {
				filterData();
				editedFlag = false;
			} else {
				monthSearchSelector.setValue(prevSelectedMonth);
				yearSearchSelector.setValue(prevSelectedYear);
			}
		} else {
			filterData();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		addedIcon.setOpacity(0);
		addedLabel.setOpacity(0);
		ArrayList<String> months = new ArrayList<>(Arrays.asList("All Months", "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October", "November", "December"));

		int currentYear = LocalDate.now().getYear();
		ArrayList<Integer> years = new ArrayList<>();
		ArrayList<String> yearsStr = new ArrayList<>();
		yearsStr.add("All Years");
		for (int i = 2018; i <= currentYear; i++) {
			years.add(i);
			yearsStr.add("" + i);
		}

		yearAddSelector.setItems(FXCollections.observableArrayList(years));
		yearSearchSelector.setItems(FXCollections.observableArrayList(yearsStr));
		yearSearchSelector.setValue("All Years");
		monthAddSelector.setItems(FXCollections.observableArrayList(months.subList(1, 13)));
		monthSearchSelector.setItems(FXCollections.observableArrayList(months));
		monthSearchSelector.setValue("All Months");
		yearAddSelector.setValue(currentYear);
		String currentMonth = LocalDate.now().getMonth().toString();
		monthAddSelector.setValue(currentMonth.charAt(0) + currentMonth.substring(1).toLowerCase());

		monthColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(
									new DateFormatSymbols().getMonths()[Integer.parseInt(x.get(0)) - 1]);
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		yearColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		numOfHoursColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		salaryColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 3) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		workedHoursTable.setEditable(true);
		numOfHoursColumn.setEditable(true);
		salaryColumn.setEditable(true);
		numOfHoursColumn.setCellFactory(TextFieldTableCell.<ArrayList<String>>forTableColumn());

		numOfHoursColumn.setOnEditCommit((CellEditEvent<ArrayList<String>, String> t) -> {
			boolean checkNumOfHours = true;
			double numOfHours = 0.0;
			try {
				numOfHours = Double.parseDouble(t.getNewValue());

			} catch (NumberFormatException e) {
				checkNumOfHours = false;
			}
			if (t.getNewValue() == null || t.getNewValue().isEmpty() || t.getNewValue().isBlank() || !checkNumOfHours
					|| numOfHours < 0) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Number Of Hours Must Be A Nonnegative Real Number");
				((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(false);
				alert.showAndWait();
				((Stage) workedHoursTable.getScene().getWindow()).setAlwaysOnTop(true);
			} else {
				if (!t.getOldValue().equals(t.getNewValue())) {
					editedFlag = true;
				}
				(t.getTableView().getItems().get(t.getTablePosition().getRow())).set(2, t.getNewValue());

				(t.getTableView().getItems().get(t.getTablePosition().getRow())).set(3,
						"" + (Double.parseDouble(t.getNewValue()) * employee.getHourlyPaid()));

			}
			workedHoursTable.refresh();
		});
	}

}