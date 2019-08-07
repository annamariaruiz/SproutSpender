package pro100.group10.sproutspender.views;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.StrEditingCell;

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
	
	private boolean tableIsEditable;
	
	private Database db;
	
	private void makeTablePopulatable() {		
		tableView.setEditable(tableIsEditable);
		Callback<TableColumn<Budget, String>, TableCell<Budget, String>> strCellFactory =
			new Callback<TableColumn<Budget, String>, TableCell<Budget, String>>() {
				public TableCell<Budget, String> call(TableColumn<Budget, String> p) {
					return new StrEditingCell();
				}
			};
	}
	
	private void linkTableToBackend() {
		@SuppressWarnings("unchecked")
		TableColumn<Budget, Float>[] columns = (TableColumn<Budget, Float>[]) new TableColumn[7];
		columns[0] = day1Col;
		columns[1] = day2Col;
		columns[2] = day3Col;
		columns[3] = day4Col;
		columns[4] = day5Col;
		columns[5] = day6Col;
		columns[6] = day7Col;
		
		for(TableColumn<Budget, Float> col : columns) {
			col.setOnEditCommit(new EventHandler<CellEditEvent<Budget, Float>>() {
				@Override
				public void handle(CellEditEvent<Budget, Float> t) {
					Budget b = (Budget) t.getTableView().getItems().get(
							t.getTablePosition().getRow());
					b.setCurrentAmount(t.getNewValue());
				
					try {
						
					} catch(SQLException sqle) {
						
					}
				}
			});
		}
	}
	
	private void onMenuItemEditMode() {
		
	}
	
	private void onLastButtonClick(ActionEvent ae) {
		
	}
	
	private void onNextButtonClick(ActionEvent ae) {
		
	}
	
	private ObservableList<Budget> parseLastHundredContacts() {
		return null;
	}
	
	private ObservableList<Budget> parseNextHundredContacts() {
		return null;
	}
}
