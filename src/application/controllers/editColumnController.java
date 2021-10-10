package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Date;

import java.time.LocalDate;

import application.Model;
import application.objects.Column;

public class editColumnController {
	
	private Stage stage;

	private Model model = new Model();
	
	@FXML
	private TextField txtFieldColumnName;
	
	@FXML
	private TextArea txtAreaDescription;
	
	@FXML
	private DatePicker datePicker;
	
	public void loadColumn(Column column) {
		txtFieldColumnName.setText(column.getColumn_name());
		txtAreaDescription.setText(column.getDescription());
		LocalDate tmpLocalDate = column.getDue_date().toLocalDate(); 
		datePicker.setValue(tmpLocalDate);
	}
	
	public void saveColumnChanges(ActionEvent event) {
		
	}
	
	public void back(ActionEvent event) {
		
	}
	
}