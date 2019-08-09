package pro100.group10.sproutspender.views;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.FloatEditingCell;

public class Table {

	@FXML
	private TableView<Budget> tableView;
	@FXML
	private TableColumn<Budget, Float> day1Col;
	@FXML
	private TableColumn<Budget, Float> day2Col;
	@FXML
	private TableColumn<Budget, Float> day3Col;
	@FXML
	private TableColumn<Budget, Float> day4Col;
	@FXML
	private TableColumn<Budget, Float> day5Col;
	@FXML
	private TableColumn<Budget, Float> day6Col;
	@FXML
	private TableColumn<Budget, Float> day7Col;
	
	@FXML
	private TextField makeNewDay1;
	@FXML
	private TextField makeNewDay2;
	@FXML
	private TextField makeNewDay3;
	@FXML
	private TextField makeNewDay4;
	@FXML
	private TextField makeNewDay5;
	@FXML
	private TextField makeNewDay6;
	@FXML
	private TextField makeNewDay7;
	
	@FXML
	private Button settings;
	@FXML
	private Button bills;
	@FXML
	private Button graphs;
	
	private int lastHundredViewed = 0;
	private boolean tableIsEditable;
	private Date endDate;
	
	private Database db;
	
	
	private void prepTableForInput() {
		tableView.setEditable(tableIsEditable);
		
		@SuppressWarnings("unchecked")
		TableColumn<Budget, Float>[] columns = (TableColumn<Budget, Float>[]) new TableColumn[7];
		columns[0] = day1Col;
		columns[1] = day2Col;
		columns[2] = day3Col;
		columns[3] = day4Col;
		columns[4] = day5Col;
		columns[5] = day6Col;
		columns[6] = day7Col;
		
		Callback<TableColumn<Budget, Float>, TableCell<Budget, Float>> floatCellFactory =
				new Callback<TableColumn<Budget, Float>, TableCell<Budget, Float>>() {
					public TableCell<Budget, Float> call(TableColumn<Budget, Float> p) {
						return new FloatEditingCell();
					}
				};
		
		for(TableColumn<Budget, Float> col : columns) {
			day1Col.setCellValueFactory(new PropertyValueFactory<Budget, Float>("currentAmount"));
			day1Col.setCellFactory(floatCellFactory);
			
			col.setOnEditCommit(new EventHandler<CellEditEvent<Budget, Float>>() {
				@Override
				public void handle(CellEditEvent<Budget, Float> t) {
					Budget b = (Budget) t.getTableView().getItems().get(
							t.getTablePosition().getRow());
					b.setCurrentAmount(t.getNewValue());
				
					try {
						db.update(b);
					} catch(SQLException sqle) {
						Alert alert = new Alert(AlertType.ERROR, "The budget with I.D. " + b.getID() + " could not be updated in the M.S. S.Q.L. database.\n" + sqle, ButtonType.CLOSE);
						Optional<ButtonType> response = alert.showAndWait();
					}
				}
			});
		}
	}
	
	private void onMenuItemEditMode() {
		tableIsEditable = !tableIsEditable;
		tableView.setEditable(tableIsEditable);
	}
	
	@FXML
	private void onLastButtonClick(ActionEvent ae) {
		if(lastHundredViewed > 0) {
			ObservableList<Budget> lastHundredBudgets = parseLastWeek();
			
			if(!lastHundredBudgets.isEmpty()) {
				tableView.setItems(lastHundredBudgets);
			}	
		}
	}
	
	@FXML
	private void onNextButtonClick(ActionEvent ae) {
		try {
			if(lastHundredViewed < db.size()) {
				ObservableList<Budget> nextHundredBudgets = parseNextWeek(); 
			
				if(!nextHundredBudgets.isEmpty()) {
					tableView.setItems(nextHundredBudgets);			
				}
			}
		} catch(SQLException sqle) {
			Alert alert = new Alert(AlertType.ERROR, "The size of the S.Q.L. database could not be determined.\n" + sqle, ButtonType.CLOSE);
			Optional<ButtonType> response = alert.showAndWait();
			sqle.printStackTrace();
		}
	}
	
	@FXML
	private void onSettingsButtonClick(ActionEvent ae) {
		Settings s = new Settings();
		s.init();
	}
	
	@FXML
	private void onGraphButtonClick(ActionEvent ae) {
		//open graphs window
		System.out.println("graph");
	}
	
	@FXML
	private void onBillsButtonClick(ActionEvent ae) {
		//open bills window
		System.out.println("bill was here");
	}
	
	private ObservableList<Budget> parseLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		Date dateToGrab = null;
		
		for(int i = 0; i < 7; i++) {
			calendar.add(Calendar.DATE, -1);
			dateToGrab = (Date) calendar.getTime();
			
		}
		
		return null;
	}
	
	private ObservableList<Budget> parseNextWeek() {
		return null;
	}
}
