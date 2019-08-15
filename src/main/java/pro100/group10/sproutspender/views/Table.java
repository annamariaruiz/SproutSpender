package pro100.group10.sproutspender.views;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
import javafx.util.Callback;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Budget.CategoryType;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.FloatEditingCell;
import pro100.group10.sproutspender.models.WeeklyPlanner;

public class Table {

	@FXML
	private TableView<WeeklyPlanner> tableView;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day1Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day2Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day3Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day4Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day5Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day6Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day7Col;
	TableColumn<WeeklyPlanner, Budget>[] columns;
	
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
	
	private boolean hasInitialized = false;
	private boolean tableIsEditable = false;
	private Date endDate = Date.valueOf(LocalDate.now());
	
	private Database db;
	
	public Database getDB() {
		return db;
	}

	public void setDB(Database db) {
		this.db = db;
	}

	@SuppressWarnings("unchecked")
	@FXML
	private void initialize() {
		if(!hasInitialized) {
			hasInitialized = true;
			tableView.setEditable(tableIsEditable);
			
			columns = (TableColumn<WeeklyPlanner, Budget>[]) new TableColumn[7];
			columns[0] = day1Col;
			columns[1] = day2Col;
			columns[2] = day3Col;
			columns[3] = day4Col;
			columns[4] = day5Col;
			columns[5] = day6Col;
			columns[6] = day7Col;
			
			Callback<TableColumn<WeeklyPlanner, Budget>, TableCell<WeeklyPlanner, Budget>> floatCellFactory =
					cb -> new FloatEditingCell();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			Date dateToGrab = null;
			
			for(int i = 6; i >= 0; i--) {
				dateToGrab = new Date(calendar.getTime().getTime());
				columns[i].setText(dateToGrab.toString());
				calendar.add(Calendar.DATE, -1);
				
				final int index = i;
				columns[i].setCellValueFactory(cellData -> {
					Budget budg = cellData.getValue().getDay(index + 1);						
					return new SimpleObjectProperty<Budget>(budg);
				});
				
				columns[i].setCellFactory(floatCellFactory);
				
				columns[i].setOnEditCommit(new EventHandler<CellEditEvent<WeeklyPlanner, Budget>>() {
					@Override
					public void handle(CellEditEvent<WeeklyPlanner, Budget> t) {
						WeeklyPlanner wp = (WeeklyPlanner) t.getTableView().getItems().get(
								t.getTablePosition().getRow());
						
						try {	
							Budget budg = wp.getDay(index + 1);
							if(budg != null) {
								budg.setCurrentAmount(t.getNewValue().getCurrentAmount());
								db.update(budg);
							}
						} catch(SQLException sqle) {
							Alert alert = new Alert(AlertType.ERROR, "The budget could not be updated in the M.S. S.Q.L. database.\n" + sqle, ButtonType.CLOSE);
							Optional<ButtonType> response = alert.showAndWait();
						}
					}
				});
			}
			
			refreshTableView();
			calculateTotals();
		}
	}
	
	@FXML
	public void cleanUp() {
		db.close();
		System.out.println("Cleaned up");
	}
	
	@FXML
	private void onMenuItemExit(ActionEvent ae) {
		
	}
	
	@FXML
	private void onMenuItemEditMode(ActionEvent ae) {
		tableIsEditable = !tableIsEditable;
		tableView.setEditable(tableIsEditable);
	}
	
	@FXML
	private void onLastButtonClick(ActionEvent ae) {
		changeEndDate(-7);
		refreshTableView();
		calculateTotals();
	}
	
	@FXML
	private void onNextButtonClick(ActionEvent ae) {
		changeEndDate(7);
		refreshTableView();
		calculateTotals();
	}
	
	private void refreshTableView() {
		ObservableList<WeeklyPlanner> wpList = FXCollections.observableArrayList();
		for(CategoryType cat : Budget.categoryRank) {
			wpList.add(parseThisWeek(cat));			
		}
		tableView.setItems(wpList);
	}
	
	private void changeEndDate(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.DATE, days);
		endDate = new Date(calendar.getTime().getTime());
		Date dateToGrab = null;
		
		for(int i = 6; i >= 0; i--) {
			dateToGrab = new Date(calendar.getTime().getTime());
			columns[i].setText(dateToGrab.toString());
			calendar.add(Calendar.DATE, -1);
		}
	}
	
	private WeeklyPlanner parseThisWeek(CategoryType category) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		Date dateToGrab = null;
		WeeklyPlanner thisWeek = new WeeklyPlanner();
		Budget budg = null;
		
		for(int i = 6; i >= 0; i--) {
			dateToGrab = new Date(calendar.getTime().getTime());
			try {
				budg = db.lookUpByDayAndCat(dateToGrab, category);
			} catch(SQLException sqle) {
				//TODO write catch block
			}
			
			if(budg != null) thisWeek.setDay(i + 1, budg);
			calendar.add(Calendar.DATE, -1);
		}
		return thisWeek;
	}
	
	private void calculateTotals() {
		ObservableList<WeeklyPlanner> wpList = tableView.getItems();
		WeeklyPlanner genWP = new WeeklyPlanner();
		for(int i = 0; i < columns.length; i++) {
			genWP.setDay(i + 1, new Budget());
			Budget genBudgForDay = genWP.getDay(i + 1);
			genBudgForDay.setCategory(CategoryType.GENERAL);
			for(int j = 0; j < Budget.categoryRank.length - 1; j++) {
				Budget catBudgForDay = columns[i].getCellData(j);
				if(catBudgForDay != null) {
					genBudgForDay.setCurrentAmount(genBudgForDay.getCurrentAmount() + catBudgForDay.getCurrentAmount());
					genBudgForDay.setLimit(genBudgForDay.getLimit() + catBudgForDay.getLimit());
				}
			}
		}
		
		if(wpList.size() > Budget.categoryRank.length - 1) {
			wpList.remove(Budget.categoryRank.length - 1);
		}
		
		wpList.add(genWP);
		
	}
	
	@FXML
	private void onSettingsButtonClick(ActionEvent ae) {
		Settings s = new Settings();
		s.init();
	}
	
	@FXML
	private void onGraphButtonClick(ActionEvent ae) {
		//open graphs window
		Graphs g = new Graphs();
		g.init();
	}
	
	@FXML
	private void onBillsButtonClick(ActionEvent ae) {
		Bills b = new Bills();
		b.init();
	}
}
