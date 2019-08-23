package pro100.group10.sproutspender.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;

public class Graphs {
	
	private Stage graphs = new Stage();
	private int numOfGraphs = 0; 

	public void init() {
		
		graphs.setTitle("Progress");
		Manager man = HomeController.manager;
		man.update(man.db);
		HashMap<String, Budget> cats = createCats();
		
		GridPane grid = new GridPane();
		GridPane upperGra = createBigGraph(cats); 
		HBox catGraphs = new HBox();
		
		for(String s : cats.keySet()) {
			Budget b =  cats.get(s);
			catGraphs.getChildren().add(createSmallGraph(cats, b.getCategory().toString()));
		}
		
		upperGra.setLayoutY(100);
		upperGra.setLayoutY(100);
		upperGra.setMinSize(400, 400);
		catGraphs.setMaxHeight(300);
		
		grid.setMinWidth(800);
		upperGra.setAlignment(Pos.CENTER);
		grid.add(upperGra, 0, 0);
		grid.add(catGraphs, 0, 1);
		
		Scene scene = new Scene(grid, 800, 750);
		scene.getStylesheets().add(getClass().getResource("../views/application.css").toString());
		graphs.setResizable(false);
		graphs.setScene(scene);
		graphs.show();
	}
	
	private GridPane createBigGraph(HashMap<String, Budget> buds) {
		GridPane upperGra = new GridPane();
		
		ArrayList<String> categories = new ArrayList<String>();
		Manager man = HomeController.manager;
		Database db = man.db;
		man.update(db);
		float values[] = new float[5];
		
		float current = 0;
		float limit = 0;
		float remainder = 0;
		int i = 0;
		
		for(String s : buds.keySet()) {
			Budget b =  buds.get(s);
			if(b != null) {
				categories.add(b.getCategory().toString());
				values[i] = b.getCurrentAmount();
				limit += man.getBudgetLimits().get(b.getCategory().toString());
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
		chart.setLegendSide(Side.BOTTOM);
		chart.setTitle("SPROUT GRAPHS");
		chart.setTitleSide(Side.TOP);
		
		Label caption = new Label("");
		caption.setTextFill(Color.BLACK);
		caption.setStyle("-fx-font: 24 arial;");

		upperGra.add(chart, 0, 1);
		upperGra.add(caption, 0, 0);
		
		for (final PieChart.Data d : pieData) {
			d.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					caption.setTranslateX(e.getSceneX() - 150);
					caption.setTranslateY(e.getSceneY());
					String pieV = String.format("%.2f", d.getPieValue());
					pieV = "$" + String.valueOf(pieV);
					caption.setText(pieV);
				}
			});
		}
		return upperGra;
	}
	
	private HashMap<String, Budget> createCats() {
		HashMap<String, Budget> buds = new HashMap<String, Budget>();
		Manager man = HomeController.manager;
		ArrayList<Budget> budg = man.getBudgets();
		for(Budget b : budg) {
			Budget bu = new Budget();
			if(buds.containsKey(b.getCategory().toString())) {
				bu.setCategory(b.getCategory());
				bu.setCurrentAmount(buds.get(b.getCategory().toString()).getCurrentAmount() + b.getCurrentAmount());
				bu.setLimit(man.getBudgetLimits().get(b.getCategory().toString()));
				buds.remove(b.getCategory().toString());
				buds.put(b.getCategory().toString(), bu);
			} else  {
				buds.put(b.getCategory().toString(), b);
			}
		}
		return buds;
	}
	
	private VBox createSmallGraph(HashMap<String, Budget> buds ,String cat) {
		VBox individ = new VBox();
		
		Manager man = HomeController.manager;
		Database db = man.db;
		man.update(db);
		float values[] = new float[2];
		
		//How am I only going to get the budgets for that time period?
		float current = 0;
		float limit = man.getBudgetLimits().get(cat);
		float remainder = 0;
		int i = 0;
		for(String s : buds.keySet()) {
			Budget b =  buds.get(s);
			if(b != null && b.getCategory().toString().equalsIgnoreCase(cat)) {
				values[i] = b.getCurrentAmount();
				current += b.getCurrentAmount();
				i++;
			}
		}
			remainder = limit - current;
			values[i] = remainder;
		
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
		chart.setLegendSide(Side.BOTTOM);
		chart.setTitle(cat);
		chart.setTitleSide(Side.TOP);
		
		Label caption = new Label("");
		caption.setTextFill(Color.BLACK);
		caption.setStyle("-fx-font: 24 arial;");

		individ.getChildren().addAll(chart, caption);
		
		for (final PieChart.Data d : pieData) {
			d.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					String pieV = String.format("%.2f", d.getPieValue());
					pieV = "$" + String.valueOf(pieV);
					caption.setText(pieV);
				}
			});
		}
		
		return individ;
	}

}
