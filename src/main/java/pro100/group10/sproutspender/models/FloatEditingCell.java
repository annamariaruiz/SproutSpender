package pro100.group10.sproutspender.models;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import pro100.group10.sproutspender.models.Budget.CategoryType;

public class FloatEditingCell extends TableCell<WeeklyPlanner, Budget> {

	private TextField textField;

	protected ChangeListener<Boolean> changeListener = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> arg0,
				Boolean arg1, Boolean arg2) {
			if(!arg2) {
				if(getItem() != null) {
					getItem().setCurrentAmount(Float.parseFloat(textField.getText().trim()));
					commitEdit(getItem());
				}
			}
		}
	};
	
	public FloatEditingCell() {}
	
	@Override
	public void startEdit() {
		if(getItem() != null && getItem().getCategory() == CategoryType.GENERAL) {
			editableProperty().setValue((Boolean) false);
		} else {
			if(!isEmpty() && getItem() != null) {
				super.startEdit();
				createTextField();
				setText(null);
				setGraphic(textField);
				// .selectAll will place the cursor at the end
//			textField.selectAll();
				textField.requestFocus();
			}
		}
	}
	
	// Make own createTextField method that commits edits on deselect and enter
	private void createTextField() {
		textField = new TextField(getString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.focusedProperty().addListener(changeListener);
		textField.setOnAction(evt -> {
			if(getItem() != null) {
				getItem().setCurrentAmount(Float.parseFloat(textField.getText().trim()));
				commitEdit(getItem());					
			}
		});
		
		textField.setOnKeyPressed((key) -> {
			if(key.getCode().equals(KeyCode.ESCAPE)) {
				textField.focusedProperty().removeListener(changeListener);
			cancelEdit();
			}
			
			if(key.getCode().equals(KeyCode.ENTER)) {
				if(getItem() != null) {
					getItem().setCurrentAmount(Float.parseFloat(textField.getText().trim()));
					commitEdit(getItem());					
				}
			}
		});
	}
	
	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}
	
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getItem() == null ? "" : getItem().toString());
		setGraphic(null);
	}
	
	@Override
	public void updateItem(Budget item, boolean empty) {
		super.updateItem(item, empty);
		
		if(empty) {
			setText(null);
			setGraphic(null);
		} else {
			if(isEditing()) {
				if(textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(null);
			}
		}
	}
	
	// Override to commit edits when another cell in the same row is selected
	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void commitEdit(Budget item) {
		textField.focusedProperty().removeListener(changeListener);
		if(isEditing()) {
			super.commitEdit(item);
		} else {
			final TableView table = getTableView();
			if(table != null) {
				TablePosition position = new TablePosition(getTableView(),
						getTableRow().getIndex(), getTableColumn());
				CellEditEvent editEvent = new CellEditEvent(table, position, 
						TableColumn.editCommitEvent(), item);
				Event.fireEvent(getTableColumn(), editEvent);
			}
			
			updateItem(item, false);
			if(table != null) {
				table.edit(-1, null);
			}
		}
		
		// Stops neighboring cell from being edited without a double-click
		getTableView().getSelectionModel().clearSelection();
	}
}
