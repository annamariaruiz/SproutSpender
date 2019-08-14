package pro100.group10.sproutspender.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pro100.group10.sproutspender.app.Main;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.views.Table;

public class HomeController {
	
	private Manager m = new Manager();
	
	@FXML
	private Button start;
	
	@FXML
	private TextField dbName;
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField password;
	
	@FXML
	private Label alert;
	
	@FXML
	public void init() {
		boolean valid = m.isValid(dbName.getText().trim());
		boolean empty = dbName.getText().trim().isEmpty();
		
		
		Stage stage = Main.getStage();
		
		Database db = new Database();
		if(valid && !empty) {
			alert.setText("");
			
			//create database
			try {
				db.setConnection(username.getText().trim(), password.getText().trim(), dbName.getText().trim());
				db.canConnect();
				//call to open table
			} catch (RuntimeException e) {
				alert.setText("Connection failed");
			}
			
		} else if (valid && empty) {
			alert.setText("Database name must not be empty");
		} else if (!valid){
			alert.setText("Database name format incorrect");
		} else {
			System.out.println("Idk, something broke");
		}
		
		try {
			FXMLLoader tableLoader = new FXMLLoader();
			tableLoader.setLocation(getClass().getResource("../views/TableStage.fxml"));
			Table table = new Table();
			table.setDB(db);
			tableLoader.setController(table);
			Parent root = tableLoader.load();
			Scene scene = new Scene(root,690,630);
			stage.setScene(scene);
			stage.setOnHidden(eh -> table.cleanUp());
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
