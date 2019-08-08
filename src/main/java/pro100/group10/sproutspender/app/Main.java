package pro100.group10.sproutspender.app;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pro100.group10.sproutspender.views.Settings;


public class Main extends Application {
	
	private static Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../views/Home.fxml"));
			
//			HomeController controller = (HomeController)root.getController();
//			controller.setStageAndSetupListeners(stage);
//			
			Scene scene = new Scene(root,600,400);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
			
			Settings s = new Settings();
			
			s.init();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getStage() {
		return stage;
	}
}
