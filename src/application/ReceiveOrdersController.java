package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	private TableView<ArrayList<String>> orderProducts;

	@FXML
	private TableColumn<ArrayList<String>, String> amount;

	@FXML
	private TableColumn<Batch, String> bedate;

	@FXML
	private TableColumn<Batch, String> bpdate;

	@FXML
	private Button cancel;

	@FXML
	private Button confirmBatch;

	@FXML
	private CheckBox confirmq;

	@FXML
	private TableColumn<ArrayList<String>, String> eDate;

	@FXML
	private TableColumn<ArrayList<String>, String> pid;

	@FXML
	private Label errorLabel;

	@FXML
	private TableColumn<ArrayList<String>, String> pDate;

	@FXML
	private TableColumn<ArrayList<String>, String> product;

	@FXML
	private TextField quantity;

	@FXML
	private TextField cost;

	@FXML
	private DatePicker receivedDate;

	@FXML
	private DatePicker payDueDate;

	@FXML
	private Label savedQ;

	SupplierOrder supplierOrder;

	UnReceivedController caller;

	@FXML
	private TableView<Batch> ProductsBatch;

	private static ArrayList<SupplierOrderBatch> orderedProducts;

	private static ArrayList<ArrayList<String>> data = new ArrayList<>();

	@FXML
	private Label orderNO;

	@FXML
	private Label choosenProduct;

	@FXML
	private Label warning;

	private String ProductName;

	private String ProductId;

	private String QuantityStored;

	@FXML
	private ImageView save;

	@FXML
	private ImageView addBatch;

	@FXML
	private StackPane mainPane;

	public void saveOnMousePressed() throws IOException, ClassNotFoundException, SQLException {
		if (receivedDate.getValue() != null && payDueDate.getValue() != null) {

			if (receivedDate.getValue().compareTo(payDueDate.getValue()) == 0
					|| receivedDate.getValue().compareTo(payDueDate.getValue()) > 0
					|| supplierOrder.getDateOfOrder().compareTo(payDueDate.getValue()) > 0
					&& receivedDate.getValue().compareTo(LocalDate.now()) > 0) {
				warning.setText("Set A Reasonable Date");

			} else if (orderedProducts.size() == counter && receivedDate.getValue() != null
					&& payDueDate.getValue() != null) {
				warning.setText("");
				ColorAdjust effect = new ColorAdjust();
				effect.setBrightness(0.8);
				save.setEffect(effect);

				if (cost.getText().isBlank() == false) {

					Queries.queryUpdate(
							"update s_order set Recieved_by=? , Recieved_Date=? , order_cost =?  where order_id=? ;",
							new ArrayList<>(Arrays.asList(Employee.getCurrentID()+"", receivedDate.getValue().toString(),
									cost.getText(), supplierOrder.getID()+"")));
					Queries.queryUpdate("update Supplier set supplier_dues =? where Supplier_ID =?;"
			                  ,new ArrayList<>(Arrays.asList(cost.getText(),supplierOrder.getSupplierID()+"")));
					
				} else {
					Queries.queryUpdate("update s_order set Recieved_by=? , Recieved_Date=? where order_id=? ;",
							new ArrayList<>(Arrays.asList(Employee.getCurrentID()+"", receivedDate.getValue().toString(),
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

				Parent page = FXMLLoader.load(getClass().getResource("UnReceivedOrders.fxml"));
				mainPane.getChildren().removeAll();
				mainPane.getChildren().setAll(page);
			}
		} else {
			warning.setText("Set A Date And Fill All Fields");
		}

	}

	public void saveOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		if (orderedProducts.size() > counter) {
			effect.setBrightness(0);
		} else {
			effect.setBrightness(0.8);
		}
		save.setEffect(effect);
	}

	public void saveOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		save.setEffect(effect);
	}

	public void saveOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		save.setEffect(effect);

	}

	public void addOnMousePressed() throws IOException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addBatch.setEffect(effect);
		AddBatchController addBatchController = new AddBatchController();
		addBatchController.setPID(supplierOrder.getID()+"", ProductName);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBatch.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();

	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		save.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		save.setEffect(effect);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		save.setEffect(effect);
	}

	public void updateTable() throws ClassNotFoundException, SQLException, ParseException {

		// upload batches to the listview
		
		ProductsBatch.setItems(
				FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult(
						"select * from Batch where product_id =? and batch_production_date <> '1111-01-01';"
						,new ArrayList<>(Arrays.asList(orderedProducts.get(counter).getPID()+""))))));
		// set order number
		orderNO.setText("Order NO." + supplierOrder.getID());

		// get products ids
		ArrayList<Product> productName = Product.getProductData(Queries.queryResult("select * from Product where product_id =?;"
				,new ArrayList<>(Arrays.asList(orderedProducts.get(counter).getPID()+""))));

		ProductName = productName.get(0).getName();
		ProductId = orderedProducts.get(counter).getPID()+"";
		QuantityStored = orderedProducts.get(counter).getAmount() + "";

		choosenProduct.setText(productName.get(0).getName());
		savedQ.setText("Quantity = " + orderedProducts.get(counter).getAmount() + "");

	}

	public void setRow(SupplierOrder supplierOrder) throws ClassNotFoundException, SQLException, ParseException {
		// this.caller = caller;
		this.supplierOrder = supplierOrder; // order id
		// get products
		
		orderedProducts = SupplierOrderBatch.getSupplierOrderBatchData(Queries.queryResult("select * from s_order_batch where order_id =?;"
				,new ArrayList<>(Arrays.asList(supplierOrder.getID()+""))));
		// upload first product batches
		ProductsBatch.setItems(
				FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult("select * from batch where product_id =? and Batch_Production_Date <> '1111-01-01';"
						,new ArrayList<>(Arrays.asList(orderedProducts.get(0).getPID()+""))))));

		orderNO.setText("Order NO." + supplierOrder.getID());
		
		ArrayList<Product> productName = Product.getProductData(Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer "
				+ " from product p,Name_manu m where P.product_name=m.product_name and Product_ID =?;"
				,new ArrayList<>(Arrays.asList(orderedProducts.get(0).getPID()+""))));
		
		choosenProduct.setText(productName.get(0).getName());
		savedQ.setText("Quantity = " + orderedProducts.get(0).getAmount() + "");
		ProductName = productName.get(0).getName();
		ProductId = orderedProducts.get(0).getPID()+"";
		QuantityStored = orderedProducts.get(0).getAmount() + "";

	}

	public void ConfirmAdd(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
		System.out.println(counter + "confirm1");
		if (orderedProducts.size() > counter) {
			if ( orderedProducts.size() > counter && ProductsBatch.getSelectionModel().getSelectedItem() != null) {
				warning.setText("");
				//
				ArrayList<String> productsEntered = new ArrayList<String>();
				if (confirmq.isSelected() == true) {
					System.out.println(counter + "confirmq true");

					productsEntered.add(ProductId);
					productsEntered.add(ProductName);
					productsEntered.add(ProductsBatch.getSelectionModel().getSelectedItem().getProductionDate().toString());
					productsEntered.add(ProductsBatch.getSelectionModel().getSelectedItem().getExpiryDate().toString());
					productsEntered.add(QuantityStored);

					if (orderedProducts.size() > counter && productsEntered.isEmpty() == false) {
						System.out.println(counter + "add");

						data.add(productsEntered);
						orderProducts.setItems(FXCollections.observableArrayList(data));
						++counter;
						if (orderedProducts.size() > counter)
							updateTable();

					}
					confirmq.setSelected(false);
				} else {
					System.out.println(counter + "confirmq false");
					if (quantity.getText().isBlank() == true) {
						warning.setText("You Have To Enter The New Quantity!");
					} else {
						int Qtemp = Integer.parseInt(quantity.getText());
						productsEntered.add(ProductId);
						productsEntered.add(ProductName);
						productsEntered.add(
								ProductsBatch.getSelectionModel().getSelectedItem().getProductionDate().toString());
						productsEntered
								.add(ProductsBatch.getSelectionModel().getSelectedItem().getExpiryDate().toString());
						productsEntered.add(Qtemp + "");

						if (orderedProducts.size() > counter && productsEntered.isEmpty() == false) {
							data.add(productsEntered);
							orderProducts.setItems(FXCollections.observableArrayList(data));
							++counter;
							if (orderedProducts.size() > counter)
								updateTable();

						}
					}
				}
			} else {
				warning.setText("You Have To Select A Batch");
			}
		} else {
			warning.setText("You Have To Save The Data");
		}
	}

	public void cancelOnAction(ActionEvent e) throws IOException {
		Parent page = FXMLLoader.load(getClass().getResource("UnReceivedOrders.fxml"));
		mainPane.getChildren().removeAll();
		mainPane.getChildren().setAll(page);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		bpdate.setCellValueFactory(new PropertyValueFactory<Batch, String>("productionDate"));
		bedate.setCellValueFactory(new PropertyValueFactory<Batch, String>("expiryDate"));

		pid.setCellValueFactory(
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
		product.setCellValueFactory(
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

		eDate.setCellValueFactory(
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

		amount.setCellValueFactory(
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
		pDate.setCellValueFactory(
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
