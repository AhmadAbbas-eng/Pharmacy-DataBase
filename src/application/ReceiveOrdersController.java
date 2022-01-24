package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.Batch;
import Relations.Employee;
import Relations.Product;
import Relations.Queries;
import Relations.SupplierOrder;
import Relations.SupplierOrderBatch;

public class ReceiveOrdersController implements Initializable {

	private int counter = 0;

	@FXML
	private TableView<ArrayList<String>> orderProductTabel;

	@FXML
	private TableColumn<ArrayList<String>, String> quantityColumn;

	@FXML
	private TableColumn<Batch, String> batchExpiryDateColumn;

	@FXML
	private TableColumn<Batch, String> batchProductionDateColumn;

	@FXML
	private Button cancelButton;

	@FXML
	private Button confirmBatchButton;

	@FXML
	private CheckBox confirmQuantityCheckBox;

	@FXML
	private TableColumn<ArrayList<String>, String> expiryDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productIDColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productionDateColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> productNameColumn;

	@FXML
	private TextField quantityTextField;

	@FXML
	private TextField costTextField;

	@FXML
	private DatePicker receivedDatePicker;

	@FXML
	private DatePicker payDueDatePicker;

	@FXML
	private Label savedQuantityLabel;

	@FXML
	private TableView<Batch> productsBatchColumn;

	@FXML
	private Label orderNumberLabel;

	@FXML
	private Label choosenProductLabel;

	@FXML
	private ImageView saveIcon;

	@FXML
	private ImageView addBatchIcon;

	@FXML
	private StackPane mainPane;
	
	private String ProductName;

	private String ProductId;

	private String QuantityStored;
	
	SupplierOrder supplierOrder;

	UnReceivedController caller;

	private static ArrayList<SupplierOrderBatch> orderedProducts;

	private static ArrayList<ArrayList<String>> data = new ArrayList<>();
	
	public void checkBoxOnAction(ActionEvent e) {
		if(confirmQuantityCheckBox.isSelected()==true) {
			quantityTextField.setOpacity(0);

		}else {
			quantityTextField.setOpacity(1);
		}
	}

	public void saveOnMousePressed() throws IOException, ClassNotFoundException, SQLException {
		if (receivedDatePicker.getValue() != null && payDueDatePicker.getValue() != null) {

			if (receivedDatePicker.getValue().compareTo(payDueDatePicker.getValue()) == 0
					|| receivedDatePicker.getValue().compareTo(payDueDatePicker.getValue()) > 0
					|| supplierOrder.getDateOfOrder().compareTo(payDueDatePicker.getValue()) > 0
					&& receivedDatePicker.getValue().compareTo(LocalDate.now()) > 0) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input");
				alert.setHeaderText(null);
				alert.setContentText("Set A Reasonable Date");
				alert.showAndWait();

			} else if (orderedProducts.size() == counter && receivedDatePicker.getValue() != null
					&& payDueDatePicker.getValue() != null) {
				ColorAdjust effect = new ColorAdjust();
				effect.setBrightness(0.8);
				saveIcon.setEffect(effect);

				if (costTextField.getText().isBlank() == false) {

					Queries.queryUpdate(
							"update s_order set Recieved_by=? , Recieved_Date=? , order_cost =?  where order_id=? ;",
							new ArrayList<>(Arrays.asList(Employee.getCurrentID()+"", receivedDatePicker.getValue().toString(),
									costTextField.getText(), supplierOrder.getID()+"")));
					Queries.queryUpdate("update Supplier set supplier_dues =? where Supplier_ID =?;"
			                  ,new ArrayList<>(Arrays.asList(costTextField.getText(),supplierOrder.getSupplierID()+"")));
					
				} else {
					Queries.queryUpdate("update s_order set Recieved_by=? , Recieved_Date=? where order_id=? ;",
							new ArrayList<>(Arrays.asList(Employee.getCurrentID()+"", receivedDatePicker.getValue().toString(),
									supplierOrder.getID()+"")));
				}
				counter = 0;
				while (data.size() > counter) {

					String pID = data.get(counter).get(0);
					String Amount = data.get(counter).get(4);

					Queries.queryUpdate(
							"update S_Order_Batch set batch_amount = batch_amount - ?"
									+ " where product_id = ? and Batch_Production_Date = '1111-01-01';",
							new ArrayList<>(Arrays.asList(Amount, pID)));

					Queries.queryUpdate(
							"update Batch set batch_amount = batch_amount + ?"
									+ " where product_id = ? and Batch_Production_Date = ?  and Batch_Expiry_Date = ?;",
							new ArrayList<>(
									Arrays.asList(Amount, pID, data.get(counter).get(2), data.get(counter).get(3))));

					Queries.queryUpdate(
							"insert into S_Order_Batch (Order_ID ,Product_ID ,Batch_Production_Date,Batch_Expiry_Date,Batch_amount)"
									+ " values(?,?,?,?,?);",
							new ArrayList<>(Arrays.asList(supplierOrder.getID()+"", pID, data.get(counter).get(2),
									data.get(counter).get(3), Amount)));
					++counter;
				}

				Region page = FXMLLoader.load(getClass().getResource("UnReceivedOrders.fxml"));
				mainPane.getChildren().removeAll();
				mainPane.getChildren().setAll(page);
				page.prefWidthProperty().bind(mainPane.widthProperty());
				page.prefHeightProperty().bind(mainPane.heightProperty());
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input");
			alert.setHeaderText(null);
			alert.setContentText("Set A Date And Fill All Fields");
			alert.showAndWait();
		}

	}

