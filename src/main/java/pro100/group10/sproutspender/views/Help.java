package pro100.group10.sproutspender.views;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Help {
	
	private Stage stage = new Stage();
	
	public void init() {
		VBox grid = new VBox();
		VBox misc = new VBox();
		VBox budgeting = new VBox();
		VBox intro = new VBox();
		VBox bills = new VBox();
		VBox spending = new VBox();
		
		ScrollPane scrollBills = new ScrollPane(bills);
		scrollBills.setFitToHeight(false);
		ScrollPane scrollBudgeting = new ScrollPane(budgeting);
		scrollBudgeting.setFitToHeight(false);
		ScrollPane scrollSpending = new ScrollPane(spending);
		scrollSpending.setFitToHeight(false);
		
		
		
		stage.setTitle("Help");		
		HBox ribbon = new HBox();
		Button introTab = new Button("Introduction");
		introTab.setPrefSize(150, 20);
		Button spendingTab = new Button("Spending");
		spendingTab.setPrefSize(150, 20);
		Button billsTab = new Button("Bills");
		billsTab.setPrefSize(150, 20);
		Button budgetingTab = new Button("Budgeting");
		budgetingTab.setPrefSize(150, 20);
		Button miscTab = new Button("Misc");
		miscTab.setPrefSize(150, 20);
		
		Font header = Font.font("Calibri Light",50);
		Font head = Font.font("Calibri Light", FontPosture.ITALIC, 30);
		Font body = Font.font("Calibri Light", 20);
		
		Color headC = Color.LIGHTGREEN;
		
		ribbon.getChildren().addAll(introTab, spendingTab, billsTab, budgetingTab, miscTab);
		grid.getChildren().add(ribbon);
		
		//Introduction
		Text introHeader = new Text("Introduction");
		introHeader.setTranslateX(15);
		introHeader.setFont(header);
		introHeader.setFill(Color.FORESTGREEN);
		
		Text introH1 = new Text("What is Sprout Spender?");
		introH1.setFont(head);
		introH1.setFill(headC);
		introH1.setTranslateX(20);
		introH1.setTranslateY(10);
		
		Text introDescrip1 = new Text("Sprout Spender is a budgeting application created by ARC(Anna, Riley, & Cece)\n"
				+ "to aid people from any demographic in managing their money. This desktop application\n"
				+ "is meant to be used to help you be more conscientious about how much you are \n"
				+ "spending. It also helps you see exactly how much you are going under or over \n"
				+ "budget each time period. Sprout Spender is fairly customizable, allowing you to \n"
				+ "customize your start date, view your records, change your records, and customize \n"
				+ "your time period. Supporting multiple financial plans per user, it could be ideal \n"
				+ "if you wanted to do a business financial plan and a personal one using the same \n"
				+ "account. Sprout Spender's team takes pride in catering to it's users, hoping that \n"
				+ "their application can help someone in need.");
		introDescrip1.setFont(body);
		introDescrip1.setFill(Color.GHOSTWHITE);
		introDescrip1.setTranslateX(30);
		introDescrip1.setTranslateY(17);
		
		
		intro.getChildren().addAll(introHeader, introH1, introDescrip1);
		introTab.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				grid.getChildren().clear();
				grid.getChildren().add(ribbon);
				grid.getChildren().add(intro);
			}
		});	
		
		
		
		//Spending
		Text spendingHeader = new Text("Spending");
		spendingHeader.setFont(header);
		spendingHeader.setFill(Color.FORESTGREEN);
		spendingHeader.setTranslateX(15);
		
		Text spendingH1 = new Text("Adding Amount Spent");
		spendingH1.setFont(head);
		spendingH1.setFill(headC);
		spendingH1.setTranslateX(20);
		spendingH1.setTranslateY(10);
		
		Text spendingDescrip1 = new Text("Spending Habits: This feature is for you to track how much you spent in a category\n on any given day.\r\n\n"
				+ "1. To use, click on an empty cell, then click \"Add Amount Spent\" from the edit menu.\n"
				+ "2. A dialogue window to record your spending will display. The date and category of \n"
				+ "spending is automatically filled in based on the cell you selected.\n"
				+ "3. If you’d like to change the date or category, the cell matching \n"
				+ "those attributes will instead be populated.\n"
				+ "4. Once you’ve filled in all the fields, click submit for the cell to be populated.\n"
				+ "5. Clicking cancel or X will leave the table as it was before you clicked\n"
				+ " \"Add Amount Spent.\"");
		spendingDescrip1.setFont(body);
		spendingDescrip1.setFill(Color.GHOSTWHITE);
		spendingDescrip1.setTranslateX(30);
		spendingDescrip1.setTranslateY(17);
		
		Text spendingH2 = new Text("Editing Amount Spent");
		spendingH2.setFont(head);
		spendingH2.setFill(headC);
		spendingH2.setTranslateX(20);
		spendingH2.setTranslateY(30);
		
		Text spendingDescrip2 = new Text("Spending Habits: This feature is for you to change the details of a tracked record.\n\n"
				+ "1. To use, double click a cell to quickly edit its amount, then press enter or select\n"
				+ " another cell to submit your changes.\n"
				+ "2. If the cell is empty, you’ll have to add an amount spent first.\n"
				+ "3. To cancel your changes, press the escape key.\n"
				+ "4. Alternatively, select the cell, then click \"Move/Edit Amount Spent\" from the \n"
				+ "edit menu.\n"
				+ "5. Here, you may move the cell to another row or column by changing the date\n"
				+ " and category in addition to the amount.\n"
				+ "6. The fields are automatically populated by the selected cell’s values.");
		spendingDescrip2.setFont(body);        
		spendingDescrip2.setFill(Color.GHOSTWHITE);		
		spendingDescrip2.setTranslateX(30);    		
		spendingDescrip2.setTranslateY(37);
		
		Text spendingH3 = new Text("Deleting Amount Spent");
		spendingH3.setFont(head);
		spendingH3.setFill(headC);
		spendingH3.setTranslateX(20);
		spendingH3.setTranslateY(45);
		
		Text spendingDescrip3 = new Text("Spending Habits: This feature is for you to erase any given record provided that \nthere was an accidental addition.\n\n"
				+ "1. To use, select a cell, then click “Remove Amount Spent” from the edit menu.\n"
				+ "2. The cell’s contents are immediately deleted, which allows you to add another \n"
				+ "amount to the cell.\n"
				+ "3. This decision is final and cannot be undone.\n\n\n");
		spendingDescrip3.setFont(body);             
		spendingDescrip3.setFill(Color.GHOSTWHITE);	
		spendingDescrip3.setTranslateX(30);    		
		spendingDescrip3.setTranslateY(52);    	
		
		
		spending.getChildren().addAll(spendingHeader, spendingH1, spendingDescrip1, spendingH2, spendingDescrip2, spendingH3, spendingDescrip3);
		spendingTab.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				grid.getChildren().clear();
				grid.getChildren().add(ribbon);
				grid.getChildren().add(scrollSpending);
			}
		});		
		
		//Bills
		Text billsHeader = new Text("Bills");
		billsHeader.setFont(header);
		billsHeader.setFill(Color.FORESTGREEN);
		billsHeader.setTranslateX(15);
		
		Text billsH1 = new Text("Adding Bills");
		billsH1.setFont(head);
		billsH1.setFill(headC);
		billsH1.setTranslateX(20);
		billsH1.setTranslateY(10);
		
		Text billsDescrip1 = new Text("Tracking: This feature is for you to keep track of what bills need to be paid.\n"
				+ "\n1. Open the bills window. \n"
				+ "2. Click \"Add\" to create a new Bill.\n"
				+ "3. Name your bill.\n"
				+ "4. Input the amount needed to pay.\n"
				+ "5. Input the next payment date by either\n"
				+ "\ta. Inputting a date in mm/dd/yyyy format.\n"
				+ "\tb. Click the calendar icon to open the calendar.\n"
				+ "6. Click the dropdown menu and select how often the bill reoccures.\n"
				+ "7. Click \"Save New Bill\"");
		billsDescrip1.setFont(body);             
		billsDescrip1.setFill(Color.GHOSTWHITE);	
		billsDescrip1.setTranslateX(30);    		
		billsDescrip1.setTranslateY(17);
		
		Text billsH2 = new Text("Editing Bills");
		billsH2.setFont(head);
		billsH2.setFill(headC);
		billsH2.setTranslateX(20);
		billsH2.setTranslateY(20);
		
		Text billsDescrip2 = new Text("Tracking: This feature is for you to change the details of a selected bill.\n\n"
				+ "1. Select a bill to edit.\n"
				+ "2. Click \"Edit\" to change or mark a bill as \"Paid\"\n"
				+ "3. If needed, update fields by following the same steps to creating a bill. \n"
				+ "4. If needed, clicked the checkbox to mark a bill as paid. \n"
				+ "5. Next day to pay will update to the next pay date when the bill is marked as paid \nand the previous date is passed.\n");
		billsDescrip2.setFont(body);             
		billsDescrip2.setFill(Color.GHOSTWHITE);	
		billsDescrip2.setTranslateX(30);    		
		billsDescrip2.setTranslateY(27);
		
		Text billsH3 = new Text("Removing Bills");
		billsH3.setFont(head);
		billsH3.setFill(headC);
		billsH3.setTranslateX(20);
		billsH3.setTranslateY(10);
		
		Text billsDescrip3 = new Text("Tracking: This feature is for you to remove a selected bill.\n\n"
				+ "1. Select a bill to be deleted.\n"
				+ "2. Click delete.\n"
				+ "3. The previously selected bill will be removed.\n\n");
		billsDescrip3.setFont(body);             
		billsDescrip3.setFill(Color.GHOSTWHITE);	
		billsDescrip3.setTranslateX(30);    		
		billsDescrip3.setTranslateY(17);
		
		bills.getChildren().addAll(billsHeader, billsH1, billsDescrip1, billsH2, billsDescrip2, billsH3, billsDescrip3);
		billsTab.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				grid.getChildren().clear();
				grid.getChildren().add(ribbon);
				grid.getChildren().add(scrollBills);
			}
		});	
		
		//Budgeting
		Text budgetingHeader = new Text("Budgeting");
		budgetingHeader.setFont(header);
		budgetingHeader.setFill(Color.FORESTGREEN);
		budgetingHeader.setTranslateX(15);
		
		Text budgetingH1 = new Text("Date Ranges");
		budgetingH1.setFont(head);
		budgetingH1.setFill(headC);
		budgetingH1.setTranslateX(20);
		budgetingH1.setTranslateY(10);
		
		Text budgetingDescrip1 = new Text("Budgets: This feature is for you to choose a day you want budgeting to start.\n\n"
				+ "1. Open the Settings by clicking the \"Settings\" button\n"
				+ "2. Select a prefered time frame for budget limits to reset by clicking either \n"
				+ "\"Week\" or \"Month\"\n"
				+ "3. Select what day your budgets will recent by either:\n"
				+ "\tInputting a date in mm/dd/yyyy format.\n"
				+ "\tClick the calendar icon to open the calendar\n");
		budgetingDescrip1.setFont(body);             
		budgetingDescrip1.setFill(Color.GHOSTWHITE);	
		budgetingDescrip1.setTranslateX(30);    		
		budgetingDescrip1.setTranslateY(17);
		
		Text budgetingH2 = new Text("Limits");
		budgetingH2.setFont(head);
		budgetingH2.setFill(headC);
		budgetingH2.setTranslateX(20);
		budgetingH2.setTranslateY(20);
		
		Text budgetingDescrip2 = new Text("Budgets: This feature was created for you to orchestrate how much you want to set \n"
				+ "aside for a time period. To see how close you are to reaching that limit, graphs were \n"
				+ "created to give you a visual representation.\n\n"
				+ "1. To set a limit of certain categories, click and drag the button on the slider to \n"
				+ "increase or decrease your budget limits. Limits will automatically be rounded in\n"
				+ " multiples of 50s.\n"
				+ "2. Click \"Save\" to preserve your settings.\n\n");
		budgetingDescrip2.setFont(body);             
		budgetingDescrip2.setFill(Color.GHOSTWHITE);	
		budgetingDescrip2.setTranslateX(30);    		
		budgetingDescrip2.setTranslateY(27);
		
		budgeting.getChildren().addAll(budgetingHeader, budgetingH1, budgetingDescrip1, budgetingH2, budgetingDescrip2);
		budgetingTab.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				grid.getChildren().clear();
				grid.getChildren().add(ribbon);
				grid.getChildren().add(scrollBudgeting);
			}
		});	
		
		//Misc
		Text miscHeader = new Text("Misc.");
		miscHeader.setFont(header);
		miscHeader.setFill(Color.FORESTGREEN);
		miscHeader.setTranslateX(15);
		
		Text miscH1 = new Text("Jump to a Specific Date");
		miscH1.setFont(head);
		miscH1.setFill(headC);
		miscH1.setTranslateX(20);
		miscH1.setTranslateY(10);
		
		Text miscDescrip1 = new Text("This feature is for you to travel back to a different budgeting period to view \n"
				+ "your previous spending habits.\n\n"
				+ "1. Skip to a specific date in time by either:\n" 
				+ "\tInputing a date in a mm/dd/yyyy format\n"
				+ "\tClick the calendar icon to open the calendar\n"
				+ "2. Go week-by-week by clicking either \"Prev Week\" to go to previous weeks or\n"
				+ " \"Next Week\" to see upcoming weeks.");
		miscDescrip1.setFont(body);             
		miscDescrip1.setFill(Color.GHOSTWHITE);	
		miscDescrip1.setTranslateX(30);    		
		miscDescrip1.setTranslateY(17);
		
		Text miscH2 = new Text("Sprout Name");
		miscH2.setFont(head);
		miscH2.setFill(headC);
		miscH2.setTranslateX(20);
		miscH2.setTranslateY(20);
		
		Text miscDescrip2 = new Text("Each user is able to have multiple financial plans for differing purposes. These \n"
				+ "financial plans and recordings are called sprouts.\n\n"
				+ "1. Name your financial plan by entering the name of your \"Sprout\" in the textbox \n"
				+ "under name your sprout.\n"
				+ "2. Choosing a different sprout name that has not been previously used will create a new\n "
				+ "financial plan, however old financial plans can still be utilized by entering the name.\n\n");
		miscDescrip2.setFont(body);             
		miscDescrip2.setFill(Color.GHOSTWHITE);	
		miscDescrip2.setTranslateX(30);    		
		miscDescrip2.setTranslateY(27);
		
		misc.getChildren().addAll(miscHeader, miscH1, miscDescrip1, miscH2, miscDescrip2);
		miscTab.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				grid.getChildren().clear();
				grid.getChildren().add(ribbon);
				grid.getChildren().add(misc);
			}
		});	
		
		grid.setId("pane");
        
		Scene scene = new Scene(grid);
		scene.getStylesheets().addAll(this.getClass().getResource("../views/help.css").toExternalForm());
		stage.setScene(scene);
		stage.setMinHeight(600);
		stage.setMinWidth(750);
		stage.setResizable(false);
		stage.setMaxWidth(750);
		stage.show();
	}
}
