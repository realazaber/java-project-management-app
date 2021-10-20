package application.controllers.edit;

import java.sql.SQLException;
import java.util.ArrayList;

import application.Model;
import application.controllers.dashboardController;
import application.domains.ActionItem;
import application.domains.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class checklistController {

	private Stage stage;
	
    @FXML
    private Label lbl_heading;

    @FXML
    private Label lbl_notification;

    @FXML
    private TableView<ActionItem> table_actionItems;
    
    @FXML
    private TableColumn<ActionItem, Integer> col_actItemID;

    @FXML
    private TableColumn<ActionItem, String> col_actItemName;

    @FXML
    private TableColumn<ActionItem, String> col_actItemDescription;
    
    @FXML
    private TableColumn<ActionItem, Boolean> col_actItemCompleted;

    @FXML
    private Button btn_addActionItem;

    @FXML
    private TextField textField_actItemName;

    @FXML
    private TextArea textArea_actItemDescription;

    Model model = new Model();
    
    int userID;
    
    int checkListID;
    
    ArrayList<ActionItem> actionItems = new ArrayList<ActionItem>();
    
    public void loadChecklist(int checkListID, int userID) throws SQLException {
    	
    	this.userID = userID;
    	
    	this.checkListID = checkListID;
    	
    	actionItems = model.getProjectDAO().loadActionItems(checkListID);
    	ObservableList<ActionItem> tableActionItems = FXCollections.observableArrayList(actionItems);
    	
    	col_actItemID.setCellValueFactory(new PropertyValueFactory<ActionItem, Integer>("actionitemID"));
    	col_actItemName.setCellValueFactory(new PropertyValueFactory<ActionItem, String>("name"));
    	col_actItemDescription.setCellValueFactory(new PropertyValueFactory<ActionItem, String>("description"));
    	col_actItemCompleted.setCellValueFactory(new PropertyValueFactory<ActionItem, Boolean>("completed"));
    	
    	table_actionItems.setItems(tableActionItems);
    
    }
    
    @FXML
    void btn_createActionItem(ActionEvent event) {
    	try {
    		System.out.println("Adding action item " + textField_actItemName.getText());
    		
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
    	
    }
    
    @FXML
	public void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userID);
		
		User user = model.getUserDAO().getUser(userID);
		
		dashboardController.loadUser(user);
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(userID);
		dashboardController.tabpane_mainTab.getSelectionModel().select(1);
		
		
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}



}