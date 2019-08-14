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

public class HomeController {
	
	private Manager manager = new Manager();
	
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
		boolean valid = manager.isValid(dbName.getText().trim());
		boolean empty = dbName.getText().trim().isEmpty();
		
		
		Stage stage = Main.getStage();
		
		if(valid && !empty) {
			alert.setText("");
			
			//create database
			Database db = new Database();
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
			Parent root = FXMLLoader.load(getClass().getResource("../views/TableStage.fxml"));
			Scene scene = new Scene(root,690,630);
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
}
