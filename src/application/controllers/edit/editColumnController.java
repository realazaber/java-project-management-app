package application.controllers.edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import application.Model;
import application.controllers.dashboardController;
import application.objects.Column;
import application.objects.User;

public class editColumnController {

	public editColumnController() {
		
	}
	
	int userId;
	
	private Stage stage;

	private Column column;
	
	private Model model = new Model();
	
	@FXML
	private TextField txtFieldColumnName;
	
	@FXML
	private TextArea txtAreaDescription;
	
	@FXML
	private DatePicker datePicker;
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void loadColumn(Column column) {
		
		this.column = column;
		
		txtFieldColumnName.setText(column.getColumn_name());
		txtAreaDescription.setText(column.getDescription());
		LocalDate tmpLocalDate = column.getDue_date().toLocalDate(); 
		datePicker.setValue(tmpLocalDate);
	}
	
	public void saveColumnChanges(ActionEvent event) throws SQLException {
		
		if (txtFieldColumnName.getText() != "" && txtAreaDescription.getText() != "" && datePicker.getValue() != null) {
			LocalDate tmpLocalDate = datePicker.getValue();
			Date tmpDate = Date.valueOf(tmpLocalDate);
			model.getProjectDAO().saveColumnChanges(column.getProjectID(), column.getColumnID(), txtFieldColumnName.getText(), tmpDate, txtAreaDescription.getText());
			System.out.println("EWFEWFWFW " + column.getProjectID());
			System.out.println("WEFEWFEW " + column.getColumnID());
		}
		
		
		
	}
	
	public void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userId);
		
		User user = model.getUserDAO().getUser(userId);
		
		dashboardController.loadUser(user);
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(userId);
		
		
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}