package application.controllers.edit;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import application.Model;
import application.controllers.dashboardController;
import application.objects.Project;
import application.objects.Task;
import application.objects.User;
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

public class editTaskController {
	
	public void editTaskController() {
		
	}
	
	private Stage stage;
	
	private Task task;
	
	private Project project;
	
	private Model model = new Model();
	


    @FXML
    private Label lbl_heading;

    @FXML
    private TextField txtFieldTaskName;

    @FXML
    private Button btn_saveChanges;

    @FXML
    private Label lbl_notification;

    @FXML
    private TextArea txtAreaDescription;

    @FXML
    private DatePicker datePicker;

    @FXML
    private CheckBox checkBox_completed;
    
	public void loadEditTask(Task task, Project project) {
		this.project = project;
		this.task = task;
		
		txtFieldTaskName.setText(task.getTaskName());
		txtAreaDescription.setText(task.getDescription());
		datePicker.setValue(task.getDueDate().toLocalDate());
		if (task.isCompleted()) {
			checkBox_completed.setSelected(true);
		}
		else {
			checkBox_completed.setSelected(false);
		}
	}

    @FXML
    void saveTaskChanges(ActionEvent event) throws SQLException {
    	if (txtFieldTaskName.getText() != "" && txtAreaDescription.getText() != "" && datePicker.getValue() != null) {
			LocalDate tmpLocalDate = datePicker.getValue();
			Date tmpDate = Date.valueOf(tmpLocalDate);
			model.getProjectDAO().saveTaskChanges(task.getTaskID(), txtFieldTaskName.getText(), txtAreaDescription.getText(), tmpDate, checkBox_completed.isSelected());
			lbl_notification.setTextFill(Color.GREEN);
			lbl_notification.setText(txtFieldTaskName.getText() + " edited successfully");
		}
    }
    
    @FXML
    void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		
		User user = model.getUserDAO().getUser(project.getUserID());
		dashboardController.setUserID(project.getUserID());
		
		
		
		dashboardController.loadUser(user);
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(project.getUserID());
		
		
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }

}
