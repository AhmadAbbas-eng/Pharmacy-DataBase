package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.CustomerOrder;
import Relations.Employee;
import Relations.Queries;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * 
 * @version 30 January 2022
 * @author Aseel Sabri
 */
public class SellController implements Initializable {
	private boolean isCustomerSelected = false;
	@FXML
	private TableColumn<ArrayList<String>, String> amountColumn;

	@FXML
	private Button approveSellButton;

	@FXML
	private Button chooseCustomerButton;

	@FXML
	private Button chooseProductButton;

	@FXML
	private Label costAfterDiscountLabel;

	@FXML
	private Label dateLabel;

	@FXML
	private TextField discountTextField;

	@FXML
	private TableColumn<ArrayList<String>, String> expiryDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> idColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> nameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> priceColumn;

	@FXML
	private TableView<ArrayList<String>> productTable;

	@FXML
	private TableColumn<ArrayList<String>, String> productionDateColumn;

	@FXML
	private Label totalCostLabel;

	@FXML
	private ImageView deleteProductButton;

	@FXML
	private ImageView deleteCustomerButton;

	@FXML
	private TextField paidTextField;

	@FXML
	private ImageView soldIcon;

	@FXML
	private Label soldLabel;

	public int numbrOfDangerDrugs = 0;

	private ObservableList<ArrayList<String>> chosenProducts;

	FXMLLoader productLoader;

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