	public void saveOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		if (orderedProducts.size() > counter) {
			effect.setBrightness(0);
		} else {
			effect.setBrightness(0.8);
		}
		saveIcon.setEffect(effect);
	}

	public void saveOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		saveIcon.setEffect(effect);
	}

	public void saveOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		saveIcon.setEffect(effect);

	}

	public void addOnMousePressed() throws IOException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addBatchIcon.setEffect(effect);
		AddBatchController addBatchController = new AddBatchController();
		addBatchController.setProduct(supplierOrder.getID(), ProductName, this);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBatch.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		saveIcon.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		saveIcon.setEffect(effect);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		saveIcon.setEffect(effect);
	}

	public void updateTable() throws ClassNotFoundException, SQLException, ParseException {

		// upload batches to the listview
		
		productsBatchColumn.setItems(
				FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult(
						"select * from Batch where product_id =? and batch_production_date <> '1111-01-01';"
						,new ArrayList<>(Arrays.asList(orderedProducts.get(counter).getPID()+""))))));
		// set order number
		orderNumberLabel.setText("Order NO." + supplierOrder.getID());

		// get products ids
		ArrayList<Product> productName = Product.getProductData(Queries.queryResult("select * from Product where product_id =?;"
				,new ArrayList<>(Arrays.asList(orderedProducts.get(counter).getPID()+""))));

		ProductName = productName.get(0).getName();
		ProductId = orderedProducts.get(counter).getPID()+"";
		QuantityStored = orderedProducts.get(counter).getAmount() + "";

		choosenProductLabel.setText(productName.get(0).getName());
		savedQuantityLabel.setText("Quantity = " + orderedProducts.get(counter).getAmount() + "");

	}

	@SuppressWarnings("exports")
	public void setRow(SupplierOrder supplierOrder) throws ClassNotFoundException, SQLException, ParseException {
		// this.caller = caller;
		this.supplierOrder = supplierOrder; // order id
		// get products
		
		orderedProducts = SupplierOrderBatch.getSupplierOrderBatchData(Queries.queryResult("select * from s_order_batch where order_id =?;"
				,new ArrayList<>(Arrays.asList(supplierOrder.getID()+""))));
		// upload first product batches
		productsBatchColumn.setItems(
				FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult("select * from batch where product_id =? and Batch_Production_Date <> '1111-01-01';"
						,new ArrayList<>(Arrays.asList(orderedProducts.get(0).getPID()+""))))));

		orderNumberLabel.setText("Order NO." + supplierOrder.getID());
		
		ArrayList<Product> productName = Product.getProductData(Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer "
				+ " from product p,Name_manu m where P.product_name=m.product_name and Product_ID =?;"
				,new ArrayList<>(Arrays.asList(orderedProducts.get(0).getPID()+""))));
		
		choosenProductLabel.setText(productName.get(0).getName());
		savedQuantityLabel.setText("Quantity = " + orderedProducts.get(0).getAmount() + "");
		ProductName = productName.get(0).getName();
		ProductId = orderedProducts.get(0).getPID()+"";
		QuantityStored = orderedProducts.get(0).getAmount() + "";

	}

	public void confirmBatchOnAction(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		if (orderedProducts.size() > counter) {
			if ( orderedProducts.size() > counter && productsBatchColumn.getSelectionModel().getSelectedItem() != null) {
				ArrayList<String> productsEntered = new ArrayList<String>();
				if (confirmQuantityCheckBox.isSelected() == true) {
					quantityTextField.setOpacity(0);
					productsEntered.add(ProductId);
					productsEntered.add(ProductName);
					productsEntered.add(productsBatchColumn.getSelectionModel().getSelectedItem().getProductionDate().toString());
					productsEntered.add(productsBatchColumn.getSelectionModel().getSelectedItem().getExpiryDate().toString());
					productsEntered.add(QuantityStored);

					if (orderedProducts.size() > counter && productsEntered.isEmpty() == false) {
						data.add(productsEntered);
						orderProductTabel.setItems(FXCollections.observableArrayList(data));
						++counter;
						if (orderedProducts.size() > counter)
							updateTable();

					}
					confirmQuantityCheckBox.setSelected(false);
				} else {
					if (quantityTextField.getText().isBlank() == true) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Empty Field!");
						alert.setHeaderText(null);
						alert.setContentText("You Have To Enter The New Quantity!");
						alert.showAndWait();
					} else {
						int Qtemp = Integer.parseInt(quantityTextField.getText());
						productsEntered.add(ProductId);
						productsEntered.add(ProductName);
						productsEntered.add(
								productsBatchColumn.getSelectionModel().getSelectedItem().getProductionDate().toString());
						productsEntered
								.add(productsBatchColumn.getSelectionModel().getSelectedItem().getExpiryDate().toString());
						productsEntered.add(Qtemp + "");

						if (orderedProducts.size() > counter && productsEntered.isEmpty() == false) {
							data.add(productsEntered);
							orderProductTabel.setItems(FXCollections.observableArrayList(data));
							++counter;
							if (orderedProducts.size() > counter)
								updateTable();

						}
					}
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Empty Field!");
				alert.setHeaderText(null);
				alert.setContentText("You Have To Select A Batch");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("You Have To Save The Data");
			alert.showAndWait();
		}
	}

	public void cancelOnAction(ActionEvent e) throws IOException {
		Parent page = FXMLLoader.load(getClass().getResource("UnReceivedOrders.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		if(confirmQuantityCheckBox.isSelected()==true) {
			
			quantityTextField.setOpacity(0);

		}else {
			quantityTextField.setOpacity(1);
		}
		
		batchProductionDateColumn.setCellValueFactory(new PropertyValueFactory<Batch, String>("productionDate"));
		batchExpiryDateColumn.setCellValueFactory(new PropertyValueFactory<Batch, String>("expiryDate"));

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

		expiryDateColumn.setCellValueFactory(
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

		quantityColumn.setCellValueFactory(
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
		productionDateColumn.setCellValueFactory(
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
	}

}
