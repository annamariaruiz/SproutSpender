package pro100.group10.sproutspender.views;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Database;

public class Credentials {
	
	private Stage credit = new Stage();
	private String username;
	private String password;
	private Manager mana;

	public void init() {
		
		credit.setTitle("Create a New User");
		credit.setResizable(false);
		
		mana = HomeController.manager;
		VBox box = new VBox();
		
		TextField user = new TextField();
		TextField pass = new TextField();
		Button create = new Button("Create User");
		
		box.getChildren().addAll(user, pass, create);	
		
		create.setOnAction(new EventHandler<ActionEvent>() { 
			@Override
            public void handle(ActionEvent e) { 
            	if(!createCredentials(user.getText(), pass.getText())) {
            		
            	}
            } 
		});
		Scene scene = new Scene(box, 200, 200);
//		scene.getStylesheets().add(getClass().getResource("../views/application.css").toString());
		credit.setResizable(false);
		credit.setScene(scene);
		credit.show();
	}
	
	private boolean createCredentials(String user, String pass) {
		boolean success = true;
		Database db = mana.db;
		db.setConnection(username, password, "master");
		Connection con = db.getConnection();
		String createLog = "CREATE LOGIN " + user + " WITH PASSWORD = '" + pass + "'";
    	try(Statement stmt = con.createStatement()) {
    		stmt.executeUpdate(createLog);
    	} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
    	return success;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}