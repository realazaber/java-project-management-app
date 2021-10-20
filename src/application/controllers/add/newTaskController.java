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

    @FXML
    void addTask(ActionEvent event) throws SQLException {
    	if (txtFieldTaskName.getText() != "" && txtAreaDescription.getText() != "" && datePicker.getValue() != null) {
			LocalDate tmpLocalDate = datePicker.getValue();
			Date tmpDate = Date.valueOf(tmpLocalDate);
			
			if (model.getProjectDAO().addTask(columnID, txtFieldTaskName.getText(), txtAreaDescription.getText(), tmpDate, checkBoxCompleted.isSelected()) ) {
				System.out.println(txtFieldTaskName.getText() + " added to column " + columnID);
				lbl_notification.setTextFill(Color.GREEN);
				lbl_notification.setText(txtFieldTaskName.getText() + " added.");
			}
			else {
				System.out.println(txtFieldTaskName.getText() + " already exists.");
				lbl_notification.setTextFill(Color.RED);
				lbl_notification.setText(txtFieldTaskName.getText() + " already exists.");
			}
		}
    }

    @FXML
    void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userID);
		User user = model.getUserDAO().getUser(userID);
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(userID);
		dashboardController.loadUser(user);
		dashboardController.tabpane_mainTab.getSelectionModel().select(1);
	
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }

}
