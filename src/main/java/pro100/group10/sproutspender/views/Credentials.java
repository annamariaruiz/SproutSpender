package pro100.group10.sproutspender.views;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.User;

public class Credentials {
	
	private Stage credit = new Stage();
	private String username;
	private String password;
	private Manager mana;
	private Database db;

	public void init(Database db) {
		this.db = db;
		
		credit.setTitle("Create a New User");
		credit.setResizable(false);
		
		mana = HomeController.manager;
		GridPane grid = new GridPane();
		
		Label intro = new Label("Create Your User!");
		Label us = new Label("Username");
		Label pw = new Label("Password");
		TextField user = new TextField();
		PasswordField pass = new PasswordField();
		Button create = new Button("Create User");
		
		grid.add(intro, 1, 0);
		grid.add(us , 0, 1);
		grid.add(pw, 0, 2);
		grid.add(user , 1, 1);
		grid.add(pass, 1, 2);
		grid.add(create, 1, 3);
		
		create.setOnAction(new EventHandler<ActionEvent>() { 
			@Override
            public void handle(ActionEvent e) { 
            	boolean exit = createCredentials(user.getText(), pass.getText());
            	if(exit) {
            		credit.close();
            	}
            }
		});
		
		pass.setOnKeyPressed(key -> {
			if(key.getCode().equals(KeyCode.ENTER)) {
				if(createCredentials(user.getText(), pass.getText())) {
					credit.close();
				}
			}
		});
		
		Scene scene = new Scene(grid, 400, 400);
//		scene.getStylesheets().add(getClass().getResource("../views/application.css").toString());
		credit.setResizable(false);
		credit.setScene(scene);
		credit.show();
	}
	
	private boolean createCredentials(String user, String pass) {
		Alert a = new Alert(AlertType.ERROR);
		a.setContentText("Account already exists with that username");
		
		db.setConnection("admin", "admin", "SproutSpenderDB");
		User u = new User(user, pass);
		boolean validate = true;
		try {
			validate = db.createUser(u);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!validate) a.show();
		return validate;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}