package application.controllers.add;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import application.Model;
import application.controllers.dashboardController;
import application.domains.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class newColumnController {
	
	private Stage stage;
	
	public int userID;
	
	public int projectID;
	
	private Model model = new Model();
	
	@FXML
	private TextField txtFieldColumnName;
	
	@FXML
	private Label lbl_heading;
	
	@FXML
	private TextArea txtAreaDescription;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private Label lbl_notification;
	
	public newColumnController() {
		
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}


	public int getProjectID() {
		return projectID;
	}


	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	

	public void setHeading(String projectName) {
		lbl_heading.setText("Add column for project " + projectName);
	}
	

	
	//Add project to database
	public void addColumn(ActionEvent event) throws SQLException {
		
		//If user has entered the necessary details.
		if (txtFieldColumnName.getText() != "" && txtAreaDescription.getText() != "" && datePicker.getValue() != null) { 
			//Check if the column already exists.
			LocalDate tmpLocalDate = datePicker.getValue();
			Date tmpDate = Date.valueOf(tmpLocalDate);

			if (model.getProjectDAO().addColumn(projectID, txtFieldColumnName.getText(), tmpDate, txtAreaDescription.getText()) == false) {
				//If it does then notify the user.
				lbl_notification.setTextFill(Color.RED);
				lbl_notification.setText("Column already exists!");
			}
			else {
				//If it doesn't then add the column.
				model.getProjectDAO().addColumn(projectID, txtFieldColumnName.getText(), tmpDate, txtAreaDescription.getText());
				lbl_notification.setTextFill(Color.GREEN);
				lbl_notification.setText("Column " + txtFieldColumnName.getText() + " added!");
			}
		}
		//If user has not filled in all necesssary fields prompt them to try again.
		else {
			lbl_notification.setTextFill(Color.ORANGE);
			lbl_notification.setText("Please fill in missing info.");
		}
		
		//Clear all fields.
		txtFieldColumnName.clear();
		txtAreaDescription.clear();
		datePicker.setValue(null);
		
		
	}
	
	//Go back to dashboard.
	public void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		//Load the dashboard and set the neccessary parameters.
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userID);
		User user = model.getUserDAO().getUser(userID);
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(userID);
		dashboardController.tabpane_mainTab.getSelectionModel().select(1);
		dashboardController.loadUser(user);
		
		//Go to dashboard.
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}