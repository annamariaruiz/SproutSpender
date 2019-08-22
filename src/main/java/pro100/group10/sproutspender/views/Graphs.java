package pro100.group10.sproutspender.views;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;

public class Graphs {
	
	private Stage graphs = new Stage();

	public void init() {
		
		graphs.setTitle("Progress");
		graphs.setResizable(false);
		
		GridPane grid = new GridPane();
		GridPane upperGra = createBigGraph(); 
		HBox catGraphs = new HBox();
		
		Manager man = HomeController.manager;
		Budget[] budg = man.getBudgets();
		for(Budget b : budg) {
			if(b != null) {
				catGraphs.getChildren().add(createSmallGraph(b.getCategory().toString()));
			}
		}
		upperGra.setLayoutY(100);
		upperGra.setLayoutY(100);
		upperGra.setMinSize(400, 400);
		catGraphs.setMaxHeight(300);
		
		grid.add(upperGra, 0, 0);
		grid.add(catGraphs, 0, 1);
		
		Scene scene = new Scene(grid, 800, 800);
		graphs.setResizable(false);
		graphs.setScene(scene);
		graphs.show();
	}
	
	private GridPane createBigGraph() {
		GridPane upperGra = new GridPane();
		
		ArrayList<String> categories = new ArrayList<String>();
		Manager man = HomeController.manager;
		Database db = man.db;
		man.update(db);
		float values[] = new float[5];
		
		Budget[] budg = man.getBudgets();
		float current = 0;
		float limit = 0;
		float remainder = 0;
		int i = 0;
		for(Budget b : budg) {
			if(b != null) {
				categories.add(b.getCategory().toString());
				values[i] = b.getCurrentAmount();
				limit += b.getLimit();
				current += b.getCurrentAmount();
				i++;
			}
		}
			remainder = limit - current;
			values[i] = remainder;
		
		
		PieChart.Data pieData[] = new PieChart.Data[categories.size() + 1];
		for (i = 0; i <= categories.size(); i++) {
			if(i == categories.size()) {
				pieData[i] = new PieChart.Data("AVAILABLE", values[i]);
			} else {
				pieData[i] = new PieChart.Data(categories.get(i), values[i]);
			}
			
		}
		PieChart chart = new PieChart(FXCollections.observableArrayList(pieData));
		chart.setLabelsVisible(false);
		chart.setLegendSide(Side.RIGHT);
		
		Label caption = new Label("");
		caption.setTextFill(Color.BLACK);
		caption.setStyle("-fx-font: 24 arial;");

		upperGra.add(chart, 0, 1);
		upperGra.add(caption, 0, 0);
		
		for (final PieChart.Data d : pieData) {
			d.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					caption.setTranslateX(e.getSceneX() + 10);
					caption.setTranslateY(e.getSceneY());
					String pieV = String.format("%.2f", d.getPieValue());
					pieV = "$" + String.valueOf(pieV);
					caption.setText(pieV);
				}
			});
		}
		return upperGra;
	}
	
	private VBox createSmallGraph(String cat) {
		VBox individ = new VBox();
		
		Manager man = HomeController.manager;
		Database db = man.db;
		man.update(db);
		float values[] = new float[2];
		
		Budget[] budg = man.getBudgets();
		float current = 0;
		float limit = 0;
		float remainder = 0;
		int i = 0;
		for(Budget b : budg) {
			if(b != null && b.getCategory().toString().equalsIgnoreCase(cat)) {
				values[i] = b.getCurrentAmount();
				limit += b.getLimit();
				current += b.getCurrentAmount();
				i++;
			}
		}
			remainder = limit - current;
			values[i] = remainder;
		
		Label l = new Label(cat);
		l.setTranslateX(200);
		PieChart.Data pieData[] = new PieChart.Data[2];
		for (i = 0; i < 2; i++) {
			if(i == 1) {
				pieData[i] = new PieChart.Data("AVAILABLE", values[i]);
			} else {
				pieData[i] = new PieChart.Data("SPENT", values[i]);
			}
			
		}
		PieChart chart = new PieChart(FXCollections.observableArrayList(pieData));
		chart.setLabelsVisible(false);
		chart.setLegendSide(Side.TOP);
		
		Label caption = new Label("");
		caption.setTextFill(Color.BLACK);
		caption.setStyle("-fx-font: 24 arial;");

		
		for (final PieChart.Data d : pieData) {
			d.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					caption.setTranslateX(e.getSceneX() + 10);
					caption.setTranslateY(e.getSceneY());
					String pieV = String.format("%.2f", d.getPieValue());
					pieV = "$" + String.valueOf(pieV);
					caption.setText(pieV);
				}
			});
		}
		
		individ.getChildren().addAll(l, chart, caption);
		return individ;
	}

}
