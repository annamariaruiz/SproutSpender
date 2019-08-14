package pro100.group10.sproutspender.views;

import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pro100.group10.sproutspender.models.Bill;
import pro100.group10.sproutspender.models.Bill.TimeFrame;
import pro100.group10.sproutspender.models.Database;

public class Bills {
	
	private Stage window = new Stage();
	private Scene scene;
	private TableView<Bill> tableView = new TableView<>();
	private Database db;
	private ObservableList<Bill> listedBills = FXCollections.observableArrayList();
	
	@FXML
	private TextField nameOfBill;
	@FXML
	private TextField amount;
	@FXML
	private DatePicker nextDate;
	@FXML
	private ChoiceBox timeFrame;
	@FXML
	private CheckBox paid;
	
	
	public void init() {
		window.setTitle("View Your Bills");
		window.setResizable(false);
		
		//columns
		TableColumn<Bill, String> name = new TableColumn<>("Name");
		name.setMinWidth(250);
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Bill, Float> amount = new TableColumn<>("Amount");
		amount.setMinWidth(75);
		amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		
		TableColumn<Bill, Date> date = new TableColumn<>("Date");
		date.setMinWidth(100);
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<Bill, TimeFrame> timeFrame = new TableColumn<>("Time Frame");
		timeFrame.setMinWidth(100);
		timeFrame.setCellValueFactory(new PropertyValueFactory<>("timeFrame"));
		
		TableColumn<Bill, Boolean> paid = new TableColumn<>("Paid");
		paid.setMinWidth(25);
		paid.setCellValueFactory(new PropertyValueFactory<>("paid"));
		
		tableView.setItems(getBills());
		tableView.getColumns().addAll(name, amount, date, timeFrame, paid);
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
		
		scene = new Scene(vertical, 600, 400);
		window.setScene(scene);
		window.show();
		
		
//		try {
//			Parent root = FXMLLoader.load(getClass().getResource("../views/Bills.fxml"));
//
//			scene = new Scene(root, 600, 400);
//			window.setResizable(false);
//			window.setScene(scene);
//			window.show();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	private void editBill() {
		window.setTitle("Add Bill");
		window.setResizable(false);
		try {
			GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("../views/AddBill.fxml"));
			Scene scene = new Scene(root,500,375);
			window.setScene(scene);
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveEditBill() {
		//compare changes made to bill
		//if no change made, leave alone
		//if change made to field, change that one field
		//save to database
	}

	private void deleteBill() {
		ObservableList<Bill> billSelected, allBills;
		allBills = tableView.getItems();
		billSelected = tableView.getSelectionModel().getSelectedItems();
		
		for(Bill bill : billSelected) {
			bill.getId();
			//removeBIll(id); - Database
			//Manager.update();
		}
		
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
			e.printStackTrace();
		}
	}
	
	private void saveNewBill() {
		//create new bill
		Bill newBill = new Bill();
		
		//add fields
		
		//save to database
		//HomeController.getDatabase();
		//Database.creatBi(newBill);
		//Manager.update();
		
	}

	private ObservableList<Bill> getBills() {
		listedBills.clear();
		int limit = 5; //# of rows in bill table in database
		for(int i=0;i<limit;i++) {
			//get bills from table
		}
		return listedBills;
	}
}