	public void chooseCustomerOnAction() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChooseCustomer.fxml"));
			Parent chooseCustomer = (Parent) loader.load();
			ChooseCustomerController chooseCustomerController = loader.getController();
			chooseCustomerController.setCaller(this);
			Stage chooseCustomerStage = new Stage();
			chooseCustomerStage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(chooseCustomer);
			chooseCustomerStage.setScene(scene);
			chooseCustomerStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void chooseProductOnAction() {
		try {
			chosenProducts = productTable.getItems();
			productLoader = new FXMLLoader(getClass().getResource("ChooseProduct.fxml"));
			Parent chooseProduct = (Parent) productLoader.load();
			ChooseProductController chooseProductController = productLoader.getController();
			chooseProductController.getChosenProducts(chosenProducts, this);
			Stage chooseProductStage = new Stage();
			chooseProductStage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(chooseProduct);
			chooseProductStage.setScene(scene);
			chooseProductStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void approveOnAction() {
		String paidStr = paidTextField.getText();
		String discountStr = discountTextField.getText();
		double paidAmount = 0.0;
		double discount = 0.0;
		boolean checkPaid = true;
		try {
			paidAmount = Double.parseDouble(paidStr);
			if (discountStr != null && !discountStr.isBlank() && !discountStr.isEmpty()) {
				discount = Double.parseDouble(discountStr);
			}
		} catch (NumberFormatException e) {
			checkPaid = false;
		}

		if (chosenProducts == null || chosenProducts.size() == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("At Least One Product Must Be Added");
			alert.showAndWait();
		} else if (Double.parseDouble(costAfterDiscountLabel.getText()) < 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Discount Must Be Less Than Total Cost");
			alert.showAndWait();
		} else if (paidStr == null || paidStr.isBlank() || paidStr.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("Paid Amount Must Be Entered");
			alert.showAndWait();
		} else if (!checkPaid) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input");
			alert.setHeaderText(null);
			alert.setContentText("Paid Amount Must Be A Real Number");
			alert.showAndWait();
		} else if (numbrOfDangerDrugs > 0 && !isCustomerSelected) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("Customer Must Be Selected For Danger Drugs");
			alert.showAndWait();
		} else if (paidAmount < Double.parseDouble(costAfterDiscountLabel.getText()) && !isCustomerSelected) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("Customer Must Be Selected When Buying In Debt");
			alert.showAndWait();
		} else {
			String customerNID = chooseCustomerButton.getText().equals("Choose Customer") ? "0"
					: chooseCustomerButton.getText();
			CustomerOrder.insertCustomerOrder(dateLabel.getText(), Double.parseDouble(totalCostLabel.getText()),
					discount, paidAmount, Employee.getCurrentID(), customerNID);

			for (ArrayList<String> row : chosenProducts) {
				CustomerOrder.insertCustomerOrderBatch("" + CustomerOrder.getMaxID(), row.get(0), row.get(2),
						row.get(3), Integer.parseInt(row.get(5)));
			}
			Queries.queryUpdate(
					"Insert into Income"
							+ " (Income_amount, Income_Date, Employee_ID, Customer_NID) Values(?, ?, ?, ?);",
					new ArrayList<>(Arrays.asList(paidAmount + "", dateLabel.getText(), Employee.getCurrentID() + "",
							customerNID)));
			showAndFade(soldIcon);
			showAndFade(soldLabel);
			chosenProducts.clear();
			productTable.getItems().clear();
			costAfterDiscountLabel.setText("0");
			totalCostLabel.setText("0");
			paidTextField.setText("");
			discountTextField.setText("");
			chooseCustomerButton.setText("Choose Customer");
			isCustomerSelected = false;

		}
	}

	public void deleteCustomerOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		deleteCustomerButton.setEffect(effect);
		chooseCustomerButton.setText("Choose Customer");
		isCustomerSelected = false;
	}

	public void deleteCustomerOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deleteCustomerButton.setEffect(effect);
	}

	public void deleteCustomerOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		deleteCustomerButton.setEffect(effect);
	}

	public void deleteCustomerOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deleteCustomerButton.setEffect(effect);
	}

	public void deleteProductOnMousePressed() throws ClassNotFoundException, SQLException {
		if (productLoader != null && productTable.getSelectionModel().getSelectedItem() != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.8);
			deleteProductButton.setEffect(effect);
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add(productTable.getSelectionModel().getSelectedItem().get(0));
			if ((Queries.queryResult("select * from Drug where Product_ID=? and Drug_Pharmacetical_Category='Danger';",
					parameters)).size() > 0) {
				numbrOfDangerDrugs--;
			}
			double deletedCost = Double.parseDouble(productTable.getSelectionModel().getSelectedItem().get(4))
					* Double.parseDouble(productTable.getSelectionModel().getSelectedItem().get(5));
			totalCostLabel.setText("" + (Double.parseDouble(totalCostLabel.getText()) - deletedCost));
			costAfterDiscountLabel.setText("" + (Double.parseDouble(costAfterDiscountLabel.getText()) - deletedCost));
			productTable.getItems().remove(productTable.getSelectionModel().getSelectedIndex());
			ChooseProductController productController = productLoader.getController();
			productController.filterBatchList();
		}
	}

	public void deleteProductOnMouseReleased() {
		if (productTable.getSelectionModel().getSelectedItem() != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			deleteProductButton.setEffect(effect);
		}
	}

	public void deleteProductOnMouseEntered() {
		if (productTable.getSelectionModel().getSelectedItem() != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.4);
			deleteProductButton.setEffect(effect);
		}
	}

	public void deleteProductOnMouseExited() {
		if (productTable.getSelectionModel().getSelectedItem() != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			deleteProductButton.setEffect(effect);
		}
	}

	public void discountOnEnter(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			costAfterDiscountLabel.requestFocus();
//			double totalCost = Double.parseDouble(totalCostLabel.getText());
//			double discount = 0.0;
//			double costAfterDiscount = totalCost;
//			String discountStr = discountTextField.getText();
//			if (discountStr != null && !discountStr.isBlank() && !discountStr.isEmpty()) {
//				try {
//					discount = Double.parseDouble(discountStr);
//					if (discount > totalCost) {
//						Alert alert = new Alert(Alert.AlertType.ERROR);
//						alert.setTitle("Wrong Input");
//						alert.setHeaderText(null);
//						alert.setContentText("Discount Must Be Less Than Total Cost");
//						alert.showAndWait();
//					}
//				} catch (NumberFormatException e) {
//					Alert alert = new Alert(Alert.AlertType.ERROR);
//					alert.setTitle("Wrong Input Format");
//					alert.setHeaderText(null);
//					alert.setContentText("Discount Must Be A Real Number");
//					alert.showAndWait();
//				}
//				costAfterDiscount -= discount;
//			}
//			costAfterDiscountLabel.setText("" + costAfterDiscount);
		}
	}

	public void updateSelectedProducts() {
		productTable.setItems(chosenProducts);
		productTable.refresh();
		double totalCost = 0.0;
		double discount = 0.0;
		for (int i = 0; chosenProducts != null && i < chosenProducts.size(); i++) {
			totalCost += Double.parseDouble(chosenProducts.get(i).get(4))
					* Double.parseDouble(chosenProducts.get(i).get(5));
		}
		double costAfterDiscount = totalCost;
		totalCostLabel.setText("" + totalCost);
		String discountStr = discountTextField.getText();
		if (discountStr != null && !discountStr.isBlank() && !discountStr.isEmpty()) {
			try {
				discount = Double.parseDouble(discountStr);
				if (discount > totalCost) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Wrong Input");
					alert.setHeaderText(null);
					alert.setContentText("Discount Must Be Less Than Total Cost");
					alert.showAndWait();
				}
			} catch (NumberFormatException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Discount Must Be A Real Number");
				alert.showAndWait();
				e.printStackTrace();
			}
			costAfterDiscount -= discount;
		}
		costAfterDiscountLabel.setText("" + costAfterDiscount);
	}

	public void setCustomerNID(String NID) {
		chooseCustomerButton.setText(NID);
		isCustomerSelected = true;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		soldIcon.setOpacity(0);
		soldLabel.setOpacity(0);
		CustomerOrder.getCustomerOrderData();
		dateLabel.setText(LocalDate.now().toString());
		idColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(0));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		nameColumn.setCellValueFactory(
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

		productionDateColumn.setCellValueFactory(
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

		expiryDateColumn.setCellValueFactory(
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

		priceColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 4) {
							return new SimpleStringProperty(x.get(4));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		amountColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 5) {
							return new SimpleStringProperty(x.get(5));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		amountColumn.setCellFactory(TextFieldTableCell.<ArrayList<String>>forTableColumn());

		amountColumn.setOnEditCommit((CellEditEvent<ArrayList<String>, String> t) -> {
			ArrayList<ArrayList<String>> amount = new ArrayList<>();
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add((t.getTableView().getItems().get(t.getTablePosition().getRow())).get(0));
			parameters.add((t.getTableView().getItems().get(t.getTablePosition().getRow())).get(2));
			parameters.add((t.getTableView().getItems().get(t.getTablePosition().getRow())).get(3));
			amount = Queries.queryResult("Select Batch_Amount from Batch where Product_ID=?"
					+ " and Batch_Production_Date=? and Batch_Expiry_Date=? ;", parameters);
			boolean checkAmount = true;
			try {
				Integer.parseInt(t.getNewValue());
			} catch (NumberFormatException e) {
				checkAmount = false;
			}
			if (checkAmount && amount.size() > 0
					&& Integer.parseInt(t.getNewValue()) <= Integer.parseInt(amount.get(0).get(0))) {
				(t.getTableView().getItems().get(t.getTablePosition().getRow())).set(5, t.getNewValue());
			} else if (!checkAmount) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Amount Must Be An Integer");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("There Isn't Enough Amount");
				alert.showAndWait();
			}
			updateSelectedProducts();
		});

		discountTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!discountTextField.isFocused()) {
				double totalCost = Double.parseDouble(totalCostLabel.getText());
				double discount = 0.0;
				double costAfterDiscount = totalCost;
				String discountStr = discountTextField.getText();
				if (discountStr != null && !discountStr.isBlank() && !discountStr.isEmpty()) {
					try {
						discount = Double.parseDouble(discountStr);
						if (discount > totalCost) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Wrong Input");
							alert.setHeaderText(null);
							alert.setContentText("Discount Must Be Less Than Total Cost");
							alert.showAndWait();
							discountTextField.setText(null);
							return;
						}
					} catch (NumberFormatException e) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Wrong Input Format");
						alert.setHeaderText(null);
						alert.setContentText("Discount Must Be A Real Number");
						alert.showAndWait();
						discountTextField.setText(null);
						return;
					}
					costAfterDiscount -= discount;
				}
				costAfterDiscountLabel.setText("" + costAfterDiscount);
			}
		});

	}
}