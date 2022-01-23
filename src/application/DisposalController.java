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
	private TableView<ArrayList<String>> Batches;

	@FXML
	private TextField cost;

	@FXML
	private Button dispose;

	@FXML
	private TableColumn<ArrayList<String>, String> eAmount;

	@FXML
	private TableColumn<ArrayList<String>, String> eID;

	@FXML
	private TableColumn<ArrayList<String>, String> eName;

	@FXML
	private TableColumn<ArrayList<String>, String> eedate;

	@FXML
	private TableColumn<ArrayList<String>, String> epdate;

	@FXML
	private Label warning;

	@FXML
	private TextField search;

	@FXML
	private TextField amount;

	@FXML
	private ComboBox<String> searchOP;

	@FXML
	private CheckBox expired;

	@FXML
	private Label success;

	@FXML
	private ImageView successIcon;
	
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

	ObservableList<String> Choices = FXCollections.observableArrayList("Name", "Expired Month", "Expired Year",
			"Production Month", "Production Year");

	public void selectionOnAction(ActionEvent e) {
		if (expired.isSelected()) {
			try {
				Batches.setItems(FXCollections.observableArrayList(Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
								+ "from batch b,product p\r\n"
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW()) \r\n"
								+ "order by b.batch_expiry_date;",
						null)));
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			amount.setOpacity(0);
		} else {

			try {
				Batches.setItems(FXCollections.observableArrayList(Queries.queryResult(
						"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
								+ "from batch b,product p\r\n"
								+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' \r\n"
								+ "order by b.batch_expiry_date;",
						null)));
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			amount.setOpacity(1);

		}

	}

	public void disposeOnAction(ActionEvent e) throws ClassNotFoundException, SQLException {
		warning.setText("");
		if (Batches.getSelectionModel().getSelectedItem() != null) {
			if (expired.isSelected()) {
				if (cost.getText().isBlank()) {
					warning.setText("Fill The Disposing Cost!");
				} else {
					boolean flag = false;
					try {
						Double num = Double.parseDouble(cost.getText());
						if (num < 0) {
							flag = true;
							warning.setText("The Cost Can't Be Negative!");

						}
					} catch (NumberFormatException e1) {
						warning.setText("Enter A Valid Cost!");
						flag = true;
					}
					if (flag == false) {
						Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(cost.getText()), "Cash");
						Queries.queryUpdate(
								"insert into drug_disposal (Disposal_amount,Disposal_date,Employee_ID,Payment_ID,Product_ID ,Batch_Production_Date"
										+ ",Batch_Expiry_Date) values(?,?,?,?,?,?,?)",
								new ArrayList<String>(
										Arrays.asList(Batches.getSelectionModel().getSelectedItem().get(4).toString(),
												java.time.LocalDate.now().toString(), Employee.getCurrentID()+"",
												Payment.getMaxID() + "",
												Batches.getSelectionModel().getSelectedItem().get(0).toString(),
												Batches.getSelectionModel().getSelectedItem().get(2).toString(),
												Batches.getSelectionModel().getSelectedItem().get(3).toString())));

						Queries.queryUpdate(
								"update batch set batch_amount = 0 where Batch_Production_Date=? and Batch_Expiry_Date=? and Product_ID=?",
								new ArrayList<String>(
										Arrays.asList(Batches.getSelectionModel().getSelectedItem().get(2).toString(),
												Batches.getSelectionModel().getSelectedItem().get(3).toString(),
												Batches.getSelectionModel().getSelectedItem().get(0).toString())));
						showAndFade(success);
						showAndFade(successIcon);

						Batches.refresh();
					}
				}
			} else {
				if (cost.getText().isBlank() == true || amount.getText().isBlank() == true) {
					warning.setText("Fill The Required Fields!");
				} else {

					boolean flag = false;

					try {
						int num2 = Integer.parseInt(amount.getText());
						if (num2 < 0) {
							flag = true;
							warning.setText("The Amount Can't Be Negative!");

						} else {
							try {
								Double num = Double.parseDouble(cost.getText());
								if (num < 0) {
									flag = true;
									warning.setText("The Cost Can't Be Negative!");

								}
							} catch (NumberFormatException e1) {
								warning.setText("Enter A Valid Cost!");
								flag = true;
							}
						}

					} catch (NumberFormatException e1) {
						warning.setText("Enter A Valid Amount!");
						flag = true;
					}

					if (flag == false) {
						Payment.insertPayment(java.time.LocalDate.now(), Double.parseDouble(cost.getText()), "Cash");
						Queries.queryUpdate(
								"insert into drug_disposal (Disposal_amount,Disposal_date,Employee_ID,Payment_ID,Product_ID ,Batch_Production_Date"
										+ ",Batch_Expiry_Date) values(?,?,?,?,?,?,?)",
								new ArrayList<String>(
										Arrays.asList(amount.getText(), java.time.LocalDate.now().toString(),
												Employee.getCurrentID()+"", Payment.getMaxID() + "",
												Batches.getSelectionModel().getSelectedItem().get(0).toString(),
												Batches.getSelectionModel().getSelectedItem().get(2).toString(),
												Batches.getSelectionModel().getSelectedItem().get(3).toString())));

						Queries.queryUpdate(
								"update batch set batch_amount = batch_amount - ? where Batch_Production_Date=? and Batch_Expiry_Date=? and Product_ID=?",
								new ArrayList<String>(Arrays.asList(amount.getText(),
										Batches.getSelectionModel().getSelectedItem().get(2).toString(),
										Batches.getSelectionModel().getSelectedItem().get(3).toString(),
										Batches.getSelectionModel().getSelectedItem().get(0).toString())));

						showAndFade(success);
						showAndFade(successIcon);
						Batches.refresh();
					}
					
				}
			}

		} else {
			warning.setText("Select A Batch");

		}
		amount.clear();
		cost.clear();
		Batches.getSelectionModel().clearSelection();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOP.setItems(Choices);

		try {
			Batches.setItems(FXCollections.observableArrayList(Queries.queryResult(
					"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
							+ "from batch b,product p\r\n"
							+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' \r\n"
							+ "order by b.batch_expiry_date;",
					null)));
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		eID.setCellValueFactory(
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
		eName.setCellValueFactory(
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
		epdate.setCellValueFactory(
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
		eedate.setCellValueFactory(
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
		eAmount.setCellValueFactory(
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

		search.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank() && expired.isSelected() == false) {
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
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Name" && expired.isSelected() == false) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
									+ " and p.product_name like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Expired Month"
					&& expired.isSelected() == false) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
									+ " and  month(b.batch_expiry_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Expired Year"
					&& expired.isSelected() == false) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
									+ " and  year(b.batch_expiry_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Production Month"
					&& expired.isSelected() == false) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
									+ " and  month(b.batch_production_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Production Year"
					&& expired.isSelected() == false) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' "
									+ " and  year(b.batch_production_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (expired.isSelected() == false) {
				try {
					ArrayList<String> parameters = new ArrayList<>();
					while (parameters.size() < 5) {
						parameters.add("%" + newValue + "%");
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
			} else if (newValue == null || newValue.isEmpty() || newValue.isBlank() && expired.isSelected() == true) {
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
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Name" && expired.isSelected() == true) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
									+ " and p.product_name like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Expired Month"
					&& expired.isSelected() == true) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
									+ " and  month(b.batch_expiry_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Expired Year"
					&& expired.isSelected() == true) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
									+ " and  year(b.batch_expiry_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Production Month"
					&& expired.isSelected() == true) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
									+ " and  month(b.batch_production_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (searchOP.getSelectionModel().getSelectedItem() == "Production Year"
					&& expired.isSelected() == true) {
				try {
					filteredList = Queries.queryResult(
							"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount "
									+ "from batch b,product p "
									+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE(NOW())"
									+ " and  year(b.batch_production_date) like ? ;",
							new ArrayList<>(Arrays.asList("%" + newValue + "%")));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

			} else if (expired.isSelected() == true) {
				try {
					ArrayList<String> parameters = new ArrayList<>();
					while (parameters.size() < 5) {
						parameters.add("%" + newValue + "%");
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
			Batches.setItems(FXCollections.observableArrayList(filteredList));
		});

	}
}
