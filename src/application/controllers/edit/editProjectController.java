package application.controllers.edit;

import java.sql.SQLException;

import application.Model;
import application.controllers.dashboardController;
import application.objects.Project;
import application.objects.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class editProjectController {
	public editProjectController() {
		
	}
	
	private Stage stage;
	
	private Project project;
	
	private Model model = new Model();
	
	@FXML
	private TextField txtFieldProjectName;
	
	@FXML 
	private CheckBox checkBox_default;
	
	@FXML
	private Label lbl_notification;
	
	public void loadProject(Project project) {
		this.project = project;
		txtFieldProjectName.setText(project.getProjectName());
		
		if (project.isDefault()) {
			checkBox_default.setSelected(true);
		}
		else {
			checkBox_default.setSelected(false);
		}	
	}
	
	public void saveProjectChanges(ActionEvent event) throws SQLException {
		model.getProjectDAO().saveProjectChanges(project.getProjectID(), project.getUserID(), txtFieldProjectName.getText(), checkBox_default.isSelected());
		System.out.println("Saved changes to project " + project.getProjectID());
		lbl_notification.setTextFill(Color.GREEN);
		lbl_notification.setText("Saved changes");
	}
	
	public void back(ActionEvent event) throws Exception {
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
		dashboardController.tabpane_mainTab.getSelectionModel().select(1);
		
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}