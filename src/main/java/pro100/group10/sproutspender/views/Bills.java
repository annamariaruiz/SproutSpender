package pro100.group10.sproutspender.views;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Bill;
import pro100.group10.sproutspender.models.Bill.TimeFrame;
import pro100.group10.sproutspender.models.Database;

public class Bills {
	
	private static Stage window = new Stage();
	private static Scene primScene;
	private TableView<Bill> tableView = new TableView<>();
	public static Database db;
	private Manager man = HomeController.manager;
	private ObservableList<Bill> listedBills = FXCollections.observableArrayList();
	private ObservableList<TimeFrame> enums = FXCollections.observableArrayList(Bill.TimeFrame.values());
	private Bill currentSelected = null;
	
	
//	public Database getDB() {
//		return db;
//	}
//	
//	public void setDB(Database db) {
//		this.db = db;
//	}
	
	@FXML
	private TextField nameOfBill;
	@FXML
	private TextField amount;
	@FXML
	private DatePicker nextDate;
	@FXML
	private CheckBox paid;
	@FXML
	private Button saveNewBill;
	@FXML
	private Button saveEditBill;
	@FXML
	private Label error;
	@FXML
    private ComboBox<Bill.TimeFrame> timetype;
	
	public void initialize() {
		timetype.getItems().removeAll(timetype.getItems());
	    timetype.getItems().addAll(enums);
	    timetype.getSelectionModel().select(enums.get(0));
	}
	
	public void init() {
		window.setTitle("View Your Bills");
		window.setResizable(false);
		
		//columns
		TableColumn<Bill, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.setMinWidth(250);
		
		TableColumn<Bill, Float> amount = new TableColumn<>("Amount");
		amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		amount.setMinWidth(75);
		
		TableColumn<Bill, Date> date = new TableColumn<>("Date");
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		date.setMinWidth(100);
		
		TableColumn<Bill, TimeFrame> timeFrame = new TableColumn<>("Time Frame");
		timeFrame.setCellValueFactory(new PropertyValueFactory<>("timeFrame"));
		timeFrame.setMinWidth(100);
		
		TableColumn<Bill, Boolean> paid = new TableColumn<>("Paid");
		paid.setCellValueFactory(new PropertyValueFactory<>("paid"));
		paid.setMinWidth(25);
		
		tableView.getColumns().addAll(name, amount, date, timeFrame, paid);
		tableView.setItems(getBills());
		tableView.setEditable(true);
		tableView.setMinHeight(50);
		
		//add and delete
		Button add = new Button("Add");
		add.setOnAction(e -> {
			addBill();
		});
		
		Button delete = new Button("Delete");
		delete.setOnAction(e -> {
			deleteBill();
		});
		
		Button edit = new Button("Edit");
		edit.setOnAction(e -> {
			editBill();
		});
		
		HBox buttons = new HBox();
		buttons.setPadding(new Insets(10, 20, 10, 20));
		buttons.setSpacing(10);
		buttons.getChildren().addAll(add, delete, edit);
		
		VBox vertical = new VBox();
		vertical.getChildren().addAll(tableView, buttons);
		
		primScene = new Scene(vertical, 600, 400);
		window.setScene(primScene);
		window.show();
	}
	
	private void editBill() {
//		currentSelected = tableView.getSelectionModel().getSelectedItem();
//		nameOfBill.setText(currentSelected.getName());
//		amount.setText(currentSelected.getAmount() + "");
//		LocalDate date = currentSelected.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		nextDate.setValue(date);
//		timetype.setValue(currentSelected.getTimeFrame());		
		
		window.setTitle("Add Bill");
		window.setResizable(false);
		try {
			GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("../views/EditBill.fxml"));
			Scene scene = new Scene(root,500,375);
			window.setScene(scene);
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void saveEditBill() {
		if (!nameOfBill.getText().isEmpty() && !amount.getText().isEmpty() && nextDate.getValue() != null && timetype.getValue() != null) {
			//compare changes made to bill
			//if no change made, leave alone
			//if change made to field, change that one field
			//save to database
			tableView.setItems(getBills());
			window.setScene(primScene);
			window.show();
			
				
		} else {
			error.setText("Values must not be empty");
		}
	}

	private void deleteBill(){
		ObservableList<Bill> billSelected, allBills;
		allBills = tableView.getItems();
		billSelected = tableView.getSelectionModel().getSelectedItems();
		
		for(Bill bill : billSelected) {
			bill.getId();
			try {
				db.removeBill(bill.getId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			man.update(db);
		}

		tableView.setItems(getBills());
		billSelected.forEach(allBills::remove);
	}

	private void addBill() {
		window.setTitle("Add Bill");
		window.setResizable(false);
		try {
			GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("../views/AddBill.fxml"));
			Scene scene = new Scene(root,500,375);
			window.setScene(scene);
			window.show();
		} catch(Exception e) {
			System.out.println(timetype.getValue());
		}
	}
	
	@FXML
	private void saveNewBill() throws SQLException {
		//check if fields are empty
		if (!nameOfBill.getText().isEmpty() && !amount.getText().isEmpty() && nextDate.getValue() != null && timetype.getValue() != null) {
			//create new bill
			Bill newBill = new Bill();
			
			//store values
			String newName = nameOfBill.getText().trim();
			String newamount = amount.getText();
			float floatAmount = Float.valueOf(newamount.trim()).floatValue();
			Date date = java.sql.Date.valueOf(nextDate.getValue());
			Bill.TimeFrame type = timetype.getValue();
			
			//add fields
			newBill.setName(newName);
			newBill.setAmount(floatAmount);
			newBill.setDate((java.sql.Date) date);
			newBill.setTimeFrame(type);
			
			//save to database
			db.createBi(newBill);
			man.update(db);
			tableView.setItems(getBills());
			window.setScene(primScene);
			window.show();
		} else {
			error.setText("Values must not be empty");
		}
		
	}

	private ObservableList<Bill> getBills() {
		listedBills.clear();
		HashMap<String, Bill> temp = db.selectAllBills();
		int limit = temp.size();
		
		for (String bill : temp.keySet()) {
			listedBills.add(temp.get(bill));
		}
		
		return listedBills;
	}
}
