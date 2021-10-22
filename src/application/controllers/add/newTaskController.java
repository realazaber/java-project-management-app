package application.controllers.add;

import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class newTaskController {

	private Stage stage;
	
	public int userID;
	
	public int columnID;

	Model model = new Model();
	
	@FXML
    private Label lbl_heading;

    @FXML
    private TextField txtFieldTaskName;

    @FXML
    private Button btn_createTask;

    @FXML
    private Label lbl_notification;

    @FXML
    private TextArea txtAreaDescription;

    @FXML
    private DatePicker datePicker;

    @FXML
    private CheckBox checkBoxCompleted;
    
    public void loadAddTask(int columnID, int userID) {
    	this.columnID = columnID;
    	this.userID = userID;
    }

    //Add task to database.
    @FXML
    void addTask(ActionEvent event) throws SQLException {
    	//If user has entered the necessary details.
    	if (txtFieldTaskName.getText() != "" && txtAreaDescription.getText() != "" && datePicker.getValue() != null) {
			LocalDate tmpLocalDate = datePicker.getValue();
			Date tmpDate = Date.valueOf(tmpLocalDate);
			
			//Check if the task already exists.
			if (model.getProjectDAO().addTask(columnID, txtFieldTaskName.getText(), txtAreaDescription.getText(), tmpDate, checkBoxCompleted.isSelected()) ) {
				//If it does then notify the user.
				System.out.println(txtFieldTaskName.getText() + " added to column " + columnID);
				lbl_notification.setTextFill(Color.GREEN);
				lbl_notification.setText(txtFieldTaskName.getText() + " added.");
			}
			//If it doesn't then add the task.
			else {
				System.out.println(txtFieldTaskName.getText() + " already exists.");
				lbl_notification.setTextFill(Color.RED);
				lbl_notification.setText(txtFieldTaskName.getText() + " already exists.");
			}
		}
    	else {
    		//If user has not filled in all necesssary fields prompt them to try again.
			lbl_notification.setTextFill(Color.ORANGE);
			lbl_notification.setText("Please fill in missing info.");
    	}
    	
    	//Clear all fields.
    	txtFieldTaskName.clear();
    	txtAreaDescription.clear();
    	datePicker.setValue(null);
    	checkBoxCompleted.setSelected(false);
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
