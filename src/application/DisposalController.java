package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.Employee;
import Relations.Payment;
import Relations.Queries;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Duration;

public class DisposalController implements Initializable {

	@FXML
	private TableView<ArrayList<String>> toBeDisposedTable;

	@FXML
	private TextField disposalCostTextField;

	@FXML
	private Button disposeButton;

	@FXML
	private TableColumn<ArrayList<String>, String> productAmountColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productIDColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productExpityDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productProductionDateColumn;

	@FXML
	private TextField searchTextField;

	@FXML
	private TextField amountToBeDisposed;

	@FXML
	private ComboBox<String> searchOperationComboBox;

	@FXML
	private CheckBox expiredSelectionCheckBox;	
	
	@FXML
	private Label success;

	private String stringToSearch="";
	
	@FXML
	private ImageView successIcon;
	ObservableList<String> searchChoices = FXCollections.observableArrayList("Name", "Expired Month", "Expired Year",
			"Production Month", "Production Year");

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



	public void selectionOnAction(ActionEvent e) {
		
		if (expiredSelectionCheckBox.isSelected()) {
			try {
				toBeDisposedTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
								+ "from batch b,product p\r\n"
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW()) \r\n"
								+ "order by b.batch_expiry_date;",
						null)));
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			amountToBeDisposed.setOpacity(0);
		} else {

			try {
				toBeDisposedTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
								+ "from batch b,product p\r\n"
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' \r\n"
								+ "order by b.batch_expiry_date;",
						null)));
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			amountToBeDisposed.setOpacity(1);

		}

	}

	public void disposeOnAction(ActionEvent e) throws ClassNotFoundException, SQLException {
		if (toBeDisposedTable.getSelectionModel().getSelectedItem() != null) {
			if (expiredSelectionCheckBox.isSelected()) {
				if (disposalCostTextField.getText().isBlank()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Fill The Disposing Cost!");
					alert.showAndWait();
				} else {
					boolean flag = false;
					try {
						Double num = Double.parseDouble(disposalCostTextField.getText());
						if (num < 0) {
							flag = true;
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle(null);
							alert.setHeaderText(null);
							alert.setContentText("The Cost Can't Be Negative!");
							alert.showAndWait();

						}
					} catch (NumberFormatException e1) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("Enter A Valid Cost!");
						alert.showAndWait();
						flag = true;
					}
					if (flag == false) {
						Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(disposalCostTextField.getText()), "Cash");
						Queries.queryUpdate(
								"insert into drug_disposal (Disposal_amount,Disposal_date,Employee_ID,Payment_ID,Product_ID ,Batch_Production_Date"
										+ ",Batch_Expiry_Date) values(?,?,?,?,?,?,?)",
								new ArrayList<String>(
										Arrays.asList(toBeDisposedTable.getSelectionModel().getSelectedItem().get(4).toString(),
												java.time.LocalDate.now().toString(), Employee.getCurrentID()+"",
												Payment.getMaxID() + "",
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(0).toString(),
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(2).toString(),
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(3).toString())));

						Queries.queryUpdate(
								"update batch set batch_amount = 0 where Batch_Production_Date=? and Batch_Expiry_Date=? and Product_ID=?",
								new ArrayList<String>(
										Arrays.asList(toBeDisposedTable.getSelectionModel().getSelectedItem().get(2).toString(),
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(3).toString(),
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(0).toString())));
						showAndFade(success);
						showAndFade(successIcon);

						try {
							toBeDisposedTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
									"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
											+ "from batch b,product p\r\n"
											+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' \r\n"
											+ "order by b.batch_expiry_date;",
									null)));
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}					}
				}
			} else {
				if (disposalCostTextField.getText().isBlank() == true || amountToBeDisposed.getText().isBlank() == true) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("Fill The Required Fields!");
					alert.showAndWait();
				} else {

					boolean flag = false;

					try {
						int num2 = Integer.parseInt(amountToBeDisposed.getText());
						if (num2 < 0) {
							flag = true;
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle(null);
							alert.setHeaderText(null);
							alert.setContentText("The Amount Can't Be Negative!");
							alert.showAndWait();

						} else {
							try {
								Double num = Double.parseDouble(disposalCostTextField.getText());
								if (num < 0) {
									flag = true;
									Alert alert = new Alert(Alert.AlertType.ERROR);
									alert.setTitle(null);
									alert.setHeaderText(null);
									alert.setContentText("The Cost Can't Be Negative!");
									alert.showAndWait();

								}
							} catch (NumberFormatException e1) {
								Alert alert = new Alert(Alert.AlertType.ERROR);
								alert.setTitle(null);
								alert.setHeaderText(null);
								alert.setContentText("Enter A Valid Cost!");
								alert.showAndWait();
								flag = true;
							}
						}

					} catch (NumberFormatException e1) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("Enter A Valid Amount!");
						alert.showAndWait();
						flag = true;
					}

					if (flag == false) {
						Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(disposalCostTextField.getText()), "Cash");
						Queries.queryUpdate(
								"insert into drug_disposal (Disposal_amount,Disposal_date,Employee_ID,Payment_ID,Product_ID ,Batch_Production_Date"
										+ ",Batch_Expiry_Date) values(?,?,?,?,?,?,?)",
								new ArrayList<String>(
										Arrays.asList(amountToBeDisposed.getText(), java.time.LocalDate.now().toString(),
												Employee.getCurrentID()+"", Payment.getMaxID() + "",
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(0).toString(),
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(2).toString(),
												toBeDisposedTable.getSelectionModel().getSelectedItem().get(3).toString())));

						Queries.queryUpdate(
								"update batch set batch_amount = batch_amount - ? where Batch_Production_Date=? and Batch_Expiry_Date=? and Product_ID=?",
								new ArrayList<String>(Arrays.asList(amountToBeDisposed.getText(),
										toBeDisposedTable.getSelectionModel().getSelectedItem().get(2).toString(),
										toBeDisposedTable.getSelectionModel().getSelectedItem().get(3).toString(),
										toBeDisposedTable.getSelectionModel().getSelectedItem().get(0).toString())));

						showAndFade(success);
						showAndFade(successIcon);
						try {
							toBeDisposedTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
									"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
											+ "from batch b,product p\r\n"
											+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' \r\n"
											+ "order by b.batch_expiry_date;",
									null)));
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
					}
					
				}
			}

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Select A Batch");
			alert.showAndWait();
		}
		amountToBeDisposed.clear();
		disposalCostTextField.clear();
		toBeDisposedTable.getSelectionModel().clearSelection();

	}
	
	public void filterList() {
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank() && expiredSelectionCheckBox.isSelected() == false) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
								+ "from batch b,product p\r\n"
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' \r\n"
								+ "order by b.batch_expiry_date;",
						null);

			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Name" && expiredSelectionCheckBox.isSelected() == false) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
								+ " and p.product_name like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Expired Month"
				&& expiredSelectionCheckBox.isSelected() == false) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
								+ " and  month(b.batch_expiry_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Expired Year"
				&& expiredSelectionCheckBox.isSelected() == false) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
								+ " and  year(b.batch_expiry_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Production Month"
				&& expiredSelectionCheckBox.isSelected() == false) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
								+ " and  month(b.batch_production_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Production Year"
				&& expiredSelectionCheckBox.isSelected() == false) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
								+ " and  year(b.batch_production_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (expiredSelectionCheckBox.isSelected() == false) {
			try {
				ArrayList<String> parameters = new ArrayList<>();
				while (parameters.size() < 5) {
					parameters.add("%" + stringToSearch + "%");
				}
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "	from batch b,product p "
								+ " where ( b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01')"
								+ "and (  year(b.batch_production_date) like ? "
								+ " or month(b.batch_production_date) like ? "
								+ " or  year(b.batch_expiry_date) like ? " + " or  p.product_name like ? "
								+ " or month(b.batch_expiry_date) like ? ) ;",
						parameters);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank() && expiredSelectionCheckBox.isSelected() == true) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
								+ "from batch b,product p\r\n"
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW()) \r\n"
								+ "order by b.batch_expiry_date;",
						null);

			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Name" && expiredSelectionCheckBox.isSelected() == true) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
								+ " and p.product_name like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Expired Month"
				&& expiredSelectionCheckBox.isSelected() == true) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
								+ " and  month(b.batch_expiry_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Expired Year"
				&& expiredSelectionCheckBox.isSelected() == true) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
								+ " and  year(b.batch_expiry_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Production Month"
				&& expiredSelectionCheckBox.isSelected() == true) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
								+ " and  month(b.batch_production_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Production Year"
				&& expiredSelectionCheckBox.isSelected() == true) {
			try {
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "from batch b,product p "
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
								+ " and  year(b.batch_production_date) like ? ;",
						new ArrayList<>(Arrays.asList("%" + stringToSearch + "%")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (expiredSelectionCheckBox.isSelected() == true) {
			try {
				ArrayList<String> parameters = new ArrayList<>();
				while (parameters.size() < 5) {
					parameters.add("%" + stringToSearch + "%");
				}
				filteredList = Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
								+ "	from batch b,product p "
								+ " where ( b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW()))"
								+ "and (  year(b.batch_production_date) like ? "
								+ " or month(b.batch_production_date) like ? "
								+ " or  year(b.batch_expiry_date) like ? " + " or  p.product_name like ? "
								+ " or month(b.batch_expiry_date) like ? ) ;",
						parameters);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		toBeDisposedTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOperationComboBox.setItems(searchChoices);

		try {
			toBeDisposedTable.setItems(FXCollections.observableArrayList(Queries.queryResult(
					"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
							+ "from batch b,product p\r\n"
							+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' \r\n"
							+ "order by b.batch_expiry_date;",
					null)));
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		productIDColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(0));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});
		productNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});
		productProductionDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});
		productExpityDateColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});
		productAmountColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(4));
						} else {
							return new SimpleStringProperty("");
						}
					}
				});

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch=newValue;
			filterList();
		});

	}
}
